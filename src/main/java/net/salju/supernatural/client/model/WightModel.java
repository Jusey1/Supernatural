package net.salju.supernatural.client.model;


import net.salju.supernatural.entity.Wight;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CrossbowItem;

public class WightModel<T extends Wight> extends SkeletonModel<T> {
	public WightModel(ModelPart root) {
		super(root);
	}

    public static LayerDefinition createBodyLayer() {
        return SkeletonModel.createBodyLayer();
    }

    public static LayerDefinition createClothingLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.0F);
        mesh.getRoot().addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
        mesh.getRoot().addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(1.9F, 12.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

	@Override
	public void setupAnim(T ghost, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.rightArm.yRot = 0.0F;
        this.rightArm.zRot = 0.0F;
        this.leftArm.yRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        this.rightLeg.yRot = 0.0F;
        this.leftLeg.yRot = 0.0F;
        this.body.xRot = 0.0F;
        this.body.yRot = 0.0F;
        this.body.zRot = 0.0F;
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.head.yRot = headYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.rightArm.xRot += Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
        this.leftArm.xRot -= Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
        this.rightArm.zRot += Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
        this.leftArm.zRot -= Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
		if (ghost.isPassenger()) {
			this.rightLeg.xRot = -1.5708F;
			this.leftLeg.xRot = -1.5708F;
			this.rightLeg.yRot = 0.2618F;
			this.leftLeg.yRot = -0.2618F;
		}
        if (ghost.isCastingSpell()) {
            if (ghost.isLeftHanded()) {
                this.rightArm.z = 0.0F;
                this.rightArm.x = -5.0F;
                this.rightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
                this.rightArm.zRot = 2.3561945F;
                this.rightArm.yRot = 0.0F;
            } else {
                this.leftArm.z = 0.0F;
                this.leftArm.x = 5.0F;
                this.leftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
                this.leftArm.zRot = -2.3561945F;
                this.leftArm.yRot = 0.0F;
            }
        }
		if (!ghost.getMainHandItem().isEmpty()) {
            if (ghost.getMainHandItem().getItem() instanceof CrossbowItem) {
                if (ghost.isAggressive()) {
                    if (ghost.isLeftHanded()) {
                        if (ghost.isCharging()) {
                            this.leftArm.xRot = -2.4876F;
                        } else {
                            this.leftArm.xRot = -1.4399F;
                        }
                    } else {
                        if (ghost.isCharging()) {
                            this.rightArm.xRot = -2.4876F;
                        } else {
                            this.rightArm.xRot = -1.4399F;
                        }
                    }
                }
            } else {
                if (ghost.isAggressive()) {
                    if (ghost.isLeftHanded()) {
                        this.leftArm.xRot = -2.0944F;
                        this.leftArm.yRot = -0.1745F;
                    } else {
                        this.rightArm.xRot = -2.0944F;
                        this.rightArm.yRot = 0.1745F;
                    }
                }
            }
		}
		if (this.attackTime > 0.0F) {
			if (ghost.isAggressive()) {
				if (ghost.isLeftHanded()) {
					float progress = this.attackTime;
					progress = 1.0F - this.attackTime;
					progress = progress * progress;
					progress = progress * progress;
					progress = 1.0F - progress;
					float f2 = Mth.sin(progress * (float) Math.PI);
					leftArm.xRot = (float) ((double) leftArm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
				} else {
					float progress = this.attackTime;
					progress = 1.0F - this.attackTime;
					progress = progress * progress;
					progress = progress * progress;
					progress = 1.0F - progress;
					float f2 = Mth.sin(progress * (float) Math.PI);
					rightArm.xRot = (float) ((double) rightArm.xRot - ((double) f2 / 1.2D - (double) 1.0F));
				}
			}
		}
        this.hat.copyFrom(this.head);
	}
}