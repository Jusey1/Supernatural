package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalModSounds;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalEnchantments;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.SupernaturalMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;

public class Necromancer extends SpellcasterIllager {
	public Necromancer(EntityType<Necromancer> type, Level world) {
		super(type, world);
		this.setPersistenceRequired();
		this.xpReward = 50;
		((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(0, new RestrictSunGoal(this));
		this.goalSelector.addGoal(0, new Necromancer.NecroCastingSpellGoal());
		this.goalSelector.addGoal(1, new Necromancer.NecroSummonSpellGoal());
		this.goalSelector.addGoal(2, new Necromancer.NecroAttackSpellGoal());
		this.goalSelector.addGoal(2, new Necromancer.NecroLifeStealSpellGoal());
		this.goalSelector.addGoal(3, new AbstractIllager.RaiderOpenDoorGoal(this));
		this.goalSelector.addGoal(3, new Raider.HoldGroundAttackGoal(this, 10.0F));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2, false));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, (float) 8));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
	}

	protected void customServerAiStep() {
		if (!this.isNoAi() && GoalUtils.hasGroundPathNavigation(this)) {
			boolean flag = ((ServerLevel) this.level()).isRaided(this.blockPosition());
			((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(flag);
		}
		super.customServerAiStep();
	}

	public void aiStep() {
		if (!SupernaturalConfig.SUN.get()) {
			boolean flag = this.isSunBurnTick();
			if (flag) {
				this.setSecondsOnFire(8);
				this.hurt(this.damageSources().inFire(), 4);
			}
		}
		super.aiStep();
	}

	@Override
	public AbstractIllager.IllagerArmPose getArmPose() {
		if (this.isAggressive()) {
			return AbstractIllager.IllagerArmPose.ATTACKING;
		} else if (this.isCastingSpell()) {
			return AbstractIllager.IllagerArmPose.SPELLCASTING;
		} else {
			return this.isCelebrating() ? AbstractIllager.IllagerArmPose.CELEBRATING : AbstractIllager.IllagerArmPose.NEUTRAL;
		}
	}

	@Override
	public MobType getMobType() {
		return MobType.ILLAGER;
	}

	public boolean isAlliedTo(Entity ally) {
		if (super.isAlliedTo(ally)) {
			return true;
		} else if (ally instanceof LivingEntity && ((LivingEntity) ally).getMobType() == MobType.ILLAGER) {
			return this.getTeam() == null && ally.getTeam() == null;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	public void applyRaidBuffs(int imp, boolean boop) {
		Raid raid = this.getCurrentRaid();
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SupernaturalModSounds.VAMPIRE_IDLE.get();
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return SupernaturalModSounds.VAMPIRE_HURT.get();
	}

	@Override
	public SoundEvent getDeathSound() {
		return SupernaturalModSounds.VAMPIRE_DEATH.get();
	}

	@Override
	public SoundEvent getCelebrateSound() {
		return SupernaturalModSounds.VAMPIRE_CELEBRATE.get();
	}

	@Override
	public SoundEvent getCastingSoundEvent() {
		return SoundEvents.EVOKER_CAST_SPELL;
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
		ItemStack sword = new ItemStack(Items.IRON_SWORD);
		sword.enchant(SupernaturalEnchantments.LEECHING.get(), 4);
		this.setItemInHand(InteractionHand.MAIN_HAND, sword);
		this.setDropChance(EquipmentSlot.MAINHAND, 1.0F);
		return super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		for (Vex bobs : this.level().getEntitiesOfClass(Vex.class, this.getBoundingBox().inflate(16.0D))) {
			bobs.hurt(source, (amount * 0.45F));
			return super.hurt(source, (amount * 0.3F));
		}
		return super.hurt(source, (amount * 0.75F));
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.35);
		builder = builder.add(Attributes.FOLLOW_RANGE, 12.0D);
		builder = builder.add(Attributes.MAX_HEALTH, 100);
		builder = builder.add(Attributes.ARMOR, 2);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		return builder;
	}

	class NecroCastingSpellGoal extends SpellcasterIllager.SpellcasterCastingSpellGoal {
		public void tick() {
			if (Necromancer.this.getTarget() != null) {
				Necromancer.this.getLookControl().setLookAt(Necromancer.this.getTarget(), (float) Necromancer.this.getMaxHeadYRot(), (float) Necromancer.this.getMaxHeadXRot());
			}
		}
	}

	class NecroAttackSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
		protected int getCastingTime() {
			return 40;
		}

		protected int getCastingInterval() {
			return 180;
		}

		protected void performSpellCasting() {
			LivingEntity target = Necromancer.this.getTarget();
			LevelAccessor world = target.level();
			double x = Math.floor(target.getX());
			double y = Math.floor(target.getY());
			double z = Math.floor(target.getZ());
			BlockPos pos = BlockPos.containing((x + 0.5), y, (z + 0.5));
			if (world instanceof ServerLevel lvl) {
				SupernaturalMod.queueServerWork(26, () -> {
					LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(lvl);
					bolt.moveTo(Vec3.atBottomCenterOf(pos));
					bolt.setVisualOnly(true);
					lvl.addFreshEntity(bolt);
					if ((Math.floor(target.getX()) == x) && (Math.floor(target.getY()) == y) && (Math.floor(target.getZ()) == z)) {
						target.hurt(target.damageSources().magic(), 12);
					} else {
						for (int bob = 0; bob < (int) (2); bob++) {
							if (Math.random() <= 0.99) {
								if (Math.random() <= 0.5) {
									Skeleton skele = EntityType.SKELETON.spawn(lvl, pos, MobSpawnType.MOB_SUMMONED);
									((PathfinderMob) skele).targetSelector.addGoal(1, new Necromancer.NecroMinionTargetGoal((PathfinderMob) skele));
								} else {
									Zombie billy = EntityType.ZOMBIE.spawn(lvl, pos, MobSpawnType.MOB_SUMMONED);
									((PathfinderMob) billy).targetSelector.addGoal(1, new Necromancer.NecroMinionTargetGoal((PathfinderMob) billy));
								}
							} else {
								Vampire vampire = SupernaturalMobs.VAMPIRE.get().spawn(lvl, pos, MobSpawnType.MOB_SUMMONED);
								vampire.setCustomName(Component.literal("Bob"));
							}
						}
					}
				});
			}
		}

		protected SoundEvent getSpellPrepareSound() {
			return SoundEvents.EVOKER_PREPARE_ATTACK;
		}

		protected SpellcasterIllager.IllagerSpell getSpell() {
			return SpellcasterIllager.IllagerSpell.FANGS;
		}
	}

	class NecroLifeStealSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
		public boolean canUse() {
			if (Necromancer.this.getTarget() != null) {
				LivingEntity target = Necromancer.this.getTarget();
				if (Necromancer.this.getHealth() > (Necromancer.this.getMaxHealth() * 0.45)) {
					return false;
				} else {
					target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 40, 0, false, false));
					return true;
				}
			} else {
				return false;
			}
		}

		protected int getCastingTime() {
			return 60;
		}

		protected int getCastingInterval() {
			return 120;
		}

		protected void performSpellCasting() {
			LivingEntity target = Necromancer.this.getTarget();
			target.hurt(target.damageSources().magic(), 4);
			Necromancer.this.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 1, false, false));
		}

		protected SoundEvent getSpellPrepareSound() {
			return SoundEvents.EVOKER_PREPARE_ATTACK;
		}

		protected SpellcasterIllager.IllagerSpell getSpell() {
			return SpellcasterIllager.IllagerSpell.FANGS;
		}
	}

	class NecroSummonSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
		private final TargetingConditions vexCountTargeting = TargetingConditions.forNonCombat().range(16.0D).ignoreLineOfSight().ignoreInvisibilityTesting();

		public boolean canUse() {
			if (!super.canUse()) {
				return false;
			} else {
				int i = Necromancer.this.level().getNearbyEntities(Vex.class, this.vexCountTargeting, Necromancer.this, Necromancer.this.getBoundingBox().inflate(16.0D)).size();
				return Necromancer.this.random.nextInt(8) + 1 > i;
			}
		}

		protected int getCastingTime() {
			return 60;
		}

		protected int getCastingInterval() {
			return 320;
		}

		protected void performSpellCasting() {
			ServerLevel lvl = (ServerLevel) Necromancer.this.level();
			for (int i = 0; i < 3; ++i) {
				BlockPos pos = Necromancer.this.blockPosition().offset(-2 + Necromancer.this.random.nextInt(5), 1, -2 + Necromancer.this.random.nextInt(5));
				Vex vex = EntityType.VEX.spawn(lvl, pos, MobSpawnType.MOB_SUMMONED);
				vex.setOwner(Necromancer.this);
				vex.setBoundOrigin(pos);
				vex.setLimitedLife(2000);
			}
		}

		protected SoundEvent getSpellPrepareSound() {
			return SoundEvents.EVOKER_PREPARE_SUMMON;
		}

		protected SpellcasterIllager.IllagerSpell getSpell() {
			return SpellcasterIllager.IllagerSpell.SUMMON_VEX;
		}
	}

	class NecroMinionTargetGoal extends TargetGoal {
		private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
		private final PathfinderMob bob;

		public NecroMinionTargetGoal(PathfinderMob bob) {
			super(bob, false);
			this.bob = bob;
		}

		public boolean canUse() {
			Necromancer target = bob.level().getNearestEntity(Necromancer.class, TargetingConditions.DEFAULT, bob, bob.getX(), bob.getY(), bob.getZ(), bob.getBoundingBox().inflate(32.0D));
			if (target != null) {
				return target.getTarget() != null && this.canAttack(target.getTarget(), this.copyOwnerTargeting);
			}
			return false;
		}

		public void start() {
			Necromancer target = bob.level().getNearestEntity(Necromancer.class, TargetingConditions.DEFAULT, bob, bob.getX(), bob.getY(), bob.getZ(), bob.getBoundingBox().inflate(32.0D));
			if (target != null) {
				bob.setTarget(target.getTarget());
			}
			super.start();
		}
	}
}