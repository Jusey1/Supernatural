package net.salju.supernatural.entity;

import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.entity.ai.spells.AbstractSpellcasterGoal;
import net.salju.supernatural.entity.ai.spells.spooky.*;
import net.salju.supernatural.entity.ai.targets.SpookyAttackSelector;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;

public class Spooky extends AbstractMinionEntity implements Spellcaster {
    public static final EntityDataAccessor<Integer> SPELL_TICK = SynchedEntityData.defineId(Spooky.class, EntityDataSerializers.INT);

	public Spooky(EntityType<Spooky> type, Level world) {
		super(type, world);
		this.moveControl = new FlyingMoveControl(this, 20, true);
	}

    @Override
    public void addAdditionalSaveData(ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SpellTick", this.getSpellTick());
    }

    @Override
    public void readAdditionalSaveData(ValueInput tag) {
        super.readAdditionalSaveData(tag);
        if (tag.getInt("SpellTick").isPresent()) {
            this.setSpellTick(tag.getInt("SpellTick").get());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPELL_TICK, 0);
    }

	@Override
	protected PathNavigation createNavigation(Level lvl) {
		return new FlyingPathNavigation(this, lvl);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new AbstractSpellcasterGoal(this));
        this.goalSelector.addGoal(1, new SpookyPossessionSpellGoal(this));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomFlyingGoal(this, 1.2));
		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, true, new SpookyAttackSelector(this)));
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SoundEvents.VEX_AMBIENT;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.VEX_HURT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.VEX_DEATH;
	}

    @Override
    public SoundEvent getCastingSoundEvent() {
        return SoundEvents.ILLUSIONER_CAST_SPELL;
    }

	@Override
	public void aiStep() {
		super.aiStep();
		this.setNoGravity(true);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (this.isAlive()) {
			if (this.getTarget() != null && this.getTarget().isAlive()) {
				if (this.getTarget().hasEffect(MobEffects.GLOWING) || this.getTarget().hasEffect(SupernaturalEffects.POSSESSION)) {
					this.setTarget(null);
				} else if (this.getTarget() instanceof ArmorStand && !SupernaturalManager.hasArmor(this.getTarget())) {
                    this.setTarget(null);
                }
			}
            if (this.getSpellTick() > 0) {
                this.setSpellTick(this.getSpellTick() - 1);
            }
            if (this.isCastingSpell()) {
                this.applySpellEffects(this.getX(), this.getY() + 0.35, this.getZ());
            }
			if (this.level() instanceof ServerLevel lvl && !this.isTamed() && this.level().isBrightOutside() && this.level().canSeeSkyFromBelowWater(BlockPos.containing(this.getX(), this.getY(), this.getZ()))) {
				this.playSound(SupernaturalSounds.SPOOK_POOF.get(), 1.0F, 1.0F);
				this.discard();
				double r = this.random.nextGaussian() * 0.02D;
				lvl.sendParticles(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 10, r, r, r, 0.25);
			}
		}
	}

    @Override
    public void setNoGravity(boolean check) {
        super.setNoGravity(true);
    }

    @Override
    public void applySpellEffects(double x, double y, double z) {
        if (this.level().isClientSide()) {
            this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, this.getSpellColor()), x, y, z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void setSpellTick(int i) {
        this.getEntityData().set(SPELL_TICK, i);
    }

    @Override
    public boolean causeFallDamage(double d, float f, DamageSource source) {
        return false;
    }

    @Override
    public boolean isCastingSpell() {
        return this.getSpellTick() > 0;
    }

    @Override
    public int getSpellTick() {
        return this.getEntityData().get(SPELL_TICK);
    }

    @Override
    public int getSpellColor() {
        return -6697729;
    }

    @Override
    protected float getSoundVolume() {
        return 0.35F;
    }
}