package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalClient;
import net.salju.supernatural.client.model.MerfolkModel;
import net.salju.supernatural.client.renderer.layers.MerfolkEyesLayer;
import net.salju.supernatural.entity.Merfolk;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.blaze3d.vertex.PoseStack;

public abstract class AbstractMerfolkRenderer extends MobRenderer<Merfolk, SupernaturalRenderState, MerfolkModel<SupernaturalRenderState>> {
	public AbstractMerfolkRenderer(EntityRendererProvider.Context context) {
		super(context, new MerfolkModel<>(context.bakeLayer(SupernaturalClient.MERFOLK)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this));
        this.addLayer(new MerfolkEyesLayer<>(this));
	}

	@Override
	public Identifier getTextureLocation(SupernaturalRenderState state) {
		return Identifier.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/merfolk/" + state.type + ".png");
	}

    @Override
    public SupernaturalRenderState createRenderState() {
        return new SupernaturalRenderState();
    }

    @Override
    public void extractRenderState(Merfolk target, SupernaturalRenderState state, float f1) {
        super.extractRenderState(target, state, f1);
        HumanoidMobRenderer.extractHumanoidRenderState(target, state, f1, this.itemModelResolver);
        state.isAggressive = target.isAggressive();
        state.isLeftHanded = target.isLeftHanded();
        state.isSwimming = target.isSwimming();
        state.onGround = target.onGround();
        state.type = this.getMerfolkType();
    }

    @Override
    public void submit(SupernaturalRenderState state, PoseStack stack, SubmitNodeCollector buffer, CameraRenderState c) {
        stack.pushPose();
        if (state.onGround && !state.isInWater) {
            stack.translate(0, -0.5, 0);
        } else {
            stack.translate(0, 0, 0);
        }
        float scale = 1.0F;
        stack.scale(scale, scale, scale);
        super.submit(state, stack, buffer, c);
        stack.popPose();
    }

    public abstract String getMerfolkType();
}