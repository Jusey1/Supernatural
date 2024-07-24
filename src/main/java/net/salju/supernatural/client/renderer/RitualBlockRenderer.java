package net.salju.supernatural.client.renderer;

import org.joml.Quaternionf;
import net.salju.supernatural.block.RitualBlockEntity;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

@OnlyIn(Dist.CLIENT)
public class RitualBlockRenderer implements BlockEntityRenderer<RitualBlockEntity> {
	private final ItemRenderer item;

	public RitualBlockRenderer(BlockEntityRendererProvider.Context context) {
		this.item = context.getItemRenderer();
	}

	@Override
	public void render(RitualBlockEntity target, float main, PoseStack pose, MultiBufferSource buffer, int i, int e) {
		ItemStack stack = target.getItem(0);
		if (!stack.isEmpty()) {
			this.renderItem(target, stack, main, pose, buffer, i);
		}
	}

	protected void renderItem(RitualBlockEntity target, ItemStack stack, float main, PoseStack pose, MultiBufferSource buffer, int light) {
		pose.pushPose();
		pose.translate(0.5, 0.75, 0.5);
		BakedModel model = this.item.getModel(stack, null, null, light);
		int i = (int) (2 * 360f);
		float f1 = (Math.floorMod(target.getLevel().getGameTime(), (long) i) + main) / (float) i;
		Quaternionf rot = Axis.YP.rotation((float) (f1 * Math.PI * 10));
		pose.mulPose(rot);
		this.item.render(stack, ItemDisplayContext.GROUND, false, pose, buffer, light, OverlayTexture.NO_OVERLAY, model);
		pose.popPose();
	}
}