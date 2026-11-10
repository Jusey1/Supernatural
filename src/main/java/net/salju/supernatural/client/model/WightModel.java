package net.salju.supernatural.client.model;

import net.salju.supernatural.client.renderer.SupernaturalRenderState;
import net.minecraft.client.model.monster.skeleton.SkeletonModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.blaze3d.vertex.PoseStack;

public class WightModel<T extends SupernaturalRenderState> extends HumanoidModel<T> {
	public WightModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		return SkeletonModel.createBodyLayer();
	}

    public static LayerDefinition createClothingLayer() {
        return LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.0F), 64, 32);
    }

	@Override
	public void translateToHand(SupernaturalRenderState state, HumanoidArm arm, PoseStack pose) {
		this.root().translateAndRotate(pose);
		float f = arm.equals(HumanoidArm.RIGHT) ? 1.0F : -1.0F;
		ModelPart part = this.getArm(arm);
		part.x += f;
		part.translateAndRotate(pose);
		part.x -= f;
	}

	@Override
	public void setupAnim(T target) {
		this.defaultPose(target);
		this.poseArms(target, target.isLeftHanded ? this.leftArm : this.rightArm, target.isLeftHanded ? this.rightArm : this.leftArm);
		if (target.attackTime > 0.0F) {
			this.setupAttackAnimation(target, target.isLeftHanded ? this.leftArm : this.rightArm);
		}
	}

	protected void setupAttackAnimation(T target, ModelPart mainArm) {
		if (target.isAggressive) {
			float progress = 1.0F - target.attackTime;
			progress = progress * progress;
			progress = progress * progress;
			progress = 1.0F - progress;
			float f2 = Mth.sin(progress * (float) Math.PI);
			mainArm.xRot = (float) ((double) mainArm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
		}
	}

	protected void defaultPose(T target) {
		this.head.xRot = target.xRot * ((float) Math.PI / 180F);
		this.head.yRot = target.yRot * ((float) Math.PI / 180F);
		this.body.xRot = 0.0F;
		this.body.yRot = 0.0F;
		this.body.zRot = 0.0F;
		this.rightArm.xRot = Mth.cos(target.walkAnimationPos * 0.6662F + (float) Math.PI) * 2.0F * target.walkAnimationSpeed * 0.5F;
		this.leftArm.xRot = Mth.cos(target.walkAnimationPos * 0.6662F) * 2.0F * target.walkAnimationSpeed * 0.5F;
		this.rightArm.yRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(target.walkAnimationPos * 0.6662F) * 1.4F * target.walkAnimationSpeed;
		this.leftLeg.xRot = Mth.cos(target.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * target.walkAnimationSpeed;
		this.rightArm.xRot += Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
		this.leftArm.xRot -= Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
		this.rightArm.zRot += Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
		this.leftArm.zRot -= Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
		if (target.isPassenger) {
			this.rightLeg.xRot = -1.5708F;
			this.leftLeg.xRot = -1.5708F;
			this.rightLeg.yRot = 0.2618F;
			this.leftLeg.yRot = -0.2618F;
		}
	}

	protected void poseArms(T target, ModelPart mainArm, ModelPart offArm) {
		if (target.isAggressive) {
            if (target.isCastingSpell) {
                if (target.isLeftHanded) {
                    mainArm.yRot = 0.1F + this.head.yRot;
                    offArm.yRot = -0.1F + this.head.yRot - 0.4F;
                } else {
                    mainArm.yRot = -0.1F + this.head.yRot;
                    offArm.yRot = 0.1F + this.head.yRot + 0.4F;
                }
                mainArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
                offArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
			} else {
				 mainArm.xRot = -2.0944F;
				 mainArm.yRot = 0.1745F;
			}
		}
	}
}