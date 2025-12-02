package net.salju.supernatural.client.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;

public class RitualBlockState extends BlockEntityRenderState {
    public ItemStackRenderState itemState = new ItemStackRenderState();
	public ItemStack item;
    public float main;
    public int outlineColor = 0;
    public long time;
}