package net.salju.supernatural.entity;

import net.salju.supernatural.procedures.SupernaturalHelpersProcedure;
import net.salju.supernatural.init.SupernaturalModEntities;
import net.salju.supernatural.init.SupernaturalConfig;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.minecraft.tags.TagKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

public class AngelEntity extends Mob {
	public AngelEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(SupernaturalModEntities.ANGEL.get(), world);
	}

	public AngelEntity(EntityType<AngelEntity> type, Level world) {
		super(type, world);
		this.xpReward = 0;
		this.setPersistenceRequired();
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.COBBLESTONE));
		this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		super.mobInteract(player, hand);
		ItemStack axe = player.getItemInHand(hand);
		ItemStack stone = this.getMainHandItem();
		if (axe.getItem() instanceof PickaxeItem) {
			if (player.isShiftKeyDown()) {
				if (this.getAngelPose() >= 7) {
					stone.setCount(1);
				} else {
					stone.setCount(stone.getCount() + 1);
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
		return InteractionResult.FAIL;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getDirectEntity() instanceof LivingEntity player) {
			ItemStack axe = player.getMainHandItem();
			if (axe.getItem() instanceof PickaxeItem) {
				return super.hurt(source, amount);
			}
		}
		if (source.is(DamageTypes.OUT_OF_WORLD) || source.is(DamageTypes.EXPLOSION))
			return super.hurt(source, amount);
		return false;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		LevelAccessor world = this.level;
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		if (!this.level.isClientSide && this.isAlive() && this.isEffectiveAi() && (SupernaturalConfig.FURIA.get() == true)) {
			for (LivingEntity target : world.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(12.0D))) {
				if (target instanceof Vex ghost) {
					ghost.setLimitedLife(0);
				} else if (SupernaturalHelpersProcedure.isVampire(target) || (target.getMobType() == MobType.UNDEAD) || target.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("supernatural:is_vampire")))) {
					if (!target.isOnFire() && !(target instanceof SpookyEntity) && !target.fireImmune()) {
						target.setSecondsOnFire(3);
					}
				}
			}
		}
	}

	public int getAngelPose() {
		return this.getMainHandItem().getCount();
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return false;
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.armor_stand.hit"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.armor_stand.break"));
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void doPush(Entity entityIn) {
	}

	@Override
	protected void pushEntities() {
	}

	@Override
	public boolean canCollideWith(Entity entity) {
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.0);
		builder = builder.add(Attributes.MAX_HEALTH, 24);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 7);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
		return builder;
	}
}
