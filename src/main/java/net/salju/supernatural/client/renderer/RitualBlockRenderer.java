package net.salju.supernatural.client.renderer;

import net.salju.supernatural.block.RitualBlockEntity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Quaternionf;

public class RitualBlockRenderer implements BlockEntityRenderer<RitualBlockEntity> {
	private final ItemRenderer item;

	public RitualBlockRenderer(BlockEntityRendererProvider.Context context) {
		this.item = context.getItemRenderer();
	}

	@Override
	public void render(RitualBlockEntity target, float f, PoseStack pose, MultiBufferSource buffer, int i, int e, Vec3 v) {
		ItemStack stack = target.getItem(0);
		if (!stack.isEmpty()) {
			this.renderItem(target, stack, f, pose, buffer, i, e);
		}
	}

	protected void renderItem(RitualBlockEntity target, ItemStack stack, float main, PoseStack pose, MultiBufferSource buffer, int light, int e) {
		pose.pushPose();
		pose.translate(0.5, 0.75, 0.5);
		int i = (int) (2 * 360f);
		float f1 = (Math.floorMod(target.getLevel().getGameTime(), (long) i) + main) / (float) i;
		Quaternionf rot = Axis.YP.rotation((float) (f1 * Math.PI * 10));
		pose.mulPose(rot);
		this.item.renderStatic(stack, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, pose, buffer, target.getLevel(), e);
		pose.popPose();
	}
}