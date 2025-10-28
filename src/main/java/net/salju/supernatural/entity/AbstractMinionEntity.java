package net.salju.supernatural.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AbstractMinionEntity extends AbstractSpellcasterEntity {
	private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> OWNER = SynchedEntityData.defineId(AbstractMinionEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);

	public AbstractMinionEntity(EntityType<? extends AbstractMinionEntity> type, Level world) {
		super(type, world);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput tag) {
		super.addAdditionalSaveData(tag);
        this.checkDespawn();
		if (this.getOwner() != null) {
			this.getOwner().store(tag, "Player");
		}
	}

	@Override
	public void readAdditionalSaveData(ValueInput tag) {
		super.readAdditionalSaveData(tag);
		EntityReference<LivingEntity> target = EntityReference.readWithOldOwnerConversion(tag, "Player", this.level());
		if (target != null) {
			try {
				this.entityData.set(OWNER, Optional.of(target));
			} catch (Throwable throwable) {
				//
			}
		} else {
			this.entityData.set(OWNER, Optional.empty());
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(OWNER, Optional.empty());
	}

    @Override
    public void checkDespawn() {
        if (!this.isTamed()) {
            super.checkDespawn();
        }
    }

    @Nullable
    public EntityReference<LivingEntity> getOwner() {
        return this.entityData.get(OWNER).orElse(null);
    }

	public void setOwner(@Nullable LivingEntity target) {
		this.entityData.set(OWNER, Optional.ofNullable(target).map(EntityReference::of));
	}

	public void setOwnerDirectly(@Nullable EntityReference<LivingEntity> target) {
		this.entityData.set(OWNER, Optional.ofNullable(target));
	}

	public boolean isTamed() {
		return this.getOwner() != null;
	}
}