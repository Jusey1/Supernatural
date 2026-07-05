package net.salju.supernatural.block.entity;

import net.salju.supernatural.init.SupernaturalBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.network.Connection;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.Container;
import net.salju.supernatural.init.SupernaturalTags;

public class WeaponEntity extends BaseContainerBlockEntity {
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);

	public WeaponEntity(BlockPos pos, BlockState state) {
		super(SupernaturalBlocks.WEAPON.get(), pos, state);
	}

    @Override
    public void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.stacks, true);
    }

    @Override
    public void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.stacks);
    }

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

    @Override
    public void onDataPacket(Connection queen, ValueInput tag) {
        this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.stacks);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider regs) {
        TagValueOutput value = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, regs);
        ContainerHelper.saveAllItems(value, this.stacks, true);
        CompoundTag tag = value.buildResult();
        return tag;
    }

	@Override
	public Component getDefaultName() {
		return Component.translatable("block.supernatural.ebonsteel_weapon_display");
	}

	@Override
	public AbstractContainerMenu createMenu(int i, Inventory bag) {
		return null;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.stacks;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> items) {
		this.stacks = items;
	}

	@Override
	public int getContainerSize() {
		return stacks.size();
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return this.getItem(0).isEmpty();
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	@Override
	public ItemStack getItem(int i) {
		return this.stacks.get(i);
	}

	@Override
	public boolean canPlaceItem(int i, ItemStack stack) {
		return stack.is(SupernaturalTags.DISPLAY_ITEMS) && this.isEmpty();
	}

	@Override
	public ItemStack removeItemNoUpdate(int i) {
		return ContainerHelper.takeItem(this.stacks, i);
	}

	@Override
	public ItemStack removeItem(int i, int e) {
		this.updateBlock();
		return ContainerHelper.removeItem(this.stacks, i, e);
	}

	@Override
	public void setItem(int i, ItemStack stack) {
		this.stacks.set(i, stack.copy());
		this.getItem(i).setCount(1);
		this.updateBlock();
	}

	@Override
	public void clearContent() {
		this.stacks.clear();
		this.updateBlock();
	}

	public void updateBlock() {
		this.setChanged();
		this.getLevel().updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
	}

    public void dropItem(int i) {
        Containers.dropItemStack(this.getLevel(), this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), this.getItem(i));
        this.updateBlock();
    }
}