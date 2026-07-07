package net.salju.supernatural.block.misc;

import net.salju.supernatural.init.*;
import net.salju.supernatural.block.entity.RitualAltarEntity;
import net.salju.supernatural.entity.AbstractMinionEntity;
import net.salju.supernatural.item.component.AnchorballData;
import net.salju.supernatural.item.component.RitualCompassData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.Stream;

public class RitualManager {
	public static void defaultResult(RitualAltarEntity target, ItemStack stack, ServerLevel lvl, Player player, BlockPos pos) {
		lvl.playSound(null, pos, SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
		lvl.sendParticles(ParticleTypes.SOUL, (pos.getX() + 0.5), (pos.getY() + 0.5), (pos.getZ() + 0.5), 21, 3, 1, 3, 0);
		if (!player.isCreative()) {
			stack.shrink(1);
            target.clearContent();
		}
	}

    public static void teleportUser(AnchorballData data, ServerLevel lvl, ServerPlayer ply) {
        if (data != null) {
            ServerLevel loc = lvl.getServer().getLevel(data.getDimension());
            double x = data.getPos().getX() + 0.5;
            double y = data.getPos().getY() + 0.7;
            double z = data.getPos().getZ() + 0.5;
            if (loc != null) {
                lvl.playSound(null, ply.blockPosition(), SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
                lvl.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, ply.blockPosition().getX(), ply.blockPosition().getY() + 0.75, ply.blockPosition().getZ(), 12, 0.5, 0.5, 0.5, 0.65);
                loc.playSound(null, BlockPos.containing(x, y, z), SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
                ply.teleport(new TeleportTransition(loc, new Vec3(x, y, z), ply.getDeltaMovement(), ply.getYRot(), ply.getXRot(), TeleportTransition.DO_NOTHING));
                loc.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 12, 0.5, 0.5, 0.5, 0.65);
            }
        }
    }

    public static boolean canTeleportTo(AnchorballData data, ServerLevel lvl, ServerPlayer ply) {
        if (data != null) {
            boolean check = true;
            if (ply.getLastDeathLocation().isPresent() && data.target() != ply.getLastDeathLocation().get()) {
                check = lvl.getPoiManager().existsAtPosition(SupernaturalBlocks.RITUAL_POI.getKey(), data.getPos());
            }
            return lvl.isInWorldBounds(data.getPos()) && check;
        }
        return false;
    }

    public static void summonEntity(Entity target, Player player, ServerLevel lvl, BlockPos pos, BlockPos poz) {
        target.snapTo(pos.getCenter());
        if (target instanceof Mob mob) {
            mob.finalizeSpawn(lvl, lvl.getCurrentDifficultyAt(pos), EntitySpawnReason.MOB_SUMMONED, null);
        }
        if (target instanceof TamableAnimal pet) {
            pet.setOwner(player);
        } else if (target instanceof AbstractMinionEntity bob) {
            bob.setOwner(player);
        }
        lvl.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.getX(), pos.getY(), pos.getZ(), 15, 0.45, 0.25, 0.45, 0);
        if (!lvl.getBlockState(poz).isAir()) {
            lvl.destroyBlock(poz, false);
        }
        lvl.addFreshEntity(target);
    }

    public static ItemStack createMirror(ItemStack stack, ItemStack copy, ServerLevel lvl, Player player, BlockPos pos) {
        if (stack.is(SupernaturalItems.EBONSTEEL_MIRROR)) {
            if (player instanceof ServerPlayer ply) {
                if (ply.getLastDeathLocation().isPresent()) {
                    copy.set(SupernaturalData.ANCHOR.get(), new AnchorballData(ply.getLastDeathLocation().get()));
                }
            }
        } else {
            copy.set(SupernaturalData.ANCHOR.get(), new AnchorballData(GlobalPos.of(lvl.dimension(), pos)));
        }
        return copy;
    }

    public static ItemStack getRitualCompass(BlockPos pos, ServerLevel lvl, int i) {
        ItemStack stack = new ItemStack(SupernaturalItems.COMPASS.get());
        if (i <= 1) {
            BlockPos loc = lvl.findNearestMapStructure(SupernaturalTags.RUINS, pos, 100, false);
            if (loc != null) {
                stack.set(SupernaturalData.COMPASS, new RitualCompassData(Optional.of(GlobalPos.of(lvl.dimension(), loc)), "ruins"));
            }
        } else if (i >= 3) {
            BlockPos loc = lvl.findNearestMapStructure(SupernaturalTags.ANCIENT, pos, 100, false);
            if (loc != null) {
                stack.set(SupernaturalData.COMPASS, new RitualCompassData(Optional.of(GlobalPos.of(lvl.dimension(), loc)), "ancient"));
            }
        } else {
            BlockPos loc = lvl.findNearestMapStructure(SupernaturalTags.LIFE, pos, 100, false);
            if (loc != null) {
                stack.set(SupernaturalData.COMPASS, new RitualCompassData(Optional.of(GlobalPos.of(lvl.dimension(), loc)), "village"));
            }
        }
        return stack;
    }

    @Nullable
    public static Stream<Holder<Enchantment>> getEnchantments(ServerLevel lvl) {
        Optional<HolderSet.Named<Enchantment>> set = lvl.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.IN_ENCHANTING_TABLE);
        return set.isEmpty() ? null : set.get().stream();
    }
}