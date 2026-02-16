package net.salju.supernatural.block.entity;

import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.salju.supernatural.block.TreasureVaultBlock;
import net.salju.supernatural.block.misc.TreasureVault;
import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.block.misc.SoulSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ContainerHelper;
import net.salju.supernatural.init.SupernaturalItems;

public class TreasureVaultBlockEntity extends BlockEntity {
    private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(1, new ItemStack(SupernaturalItems.EBONSTEEL_KEY.get()));
    private int dcd;
    private int cd;

	public TreasureVaultBlockEntity(BlockPos pos, BlockState state) {
		super(SupernaturalBlocks.TV.get(), pos, state);
	}

	@Override
    public void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.stacks);
        tag.putInt("CD", this.getCD());
        tag.putInt("DCD", this.getDisplayCD());
	}

	@Override
    public void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        this.stacks = NonNullList.withSize(stacks.size(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.stacks);
        if (tag.getInt("CD").isPresent()) {
            this.setCD(tag.getInt("CD").get());
        }
        if (tag.getInt("DCD").isPresent()) {
            this.setDisplayCD(tag.getInt("DCD").get());
        }
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

    @Override
    public void onDataPacket(Connection queen, ValueInput tag) {
        this.stacks = NonNullList.withSize(stacks.size(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.stacks);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider regs) {
        TagValueOutput value = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, regs);
        ContainerHelper.saveAllItems(value, this.stacks, true);
        CompoundTag tag = value.buildResult();
        return tag;
    }

    public ItemStack getRenderStack() {
        return this.stacks.get(0);
    }

    public boolean canGiveTreasure() {
        return this.getCD() <= 0;
    }

    public int getCD() {
        return this.cd;
    }

    public int getDisplayCD() {
        return this.dcd;
    }

    public void setCD(int i) {
        this.cd = i;
    }

    public void setDisplayCD(int i) {
        this.dcd = i;
    }

    public void setRenderItem(ItemStack stack) {
        this.stacks.set(0, stack);
        this.updateBlock();
    }

    public void updateBlock() {
        this.setChanged();
        this.getLevel().updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public static void clientTick(Level world, BlockPos pos, BlockState state, TreasureVaultBlockEntity target) {
        SoulSpawner.particleTick(world, pos);
    }

    public static void serverTick(Level world, BlockPos pos, BlockState state, TreasureVaultBlockEntity target) {
        if (world instanceof ServerLevel lvl) {
            if (target.getDisplayCD() > 0) {
                target.setDisplayCD(target.getDisplayCD() - 1);
            } else if (target.canGiveTreasure()) {
                target.setRenderItem(TreasureVault.getRewards(lvl, pos, target, "gameplay/treasure_vault_loot").getLast());
                target.setDisplayCD(30);
            }
            if (target.getCD() > 0) {
                target.setCD(target.getCD() - 1);
            } else if (!target.canGiveTreasure()) {
                world.setBlock(pos, state.setValue(TreasureVaultBlock.TREASURE, false), 2);
            }
        }
    }
}