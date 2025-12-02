package net.salju.supernatural.client.renderer.layers;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalClient;
import net.salju.supernatural.client.model.WightModel;
import net.salju.supernatural.entity.Wight;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;

public class WightClothingLayer<T extends Wight, M extends WightModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation WIGHT_CLOTHING = ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/wight/wight_clothing.png");
    private final WightModel<T> wight;

	public WightClothingLayer(RenderLayerParent<T, M> parent, EntityModelSet set) {
		super(parent);
        this.wight = new WightModel<>(set.bakeLayer(SupernaturalClient.WIGHT_CLOTHING));
	}

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int i, T target, float f1, float f2, float f3, float f4, float f5, float f6) {
        coloredCutoutModelCopyLayerRender(this.getParentModel(), this.wight, WIGHT_CLOTHING, pose, buffer, i, target, f1, f2, f4, f5, f6, f3, -1);
    }
}