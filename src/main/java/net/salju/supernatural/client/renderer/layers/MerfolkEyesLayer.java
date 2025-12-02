package net.salju.supernatural.client.renderer.layers;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.client.model.MerfolkModel;
import net.salju.supernatural.client.renderer.SupernaturalRenderState;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;

public class MerfolkEyesLayer<S extends SupernaturalRenderState, M extends MerfolkModel<S>> extends EyesLayer<S, M> {
	public MerfolkEyesLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector buffer, int i, S state, float f1, float f2) {
        buffer.order(1).submitModel(this.getParentModel(), state, pose, this.renderSpecialType(state.type), i, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
    }

    @Override
    public RenderType renderType() {
        return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/merfolk/diamond_glow.png"));
    }

    public RenderType renderSpecialType(String type) {
        if (!type.isEmpty()) {
            return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/merfolk/" + type + "_glow.png"));
        }
        return this.renderType();
    }
}