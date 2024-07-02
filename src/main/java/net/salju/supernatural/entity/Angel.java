package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.events.SupernaturalManager;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.BlockPos;

import java.util.List;

public class Angel extends Mob {
	public static final EntityDataAccessor<Integer> POSE = SynchedEntityData.defineId(Angel.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> CURSED = SynchedEntityData.defineId(Angel.class, EntityDataSerializers.BOOLEAN);

	public Angel(EntityType<Angel> type, Level world) {
		super(type, world);
		this.setMaxUpStep(1.0F);
		this.setPersistenceRequired();
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Angel", this.getAngelPose());
		tag.putBoolean("Cursed", this.isCursed());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.getEntityData().set(POSE, tag.getInt("Angel"));
		this.getEntityData().set(CURSED, tag.getBoolean("Cursed"));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(POSE, 1);
		this.entityData.define(CURSED, false);
	}

	@Override
	public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
		if (player.getItemInHand(hand).getItem() instanceof PickaxeItem && !this.isCursed()) {
			if (player.isCrouching()) {
				if (this.getAngelPose() >= 7) {
					this.getEntityData().set(POSE, 1);
				} else {
					this.getEntityData().set(POSE, this.getAngelPose() + 1);
				}
			} else {
				float rot = (float) Mth.floor((Mth.wrapDegrees(player.getYRot() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
				this.setYRot(rot);
				this.setYBodyRot(rot);
				this.setYHeadRot(rot);
				this.yRotO = rot;
				this.yBodyRotO = rot;
				this.yHeadRotO = rot;
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		boolean check = (source.getEntity() instanceof LivingEntity target && target.getMainHandItem().getItem() instanceof PickaxeItem && !this.isCursed());
		if (check || source.is(DamageTypes.FELL_OUT_OF_WORLD) || source.is(DamageTypes.EXPLOSION) || source.isCreativePlayer() || source.getEntity() instanceof Creeper) {
			return super.hurt(source, amount);
		}
		return false;
	}

	@Override
	public void die(DamageSource source) {
		super.die(source);
		this.showBreakingParticles();
		this.discard();
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (this.level() instanceof ServerLevel lvl && this.isAlive() && this.isEffectiveAi()) {
			if (this.isCursed()) {
				Player player = lvl.getNearestPlayer(this, 32);
				if (player != null && !player.isCreative() && !player.isSpectator() && !SupernaturalManager.isVampire(player) && !SupernaturalManager.isWerewolf(player)) {
					if (player.isCloseEnough(this, 1)) {
						if (player.isAlive()) {
							this.getEntityData().set(POSE, 0);
							player.hurt(this.damageSources().mobAttack(this), 6.0F);
						} else {
							this.getEntityData().set(POSE, 7);
						}
					} else {
						this.getEntityData().set(POSE, 3);
						int i = 0;
						List<ServerPlayer> list = lvl.getPlayers(LivingEntity::isAlive);
						for (ServerPlayer ply : list) {
							if (!SupernaturalManager.isVampire(ply) && !SupernaturalManager.isWerewolf(ply)) {
								if (this.isLookingAtMe(ply)) {
									++i;
								} else {
									--i;
								}
							}
							if (i <= 0) {
								this.getNavigation().moveTo(player, 1.2);
							} else {
								this.getNavigation().stop();
							}
						}
					}
				} else {
					this.getEntityData().set(POSE, 3);
				}
			} else if (SupernaturalConfig.FURIA.get()) {
				for (Mob target : this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(12.0))) {
					if (target instanceof Vex ghost) {
						ghost.setLimitedLife(0);
					} else if (target.getMobType() == MobType.UNDEAD && !target.isOnFire() && !target.fireImmune()) {
						target.setSecondsOnFire(3);
					}
				}
			}
		}
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.STONE_STEP, 0.15f, 1);
	}

	@Override
	public SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ARMOR_STAND_HIT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.ARMOR_STAND_BREAK;
	}

	@Override
	public ItemStack getPickResult() {
		return new ItemStack(SupernaturalItems.ANGEL_STATUE.get());
	}

	@Override
	protected void doPush(Entity target) {
		//
	}

	@Override
	protected void pushEntities() {
		//
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return this.isCursed();
	}

	@Override
	public boolean isAffectedByPotions() {
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean canCollideWith(Entity entity) {
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	public int getAngelPose() {
		return this.getEntityData().get(POSE);
	}

	public boolean isLookingAtMe(Player player) {
		Vec3 view = player.getViewVector(1.0F).normalize();
		Vec3 dis = new Vec3(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
		double length = dis.length();
		dis = dis.normalize();
		double circle = view.dot(dis);
		return circle > 0.0005 / length ? player.hasLineOfSight(this) : false;
	}

	public boolean isCursed() {
		return this.getEntityData().get(CURSED);
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.25);
		builder = builder.add(Attributes.MAX_HEALTH, 24);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 6);
		builder = builder.add(Attributes.FOLLOW_RANGE, 32);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
		return builder;
	}

	private void showBreakingParticles() {
		if (this.level() instanceof ServerLevel lvl) {
			lvl.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()), this.getX(), this.getY(0.35D), this.getZ(), 10, (double) (this.getBbWidth() / 4.0F), (double) (this.getBbHeight() / 4.0F),
					(double) (this.getBbWidth() / 4.0F), 0.05D);
		}
	}
}