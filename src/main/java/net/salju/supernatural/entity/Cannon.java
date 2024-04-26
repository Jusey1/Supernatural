package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalModSounds;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.SupernaturalMod;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.NonNullList;
import net.minecraft.ChatFormatting;
import java.util.UUID;

public class Cannon extends LivingEntity {
	public static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(Cannon.class, EntityDataSerializers.INT);
	private final NonNullList<ItemStack> handItems = NonNullList.withSize(2, ItemStack.EMPTY);
	private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
	private ItemStack cannon = new ItemStack(SupernaturalItems.CANNON.get());
	public int cannonlife;
	public int rotation;
	public long lastHit;
	private UUID owner;

	public Cannon(EntityType<Cannon> type, Level world) {
		super(type, world);
		this.setMaxUpStep(0.0F);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.owner != null) {
			tag.putUUID("Player", this.owner);
		}
		tag.putInt("CannonLife", this.cannonlife);
		tag.putInt("CannonType", this.getCannonType());
		tag.put("CannonItem", this.cannon.save(new CompoundTag()));
		if (!this.isEmpty()) {
			tag.put("Cannonballs", this.getMainHandItem().save(new CompoundTag()));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.getEntityData().set(TYPE, tag.getInt("CannonType"));
		if (tag.contains("Player")) {
			UUID target = tag.getUUID("Player");
			this.owner = target;
		}
		this.cannonlife = tag.getInt("CannonLife");
		this.cannon = ItemStack.of(tag.getCompound("CannonItem"));
		if (tag.contains("Cannonballs")) {
			this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.of(tag.getCompound("Cannonballs")));
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(TYPE, 0);
	}

	@Override
	public Component getName() {
		if (this.getMainHandItem().is(SupernaturalItems.COPPER_CANNONBALL.get())) {
			return Component.literal(Integer.toString(this.getMainHandItem().getCount())).withStyle(ChatFormatting.GOLD);
		} else if (!this.isEmpty()) {
			return Component.literal(Integer.toString(this.getMainHandItem().getCount()));
		}
		return super.getName();
	}

	@Override
	public boolean shouldShowName() {
		return (this.isEmpty() ? super.shouldShowName() : true);
	}

	@Override
	public InteractionResult interactAt(Player player, Vec3 v, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand).copy();
		if (player.level() instanceof ServerLevel && player == this.getOwner()) {
			if (stack.is(SupernaturalItems.CANNONBALL.get()) || stack.is(SupernaturalItems.COPPER_CANNONBALL.get())) {
				if (this.getMainHandItem().isEmpty()) {
					this.setItemInHand(InteractionHand.MAIN_HAND, stack);
					if (!player.isCreative()) {
						player.getItemInHand(hand).shrink(player.getItemInHand(hand).getCount());
					}
				} else if (stack.getItem() != this.getMainHandItem().getItem()) {
					if (!player.isCreative()) {
						player.getItemInHand(hand).shrink(player.getItemInHand(hand).getCount());
						ItemEntity drop = new ItemEntity(this.level(), this.getX(), (this.getY() + 1.45), this.getZ(), this.getMainHandItem());
						drop.setPickUpDelay(10);
						this.level().addFreshEntity(drop);
					}
					this.setItemInHand(InteractionHand.MAIN_HAND, stack);
				} else {
					int i = (64 - this.getMainHandItem().getCount());
					this.getMainHandItem().setCount(this.getMainHandItem().getCount() + i);
					if (!player.isCreative()) {
						player.getItemInHand(hand).shrink(i);
					}
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean hurt(DamageSource source, float f) {
		if (!this.level().isClientSide && !this.isRemoved()) {
			if (source.getEntity() != null) {
				if (source.isCreativePlayer() || source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || source.is(DamageTypeTags.IS_EXPLOSION)) {
					this.die(source);
				} else {
					long i = this.level().getGameTime();
					if (i - this.lastHit > 5L) {
						this.level().broadcastEntityEvent(this, (byte) 32);
						this.gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
						this.lastHit = i;
					} else {
						this.die(source);
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void die(DamageSource source) {
		super.die(source);
		boolean flag = (source.getEntity() instanceof Player player && player.isCreative());
		this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ARMOR_STAND_BREAK, this.getSoundSource(), 1.0F, 1.0F);
		this.showBreakingParticles();
		if (!flag) {
			ItemEntity target = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), getCannonStack());
			target.setPickUpDelay(10);
			this.level().addFreshEntity(target);
			if (!this.getMainHandItem().isEmpty()) {
				ItemEntity drop = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.getMainHandItem());
				drop.setPickUpDelay(10);
				this.level().addFreshEntity(drop);
			}
		}
		this.discard();
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (this.level() instanceof ServerLevel lvl) {
			this.cannonlife++;
			this.rotation++;
			if (this.rotation >= 12) {
				this.rotation = 0;
				float rot = (this.getYHeadRot() + 45.0F);
				this.setYHeadRot(rot);
				this.yHeadRotO = rot;
				this.yHeadRot = rot;
				lvl.playSound(null, this.blockPosition(), SupernaturalModSounds.CANNON_SPIN.get(), SoundSource.AMBIENT, 0.1F, 1.0F);
			} else if (this.rotation == 6 && !this.isEmpty()) {
				for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(16.76D))) {
					if ((target instanceof Enemy || (this.getOwner().getLastHurtMob() == target && target != this)) && this.isLookingAt(target)) {
						Cannonball ammo = new Cannonball(this.level(), this, this.getCannonballType());
						ammo.shoot(this.getViewVector(1.0F).x, this.getViewVector(1.0F).y, this.getViewVector(1.0F).z, 1.25F, 1.0F);
						this.playSound(SupernaturalModSounds.CANNON_SHOOT.get(), 1.0F, 1.0F);
						this.level().addFreshEntity(ammo);
						this.getMainHandItem().shrink(1);
						SupernaturalMod.queueServerWork(2, () -> {
							lvl.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, ammo.getX(), (ammo.getEyeY() + 0.25), ammo.getZ(), 1, 0, 0, 0, 0);
						});
						break;
					}
				}
			}
			if (this.cannonlife >= 36000) {
				this.die(this.damageSources().explosion(null, null));
				for (int i = 0; i < this.getRandom().nextInt(1) + 1; ++i) {
					lvl.sendParticles(ParticleTypes.LAVA, this.getX(), this.getEyeY(), this.getZ(), 5, 0.1, 0.15, 0.1, 0);
				}
			} else if (this.cannonlife >= 24000) {
				if (this.rotation == 5 || this.rotation == 15) {
					for (int i = 0; i < this.getRandom().nextInt(1) + 1; ++i) {
						lvl.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(), (this.getEyeY() + 0.75), this.getZ(), 1, 0.1, 0.15, 0.1, 0);
						lvl.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getEyeY(), this.getZ(), 2, 0.5, 0.15, 0.5, 0);
					}
				}
			}
		}
	}

	@Override
	public void handleEntityEvent(byte b) {
		if (b == 32) {
			if (this.level().isClientSide) {
				this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ARMOR_STAND_HIT, this.getSoundSource(), 0.3F, 1.0F, false);
				this.lastHit = this.level().getGameTime();
			}
		} else {
			super.handleEntityEvent(b);
		}
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
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
		return cannon;
	}

	@Override
	public HumanoidArm getMainArm() {
		return HumanoidArm.RIGHT;
	}

	@Override
	public Iterable<ItemStack> getHandSlots() {
		return this.handItems;
	}

	@Override
	public Iterable<ItemStack> getArmorSlots() {
		return this.armorItems;
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlot slot) {
		switch (slot.getType()) {
			case HAND :
				return this.handItems.get(slot.getIndex());
			case ARMOR :
				return this.armorItems.get(slot.getIndex());
			default :
				return ItemStack.EMPTY;
		}
	}

	@Override
	public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
		this.verifyEquippedItem(stack);
		switch (slot.getType()) {
			case HAND :
				this.onEquipItem(slot, this.handItems.set(slot.getIndex(), stack), stack);
				break;
			case ARMOR :
				this.onEquipItem(slot, this.armorItems.set(slot.getIndex(), stack), stack);
		}
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

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
		return 1.5F;
	}

	public void setCannonStack(ItemStack stack) {
		this.cannon = stack;
	}

	public void setOwner(UUID player) {
		this.owner = player;
	}

	public LivingEntity getOwner() {
		if (this.owner != null) {
			Player player = this.level().getPlayerByUUID(this.owner);
			if (player != null) {
				return player;
			}
		}
		return this;
	}

	public ItemStack getCannonStack() {
		return this.cannon;
	}

	public int getCannonType() {
		return this.getEntityData().get(TYPE);
	}

	public int getCannonballType() {
		return (this.getMainHandItem().is(SupernaturalItems.COPPER_CANNONBALL.get()) ? 1 : 0);
	}

	public boolean isEmpty() {
		return (this.getMainHandItem().isEmpty());
	}

	public boolean isLookingAt(LivingEntity target) {
		if (target.getBoundingBox().getCenter().y() < target.getY() + 0.5 && target.getY() <= this.getY()) {
			return false;
		} else if (target.getY() > (this.getY() + 1.5) || target.getY() < (this.getY() - 1.5)) {
			return false;
		}
		Vec3 view = this.getViewVector(1.0F).normalize();
		Vec3 dis = new Vec3(target.getBoundingBox().getCenter().x() - this.getX(), 0, target.getBoundingBox().getCenter().z() - this.getZ());
		double length = dis.length();
		dis = dis.normalize();
		double circle = view.dot(dis);
		return circle > 1.0 - (target.getBoundingBox().getSize() * 0.025) / length ? this.hasLineOfSight(target) : false;
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.0);
		builder = builder.add(Attributes.MAX_HEALTH, 30);
		builder = builder.add(Attributes.ARMOR, 12);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 0);
		builder = builder.add(Attributes.FOLLOW_RANGE, 0);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
		return builder;
	}

	private void showBreakingParticles() {
		if (this.level() instanceof ServerLevel lvl) {
			lvl.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.IRON_BLOCK.defaultBlockState()), this.getX(), this.getY(0.35D), this.getZ(), 10, (double) (this.getBbWidth() / 4.0F), (double) (this.getBbHeight() / 4.0F),
					(double) (this.getBbWidth() / 4.0F), 0.05D);
		}
	}
}