package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AbstractMinionEntity extends Monster implements OwnableEntity {
	private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> OWNER = SynchedEntityData.defineId(AbstractMinionEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    private static final EntityDataAccessor<Boolean> FOLLOW = SynchedEntityData.defineId(AbstractMinionEntity.class, EntityDataSerializers.BOOLEAN);

	public AbstractMinionEntity(EntityType<? extends AbstractMinionEntity> type, Level world) {
		super(type, world);
        this.xpReward = 10;
        this.setPathfindingMalus(PathType.WATER, -1.0F);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput tag) {
		super.addAdditionalSaveData(tag);
        tag.putBoolean("Following", this.isFollowing());
        this.entityData.get(OWNER).ifPresent(target -> target.store(tag, "Owner"));
    }

	@Override
	public void readAdditionalSaveData(ValueInput tag) {
		super.readAdditionalSaveData(tag);
        this.setFollowing(tag.getBooleanOr("Following", false));
		EntityReference<LivingEntity> target = EntityReference.readWithOldOwnerConversion(tag, "Owner", this.level());
		if (target != null) {
            this.entityData.set(OWNER, Optional.of(target));
		} else {
			this.entityData.set(OWNER, Optional.empty());
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(OWNER, Optional.empty());
        builder.define(FOLLOW, false);
	}

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.getOwner() != null && this.getOwner().is(player)) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.is(SupernaturalItems.COMMANDER_WAND)) {
                this.setFollowing(this.isFollowing() ? false : true);
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double d) {
        return this.isNatural() ? super.removeWhenFarAway(d) : false;
    }

    @Override
    public EntityReference<LivingEntity> getOwnerReference() {
        return this.entityData.get(OWNER).orElse(null);
    }

    public boolean isNatural() {
        return this.getOwnerReference() == null;
    }

    public boolean isFollowing() {
        return this.getEntityData().get(FOLLOW);
    }

    public boolean unableToMove() {
        return !this.isFollowing() || this.isAggressive() || this.isPassenger() || this.mayBeLeashed() || this.getOwner() != null && this.getOwner().isSpectator();
    }

    public boolean shouldAttemptTeleport() {
        return this.getOwner() != null && this.distanceToSqr(this.getOwner()) >= 175.6;
    }

    public void setFollowing(boolean check) {
        this.getEntityData().set(FOLLOW, check);
    }

    public void attemptTeleport() {
        if (this.getOwner() != null) {
            BlockPos pos = this.getOwner().blockPosition();
            for (int i = 0; i < 10; ++i) {
                int j = this.random.nextIntBetweenInclusive(-3, 3);
                int k = this.random.nextIntBetweenInclusive(-3, 3);
                if (Math.abs(j) >= 2 || Math.abs(k) >= 2) {
                    int l = this.random.nextIntBetweenInclusive(-1, 1);
                    if (this.maybeTeleportTo(pos.getX() + j, pos.getY() + l, pos.getZ() + k)) {
                        return;
                    }
                }
            }
        }
    }

    private boolean maybeTeleportTo(int x, int y, int z) {
        if (!this.canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        } else {
            this.snapTo(x + 0.5, y, z + 0.5, this.getYRot(), this.getXRot());
            this.navigation.stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos pos) {
        if (WalkNodeEvaluator.getPathTypeStatic(this, pos) != PathType.WALKABLE) {
            return false;
        } else {
            return this.level().noCollision(this, this.getBoundingBox().move(pos.subtract(this.blockPosition())));
        }
    }

	public void setOwner(@Nullable LivingEntity target) {
		this.entityData.set(OWNER, Optional.ofNullable(target).map(EntityReference::of));
	}
}