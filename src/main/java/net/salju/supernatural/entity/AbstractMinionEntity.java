package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalTags;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AbstractMinionEntity extends AbstractSpellcasterEntity implements OwnableEntity {
	private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> OWNER = SynchedEntityData.defineId(AbstractMinionEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    private ItemStack primary = new ItemStack(Items.IRON_SWORD);
    private ItemStack secondary = new ItemStack(Items.CROSSBOW);

	public AbstractMinionEntity(EntityType<? extends AbstractMinionEntity> type, Level world) {
		super(type, world);
        this.xpReward = 10;
        this.setPathfindingMalus(PathType.WATER, -1.0F);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput tag) {
		super.addAdditionalSaveData(tag);
        this.entityData.get(OWNER).ifPresent(target -> target.store(tag, "Owner"));
        if (!this.getPrimary().isEmpty()) {
            tag.store("Primary", ItemStack.CODEC, this.getPrimary());
        }
        if (!this.getSecondary().isEmpty()) {
            tag.store("Secondary", ItemStack.CODEC, this.getSecondary());
        }
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
        if (tag.read("Primary", ItemStack.CODEC).isPresent()) {
            this.setPrimary(tag.read("Primary", ItemStack.CODEC).orElse(ItemStack.EMPTY));
        }
        if (tag.read("Secondary", ItemStack.CODEC).isPresent()) {
            this.setSecondary(tag.read("Secondary", ItemStack.CODEC).orElse(ItemStack.EMPTY));
        }
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(OWNER, Optional.empty());
	}

    @Override
    protected boolean canReplaceCurrentItem(ItemStack drop, ItemStack hand, EquipmentSlot slot) {
        if (drop.is(SupernaturalTags.DARK_ARMOR)) {
            return !hand.is(SupernaturalTags.DARK_ARMOR) || !hand.isEnchanted();
        } else if (drop.is(Items.IRON_SWORD) && !hand.isEnchanted()) {
            this.setPrimary(drop);
            return true;
        }
        return false;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public boolean shouldDropExperience() {
        return this.isNatural();
    }

    @Override
    public boolean removeWhenFarAway(double d) {
        return this.isNatural() ? super.removeWhenFarAway(d) : false;
    }

    @Override
    public EntityReference<LivingEntity> getOwnerReference() {
        return this.entityData.get(OWNER).orElse(null);
    }

    public ItemStack getPrimary() {
        return this.primary;
    }

    public ItemStack getSecondary() {
        return this.secondary;
    }

    public boolean isNatural() {
        return this.getOwnerReference() == null;
    }

	public void setOwner(@Nullable LivingEntity target) {
		this.entityData.set(OWNER, Optional.ofNullable(target).map(EntityReference::of));
	}

    public void setPrimary(ItemStack stack) {
        this.primary = stack;
    }

    public void setSecondary(ItemStack stack) {
        this.secondary = stack;
    }
}