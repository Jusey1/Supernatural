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
	public void setupAnim(T target) {
		this.defaultPose(target);
		this.poseArms(target, target.isLeftHanded ? this.leftArm : this.rightArm, target.isLeftHanded ? this.rightArm : this.leftArm);
		if (target.attackTime > 0.0F) {
			this.setupAttackAnimation(target);
		}
	}

	@Override
	protected void setupAttackAnimation(T target) {
		if (target.isAggressive) {
			ModelPart arm = this.getArm(target.attackArm);
			float progress = 1.0F - target.attackTime;
			progress = progress * progress;
			progress = progress * progress;
			progress = 1.0F - progress;
			float f2 = Mth.sin(progress * (float) Math.PI);
			arm.xRot = (float) ((double) arm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
		}
	}

	protected void defaultPose(T target) {
		this.rightArm.xRot = Mth.cos(target.walkAnimationPos * 0.6662F + (float) Math.PI) * 2.0F * target.walkAnimationSpeed * 0.5F - (0.0025F);
		this.leftArm.xRot = Mth.cos(target.walkAnimationPos * 0.6662F) * 2.0F * target.walkAnimationSpeed * 0.5F - (0.0025F);
		this.rightArm.yRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.body.xRot = 0.0F;
		this.body.yRot = 0.0F;
		this.body.zRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(target.walkAnimationPos * 0.6662F) * 1.4F * target.walkAnimationSpeed + (0.0025F);
		this.leftLeg.xRot = Mth.cos(target.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * target.walkAnimationSpeed - (0.0025F);
		this.head.yRot = target.yRot * ((float) Math.PI / 180F);
		this.head.xRot = target.xRot * ((float) Math.PI / 180F);
		this.hat.yRot = target.yRot * ((float) Math.PI / 180F);
		this.hat.xRot = target.xRot * ((float) Math.PI / 180F);
		if (target.isPassenger) {
			this.rightLeg.xRot = -1.5708F;
			this.leftLeg.xRot = -1.5708F;
			this.rightLeg.yRot = 0.2618F;
			this.leftLeg.yRot = -0.2618F;
		}
	}

	protected void poseArms(T target, ModelPart mainArm, ModelPart offArm) {
		if (!target.getMainHandItemStack().isEmpty()) {
			if (target.isAggressive) {
				mainArm.xRot = -2.0944F;
				mainArm.yRot = target.isLeftHanded ? -0.1745F : 0.1745F;
			}
		}
	}
}