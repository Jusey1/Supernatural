package net.salju.supernatural.entity.ai.thrall;

import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.block.entity.RitualAltarEntity;
import net.salju.supernatural.entity.Thrall;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;

public class ThrallAltarGoal extends MoveToBlockGoal {
    private final Thrall bob;

	public ThrallAltarGoal(Thrall bob, double speed, int range, int v) {
        super(bob, speed, range, v);
        this.bob = bob;
	}

    @Override
    public boolean canUse() {
        ItemStack stack = this.bob.getOffhandItem();
        if (stack.isEmpty() || stack.is(SupernaturalItems.SOULGEM.get())) {
            return super.canUse();
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isReachedTarget()) {
            if (this.bob.level().getBlockEntity(this.getMoveToTarget()) instanceof RitualAltarEntity target) {
                ItemStack stack = this.bob.getOffhandItem();
                if (stack.is(SupernaturalItems.SOULGEM.get())) {
                    target.setItem(0, stack);
                } else {
                    target.clearContent();
                }
                this.bob.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.AMETHYST_SHARD));
                this.bob.setDropChance(EquipmentSlot.OFFHAND, 1.0F);
                this.stop();
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.bob.getNavigation().stop();
        this.blockPos = BlockPos.ZERO;
    }

    @Override
    public double acceptedDistance() {
        return 2.76;
    }

    @Override
    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof RitualAltarEntity target) {
            return target.getItem(0).is(Items.AMETHYST_SHARD);
        }
        return false;
    }

    @Override
    protected BlockPos getMoveToTarget() {
        return this.blockPos;
    }
}