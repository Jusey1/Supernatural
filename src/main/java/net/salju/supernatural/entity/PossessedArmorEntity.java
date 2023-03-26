package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalModSounds;
import net.salju.supernatural.init.SupernaturalModMobEffects;
import net.salju.supernatural.init.SupernaturalModEntities;
import net.salju.supernatural.init.SupernaturalItems;

import net.minecraftforge.network.PlayMessages;

import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;

public class PossessedArmorEntity extends Monster {
	public PossessedArmorEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(SupernaturalModEntities.POSSESSED_ARMOR.get(), world);
	}

	public PossessedArmorEntity(EntityType<PossessedArmorEntity> type, Level world) {
		super(type, world);
		xpReward = 5;
		setPersistenceRequired();
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, (float) 6));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, true, true));
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return SupernaturalModSounds.ARMOR_HURT.get();
	}

	@Override
	public SoundEvent getDeathSound() {
		return SupernaturalModSounds.ARMOR_DEATH.get();
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source == DamageSource.FALL)
			return false;
		if (source == DamageSource.CACTUS)
			return false;
		if (source == DamageSource.DROWN)
			return false;
		return super.hurt(source, amount);
	}

	@Override
	public void die(DamageSource source) {
		super.die(source);
		LevelAccessor world = this.level;
		ItemStack empty = (ItemStack.EMPTY);
		empty.setCount(1);
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		if (world instanceof Level lvl && !lvl.isClientSide()) {
			ItemEntity helm = new ItemEntity(lvl, x, y, z, (this.getItemBySlot(EquipmentSlot.HEAD)));
			helm.setPickUpDelay(10);
			lvl.addFreshEntity(helm);
			ItemEntity chest = new ItemEntity(lvl, x, y, z, (this.getItemBySlot(EquipmentSlot.CHEST)));
			chest.setPickUpDelay(10);
			lvl.addFreshEntity(chest);
			ItemEntity legs = new ItemEntity(lvl, x, y, z, (this.getItemBySlot(EquipmentSlot.LEGS)));
			legs.setPickUpDelay(10);
			lvl.addFreshEntity(legs);
			ItemEntity boots = new ItemEntity(lvl, x, y, z, (this.getItemBySlot(EquipmentSlot.FEET)));
			boots.setPickUpDelay(10);
			lvl.addFreshEntity(boots);
		}
		this.setItemSlot(EquipmentSlot.HEAD, empty);
		this.setItemSlot(EquipmentSlot.CHEST, empty);
		this.setItemSlot(EquipmentSlot.LEGS, empty);
		this.setItemSlot(EquipmentSlot.FEET, empty);
		this.setItemInHand(InteractionHand.MAIN_HAND, empty);
		this.setItemInHand(InteractionHand.OFF_HAND, empty);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
		SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
		RandomSource randy = world.getRandom();
		this.populateDefaultEquipmentSlots(randy, difficulty);
		this.addEffect(new MobEffectInstance(SupernaturalModMobEffects.POSSESSION.get(), 999999, 0));
		return retval;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance souls) {
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
		this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
		this.populateDefaultEquipmentEnchantments(randy, souls);
		this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(SupernaturalItems.GOTHIC_IRON_HELMET.get()));
		this.setDropChance(EquipmentSlot.HEAD, 0.0F);
		this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
		this.setDropChance(EquipmentSlot.CHEST, 0.0F);
		this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
		this.setDropChance(EquipmentSlot.LEGS, 0.0F);
		this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
		this.setDropChance(EquipmentSlot.FEET, 0.0F);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (!(this.hasEffect(SupernaturalModMobEffects.POSSESSION.get()))) {
			this.kill();
		}
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.25);
		builder = builder.add(Attributes.MAX_HEALTH, 20);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 1);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
		return builder;
	}
}