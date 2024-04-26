package net.salju.supernatural.client.renderer;

import net.salju.supernatural.entity.Cannon;
import net.salju.supernatural.client.model.ArtCanModel;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

public class ArtCanRenderer extends LivingEntityRenderer<Cannon, ArtCanModel<Cannon>> {
	public ArtCanRenderer(EntityRendererProvider.Context context) {
		super(context, new ArtCanModel(context.bakeLayer(ArtCanModel.CANNON_MODEL)), 0.85f);
	}

	@Override
	public ResourceLocation getTextureLocation(Cannon target) {
		return new ResourceLocation("supernatural:textures/entity/artificer_cannon_" + Integer.toString(target.getCannonType()) + ".png");
	}

	@Override
	protected boolean shouldShowName(Cannon target) {
		return (!target.isEmpty());
	}

	@Override
	protected void setupRotations(Cannon target, PoseStack pose, float f1, float f2, float f3) {
		pose.mulPose(Axis.YP.rotationDegrees(180.0F - f2));
		float f = (float) (target.level().getGameTime() - target.lastHit) + f3;
		if (f < 5.0F) {
			pose.mulPose(Axis.YP.rotationDegrees(Mth.sin(f / 1.5F * (float) Math.PI) * 3.0F));
		}
	}
}