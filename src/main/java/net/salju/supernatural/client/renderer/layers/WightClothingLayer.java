package net.salju.supernatural.client.renderer.layers;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalClient;
import net.salju.supernatural.client.model.WightModel;
import net.salju.supernatural.client.renderer.SupernaturalRenderState;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.vertex.PoseStack;

public class WightClothingLayer<S extends SupernaturalRenderState, M extends WightModel<S>> extends RenderLayer<S, M> {
    private static final Identifier WIGHT_CLOTHING = Identifier.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/wight/wight_clothing.png");
    private final WightModel<S> wight;

	public WightClothingLayer(RenderLayerParent<S, M> parent, EntityModelSet set) {
		super(parent);
        this.wight = new WightModel<>(set.bakeLayer(SupernaturalClient.WIGHT_CLOTHING));
	}

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector buffer, int i, S state, float f1, float f2) {
        coloredCutoutModelCopyLayerRender(this.wight, WIGHT_CLOTHING, pose, buffer, i, state, -1, 1);
    }
}