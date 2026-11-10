package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.entity.Vampyre;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.illager.IllagerModel;
import com.mojang.blaze3d.vertex.PoseStack;

public class VampyreRenderer extends IllagerRenderer<Vampyre, VampyreRenderState> {
	public VampyreRenderer(EntityRendererProvider.Context context) {
		super(context, new IllagerModel<>(context.bakeLayer(ModelLayers.VINDICATOR)), 0.5F);
		this.addLayer(new EyesLayer<>(this) {
			@Override
			public RenderType renderType() {
				return RenderTypes.eyes(Identifier.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/vampyre/vampyre_eyes.png"));
			}
		});
		this.addLayer(new ItemInHandLayer<>(this) {
			public void submit(PoseStack pose, SubmitNodeCollector buffer, int i, VampyreRenderState target, float f1, float f2) {
				if (target.isAggressive) {
					super.submit(pose, buffer, i, target, f1, f2);
				}
			}
		});
	}

	@Override
	public Identifier getTextureLocation(VampyreRenderState target) {
		return Identifier.fromNamespaceAndPath(Supernatural.MODID, target.type);
	}

	@Override
	public VampyreRenderState createRenderState() {
		return new VampyreRenderState();
	}

	@Override
	public void extractRenderState(Vampyre target, VampyreRenderState state, float f1) {
		super.extractRenderState(target, state, f1);
		state.type = "textures/entity/vampyre/vampyre" + (target.isNecromancer() ? "_necromancer.png" : ".png");
	}
}