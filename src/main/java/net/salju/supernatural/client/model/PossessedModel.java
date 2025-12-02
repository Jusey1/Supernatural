package net.salju.supernatural.client.model;

import net.salju.supernatural.entity.PossessedArmor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.util.Mth;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;

public class PossessedModel<T extends PossessedArmor> extends HumanoidModel<T> {
	public PossessedModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		return LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64);
	}

	@Override
	public void setupAnim(T ghost, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
		this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F - (0.0025F);
		this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F - (0.0025F);
		this.rightArm.yRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		this.body.xRot = 0.0F;
		this.body.yRot = 0.0F;
		this.body.zRot = 0.0F;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount + (0.0025F);
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount - (0.0025F);
		this.head.yRot = headYaw * ((float) Math.PI / 180F);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);
		if (ghost.isPassenger()) {
			this.rightLeg.xRot = -1.5708F;
			this.leftLeg.xRot = -1.5708F;
			this.rightLeg.yRot = 0.2618F;
			this.leftLeg.yRot = -0.2618F;
		}
		if (ghost.hasItemInSlot(EquipmentSlot.MAINHAND)) {
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
			} else {
				if (ghost.hasItemInSlot(EquipmentSlot.OFFHAND)) {
					float progress = this.attackTime;
					this.body.yRot = Mth.sin(Mth.sqrt(progress) * ((float) Math.PI * 2F)) * 0.2F;
					this.rightArm.yRot += this.body.yRot;
					this.leftArm.yRot += this.body.yRot;
					this.leftArm.xRot += this.body.yRot;
					progress = 1.0F - this.attackTime;
					progress = progress * progress;
					progress = progress * progress;
					progress = 1.0F - progress;
					float f2 = Mth.sin(progress * (float) Math.PI);
					float f3 = Mth.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
					rightArm.xRot = (float) ((double) rightArm.xRot - ((double) f2 * 1.2D + (double) f3));
					rightArm.yRot += this.body.yRot * 2.0F;
					rightArm.zRot += Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
				}
			}
		}
        this.hat.copyFrom(this.head);
	}
}