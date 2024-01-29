package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalEnchantments;
import net.salju.supernatural.entity.ai.SupernaturalSummonVexSpellGoal;
import net.salju.supernatural.entity.ai.SupernaturalNecroSpellGoal;

import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;

public class Necromancer extends AbstractVampireEntity {
	public Necromancer(EntityType<Necromancer> type, Level world) {
		super(type, world);
		this.xpReward = 50;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new SupernaturalSummonVexSpellGoal(this));
		this.goalSelector.addGoal(2, new SupernaturalNecroSpellGoal(this));
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
		ItemStack sword = new ItemStack(Items.IRON_SWORD);
		sword.enchant(SupernaturalEnchantments.LEECHING.get(), 4);
		this.setItemInHand(InteractionHand.MAIN_HAND, sword);
		this.setDropChance(EquipmentSlot.MAINHAND, 1.0F);
		return super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.35);
		builder = builder.add(Attributes.FOLLOW_RANGE, 12.0D);
		builder = builder.add(Attributes.MAX_HEALTH, 100);
		builder = builder.add(Attributes.ARMOR, 2);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		return builder;
	}
}