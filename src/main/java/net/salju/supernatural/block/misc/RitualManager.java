package net.salju.supernatural.block.misc;

import net.salju.supernatural.init.*;
import net.salju.supernatural.events.GothicEvent;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.block.entity.RitualAltarEntity;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
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

	public static void summonMob(ServerLevel lvl, BlockPos pos, ItemStack stack) {
		Entity entity = EntityType.loadEntityRecursive(SupernaturalManager.getSoulTag(stack), lvl, EntitySpawnReason.MOB_SUMMONED, o -> o);
		if (entity != null) {
			entity.teleportTo(Vec3.atBottomCenterOf(pos).x, Vec3.atBottomCenterOf(pos).y, Vec3.atBottomCenterOf(pos).z);
			if (entity instanceof Villager bob) {
				ZombieVillager zomby = bob.convertTo(EntityType.ZOMBIE_VILLAGER, ConversionParams.single(bob, true, true), newbie -> { EventHooks.onLivingConvert(bob, newbie); });
				if (zomby != null) {
					zomby.setVillagerData(bob.getVillagerData());
					zomby.setGossips(bob.getGossips());
					zomby.setTradeOffers(bob.getOffers());
					zomby.setVillagerXp(bob.getVillagerXp());
				}
			}
			if (lvl.canSeeSky(pos)) {
				LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(lvl, EntitySpawnReason.MOB_SUMMONED);
				if (bolt != null) {
					bolt.move(MoverType.SELF, Vec3.atBottomCenterOf(pos));
					bolt.setVisualOnly(true);
					lvl.addFreshEntity(bolt);
				}
			}
			lvl.addFreshEntity(entity);
		}
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