package net.salju.supernatural.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import java.util.UUID;

public abstract class AbstractMinionEntity extends AbstractSpellcasterEntity {
	private UUID friend;

	public AbstractMinionEntity(EntityType<? extends AbstractMinionEntity> type, Level world) {
		super(type, world);
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
			this.friend = tag.getUUID("Player");
		}
	}

    @Override
    public boolean shouldDespawnInPeaceful() {
        return !this.isTamed();
    }

    @Override
    public boolean shouldDropExperience() {
        return !this.isTamed();
    }

	public UUID getOwner() {
		return this.friend;
	}

	public void setOwner(UUID player) {
		this.friend = player;
	}

	public boolean isTamed() {
		return this.getOwner() != null;
	}
}