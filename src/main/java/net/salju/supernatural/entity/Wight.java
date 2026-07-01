package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.entity.ai.wight.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.DifficultyInstance;
import javax.annotation.Nullable;

public class Wight extends AbstractMinionEntity implements CrossbowAttackMob, RangedAttackMob {
	private static final EntityDataAccessor<Boolean> DATA_CHARGING_STATE = SynchedEntityData.defineId(Wight.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> CAPTAIN = SynchedEntityData.defineId(Wight.class, EntityDataSerializers.BOOLEAN);

	public Wight(EntityType<Wight> type, Level world) {
		super(type, world);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("Captain", this.isCaptain());
	}

	@Override
	public void readAdditionalSaveData(ValueInput tag) {
		super.readAdditionalSaveData(tag);
		this.getEntityData().set(CAPTAIN, tag.getBooleanOr("Captain", false));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_CHARGING_STATE, false);
		builder.define(CAPTAIN, false);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new WightCrossbowGoal<>(this, 1.0D, 12.0F));
		this.goalSelector.addGoal(3, new WightMeleeAttackGoal(this, 1.2, true));
		this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 8));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, false, new WightAttackSelector(this)));
	}

	@Override
	public void performRangedAttack(LivingEntity target, float f) {
        this.performCrossbowAttack(this, 2.0F);
	}

	@Override
	public void onCrossbowAttackPerformed() {
		this.noActionTime = 0;
	}

	@Override
	public void setChargingCrossbow(boolean check) {
		this.entityData.set(DATA_CHARGING_STATE, check);
	}

	public boolean isCharging() {
		return this.entityData.get(DATA_CHARGING_STATE);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (this.isAlive()) {
			if (this.level().isClientSide()) {
				this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
			}
		}
	}

	@Override
	protected void dropCustomDeathLoot(ServerLevel lvl, DamageSource source, boolean check) {
		super.dropCustomDeathLoot(lvl, source, check);
		if (this.isCaptain()) {
			this.spawnAtLocation(lvl, new ItemStack(SupernaturalItems.EBONSTEEL_KEY.get()));
		}
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor lvl, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
		if (Mth.nextInt(this.getRandom(), 1, 100) >= 85) {
			this.getEntityData().set(CAPTAIN, true);
		}
		this.populateDefaultEquipmentSlots(lvl.getRandom(), difficulty);
        this.setCanPickUpLoot(true);
		return super.finalizeSpawn(lvl, difficulty, reason, data);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.CROSSBOW));
        this.getOffhandItem().enchant(SupernaturalManager.getEnchantment(this.level(), "minecraft", "quick_charge"), 2);
		if (this.isCaptain()) {
			this.setItemSlot(EquipmentSlot.HEAD, SupernaturalManager.dyeHelmet(SupernaturalItems.GOTHIC_EBONSTEEL_HELMET.get()));
		} else {
			this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(SupernaturalItems.EBONSTEEL_HELMET.get()));
		}
		this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(SupernaturalItems.EBONSTEEL_CHESTPLATE.get()));
		this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(SupernaturalItems.EBONSTEEL_LEGGINGS.get()));
		this.setItemSlot(EquipmentSlot.FEET, new ItemStack(SupernaturalItems.EBONSTEEL_BOOTS.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.15F);
        this.setDropChance(EquipmentSlot.OFFHAND, 0.15F);
		this.setDropChance(EquipmentSlot.HEAD, 0.0F);
		this.setDropChance(EquipmentSlot.CHEST, 0.0F);
		this.setDropChance(EquipmentSlot.LEGS, 0.0F);
		this.setDropChance(EquipmentSlot.FEET, 0.0F);
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SupernaturalSounds.WIGHT_IDLE.get();
	}

	@Override
	public SoundEvent getHurtSound(DamageSource source) {
		return SupernaturalSounds.WIGHT_HURT.get();
	}

	@Override
	public SoundEvent getDeathSound() {
		return SupernaturalSounds.WIGHT_DEATH.get();
	}

	public boolean isCaptain() {
		return this.getEntityData().get(CAPTAIN);
	}

    public boolean shouldUseCrossbow() {
        return this.getTarget() != null && this.getTarget().distanceTo(this) >= 5.76F;
    }
}