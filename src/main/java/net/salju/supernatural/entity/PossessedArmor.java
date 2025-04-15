package net.salju.supernatural.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class PossessedArmor extends AbstractGolem {
	private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> OWNER = SynchedEntityData.defineId(PossessedArmor.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);

	public PossessedArmor(EntityType<PossessedArmor> type, Level world) {
		super(type, world);
		this.setPersistenceRequired();
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, (float) 6));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, true, new PossessedArmor.PossessedAttackSelector(this)));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.getOwner() != null) {
			this.getOwner().store(tag, "Player");
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		EntityReference<LivingEntity> target = EntityReference.readWithOldOwnerConversion(tag, "Player", this.level());
		if (target != null) {
			try {
				this.entityData.set(OWNER, Optional.of(target));
			} catch (Throwable throwable) {
				//
			}
		} else {
			this.entityData.set(OWNER, Optional.empty());
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(OWNER, Optional.empty());
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
	public boolean hurtServer(ServerLevel lvl, DamageSource source, float amount) {
		if (source.is(DamageTypes.FALL) || source.is(DamageTypes.CACTUS) || source.is(DamageTypes.DROWN)) {
			return false;
		}
		return super.hurtServer(lvl, source, amount);
	}

	@Override
	protected void dropCustomDeathLoot(ServerLevel lvl, DamageSource src, boolean check) {
		this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		this.spawnAtLocation(lvl, this.getItemBySlot(EquipmentSlot.HEAD));
		this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
		this.spawnAtLocation(lvl, this.getItemBySlot(EquipmentSlot.BODY));
		this.setItemSlot(EquipmentSlot.BODY, ItemStack.EMPTY);
		this.spawnAtLocation(lvl, this.getItemBySlot(EquipmentSlot.LEGS));
		this.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
		this.spawnAtLocation(lvl, this.getItemBySlot(EquipmentSlot.FEET));
		this.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
		this.populateDefaultEquipmentSlots(world.getRandom(), difficulty);
		this.addEffect(new MobEffectInstance(SupernaturalEffects.POSSESSION, Integer.MAX_VALUE, 0));
		return super.finalizeSpawn(world, difficulty, reason, data);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance souls) {
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
		this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(SupernaturalItems.GOTHIC_IRON_HELMET.get()));
		this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
		this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
		this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (this.level() instanceof ServerLevel lvl) {
			if (!this.hasEffect(SupernaturalEffects.POSSESSION) || !SupernaturalManager.hasArmor(this)) {
				this.kill(lvl);
			}
			if (this.isAlive() && this.isTamed() && this.getEffect(SupernaturalEffects.POSSESSION).getDuration() <= 10) {
				this.addEffect(new MobEffectInstance(SupernaturalEffects.POSSESSION, Integer.MAX_VALUE, 0));
			}
		}
	}

	public void aiStep() {
		this.updateSwingTime();
		super.aiStep();
	}

	public void setOwner(@Nullable LivingEntity target) {
		this.entityData.set(OWNER, Optional.ofNullable(target).map(EntityReference::new));
	}

	public void setOwnerDirectly(@Nullable EntityReference<LivingEntity> target) {
		this.entityData.set(OWNER, Optional.ofNullable(target));
	}

	@Nullable
	public EntityReference<LivingEntity> getOwner() {
		return this.entityData.get(OWNER).orElse(null);
	}

	public boolean isTamed() {
		return this.getOwner() != null;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.ATTACK_DAMAGE, 1).add(Attributes.KNOCKBACK_RESISTANCE, 0.5).add(Attributes.MOVEMENT_SPEED, 0.25);
	}

	static class PossessedAttackSelector implements TargetingConditions.Selector {
		private final PossessedArmor armor;

		public PossessedAttackSelector(PossessedArmor source) {
			this.armor = source;
		}

		public boolean test(@Nullable LivingEntity target, ServerLevel lvl) {
			if (armor.isTamed() && armor.getOwner() != null) {
				Player player = armor.level().getPlayerByUUID(armor.getOwner().getUUID());
				if (player != null && player.getLastHurtByMob() != null && player.getLastHurtByMob().isAlive()) {
					return (target == player.getLastHurtByMob());
				}
				return (target.getType().is(SupernaturalTags.ARMOR));
			}
			return (target instanceof Player);
		}
	}
}