package net.salju.supernatural.entity;

import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.init.SupernaturalItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Scourge extends AbstractHorse {
	public Scourge(EntityType<Scourge> type, Level world) {
		super(type, world);
        this.setSaddle();
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
	}

    @Override
    protected void addBehaviourGoals() {
        //
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
    public boolean canUseSlot(EquipmentSlot slot) {
        return true;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        if (slot.equals(EquipmentSlot.FEET))
            return getBoots(new ItemStack(SupernaturalItems.EBONSTEEL_BOOTS));
        return super.getItemBySlot(slot);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return isEyeInFluid(FluidTags.WATER) ? SoundEvents.SKELETON_HORSE_AMBIENT_WATER : SoundEvents.SKELETON_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SKELETON_HORSE_HURT;
    }

    @Override
    protected SoundEvent getSwimSound() {
        if (onGround()) {
            if (!isVehicle())
                return SoundEvents.SKELETON_HORSE_STEP_WATER;
            this.gallopSoundCounter++;
            if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0)
                return SoundEvents.SKELETON_HORSE_GALLOP_WATER;
            if (this.gallopSoundCounter <= 5)
                return SoundEvents.SKELETON_HORSE_STEP_WATER;
        }
        return SoundEvents.SKELETON_HORSE_SWIM;
    }

    @Override
    protected void playSwimSound(float p_30911_) {
        if (onGround()) {
            super.playSwimSound(0.3F);
        } else {
            super.playSwimSound(Math.min(0.1F, p_30911_ * 25.0F));
        }
    }

    @Override
    protected void playJumpSound() {
        if (isInWater()) {
            playSound(SoundEvents.SKELETON_HORSE_JUMP_WATER, 0.4F, 1.0F);
        } else {
            super.playJumpSound();
        }
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.96F;
    }

    public ItemStack getBoots(ItemStack stack) {
        stack.enchant(SupernaturalManager.getEnchantment(level(), "minecraft", "frost_walker"), 2);
        return stack;
    }

    public void setSaddle() {
        this.setBodyArmorItem(new ItemStack(SupernaturalItems.EBONSTEEL_HORSE_ARMOR.get()));
        this.setItemSlotAndDropWhenKilled(EquipmentSlot.SADDLE, new ItemStack(Items.SADDLE));
        this.setTamed(true);
    }
}