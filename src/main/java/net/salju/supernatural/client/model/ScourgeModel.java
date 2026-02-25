package net.salju.supernatural.client.model;

import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.animal.equine.AbstractEquineModel;
import net.minecraft.client.renderer.entity.state.EquineRenderState;

public class ScourgeModel<T extends EquineRenderState> extends AbstractEquineModel<T> {
	public ScourgeModel(ModelPart root) {
        super(root);
	}

	public static LayerDefinition createBodyLayer() {
		return LayerDefinition.create(AbstractEquineModel.createBodyMesh(CubeDeformation.NONE), 64, 64);
	}
}