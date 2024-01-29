package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalEnchantments;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;

public class Vampire extends AbstractVampireEntity {
	public Vampire(EntityType<Vampire> type, Level world) {
		super(type, world);
		this.xpReward = 6;
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
		ItemStack sword = new ItemStack(Items.IRON_SWORD);
		if (Math.random() >= 0.85) {
			sword.enchant(SupernaturalEnchantments.LEECHING.get(), 2);
		} else if (Math.random() >= 0.45) {
			sword.enchant(SupernaturalEnchantments.LEECHING.get(), 1);
		}
		this.setItemInHand(InteractionHand.MAIN_HAND, sword);
		return super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.35);
		builder = builder.add(Attributes.FOLLOW_RANGE, 12.0D);
		builder = builder.add(Attributes.MAX_HEALTH, 24);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		return builder;
	}
}