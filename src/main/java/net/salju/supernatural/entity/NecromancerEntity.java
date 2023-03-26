package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalModSounds;
import net.salju.supernatural.init.SupernaturalModMobEffects;
import net.salju.supernatural.init.SupernaturalModEntities;
import net.salju.supernatural.init.SupernaturalEnchantments;
import net.salju.supernatural.SupernaturalMod;

import net.minecraftforge.network.PlayMessages;

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

public class NecromancerEntity extends SpellcasterIllager {
	public NecromancerEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(SupernaturalModEntities.NECROMANCER.get(), world);
	}

	public NecromancerEntity(EntityType<NecromancerEntity> type, Level world) {
		super(type, world);
		this.setPersistenceRequired();
		xpReward = 50;
		((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(0, new RestrictSunGoal(this));
		this.goalSelector.addGoal(0, new NecromancerEntity.NecroCastingSpellGoal());
		this.goalSelector.addGoal(1, new NecromancerEntity.NecroSummonSpellGoal());
		this.goalSelector.addGoal(2, new NecromancerEntity.NecroAttackSpellGoal());
		this.goalSelector.addGoal(2, new NecromancerEntity.NecroLifeStealSpellGoal());
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
			boolean flag = ((ServerLevel) this.level).isRaided(this.blockPosition());
			((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(flag);
		}
		super.customServerAiStep();
	}

	public void aiStep() {
		boolean flag = this.isSunBurnTick();
		if (flag) {
			this.setSecondsOnFire(8);
			this.hurt(new DamageSource("vampire.sun").bypassArmor(), 4);
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
		SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
		ItemStack sword = new ItemStack(Items.IRON_SWORD);
		sword.setCount(1);
		sword.enchant(SupernaturalEnchantments.LEECHING.get(), 4);
		this.setItemInHand(InteractionHand.MAIN_HAND, sword);
		this.setDropChance(EquipmentSlot.MAINHAND, 1.0F);
		return retval;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (this.isAggressive()) {
			this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 0, (false), (false)));
			this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 0, (false), (false)));
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		for (NewVexEntity bobs : this.level.getEntitiesOfClass(NewVexEntity.class, this.getBoundingBox().inflate(16.0D))) {
			bobs.hurt(source, (amount * 0.45F));
			return super.hurt(source, (amount * 0.3F));
		}
		return super.hurt(source, (amount * 0.75F));
	}

	public static void init() {
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
			if (NecromancerEntity.this.getTarget() != null) {
				NecromancerEntity.this.getLookControl().setLookAt(NecromancerEntity.this.getTarget(), (float) NecromancerEntity.this.getMaxHeadYRot(), (float) NecromancerEntity.this.getMaxHeadXRot());
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
			LivingEntity target = NecromancerEntity.this.getTarget();
			LevelAccessor world = target.getLevel();
			double x = Math.floor(target.getX());
			double y = Math.floor(target.getY());
			double z = Math.floor(target.getZ());
			BlockPos pos = new BlockPos((x + 0.5), y, (z + 0.5));
			if (world instanceof ServerLevel sev) {
				SupernaturalMod.queueServerWork(26, () -> {
					if ((Math.floor(target.getX()) == x) && (Math.floor(target.getY()) == y) && (Math.floor(target.getZ()) == z)) {
						LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(sev);
						bolt.moveTo(Vec3.atBottomCenterOf(pos));
						bolt.setVisualOnly(true);
						sev.addFreshEntity(bolt);
						target.hurt(DamageSource.MAGIC, 12);
					} else {
						for (int bob = 0; bob < (int) (2); bob++) {
							if (Math.random() <= 0.99) {
								if (Math.random() <= 0.5) {
									LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(sev);
									bolt.moveTo(Vec3.atBottomCenterOf(pos));
									bolt.setVisualOnly(true);
									Mob skele = new Skeleton(EntityType.SKELETON, sev);
									skele.moveTo((x + 0.5), y, (z + 0.5), 0, 0);
									((PathfinderMob) skele).targetSelector.addGoal(1, new NecromancerEntity.NecroMinionTargetGoal((PathfinderMob) skele));
									skele.finalizeSpawn(sev, world.getCurrentDifficultyAt(skele.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
									world.addFreshEntity(bolt);
									world.addFreshEntity(skele);
								} else {
									LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(sev);
									bolt.moveTo(Vec3.atBottomCenterOf(pos));
									bolt.setVisualOnly(true);
									Mob billy = new Zombie(EntityType.ZOMBIE, sev);
									billy.moveTo((x + 0.5), y, (z + 0.5), 0, 0);
									((PathfinderMob) billy).targetSelector.addGoal(1, new NecromancerEntity.NecroMinionTargetGoal((PathfinderMob) billy));
									billy.finalizeSpawn(sev, world.getCurrentDifficultyAt(billy.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
									sev.addFreshEntity(bolt);
									sev.addFreshEntity(billy);
								}
							} else {
								LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(sev);
								bolt.moveTo(Vec3.atBottomCenterOf(pos));
								bolt.setVisualOnly(true);
								Mob vampire = new VampireEntity(SupernaturalModEntities.VAMPIRE.get(), sev);
								vampire.moveTo((x + 0.5), y, (z + 0.5), 0, 0);
								vampire.setCustomName(Component.literal("Bob"));
								vampire.finalizeSpawn(sev, world.getCurrentDifficultyAt(vampire.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
								sev.addFreshEntity(bolt);
								sev.addFreshEntity(vampire);
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
			if (NecromancerEntity.this.getTarget() != null) {
				LivingEntity target = NecromancerEntity.this.getTarget();
				if (target.hasEffect(SupernaturalModMobEffects.BLEEDING.get()) || (NecromancerEntity.this.getHealth() > (NecromancerEntity.this.getMaxHealth() * 0.45))) {
					return false;
				} else {
					target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 40, 0, (false), (false)));
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
			LivingEntity target = NecromancerEntity.this.getTarget();
			target.hurt(DamageSource.MAGIC, 4);
			target.addEffect(new MobEffectInstance(SupernaturalModMobEffects.BLEEDING.get(), 380, 0));
			NecromancerEntity.this.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 1, (false), (false)));
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
				int i = NecromancerEntity.this.level.getNearbyEntities(Vex.class, this.vexCountTargeting, NecromancerEntity.this, NecromancerEntity.this.getBoundingBox().inflate(16.0D)).size();
				return NecromancerEntity.this.random.nextInt(8) + 1 > i;
			}
		}

		protected int getCastingTime() {
			return 60;
		}

		protected int getCastingInterval() {
			return 320;
		}

		protected void performSpellCasting() {
			ServerLevel serverlevel = (ServerLevel) NecromancerEntity.this.level;
			for (int i = 0; i < 3; ++i) {
				BlockPos blockpos = NecromancerEntity.this.blockPosition().offset(-2 + NecromancerEntity.this.random.nextInt(5), 1, -2 + NecromancerEntity.this.random.nextInt(5));
				Vex vex = EntityType.VEX.create(NecromancerEntity.this.level);
				vex.moveTo(blockpos, 0.0F, 0.0F);
				vex.finalizeSpawn(serverlevel, NecromancerEntity.this.level.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData) null, (CompoundTag) null);
				vex.setOwner(NecromancerEntity.this);
				vex.setBoundOrigin(blockpos);
				vex.setLimitedLife(20 * (30 + NecromancerEntity.this.random.nextInt(90)));
				serverlevel.addFreshEntityWithPassengers(vex);
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
			for (NecromancerEntity target : bob.getLevel().getEntitiesOfClass(NecromancerEntity.class, bob.getBoundingBox().inflate(32.0D))) {
				return target != null && target.getTarget() != null && this.canAttack(target.getTarget(), this.copyOwnerTargeting);
			}
			return false;
		}

		public void start() {
			for (NecromancerEntity target : bob.getLevel().getEntitiesOfClass(NecromancerEntity.class, bob.getBoundingBox().inflate(32.0D))) {
				bob.setTarget(target.getTarget());
			}
			super.start();
		}
	}
}