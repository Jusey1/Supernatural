package net.salju.supernatural.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AbstractMinionEntity extends AbstractSpellcasterEntity implements OwnableEntity {
	private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> OWNER = SynchedEntityData.defineId(AbstractMinionEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);

	public AbstractMinionEntity(EntityType<? extends AbstractMinionEntity> type, Level world) {
		super(type, world);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput tag) {
		super.addAdditionalSaveData(tag);
        this.entityData.get(OWNER).ifPresent(target -> target.store(tag, "Owner"));
    }

	@Override
	public void readAdditionalSaveData(ValueInput tag) {
		super.readAdditionalSaveData(tag);
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
	}

    @Override
    public boolean shouldDropExperience() {
        return this.isNatural();
    }

    @Override
    protected boolean shouldDropLoot(ServerLevel lvl) {
        return this.isNatural() ? super.shouldDropLoot(lvl) : false;
    }

    @Override
    public EntityReference<LivingEntity> getOwnerReference() {
        return this.entityData.get(OWNER).orElse(null);
    }

    public boolean isNatural() {
        return this.getOwnerReference() == null;
    }

	public void setOwner(@Nullable LivingEntity target) {
		this.entityData.set(OWNER, Optional.ofNullable(target).map(EntityReference::of));
	}
}