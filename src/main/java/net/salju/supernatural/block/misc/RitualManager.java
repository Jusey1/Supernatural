package net.salju.supernatural.block.misc;

import net.salju.supernatural.init.*;
import net.salju.supernatural.events.GothicEvent;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.block.entity.RitualBlockEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class RitualManager {
	public static void defaultResult(RitualBlockEntity target, ItemStack stack, ServerLevel lvl, Player player, BlockPos pos) {
		target.clearContent();
		lvl.playSound(null, pos, SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
		lvl.sendParticles(ParticleTypes.SOUL, (pos.getX() + 0.5), (pos.getY() + 0.5), (pos.getZ() + 0.5), 21, 3, 1, 3, 0);
		if (!player.isCreative()) {
			stack.shrink(1);
		}
	}

	public static void summonMob(ServerLevel lvl, BlockPos pos, ItemStack stack) {
		Entity entity = EntityType.loadEntityRecursive(SupernaturalManager.getSoulTag(stack), lvl, o -> o);
		if (entity != null) {
			entity.moveTo(Vec3.atBottomCenterOf(pos));
			if (entity instanceof Villager bob) {
				ZombieVillager zomby = bob.convertTo(EntityType.ZOMBIE_VILLAGER, true);
				if (zomby != null) {
					zomby.setVillagerData(bob.getVillagerData());
					zomby.setGossips(bob.getGossips().store(NbtOps.INSTANCE));
					zomby.setTradeOffers(bob.getOffers());
					zomby.setVillagerXp(bob.getVillagerXp());
				}
			}
			if (lvl.canSeeSky(pos)) {
				LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(lvl);
				if (bolt != null) {
					bolt.moveTo(Vec3.atBottomCenterOf(pos));
					bolt.setVisualOnly(true);
					lvl.addFreshEntity(bolt);
				}
			}
			lvl.addFreshEntity(entity);
		}
	}

	public static Item getHelmet(Item target) {
        if (target.getDescriptionId().contains("copper_helmet")) {
            return SupernaturalItems.GOTHIC_COPPER_HELMET.get();
        }
        GothicEvent event = SupernaturalManager.onGothicEvent(target);
        if (event.getHelmet() != Items.AIR) {
            return event.getHelmet();
        }
		Map<Item, Item> map = new HashMap<>();
		map.put(Items.IRON_HELMET, SupernaturalItems.GOTHIC_IRON_HELMET.get());
		map.put(Items.GOLDEN_HELMET, SupernaturalItems.GOTHIC_GOLDEN_HELMET.get());
		map.put(Items.DIAMOND_HELMET, SupernaturalItems.GOTHIC_DIAMOND_HELMET.get());
        map.put(Items.NETHERITE_HELMET, SupernaturalItems.GOTHIC_NETHERITE_HELMET.get());
        map.put(SupernaturalItems.EBONSTEEL_HELMET.get(), SupernaturalItems.GOTHIC_EBONSTEEL_HELMET.get());
		return map.getOrDefault(target, Items.AIR);
	}

    @Nullable
    public static Mob getSacrifice(ServerLevel lvl, Class<? extends Mob> mob, BlockPos pos) {
        return getSacrifice(lvl, ItemStack.EMPTY, mob, pos);
    }

    @Nullable
    public static Mob getSacrifice(ServerLevel lvl, ItemStack stack, Class<? extends Mob> mob, BlockPos pos) {
        return getSacrifice(lvl, stack, new AABB(pos).inflate(SupernaturalConfig.ALTARRANGE.get()), mob);
    }

	@Nullable
	public static Mob getSacrifice(ServerLevel lvl, ItemStack stack, AABB box, Class<? extends Mob> mob) {
		for (Mob target : lvl.getEntitiesOfClass(mob, box)) {
            if (isValidSacrificeTarget(target, stack) && !target.getType().is(SupernaturalTags.IMMUNITY)) {
                return target;
            }
		}
		return null;
	}

    private static boolean isValidSacrificeTarget(Mob target, ItemStack stack) {
        if (!stack.isEmpty()) {
            return SupernaturalManager.getSoulLevel(SupernaturalManager.getSoulLevel(target)) >= SupernaturalManager.getSoulLevel(SupernaturalManager.getSoulgem(stack));
        }
        return true;
    }
}