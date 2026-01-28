package net.salju.supernatural.client.model;

import net.salju.supernatural.client.renderer.SupernaturalRenderState;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.EntityModel;

public class AngelModel<T extends SupernaturalRenderState> extends EntityModel<T> {
	private final ModelPart body = root.getChild("body");
	private final ModelPart head = this.body.getChild("head");
	private final ModelPart hat = this.head.getChild("hat");
	private final ModelPart rightArm = this.body.getChild("right_arm");
	private final ModelPart leftArm = this.body.getChild("left_arm");
	private final ModelPart rightWing = this.body.getChild("right_wing");
	private final ModelPart leftWing = this.body.getChild("left_wing");

	public AngelModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(14, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(12, 30).addBox(-4.5F, 10.0F, -2.5F, 9.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(11, 44).addBox(-5.0F, 19.0F, -3.0F, 10.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 2.0F, 0.0F));
		body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(0.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, 2.0F, 0.0F));
		body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(42, 5).mirror().addBox(2.0F, -11.0F, -8.5F, 0.0F, 19.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 4.0F, 5.0F));
		body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(42, 5).addBox(-2.0F, -11.0F, -8.5F, 0.0F, 19.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 4.0F, 5.0F));
		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(T target) {
		this.defaultPose(target);
		this.poseArms(target, target.isLeftHanded ? this.leftArm : this.rightArm, target.isLeftHanded ? this.rightArm : this.leftArm);
	}

	protected void defaultPose(T target) {
		this.head.xRot = 0.0F;
		this.head.yRot = 0.0F;
		this.head.zRot = 0.0F;
		this.hat.xRot = 0.0F;
		this.hat.yRot = 0.0F;
		this.hat.zRot = 0.0F;
		this.body.xRot = 0.0F;
		this.body.yRot = target.yRot * ((float) Math.PI / 180F);
		this.body.zRot = 0.0F;
		this.rightWing.xRot = 0.0F;
		this.rightWing.yRot = 2.7053F;
		this.rightWing.zRot = 0.0F;
		this.leftWing.xRot = 0.0F;
		this.leftWing.yRot = -2.7053F;
		this.leftWing.zRot = 0.0F;
		this.rightArm.xRot = 0.0F;
		this.rightArm.yRot = 0.0F;
		this.rightArm.zRot = 0.0F;
		this.leftArm.xRot = 0.0F;
		this.leftArm.yRot = 0.0F;
		this.leftArm.zRot = 0.0F;
	}

	protected void poseArms(T target, ModelPart mainArm, ModelPart offArm) {
		if (target.pose == 0) {
			mainArm.xRot = -1.5708F;
			offArm.xRot = -1.5708F;
		} else if (target.pose == 2) {
			mainArm.xRot = -0.7854F;
			mainArm.yRot = target.isLeftHanded ? 0.5236F : -0.5236F;
			offArm.xRot = -0.7854F;
			offArm.yRot = target.isLeftHanded ? -0.5236F : 0.5236F;
		} else if (target.pose == 3) {
			this.head.xRot = 0.2618F;
			this.hat.xRot = 0.2618F;
			mainArm.xRot = -1.9199F;
			mainArm.yRot = target.isLeftHanded ? 0.4363F : -0.4363F;
			offArm.xRot = -1.9199F;
			offArm.yRot = target.isLeftHanded ? -0.4363F : 0.4363F;
		} else if (target.pose == 4) {
			mainArm.xRot = -1.5708F;
		} else if (target.pose == 5) {
			this.head.xRot = -0.2618F;
			this.hat.xRot = -0.2618F;
			mainArm.xRot = -3.0543F;
			mainArm.zRot = target.isLeftHanded ? 0.0873F : -0.0873F;
			offArm.xRot = -3.0543F;
			offArm.zRot = target.isLeftHanded ? -0.0873F : 0.0873F;
		} else if (target.pose == 6) {
			mainArm.xRot = -0.0436F;
			mainArm.zRot = target.isLeftHanded ? -1.5708F : 1.5708F;
			offArm.xRot = -0.0436F;
			offArm.zRot = target.isLeftHanded ? 1.5708F : -1.5708F;
		} else if (target.pose == 7) {
			this.head.xRot = 0.2618F;
			this.head.yRot = target.isLeftHanded ? -0.7854F : 0.7854F;
			this.hat.xRot = 0.2618F;
			this.hat.yRot = target.isLeftHanded ? -0.7854F : 0.7854F;
			mainArm.xRot = -1.9199F;
			mainArm.yRot = target.isLeftHanded ? -0.7854F : 0.7854F;
			offArm.xRot = -1.9199F;
			offArm.yRot = target.isLeftHanded ? -0.8727F : 0.8727F;
		}
	}
}