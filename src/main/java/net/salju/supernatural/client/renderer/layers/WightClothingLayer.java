package net.salju.supernatural.client.renderer.layers;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalClient;
import net.salju.supernatural.client.model.WightModel;
import net.salju.supernatural.client.renderer.SupernaturalRenderState;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.vertex.PoseStack;

public class WightClothingLayer<S extends SupernaturalRenderState, M extends WightModel<S>> extends RenderLayer<S, M> {
    private final WightModel<S> model;

	public WightClothingLayer(RenderLayerParent<S, M> parent, EntityModelSet set) {
		super(parent);
        this.model = new WightModel<>(set.bakeLayer(SupernaturalClient.WIGHT_CLOTHING));
	}

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector buffer, int i, S target, float f1, float f2) {
        coloredCutoutModelCopyLayerRender(this.model, Identifier.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/wight/wight_clothing.png"), pose, buffer, i, target, -1, 1);
    }
}