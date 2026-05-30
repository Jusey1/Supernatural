package net.salju.supernatural.entity;

import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.entity.ai.merfolk.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import java.util.Optional;

public class Merfolk extends Monster implements RangedAttackMob {
    private static final EntityDataAccessor<Boolean> CAPTAIN = SynchedEntityData.defineId(Merfolk.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SEADOG = SynchedEntityData.defineId(Merfolk.class, EntityDataSerializers.BOOLEAN);
    private Optional<EntityReference<Entity>> thrownTrident = Optional.empty();
    private ItemStack trident = new ItemStack(Items.TRIDENT);
    private int cooldown;

	public Merfolk(EntityType<? extends Merfolk> type, Level world) {
		super(type, world);
		this.xpReward = 8;
        this.moveControl = new MerfolkMoveControl(this);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
	}

    @Override
    public void addAdditionalSaveData(ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Captain", this.isCaptain());
        tag.putBoolean("Seadog", this.isSeadog());
        if (!this.getTrident().isEmpty()) {
            tag.store("Trident", ItemStack.CODEC, this.getTrident());
        }
        if (this.getTridentReference() != null) {
            EntityReference.store(this.getTridentReference(), tag, "ThrownTrident");
        }
        tag.putInt("CD", this.getCD());
    }

    @Override
    public void readAdditionalSaveData(ValueInput tag) {
        super.readAdditionalSaveData(tag);
        this.getEntityData().set(CAPTAIN, tag.getBooleanOr("Captain", false));
        this.getEntityData().set(SEADOG, tag.getBooleanOr("Seadog", false));
        if (tag.read("Trident", ItemStack.CODEC).isPresent()) {
            this.setTrident(tag.read("Trident", ItemStack.CODEC).orElse(ItemStack.EMPTY));
        }
        EntityReference<Entity> target = EntityReference.read(tag, "ThrownTrident");
        if (target != null) {
            this.thrownTrident = Optional.of(target);
        } else {
            this.thrownTrident = Optional.empty();
        }
        if (tag.getInt("CD").isPresent()) {
            this.setCD(tag.getInt("CD").get());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CAPTAIN, false);
        builder.define(SEADOG, false);
    }

    @Override
    public Component getName() {
        if (this.getCustomName() == null) {
            if (this.isCaptain()) {
                return Component.translatable(this.getType().getDescriptionId() + "_captain");
            } else if (this.isSeadog()) {
                return Component.translatable(this.getType().getDescriptionId() + "_seadog");
            }
        }
        return super.getName();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MerfolkTridentGoal(this, 1.0D, 40, 10.0F));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.6, false));
        this.goalSelector.addGoal(2, new MerfolkSwimGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, (float) 6));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, true, new MerfolkAttackSelector(this)));
    }

    @Override
    public SoundEvent getAmbientSound() {
        return this.isInWater() ? SupernaturalSounds.MERFOLK_IDLE.get() : SupernaturalSounds.MERLAND_IDLE.get();
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return this.isInWater() ? SupernaturalSounds.MERFOLK_HURT.get() : SupernaturalSounds.MERLAND_HURT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return this.isInWater() ? SupernaturalSounds.MERFOLK_DEATH.get() : SupernaturalSounds.MERLAND_DEATH.get();
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState state) {
        if (this.isInWater()) {
            this.playSound(SoundEvents.DROWNED_SWIM, 0.15F, 1);
        }
    }

    @Override
    protected PathNavigation createNavigation(Level lvl) {
        return new WaterBoundPathNavigation(this, lvl);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float f) {
        InteractionHand hand = ProjectileUtil.getWeaponHoldingHand(this, stack -> (stack instanceof TridentItem));
        if (this.getItemInHand(hand).getItem() instanceof TridentItem) {
            this.setTrident(this.getItemInHand(hand));
            ThrownTrident proj = new ThrownTrident(this.level(), this, this.getTrident());
            double d0 = target.getX() - this.getX();
            double d1 = target.getY(0.3333333333333333D) - proj.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
            proj.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - this.level().getDifficulty().getId() * 4));
            this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(proj);
            this.setItemInHand(hand, ItemStack.EMPTY);
            this.setTridentReference(proj);
            this.setCD(1200);
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
        this.setType(Mth.nextInt(this.getRandom(), 1, 100));
        if (this.isCaptain()) {
            this.getTrident().enchant(SupernaturalManager.getEnchantment(world.getLevel(), "minecraft", "loyalty"), 3);
        }
        this.setItemInHand(InteractionHand.MAIN_HAND, this.getTrident());
        this.setDropChance(EquipmentSlot.MAINHAND, 0.05F);
        if (!this.isCaptain() && !this.isSeadog()) {
            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }
        return super.finalizeSpawn(world, difficulty, reason, data);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(SupernaturalItems.MERFOLK_SPAWN_EGG.get());
    }

    @Override
    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();
        this.handleAirSupply(i);
        if (this.level() instanceof ServerLevel lvl && this.isAlive() && this.isEffectiveAi()) {
            this.checkTrident(lvl);
            if (this.getCD() > 0) {
                this.setCD(this.getCD() - 1);
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel lvl, DamageSource source, boolean check) {
        super.dropCustomDeathLoot(lvl, source, check);
        if (Mth.nextInt(this.getRandom(), 1, 100) <= 5) {
            this.spawnAtLocation(lvl, new ItemStack(this.isCaptain() ? Items.DIAMOND : this.isSeadog() ? Items.EMERALD : Items.AMETHYST_SHARD));
        }
    }

    @Override
    public void travel(Vec3 v) {
        if (this.isAlive() && this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.01F, v);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.002D, 0.0D));
            }
        } else {
            super.travel(v);
        }
    }

    @Override
    public void updateSwimming() {
        if (this.level() instanceof ServerLevel && this.isAlive() && this.isEffectiveAi()) {
            if (this.isInWater() && this.wantsToSwim()) {
                this.setSwimming(true);
            } else {
                this.setSwimming(false);
            }
        }
    }

    @Override
    public boolean hurtServer(ServerLevel lvl, DamageSource source, float amount) {
        return !(source.getEntity() instanceof Merfolk) && super.hurtServer(lvl, source, amount);
    }

    @Override
    public boolean doHurtTarget(ServerLevel lvl, Entity target) {
        if (this.isLeftHanded()) {
            this.setLeftHanded(false);
        } else {
            this.setLeftHanded(true);
        }
        return super.doHurtTarget(lvl, target);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader world) {
        return world.isUnobstructed(this);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    protected void handleAirSupply(int i) {
        if (this.isAlive() && !this.isInWater()) {
            this.setAirSupply(i - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().drown(), 2.0F);
            }
        } else {
            this.setAirSupply(300);
        }
    }

    public void checkTrident(ServerLevel lvl) {
        if (this.getTridentReference() != null && lvl.getEntity(this.getTridentReference().getUUID()) instanceof ThrownTrident proj && proj.getOwner() != null) {
            if (proj.getOwner().is(this) && (this.getCD() <= 1195 || proj.isNoPhysics())) {
                if (this.distanceTo(proj) < 2.5F || this.getCD() <= 1) {
                    this.giveTrident(proj);
                } else if (this.distanceTo(proj) < 32.0F && this.getTarget() == null && !this.getNavigation().isInProgress()) {
                    this.getNavigation().moveTo(proj, 1.0F);
                }
            }
        } else if (this.getTridentReference() != null && this.getCD() <= 1) {
            this.giveTrident(null);
        }
    }

    protected void giveTrident(@Nullable ThrownTrident proj) {
        this.setItemInHand(InteractionHand.MAIN_HAND, this.getTrident());
        this.setTridentReference(null);
        if (this.getCD() > 1 && proj != null) {
            this.setCD(0);
            proj.discard();
        }
    }

    public void setTrident(ItemStack stack) {
        this.trident = stack;
    }

    public void setTridentReference(@Nullable ThrownTrident proj) {
        this.thrownTrident = Optional.ofNullable(proj).map(EntityReference::of);
    }

    public void setCD(int i) {
        this.cooldown = i;
    }

    public void setType(int i) {
        if (i <= 15) {
            this.getEntityData().set(CAPTAIN, true);
        } else if (i >= 65) {
            this.getEntityData().set(SEADOG, true);
        }
    }

    public ItemStack getTrident() {
        return this.trident;
    }

    @Nullable
    public EntityReference<Entity> getTridentReference() {
        return this.thrownTrident.orElse(null);
    }

    public int getCD() {
        return this.cooldown;
    }

    public boolean wantsToSwim() {
        return this.getTarget() != null && this.getTarget().isInWater();
    }

    public boolean isCaptain() {
        return this.getEntityData().get(CAPTAIN);
    }

    public boolean isSeadog() {
        return this.getEntityData().get(SEADOG);
    }
}