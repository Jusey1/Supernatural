package net.salju.supernatural.client.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class SupernaturalBlockState extends BlockEntityRenderState {
    public ItemStackRenderState itemState = new ItemStackRenderState();
    public @Nullable EntityRenderState displayEntity;
	public ItemStack item;
    public float spin;
    public float scale;
    public float main;
    public int outlineColor = 0;
    public long time;
}