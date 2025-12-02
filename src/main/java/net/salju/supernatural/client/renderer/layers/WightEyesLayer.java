package net.salju.supernatural.client.renderer.layers;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.client.model.WightModel;
import net.salju.supernatural.entity.Wight;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;

public class WightEyesLayer<S extends Wight, M extends WightModel<S>> extends EyesLayer<S, M> {
	public WightEyesLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int i, Wight target, float f1, float f2, float f3, float f4, float f5, float f6) {
        this.getParentModel().renderToBuffer(pose, buffer.getBuffer(this.renderType()), i, OverlayTexture.NO_OVERLAY);
    }

    @Override
    public RenderType renderType() {
        return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/wight/wight_eyes.png"));
    }
}