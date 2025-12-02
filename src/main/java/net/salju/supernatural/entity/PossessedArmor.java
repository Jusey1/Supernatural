package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.entity.ai.targets.MinionAttackSelector;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import javax.annotation.Nullable;

public class PossessedArmor extends AbstractMinionEntity {
	public PossessedArmor(EntityType<PossessedArmor> type, Level world) {
		super(type, world);
		this.setPersistenceRequired();
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 6.0F));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, true, new MinionAttackSelector(this)));
	}

	@Override
	public SoundEvent getHurtSound(DamageSource source) {
		return SupernaturalSounds.ARMOR_HURT.get();
	}

	@Override
	public SoundEvent getDeathSound() {
		return SupernaturalSounds.ARMOR_DEATH.get();
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.is(DamageTypes.FALL) || source.is(DamageTypes.CACTUS) || source.is(DamageTypes.DROWN)) {
			return false;
		}
		return super.hurt(source, amount);
	}

	@Override
	protected void dropCustomDeathLoot(ServerLevel lvl, DamageSource src, boolean check) {
		this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (!this.getItemBySlot(slot).isEmpty()) {
				this.spawnAtLocation(this.getItemBySlot(slot));
				this.setItemSlot(slot, ItemStack.EMPTY);
			}
		}
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data) {
		this.populateDefaultEquipmentSlots(world.getRandom(), difficulty);
		this.addEffect(new MobEffectInstance(SupernaturalEffects.POSSESSION, Integer.MAX_VALUE, 0));
        return super.finalizeSpawn(world, difficulty, reason, data);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance difficulty) {
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
		this.setItemSlot(EquipmentSlot.HEAD, SupernaturalManager.dyeHelmet(SupernaturalItems.GOTHIC_IRON_HELMET.get()));
		this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
		this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
		this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (!this.hasEffect(SupernaturalEffects.POSSESSION) || !SupernaturalManager.hasArmor(this)) {
			this.kill();
		}
		if (this.isAlive() && this.isTamed() && this.getEffect(SupernaturalEffects.POSSESSION).getDuration() <= 10) {
			this.addEffect(new MobEffectInstance(SupernaturalEffects.POSSESSION, Integer.MAX_VALUE, 0));
		}
	}
}