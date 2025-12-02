package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalClient;
import net.salju.supernatural.client.model.MerfolkModel;
import net.salju.supernatural.client.renderer.layers.MerfolkEyesLayer;
import net.salju.supernatural.entity.AbstractMerfolkEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;

public abstract class AbstractMerfolkRenderer extends MobRenderer<AbstractMerfolkEntity, MerfolkModel<AbstractMerfolkEntity>> {
	public AbstractMerfolkRenderer(EntityRendererProvider.Context context) {
		super(context, new MerfolkModel<>(context.bakeLayer(SupernaturalClient.MERFOLK)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new MerfolkEyesLayer<>(this));
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractMerfolkEntity target) {
		return ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/merfolk/" + this.getMerfolkType() + ".png");
	}

    @Override
    public void render(AbstractMerfolkEntity merfolk, float f1, float f2, PoseStack stack, MultiBufferSource buffer, int i) {
        stack.pushPose();
        if (merfolk.onGround() && !merfolk.isInWater()) {
            stack.translate(0, -0.5, 0);
        } else {
            stack.translate(0, 0, 0);
        }
        float scale = 1.0F;
        stack.scale(scale, scale, scale);
        super.render(merfolk, f1, f2, stack, buffer, i);
        stack.popPose();
    }

    public abstract String getMerfolkType();
}