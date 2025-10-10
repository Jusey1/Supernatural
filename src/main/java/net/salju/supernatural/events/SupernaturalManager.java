package net.salju.supernatural.events;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.*;
import net.salju.supernatural.block.RitualBlockEntity;
import net.salju.supernatural.item.component.SoulgemData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Stream;
import com.google.common.collect.Multimap;
import com.google.common.collect.Lists;
import com.google.common.collect.HashMultimap;

public class SupernaturalManager {
	public static int getEnchantmentLevel(ItemStack stack, Level world, String id, String name) {
		return stack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(id, name))));
	}

    public static AttributeSupplier.Builder createAttributes(double h, double d, double a, double s) {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, h).add(Attributes.ATTACK_DAMAGE, d).add(Attributes.ARMOR, a).add(Attributes.MOVEMENT_SPEED, s);
    }

	public static boolean isVampire(LivingEntity target) {
		if (target instanceof Player player) {
			if (player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG).isPresent()) {
				return player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG).get().getBooleanOr("isVampire", false);
			}
			return false;
		}
		return target.getType().is(SupernaturalTags.VAMPIRE);
	}

	public static boolean hasVampirism(LivingEntity target) {
		return target.hasEffect(SupernaturalEffects.VAMPIRISM) && target.getEffect(SupernaturalEffects.VAMPIRISM).getAmplifier() >= 1;
	}

	public static void setVampire(Player player, boolean check) {
		if (player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG).isEmpty()) {
			player.getPersistentData().put(Player.PERSISTED_NBT_TAG, new CompoundTag());
		}
		CompoundTag data = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG).get();
		if (!check) {
			data.remove("isVampire");
			player.getPersistentData().put(Player.PERSISTED_NBT_TAG, data);
			player.getAttributes().removeAttributeModifiers(createSupernatural());
		} else {
			data.putBoolean("isVampire", true);
			player.getPersistentData().put(Player.PERSISTED_NBT_TAG, data);
		}
	}

	public static void addVampireEffects(Player player) {
		player.getAttributes().addTransientAttributeModifiers(createSupernatural());
		player.addEffect(new MobEffectInstance(SupernaturalEffects.VAMPIRISM, 5, 4, false, false, false));
		if (player.isCrouching()) {
			player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 10, 0, false, false, false));
		}
	}

	private static Multimap<Holder<Attribute>, AttributeModifier> createSupernatural() {
		Multimap<Holder<Attribute>, AttributeModifier> stats = HashMultimap.create();
		if (SupernaturalConfig.DAMAGE.get() > 0) {
			stats.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "vdps"), ((float) SupernaturalConfig.DAMAGE.get()), AttributeModifier.Operation.ADD_VALUE));
		}
		if (SupernaturalConfig.SPEED.get() > 0) {
			stats.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "vspeed"), ((float) SupernaturalConfig.SPEED.get() / 100), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		}
		return stats;
	}

	public static ItemStack setSoul(ItemStack stack, LivingEntity target) {
		TagValueOutput value = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
		target.save(value);
		CompoundTag mobster = value.buildResult();
		mobster.remove("ActiveEffects");
		mobster.remove("Passengers");
		mobster.remove("DeathTime");
		mobster.remove("Health");
		mobster.remove("Leash");
		mobster.remove("Fire");
		mobster.remove("UUID");
		stack.set(SupernaturalData.SOULGEM, new SoulgemData(mobster, getSoulLevel(target)));
		return stack;
	}

	public static CompoundTag getSoulTag(ItemStack stack) {
		SoulgemData data = stack.getOrDefault(SupernaturalData.SOULGEM, SoulgemData.EMPTY);
		return data.getSoul();
	}

	public static String getSoulLevel(LivingEntity target) {
		if (target.getType().is(SupernaturalTags.GRAND)) {
			return "soulgem.supernatural.grand";
		} else if (target.getType().is(SupernaturalTags.GREATER)) {
			return "soulgem.supernatural.greater";
		} else if (target.getType().is(SupernaturalTags.COMMON)) {
			return "soulgem.supernatural.common";
		} else if (target.getType().is(SupernaturalTags.LESSER)) {
			return "soulgem.supernatural.lesser";
		} else if (target.getType().is(SupernaturalTags.PETTY)) {
			return "soulgem.supernatural.petty";
		} else {
			if (target instanceof Monster) {
				return "soulgem.supernatural.common";
			} else if (target instanceof Animal) {
				return "soulgem.supernatural.lesser";
			} else {
				return "soulgem.supernatural.petty";
			}
		}
	}

	public static int getSoulLevel(String str) {
		Map<String, Integer> soulMap = new HashMap<>();
		soulMap.put("soulgem.supernatural.petty", 1);
		soulMap.put("soulgem.supernatural.lesser", 2);
		soulMap.put("soulgem.supernatural.common", 3);
		soulMap.put("soulgem.supernatural.greater", 4);
		soulMap.put("soulgem.supernatural.grand", 5);
		return soulMap.getOrDefault(str, 0);
	}

	public static String getSoulgem(ItemStack stack) {
		SoulgemData data = stack.getOrDefault(SupernaturalData.SOULGEM, SoulgemData.EMPTY);
		return data.getSoulPower();
	}

	@Nullable
	public static RitualBlockEntity getAltar(BlockPos pos, ServerLevel lvl, int r, Item i) {
		Stream<PoiRecord> stream = lvl.getPoiManager().getInRange(type -> type.is(SupernaturalBlocks.RITUAL_POI.getKey()), pos, r, PoiManager.Occupancy.ANY);
		for (PoiRecord record : stream.toList()) {
			if (lvl.getBlockEntity(record.getPos()) instanceof RitualBlockEntity target) {
				if (target.getItem(0).is(i) && pos.closerThan(record.getPos(), r) && canRitualsWork(lvl, record.getPos(), target)) {
					return target;
				}
			}
		}
		return null;
	}

	public static boolean canRitualsWork(ServerLevel lvl, BlockPos pos, RitualBlockEntity target) {
		return (lvl.dimensionType().natural() && !target.getGreedy() && lvl.getBrightness(LightLayer.BLOCK, pos) < 6 && (lvl.getBrightness(LightLayer.SKY, pos) < 6 || lvl.isDarkOutside()));
	}

	public static int getPower(ServerLevel lvl, BlockPos pos) {
		int i = 0;
		List<BlockPos> list = getCircle(pos);
		for (BlockPos poz : list) {
			BlockState state = lvl.getBlockState(poz);
			if (state.getBlock() instanceof CandleBlock && state.getValue(CandleBlock.LIT)) {
				i = (i + state.getValue(CandleBlock.CANDLES));
				lvl.setBlock(poz, state.setValue(CandleBlock.LIT, false), 3);
			}
		}
		return Math.min(28, i);
	}

	public static List<BlockPos> getCircle(BlockPos pos) {
		List<BlockPos> list = Lists.newArrayList();
		list.add(pos.north(4));
		list.add(pos.west(4));
		list.add(pos.south(4));
		list.add(pos.east(4));
		list.add(BlockPos.containing((pos.getX() + 3), pos.getY(), (pos.getZ() + 2)));
		list.add(BlockPos.containing((pos.getX() + 3), pos.getY(), (pos.getZ() - 2)));
		list.add(BlockPos.containing((pos.getX() - 3), pos.getY(), (pos.getZ() + 2)));
		list.add(BlockPos.containing((pos.getX() - 3), pos.getY(), (pos.getZ() - 2)));
		list.add(BlockPos.containing((pos.getX() + 2), pos.getY(), (pos.getZ() + 3)));
		list.add(BlockPos.containing((pos.getX() + 2), pos.getY(), (pos.getZ() - 3)));
		list.add(BlockPos.containing((pos.getX() - 2), pos.getY(), (pos.getZ() + 3)));
		list.add(BlockPos.containing((pos.getX() - 2), pos.getY(), (pos.getZ() - 3)));
		return list;
	}

	public static boolean hasArmor(LivingEntity target) {
		int i = 0;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (!target.getItemBySlot(slot).isEmpty()) {
				i++;
			}
		}
		return (i >= 4);
	}

	public static <T extends Mob> T convertArmor(ArmorStand target, EntityType<T> type, boolean equip) {
		T armor = type.create(target.level(), EntitySpawnReason.CONVERSION);
		armor.copyPosition(target);
		if (target.hasCustomName()) {
			armor.setCustomName(target.getCustomName());
			armor.setCustomNameVisible(target.isCustomNameVisible());
		}
		armor.setPersistenceRequired();
		armor.addEffect(new MobEffectInstance(SupernaturalEffects.POSSESSION, 999999, 0));
		armor.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
		armor.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
		if (equip) {
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				ItemStack stack = target.getItemBySlot(slot);
				if (!stack.isEmpty()) {
					armor.setItemSlot(slot, stack.copy());
					armor.setDropChance(slot, 1.0F);
					stack.setCount(0);
				}
			}
		}
		target.level().addFreshEntity(armor);
		target.discard();
		return armor;
	}
}