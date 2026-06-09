package net.salju.supernatural.block.misc;

import net.salju.supernatural.init.*;
import net.salju.supernatural.events.GothicEvent;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.block.entity.RitualAltarEntity;
import net.salju.supernatural.item.component.AnchorballData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
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

    public static void teleportUser(AnchorballData data, Player player, ServerLevel lvl) {
        if (data != null) {
            ServerLevel loc = lvl.getServer().getLevel(data.target().dimension());
            double x = data.getPos().getX() + 0.5;
            double y = data.getPos().getY() + 0.7;
            double z = data.getPos().getZ() + 0.5;
            if (loc != null && player instanceof ServerPlayer ply) {
                ply.teleport(new TeleportTransition(loc, new Vec3(x, y, z), ply.getDeltaMovement(), ply.getYRot(), ply.getXRot(), TeleportTransition.DO_NOTHING));
                loc.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 12, 0.5, 0.5, 0.5, 0.65);
            }
        }
    }

    public static boolean canTeleportTo(AnchorballData data, ServerLevel lvl) {
        if (data != null) {
            return lvl.isInWorldBounds(data.getPos()) && lvl.getPoiManager().existsAtPosition(SupernaturalBlocks.RITUAL_POI.getKey(), data.getPos());
        }
        return false;
    }

	public static Item getHelmet(Item target) {
		GothicEvent event = SupernaturalManager.onGothicEvent(target);
		if (event.getHelmet() != Items.AIR) {
			return event.getHelmet();
		}
		Map<Item, Item> map = new HashMap<>();
		map.put(Items.COPPER_HELMET, SupernaturalItems.GOTHIC_COPPER_HELMET.get());
		map.put(Items.IRON_HELMET, SupernaturalItems.GOTHIC_IRON_HELMET.get());
		map.put(Items.GOLDEN_HELMET, SupernaturalItems.GOTHIC_GOLDEN_HELMET.get());
		map.put(Items.DIAMOND_HELMET, SupernaturalItems.GOTHIC_DIAMOND_HELMET.get());
		map.put(Items.NETHERITE_HELMET, SupernaturalItems.GOTHIC_NETHERITE_HELMET.get());
		map.put(SupernaturalItems.EBONSTEEL_HELMET.get(), SupernaturalItems.GOTHIC_EBONSTEEL_HELMET.get());
		return map.getOrDefault(target, Items.AIR);
	}

	public static Item getEbonsteel(Item target) {
		Map<Item, Item> map = new HashMap<>();
		map.put(Items.IRON_HELMET, SupernaturalItems.EBONSTEEL_HELMET.get());
		map.put(Items.IRON_CHESTPLATE, SupernaturalItems.EBONSTEEL_CHESTPLATE.get());
		map.put(Items.IRON_LEGGINGS, SupernaturalItems.EBONSTEEL_LEGGINGS.get());
		map.put(Items.IRON_BOOTS, SupernaturalItems.EBONSTEEL_BOOTS.get());
		map.put(SupernaturalItems.GOTHIC_IRON_HELMET.get(), SupernaturalItems.GOTHIC_EBONSTEEL_HELMET.get());
		return map.getOrDefault(target, Items.AIR);
	}

    @Nullable
    public static Stream<Holder<Enchantment>> getEnchantments(ServerLevel lvl) {
        Optional<HolderSet.Named<Enchantment>> set = lvl.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.IN_ENCHANTING_TABLE);
        return set.isEmpty() ? null : set.get().stream();
    }

	@Nullable
	public static Mob getSacrifice(ServerLevel lvl, Class<? extends Mob> mob, BlockPos pos) {
		return getSacrifice(lvl, new AABB(pos).inflate(SupernaturalConfig.ALTARRANGE.get()), mob, null);
	}

    @Nullable
    public static Mob getSacrifice(ServerLevel lvl, TagKey<EntityType<?>> tag, BlockPos pos) {
        return getSacrifice(lvl, new AABB(pos).inflate(SupernaturalConfig.ALTARRANGE.get()), Mob.class, tag);
    }

	@Nullable
	public static Mob getSacrifice(ServerLevel lvl, AABB box, Class<? extends Mob> mob, @Nullable TagKey<EntityType<?>> tag) {
		for (Mob target : lvl.getEntitiesOfClass(mob, box)) {
			if (isValidTarget(target, tag) && !target.getType().is(SupernaturalTags.IMMUNITY)) {
				return target;
			}
		}
		return null;
	}

    private static boolean isValidTarget(Mob target, @Nullable TagKey<EntityType<?>> tag) {
        if (tag != null) {
            return target.getType().is(tag);
        }
        return true;
    }
}