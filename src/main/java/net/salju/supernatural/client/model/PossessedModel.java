package net.salju.supernatural.client.model;

import net.salju.supernatural.client.renderer.SupernaturalRenderState;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;

public class PossessedModel<T extends SupernaturalRenderState> extends HumanoidModel<T> {
	public PossessedModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(T ghost) {
		this.rightArm.xRot = Mth.cos(ghost.walkAnimationPos * 0.6662F + (float) Math.PI) * 2.0F * ghost.walkAnimationSpeed * 0.5F - (0.0025F);
		this.leftArm.xRot = Mth.cos(ghost.walkAnimationPos * 0.6662F) * 2.0F * ghost.walkAnimationSpeed * 0.5F - (0.0025F);
		this.rightArm.yRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.body.xRot = 0.0F;
		this.body.yRot = 0.0F;
		this.body.zRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(ghost.walkAnimationPos * 0.6662F) * 1.4F * ghost.walkAnimationSpeed + (0.0025F);
		this.leftLeg.xRot = Mth.cos(ghost.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * ghost.walkAnimationSpeed - (0.0025F);
		this.head.yRot = ghost.yRot * ((float) Math.PI / 180F);
		this.head.xRot = ghost.xRot * ((float) Math.PI / 180F);
		this.hat.yRot = ghost.yRot * ((float) Math.PI / 180F);
		this.hat.xRot = ghost.xRot * ((float) Math.PI / 180F);
		if (ghost.isPassenger) {
			this.rightLeg.xRot = -1.5708F;
			this.leftLeg.xRot = -1.5708F;
			this.rightLeg.yRot = 0.2618F;
			this.leftLeg.yRot = -0.2618F;
		}
		if (!ghost.getMainHandItem().isEmpty()) {
			if (ghost.isAggressive) {
				if (ghost.isLeftHanded) {
					this.leftArm.xRot = -2.0944F;
					this.leftArm.yRot = -0.1745F;
				} else {
					this.rightArm.xRot = -2.0944F;
					this.rightArm.yRot = 0.1745F;
				}
			}
		}
		if (ghost.attackTime > 0.0F) {
			if (ghost.isAggressive) {
				if (ghost.isLeftHanded) {
					float progress = ghost.attackTime;
					progress = 1.0F - ghost.attackTime;
					progress = progress * progress;
					progress = progress * progress;
					progress = 1.0F - progress;
					float f2 = Mth.sin(progress * (float) Math.PI);
					leftArm.xRot = (float) ((double) leftArm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
				} else {
					float progress = ghost.attackTime;
					progress = 1.0F - ghost.attackTime;
					progress = progress * progress;
					progress = progress * progress;
					progress = 1.0F - progress;
					float f2 = Mth.sin(progress * (float) Math.PI);
					rightArm.xRot = (float) ((double) rightArm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
				}
			}
		}
	}
}