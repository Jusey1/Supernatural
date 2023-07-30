package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalModSounds;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.events.SupernaturalHelpers;
import net.minecraftforge.network.PlayMessages;

import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.UUID;
import java.util.Map;

public class PossessedArmor extends AbstractGolem {
	private UUID friend;

	public PossessedArmor(PlayMessages.SpawnEntity packet, Level world) {
		this(SupernaturalMobs.POSSESSED_ARMOR.get(), world);
	}

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
		if (this.friend != null) {
			tag.putUUID("Player", this.friend);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Player")) {
			UUID target = tag.getUUID("Player");
			this.friend = target;
		}
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return SupernaturalModSounds.ARMOR_HURT.get();
	}

	@Override
	public SoundEvent getDeathSound() {
		return SupernaturalModSounds.ARMOR_DEATH.get();
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		if (this.isTamed()) {
			Iterable<ItemStack> armor = this.getArmorSlots();
			for (ItemStack stack : armor) {
				Map<Enchantment, Integer> magic = EnchantmentHelper.getEnchantments(stack);
				if (magic.containsKey(Enchantments.MENDING)) {
					if (stack.isDamaged()) {
						int i = (stack.getDamageValue() - 6);
						stack.setDamageValue(i);
					}
				}
			}
		}
		return super.doHurtTarget(entity);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.is(DamageTypes.FALL) || source.is(DamageTypes.CACTUS) || source.is(DamageTypes.DROWN)) {
			return false;
		}
		return super.hurt(source, amount);
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource source, int i, boolean check) {
		this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		Iterable<ItemStack> armor = this.getArmorSlots();
		for (ItemStack stack : armor) {
			EquipmentSlot slot = this.getEquipmentSlotForItem(stack);
			this.spawnAtLocation(stack);
			this.setItemSlot(slot, ItemStack.EMPTY);
		}
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
		SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
		RandomSource randy = world.getRandom();
		this.populateDefaultEquipmentSlots(randy, difficulty);
		this.addEffect(new MobEffectInstance(SupernaturalEffects.POSSESSION.get(), Integer.MAX_VALUE, 0));
		return retval;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource randy, DifficultyInstance souls) {
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
		this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(SupernaturalItems.GOTHIC_IRON_HELMET.get()));
		this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
		this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
		this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
		this.populateDefaultEquipmentEnchantments(randy, souls);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (!this.hasEffect(SupernaturalEffects.POSSESSION.get()) || !SupernaturalHelpers.hasArmor(this)) {
			this.kill();
		}
		if (this.isAlive()) {
			if (this.isTamed()) {
				if (this.getEffect(SupernaturalEffects.POSSESSION.get()).getDuration() <= 10) {
					this.addEffect(new MobEffectInstance(SupernaturalEffects.POSSESSION.get(), Integer.MAX_VALUE, 0));
				}
			}
		}
	}

	public void aiStep() {
		this.updateSwingTime();
		super.aiStep();
	}

	public void setOwner(UUID player) {
		this.friend = player;
	}

	public UUID getOwner() {
		return this.friend;
	}

	public boolean isTamed() {
		return (this.friend != null);
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.25);
		builder = builder.add(Attributes.MAX_HEALTH, 20);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 1);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
		return builder;
	}

	static class PossessedAttackSelector implements Predicate<LivingEntity> {
		private final PossessedArmor armor;

		public PossessedAttackSelector(PossessedArmor source) {
			this.armor = source;
		}

		public boolean test(@Nullable LivingEntity target) {
			if (armor.isTamed()) {
				Player player = armor.level().getPlayerByUUID(armor.getOwner());
				if (player != null && player.getLastHurtByMob() != null) {
					if (player.getLastHurtByMob().isAlive()) {
						return (target == player.getLastHurtByMob());
					}
				}
				if (target instanceof Enemy) {
					return (target.getMobType() == MobType.UNDEAD || target.getMobType() == MobType.ARTHROPOD || target.getMobType() == MobType.ILLAGER);
				}
			} else {
				return (target instanceof Player);
			}
			return false;
		}
	}
}