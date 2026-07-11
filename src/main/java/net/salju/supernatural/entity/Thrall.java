package net.salju.supernatural.entity;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.entity.ai.thrall.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.DifficultyInstance;

public class Thrall extends AbstractMinionEntity {
    private static final Identifier SPEED_BABY_ID = Identifier.fromNamespaceAndPath(Supernatural.MODID, "baby");
    private static final EntityDataAccessor<Boolean> BABY = SynchedEntityData.defineId(Thrall.class, EntityDataSerializers.BOOLEAN);

	public Thrall(EntityType<Thrall> type, Level world) {
		super(type, world);
	}

    @Override
    public void addAdditionalSaveData(ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Baby", this.isBaby());
    }

    @Override
    public void readAdditionalSaveData(ValueInput tag) {
        super.readAdditionalSaveData(tag);
        this.setBaby(tag.getBooleanOr("Baby", false));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BABY, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (BABY.equals(data)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(data);
    }

	@Override
	protected void registerGoals() {
		super.registerGoals();
        this.goalSelector.addGoal(0, new ThrallAltarGoal(this, 1.2, 12, 3));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, true));
        this.goalSelector.addGoal(3, new ThrallFollowGoal(this, 1.2, 10.0F, 2.0F));
		this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 8));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, false, new ThrallAttackSelector(this)));
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
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.getMainHandItem().enchant(SupernaturalManager.getEnchantment(this.level(), Supernatural.MODID, "soulbinding"), 1);
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(SupernaturalItems.EBONSTEEL_HELMET.get()));
        if (randy.nextFloat() < 0.12F) {
            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.AMETHYST_SHARD));
        }
        this.setDropChance(EquipmentSlot.OFFHAND, 1.0F);
        this.setDropChance(EquipmentSlot.HEAD, 0.0F);
	}

    @Override
    public void setBaby(boolean check) {
        this.getEntityData().set(BABY, check);
        if (this.level() instanceof ServerLevel lvl) {
            AttributeInstance ats = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (ats != null) {
                ats.removeModifier(SPEED_BABY_ID);
                if (check) {
                    ats.addTransientModifier(new AttributeModifier(SPEED_BABY_ID, 0.5F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                }
            }
        }
    }

    @Override
    public boolean doHurtTarget(ServerLevel lvl, Entity target) {
        if (super.doHurtTarget(lvl, target)) {
            if (target.canFreeze()) {
                if (target instanceof LivingEntity bob) {
                    SupernaturalManager.hurtWithFrostbite(bob, 125);
                }
            }
            return true;
        }
        return false;
    }

	@Override
	public SoundEvent getAmbientSound() {
		return SupernaturalSounds.THRALL_IDLE.get();
	}

	@Override
	public SoundEvent getHurtSound(DamageSource source) {
		return SupernaturalSounds.THRALL_HURT.get();
	}

	@Override
	public SoundEvent getDeathSound() {
		return SupernaturalSounds.THRALL_DEATH.get();
	}

    @Override
    public boolean isBaby() {
        return this.getEntityData().get(BABY);
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel lvl) {
        if (this.isBaby()) {
            this.xpReward = this.xpReward * 3;
        }
        return super.getBaseExperienceReward(lvl);
    }
}