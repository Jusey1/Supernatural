package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.entity.ai.wight.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.DifficultyInstance;

public class Wight extends AbstractMinionEntity implements RangedAttackMob {
	public Wight(EntityType<Wight> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
        this.goalSelector.addGoal(1, new WightBowAttackGoal<>(this, 1.2, 30, 12.0F));
        this.goalSelector.addGoal(2, new WightMeleeAttackGoal(this, 1.2, true));
        this.goalSelector.addGoal(3, new WightFollowGoal(this, 1.2, 10.0F, 2.0F));
		this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 8));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, false, new WightAttackSelector(this)));
	}

	@Override
	public void performRangedAttack(LivingEntity target, float f) {
        ItemStack weapon = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem));
        ItemStack ammo = this.getProjectile(weapon);
        AbstractArrow arrow = ProjectileUtil.getMobArrow(this, ammo, f, weapon);
        if (arrow instanceof Arrow effect) {
            effect.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 600));
        }
        if (weapon.getItem() instanceof ProjectileWeaponItem item) {
            arrow = item.customArrow(arrow, ammo, weapon);
        }
        double d = target.getX() - this.getX();
        double db = target.getY(0.3333333333333333) - arrow.getY();
        double dd = target.getZ() - this.getZ();
        double dr = Math.sqrt(d * d + dd * dd);
        arrow.shoot(d, db + dr * 0.2F, dd, 1.6F, (14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(arrow);
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
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(SupernaturalItems.EBONSTEEL_HELMET.get()));
        double d = Math.random();
        if (d <= 0.05 || d >= 0.95) {
            this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(SupernaturalItems.EBONSTEEL_CHESTPLATE.get()));
        }
        if (d <= 0.15) {
            this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(SupernaturalItems.EBONSTEEL_LEGGINGS.get()));
            this.setItemSlot(EquipmentSlot.FEET, new ItemStack(SupernaturalItems.EBONSTEEL_BOOTS.get()));
        }
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
}