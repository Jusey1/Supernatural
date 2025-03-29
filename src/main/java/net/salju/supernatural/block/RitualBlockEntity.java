package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.init.SupernaturalTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Containers;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Container;

public class RitualBlockEntity extends BaseContainerBlockEntity {
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
	private boolean greedy;
	private int timer = 0;

	public RitualBlockEntity(BlockPos pos, BlockState state) {
		super(SupernaturalBlocks.RITUAL.get(), pos, state);
	}

	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider regs) {
		super.saveAdditional(tag, regs);
		ContainerHelper.saveAllItems(tag, this.stacks, regs);
		tag.putBoolean("Greedy", this.greedy);
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider regs) {
		super.loadAdditional(tag, regs);
		this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, this.stacks, regs);
		this.greedy = tag.getBoolean("Greedy");
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection queen, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider regs) {
		this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(packet.getTag(), this.stacks, regs);
		this.greedy = packet.getTag().getBoolean("Greedy");
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider regs) {
		CompoundTag tag = new CompoundTag();
		ContainerHelper.saveAllItems(tag, this.stacks, regs);
		tag.putBoolean("Greedy", this.greedy);
		return tag;
	}

	@Override
	public Component getDefaultName() {
		return Component.translatable("block.supernatural.ritual_altar");
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
		return this.isEmpty();
	}

	@Override
	public ItemStack removeItemNoUpdate(int i) {
		return this.getItem(i).is(SupernaturalTags.KEPT) || this.getGreedy() ? ItemStack.EMPTY : ContainerHelper.takeItem(this.stacks, i);
	}

	@Override
	public ItemStack removeItem(int i, int e) {
		this.updateBlock();
		return this.getItem(i).is(SupernaturalTags.KEPT) || this.getGreedy() ? ItemStack.EMPTY : ContainerHelper.removeItem(this.stacks, i, e);
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
		if (!this.getGreedy()) {
			Containers.dropItemStack(this.getLevel(), this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), this.getItem(i));
			this.updateBlock();
		}
	}

	public void cloneItem(ItemStack stack) {
		Containers.dropItemStack(this.getLevel(), this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), stack);
	}

	public void setGreedy(boolean check) {
		this.greedy = check;
	}

	public boolean getGreedy() {
		return this.greedy;
	}

	public static void tick(Level world, BlockPos pos, BlockState state, RitualBlockEntity target) {
		if (world instanceof ServerLevel lvl && !target.isEmpty()) {
			++target.timer;
			if (target.timer >= 5) {
				lvl.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, (pos.getX() + 0.5), (pos.getY() + 0.5), (pos.getZ() + 0.5), 2, 0.1, 0.15, 0.1, 0);
				target.timer = 0;
			}
		}
	}
}