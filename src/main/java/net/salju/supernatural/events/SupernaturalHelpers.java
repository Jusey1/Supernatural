package net.salju.supernatural.events;

import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.block.RitualBlockEntity;

import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.SectionPos;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.HashMultimap;

public class SupernaturalHelpers {
	public static boolean isVampire(LivingEntity target) {
		return (target.getPersistentData().getBoolean("isVampire") || target.hasEffect(SupernaturalEffects.VAMPIRISM.get()) || target.getType().is(SupernaturalTags.VAMPIRE));
	}

	public static void setVampire(Player player, boolean check) {
		if (!check) {
			player.getPersistentData().remove("isVampire");
			player.removeEffect(SupernaturalEffects.VAMPIRISM.get());
			player.getAttributes().removeAttributeModifiers(createVampire());
		} else {
			player.getPersistentData().putBoolean("isVampire", true);
		}
	}

	public static void addVampireEffects(Player player) {
		player.addEffect(new MobEffectInstance(SupernaturalEffects.VAMPIRISM.get(), 10, 0, false, false));
		player.getAttributes().addTransientAttributeModifiers(createVampire());
		if (player.isCrouching()) {
			player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 10, 0, false, false));
		}
	}

	private static Multimap<Attribute, AttributeModifier> createVampire() {
		Multimap<Attribute, AttributeModifier> stats = HashMultimap.create();
		if (SupernaturalConfig.DAMAGE.get() > 0) {
			stats.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("221b1306-90e7-11ee-b9d1-0242ac120002"), "V-DPS", ((float) SupernaturalConfig.DAMAGE.get()), AttributeModifier.Operation.ADDITION));
		}
		if (SupernaturalConfig.SPEED.get() > 0) {
			stats.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("6b4966f4-90e7-11ee-b9d1-0242ac120002"), "V-SPEED", ((float) SupernaturalConfig.SPEED.get() / 100), AttributeModifier.Operation.MULTIPLY_TOTAL));
		}
		return stats;
	}

	public static ItemStack setUUID(ItemStack stack, LivingEntity target) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putUUID("SupernaturalUUID", target.getUUID());
		return stack;
	}

	@Nullable
	public static UUID getUUID(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.hasUUID("SupernaturalUUID")) {
			return tag.getUUID("SupernaturalUUID");
		}
		return null;
	}

	public static ItemStack setSoul(ItemStack stack, LivingEntity target) {
		CompoundTag tag = stack.getOrCreateTag();
		CompoundTag mobster = new CompoundTag();
		target.save(mobster);
		mobster.remove("ActiveEffects");
		mobster.remove("Passengers");
		mobster.remove("DeathTime");
		mobster.remove("Health");
		mobster.remove("Leash");
		mobster.remove("Fire");
		mobster.remove("UUID");
		tag.putString("Soul", target.getType().toString());
		tag.put("SoulTag", mobster);
		tag.putString("Soulgem", getSoulLevel(target));
		return stack;
	}

	public static String getSoul(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.getString("Soul") != null) {
			return tag.getString("Soul");
		}
		return "";
	}

	public static CompoundTag getSoulTag(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.get("SoulTag") != null) {
			return (CompoundTag) tag.get("SoulTag");
		}
		return new CompoundTag();
	}

	public static String getSoulLevel(LivingEntity target) {
		if (target.getType().is(SupernaturalTags.GRAND)) {
			return "soulgem.supernatural.grand";
		} else if (target.getMaxHealth() >= 40.0F || target instanceof Raider) {
			return "soulgem.supernatural.greater";
		} else if (target.getMaxHealth() >= 20.0F) {
			return "soulgem.supernatural.common";
		} else if (target.getMaxHealth() >= 12.0F) {
			return "soulgem.supernatural.lesser";
		}
		return "soulgem.supernatural.petty";
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
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.getString("Soulgem") != null) {
			return tag.getString("Soulgem");
		}
		return "";
	}

	@Nullable
	public static RitualBlockEntity getAltar(BlockPos pos, Level world, int radius, Item item) {
		int minX = SectionPos.blockToSectionCoord(pos.getX() - radius);
		int minZ = SectionPos.blockToSectionCoord(pos.getZ() - radius);
		int maxX = SectionPos.blockToSectionCoord(pos.getX() + radius);
		int maxZ = SectionPos.blockToSectionCoord(pos.getZ() + radius);
		for (int chunkX = minX; chunkX <= maxX; chunkX++) {
			for (int chunkZ = minZ; chunkZ <= maxZ; chunkZ++) {
				LevelChunk chunk = world.getChunkSource().getChunk(chunkX, chunkZ, false);
				if (chunk != null) {
					for (BlockPos poz : chunk.getBlockEntitiesPos()) {
						if (world.getBlockEntity(poz) instanceof RitualBlockEntity target) {
							if (target.getItem(0).is(item) && pos.closerThan(poz, radius)) {
								return target;
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static boolean hasArmor(LivingEntity target) {
		Iterable<ItemStack> armor = target.getArmorSlots();
		int i = 0;
		for (ItemStack stack : armor) {
			if (!stack.isEmpty()) {
				i++;
			}
		}
		return (i >= 4);
	}

	public static <T extends Mob> T convertArmor(ArmorStand target, EntityType<T> type, boolean equip) {
		T armor = type.create(target.level());
		armor.copyPosition(target);
		if (target.hasCustomName()) {
			armor.setCustomName(target.getCustomName());
			armor.setCustomNameVisible(target.isCustomNameVisible());
		}
		armor.setPersistenceRequired();
		armor.addEffect(new MobEffectInstance(SupernaturalEffects.POSSESSION.get(), 999999, 0));
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