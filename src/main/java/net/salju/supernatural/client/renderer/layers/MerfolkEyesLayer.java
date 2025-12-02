package net.salju.supernatural.client.renderer.layers;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.client.model.MerfolkModel;
import net.salju.supernatural.entity.AbstractMerfolkEntity;
import net.salju.supernatural.entity.MerfolkAmethyst;
import net.salju.supernatural.entity.MerfolkEmerald;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;

public class MerfolkEyesLayer<S extends AbstractMerfolkEntity, M extends MerfolkModel<S>> extends EyesLayer<S, M> {
	public MerfolkEyesLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int i, AbstractMerfolkEntity merfolk, float f1, float f2, float f3, float f4, float f5, float f6) {
        this.getParentModel().renderToBuffer(pose, buffer.getBuffer(this.renderSpecialType(this.getMerfolkEyes(merfolk))), i, OverlayTexture.NO_OVERLAY);
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

    public String getMerfolkEyes(AbstractMerfolkEntity merfolk) {
        if (merfolk instanceof MerfolkAmethyst) {
            return "amethyst";
        } else if (merfolk instanceof MerfolkEmerald) {
            return "emerald";
        }
        return "diamond";
    }
}