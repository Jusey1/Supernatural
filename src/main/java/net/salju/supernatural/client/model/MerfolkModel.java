package net.salju.supernatural.client.model;

import net.salju.supernatural.client.renderer.SupernaturalRenderState;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.blaze3d.vertex.PoseStack;

public class MerfolkModel<T extends SupernaturalRenderState> extends EntityModel<T> implements ArmedModel {
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart upperTail;
	private final ModelPart lowerTail;

	public MerfolkModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightArm = root.getChild("right_arm");
		this.leftArm = root.getChild("left_arm");
		this.upperTail = root.getChild("upper_tail");
		this.lowerTail = root.getChild("lower_tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(4, 0).addBox(-6.0F, -8.0F, 0.0F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(4.0F, -8.0F, 0.0F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(32, 1).addBox(0.0F, -10.0F, -2.0F, 0.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 1.0F, 0.0F));
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(50, 44).addBox(0.0F, -12.0F, 2.0F, 0.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(28, 28).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 16.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 13.0F, 0.0F));
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(16, 48).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 2.0F, 0.0F));
		root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 48).addBox(0.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 2.0F, 0.0F));
		root.addOrReplaceChild("upper_tail", CubeListBuilder.create().texOffs(42, 16).addBox(-3.5F, 0.0F, -2.0F, 7.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 0.0F));
		root.addOrReplaceChild("lower_tail", CubeListBuilder.create().texOffs(32, 48).addBox(-3.0F, 8.0F, -1.5F, 6.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(32, 0).addBox(-8.0F, 15.0F, 0.0F, 16.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 0.0F));
		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void translateToHand(EntityRenderState state, HumanoidArm arm, PoseStack pose) {
		switch (arm) {
			case LEFT -> {
				this.leftArm.translateAndRotate(pose);
				pose.translate(0.045, 0.05, 0.0);
				pose.scale(0.95F, 0.95F, 0.95F);
			}
			case RIGHT -> {
				this.rightArm.translateAndRotate(pose);
				pose.translate(-0.045, 0.05, 0.0);
				pose.scale(0.95F, 0.95F, 0.95F);
			}
		}
	}

	@Override
	public void setupAnim(T target) {
		this.defaultPose(target);
		this.poseArms(target, target.isLeftHanded ? this.leftArm : this.rightArm, target.isLeftHanded ? this.rightArm : this.leftArm);
		if (target.attackTime > 0.0F) {
			this.setupAttackAnimation(target, target.isLeftHanded ? this.leftArm : this.rightArm, target.isLeftHanded ? this.rightArm : this.leftArm);
		}
	}

	protected void setupAttackAnimation(T target, ModelPart mainArm, ModelPart offArm) {
		float progress = target.attackTime;
		this.body.yRot = Mth.sin(Mth.sqrt(progress) * ((float) Math.PI * 2F)) * 0.2F;
		mainArm.yRot += this.body.yRot;
		offArm.yRot += this.body.yRot;
		offArm.xRot += this.body.yRot;
		progress = 1.0F - target.attackTime;
		progress = progress * progress;
		progress = progress * progress;
		progress = 1.0F - progress;
		float f2 = Mth.sin(progress * (float) Math.PI);
		float f3 = Mth.sin(target.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
		mainArm.xRot = (float) ((double) mainArm.xRot - ((double) f2 * 1.2D + (double) f3));
		mainArm.yRot += this.body.yRot * 2.0F;
		mainArm.zRot += Mth.sin(target.attackTime * (float) Math.PI) * -0.4F;
	}

	protected void defaultPose(T target) {
		this.head.xRot = target.xRot * ((float) Math.PI / 180F);
		this.head.yRot = target.yRot * ((float) Math.PI / 180F);
        this.body.xRot = 0.0F;
        this.body.yRot = 0.0F;
        this.body.zRot = 0.0F;
		this.rightArm.xRot = 0.0F;
		this.leftArm.xRot = 0.0F;
		this.rightArm.yRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightArm.xRot += Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
		this.leftArm.xRot -= Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
		this.rightArm.zRot += Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
		this.leftArm.zRot -= Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
		this.upperTail.xRot = 0.1491F;
		this.lowerTail.xRot = 0.1627F;
		this.upperTail.xRot += Mth.cos(target.ageInTicks * 0.2F) * 0.18F;
		this.lowerTail.xRot += Mth.cos(target.ageInTicks * 0.2F) * 0.26F;
		if ((target.onGround && !target.isInWater) || target.isPassenger) {
			this.upperTail.xRot = -1.3963F;
			this.lowerTail.xRot = -1.4399F;
		}
	}

	protected void poseArms(T target, ModelPart mainArm, ModelPart offArm) {
		if (!target.getMainHandItemStack().isEmpty()) {
			if (target.isAggressive) {
				mainArm.xRot = -2.7925F;
				mainArm.zRot = -0.1745F;
				offArm.xRot = 0.3491F;
				offArm.xRot += Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
			}
		} else if (target.isSwimming) {
			mainArm.xRot = 0.0873F;
			offArm.xRot = 0.0873F;
			mainArm.xRot += Mth.cos(target.ageInTicks * 0.12F) * 0.08F + 0.04F;
			offArm.xRot -= Mth.cos(target.ageInTicks * 0.12F) * 0.08F + 0.04F;
			this.upperTail.xRot = 0.8727F;
			this.lowerTail.xRot = 0.9163F;
			this.upperTail.xRot += Mth.cos(target.ageInTicks * 0.32F) * 0.18F;
			this.lowerTail.xRot += Mth.cos(target.ageInTicks * 0.32F) * 0.26F;
		}
	}
}