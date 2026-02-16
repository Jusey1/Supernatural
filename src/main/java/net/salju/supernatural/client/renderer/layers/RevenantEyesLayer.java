package net.salju.supernatural.client.renderer.layers;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.client.model.RevenantModel;
import net.salju.supernatural.client.renderer.SupernaturalRenderState;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.vertex.PoseStack;

public class RevenantEyesLayer<S extends SupernaturalRenderState, M extends RevenantModel<S>> extends EyesLayer<S, M> {
	public RevenantEyesLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector buffer, int i, S state, float f1, float f2) {
        if (!state.isInvisible) {
            buffer.order(1).submitModel(this.getParentModel(), state, pose, this.renderType(), i, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
        }
    }

    @Override
    public RenderType renderType() {
        return RenderTypes.eyes(Identifier.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/revenant/revenant_eyes.png"));
    }
}