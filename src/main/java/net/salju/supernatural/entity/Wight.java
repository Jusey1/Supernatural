package net.salju.supernatural.entity;

import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.entity.ai.abstractai.AbstractSpellcasterGoal;
import net.salju.supernatural.entity.ai.wight.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.DifficultyInstance;
import javax.annotation.Nullable;

public class Wight extends AbstractSpellcasterEntity implements Enemy, CrossbowAttackMob, RangedAttackMob {
	private static final EntityDataAccessor<Boolean> DATA_CHARGING_STATE = SynchedEntityData.defineId(Wight.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> CAPTAIN = SynchedEntityData.defineId(Wight.class, EntityDataSerializers.BOOLEAN);
	private ItemStack primary = new ItemStack(Items.IRON_SWORD);
	private ItemStack secondary = new ItemStack(Items.CROSSBOW);

	public Wight(EntityType<Wight> type, Level world) {
		super(type, world);
		this.xpReward = 10;
		this.setPathfindingMalus(PathType.WATER, -1.0F);
	}

    @Override
    public void addAdditionalSaveData(ValueOutput tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("Captain", this.isCaptain());
        if (!this.getPrimary().isEmpty()) {
            tag.store("Primary", ItemStack.CODEC, this.getPrimary());
        }
        if (!this.getSecondary().isEmpty()) {
            tag.store("Secondary", ItemStack.CODEC, this.getSecondary());
        }
	}

    @Override
    public void readAdditionalSaveData(ValueInput tag) {
		super.readAdditionalSaveData(tag);
		this.getEntityData().set(CAPTAIN, tag.getBooleanOr("Captain", false));
        if (tag.read("Primary", ItemStack.CODEC).isPresent()) {
            this.setPrimary(tag.read("Primary", ItemStack.CODEC).orElse(ItemStack.EMPTY));
        }
        if (tag.read("Secondary", ItemStack.CODEC).isPresent()) {
            this.setSecondary(tag.read("Secondary", ItemStack.CODEC).orElse(ItemStack.EMPTY));
        }
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
		this.goalSelector.addGoal(0, new AbstractSpellcasterGoal(this));
		this.goalSelector.addGoal(0, new WightTeleportSpellGoal(this));
		this.goalSelector.addGoal(1, new WightLifeSpellGoal(this));
		this.goalSelector.addGoal(2, new WightCrossbowGoal<>(this, 1.0D, 12.0F));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2, true));
		this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 8));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this, Wight.class).setAlertOthers());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	public void performRangedAttack(LivingEntity target, float f) {
		if (this.getMainHandItem().getItem() instanceof CrossbowItem) {
			this.performCrossbowAttack(this, 2.0F);
		}
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
				if (this.isCastingSpell()) {
					float f = this.yBodyRot * ((float) Math.PI / 180F) + Mth.cos((float) this.tickCount * 0.6662F) * 0.25F;
					if (this.isLeftHanded()) {
						this.applySpellEffects(this.getX() - (double) Mth.cos(f) * 0.6 * (double) this.getScale(), this.getY() + 1.8 * (double) this.getScale(), this.getZ() - (double) Mth.sin(f) * 0.6 * (double) this.getScale());
					} else {
						this.applySpellEffects(this.getX() + (double) Mth.cos(f) * 0.6 * (double) this.getScale(), this.getY() + 1.8 * (double) this.getScale(), this.getZ() + (double) Mth.sin(f) * 0.6 * (double) this.getScale());
					}
				}
				this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
			}
			if (this.getTarget() != null && this.getTarget().isAlive() && this.shouldSwapToWeapon(this.getTarget())) {
                this.setItemSlot(EquipmentSlot.MAINHAND, this.getSwapToWeapon());
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
		int i = Mth.nextInt(this.getRandom(), 1, 100);
		if (i >= 85 || reason.equals(EntitySpawnReason.CONVERSION)) {
			this.getEntityData().set(CAPTAIN, true);
		}
		this.getSecondary().enchant(SupernaturalManager.getEnchantment(lvl.getLevel(), "minecraft", "quick_charge"), 2);
		this.populateDefaultEquipmentSlots(lvl.getRandom(), difficulty);
		return super.finalizeSpawn(lvl, difficulty, reason, data);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance difficulty) {
		this.setItemSlot(EquipmentSlot.MAINHAND, this.getPrimary());
		if (this.isCaptain()) {
			this.setItemSlot(EquipmentSlot.HEAD, SupernaturalManager.dyeHelmet(SupernaturalItems.GOTHIC_EBONSTEEL_HELMET.get()));
		} else {
			this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(SupernaturalItems.EBONSTEEL_HELMET.get()));
		}
		this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(SupernaturalItems.EBONSTEEL_CHESTPLATE.get()));
		this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(SupernaturalItems.EBONSTEEL_LEGGINGS.get()));
		this.setItemSlot(EquipmentSlot.FEET, new ItemStack(SupernaturalItems.EBONSTEEL_BOOTS.get()));
		this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
		this.setDropChance(EquipmentSlot.HEAD, 0.0F);
		this.setDropChance(EquipmentSlot.CHEST, 0.0F);
		this.setDropChance(EquipmentSlot.LEGS, 0.0F);
		this.setDropChance(EquipmentSlot.FEET, 0.0F);
	}

	@Override
	public ItemStack getProjectile(ItemStack stack) {
		if (stack.getItem() instanceof ProjectileWeaponItem weapon) {
			ItemStack extra = ProjectileWeaponItem.getHeldProjectile(this, weapon.getSupportedHeldProjectiles(stack));
			return extra.isEmpty() ? new ItemStack(Items.ARROW) : extra;
		} else {
			return super.getProjectile(stack);
		}
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

	@Override
	public SoundEvent getCastingSoundEvent() {
		return SoundEvents.EVOKER_CAST_SPELL;
	}

	public ItemStack getPrimary() {
		return this.primary;
	}

	public ItemStack getSecondary() {
		return this.secondary;
	}

    public ItemStack getSwapToWeapon() {
        return this.hasCrossbow() ? this.getPrimary() : this.getSecondary();
    }

    public boolean shouldSwapToWeapon(LivingEntity target) {
        if (this.isCastingSpell()) {
            return false;
        } else if (this.isPassenger()) {
            if (this.getVehicle() instanceof AbstractHorse) {
                return this.hasCrossbow();
            } else {
                return !this.hasCrossbow();
            }
        } else if (this.hasCrossbow()) {
            return this.distanceTo(target) <= 5.76 && !target.isInWater();
        } else {
            return this.distanceTo(target) >= 8.25 || target.isInWater();
        }
    }

	public void setPrimary(ItemStack stack) {
		this.primary = stack;
	}

	public void setSecondary(ItemStack stack) {
		this.secondary = stack;
	}

	public boolean isCaptain() {
		return this.getEntityData().get(CAPTAIN);
	}

    public boolean hasCrossbow() {
        return this.getMainHandItem().getItem() instanceof CrossbowItem;
    }
}