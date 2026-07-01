package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalTags;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AbstractMinionEntity extends Monster implements OwnableEntity {
	private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> OWNER = SynchedEntityData.defineId(AbstractMinionEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);

	public AbstractMinionEntity(EntityType<? extends AbstractMinionEntity> type, Level world) {
		super(type, world);
        this.xpReward = 10;
        this.setPathfindingMalus(PathType.WATER, -1.0F);
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
    protected boolean canReplaceCurrentItem(ItemStack drop, ItemStack hand, EquipmentSlot slot) {
        if (drop.is(SupernaturalTags.DARK_ARMOR) || drop.is(Items.IRON_SWORD)) {
            return hand.isEmpty() || this.canReplaceEqualItem(drop, hand);
        }
        return false;
    }

    @Override
    public boolean canReplaceEqualItem(ItemStack drop, ItemStack hand) {
        if (drop.isEnchanted()) {
            if (hand.isEnchanted()) {
                int id = 0;
                ItemEnchantments.Mutable dropMap = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(drop));
                for (Holder<Enchantment> e : dropMap.keySet()) {
                    id = (id + dropMap.getLevel(e));
                }
                int ih = 0;
                ItemEnchantments.Mutable handMap = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(hand));
                for (Holder<Enchantment> e : handMap.keySet()) {
                    ih = (ih + handMap.getLevel(e));
                }
                return id > ih;
            }
            return !hand.isEnchanted();
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

    public boolean isNatural() {
        return this.getOwnerReference() == null;
    }

	public void setOwner(@Nullable LivingEntity target) {
		this.entityData.set(OWNER, Optional.ofNullable(target).map(EntityReference::of));
	}
}