package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalItems;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;

public class Cannonball extends ThrowableItemProjectile {
	public static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(Cannon.class, EntityDataSerializers.INT);

	public Cannonball(EntityType<? extends Cannonball> type, Level world) {
		super(type, world);
	}

	public Cannonball(Level world, LivingEntity cannon, int i) {
		super(SupernaturalMobs.CANNONBALL.get(), cannon, world);
		this.getEntityData().set(TYPE, i);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("CannonballType", this.getEntityData().get(TYPE));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.getEntityData().set(TYPE, tag.getInt("CannonballType"));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(TYPE, 0);
	}

	@Override
	public void shoot(double x, double y, double z, float f1, float f2) {
		Vec3 vec3 = (new Vec3(x, y, z)).normalize().scale((double) f1);
		this.setDeltaMovement(vec3);
		double d0 = vec3.horizontalDistance();
		this.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI)));
		this.setXRot((float) (Mth.atan2(vec3.y, d0) * (double) (180F / (float) Math.PI)));
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
	}

	@Override
	protected void onHitEntity(EntityHitResult rez) {
		float damage = (this.isCopper() ? 15.0F : (float) Mth.nextInt(this.level().getRandom(), 10, 30));
		if (this.getOwner() instanceof Cannon target && target.getOwner() instanceof Player) {
			if (rez.getEntity() instanceof Enemy || target.getOwner().getLastHurtMob() == rez.getEntity()) {
				rez.getEntity().hurt(this.damageSources().thrown(this, target.getOwner()), damage);
			}
		} else {
			rez.getEntity().hurt(this.damageSources().thrown(this, null), damage);
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult rez) {
		super.onHitBlock(rez);
		if (this.level() instanceof ServerLevel lvl) {
			this.playSound(SoundEvents.ANVIL_BREAK, 1.0F, 0.4F / (this.level().getRandom().nextFloat() * 0.4F + 0.8F));
			for (int i = 0; i < this.level().getRandom().nextInt(1) + 1; ++i) {
				lvl.sendParticles((this.isInWaterOrBubble() ? ParticleTypes.BUBBLE : ParticleTypes.LAVA), this.getX(), this.getEyeY(), this.getZ(), 5, 0.1, 0.15, 0.1, 0);
			}
		}
		this.discard();
	}

	@Override
	protected Item getDefaultItem() {
		return (this.isCopper() ? SupernaturalItems.COPPER_CANNONBALL.get() : SupernaturalItems.CANNONBALL.get());
	}

	@Override
	protected float getGravity() {
		return (this.isInWaterOrBubble() ? 0.025F : 0.0025F);
	}

	public boolean isCopper() {
		return this.getEntityData().get(TYPE) == 1;
	}
}