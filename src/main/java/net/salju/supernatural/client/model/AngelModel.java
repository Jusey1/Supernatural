package net.salju.supernatural.client.model;

import net.salju.supernatural.client.renderer.AngelState;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.EntityModel;

public class AngelModel<T extends AngelState> extends EntityModel<T> {
	private final ModelPart body = root.getChild("body");
	private final ModelPart head = this.body.getChild("head");
	private final ModelPart hat = this.head.getChild("hat");
	private final ModelPart right_arm = this.body.getChild("right_arm");
	private final ModelPart left_arm = this.body.getChild("left_arm");
	private final ModelPart right_wing = this.body.getChild("right_wing");
	private final ModelPart left_wing = this.body.getChild("left_wing");

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
	public void setupAnim(T angel) {
		this.head.xRot = 0.0F;
		this.head.yRot = 0.0F;
		this.head.zRot = 0.0F;
		this.hat.xRot = 0.0F;
		this.hat.yRot = 0.0F;
		this.hat.zRot = 0.0F;
		this.body.xRot = 0.0F;
		this.body.yRot = angel.yRot * ((float) Math.PI / 180F);
		this.body.zRot = 0.0F;
		this.right_wing.xRot = 0.0F;
		this.right_wing.yRot = 2.7053F;
		this.right_wing.zRot = 0.0F;
		this.left_wing.xRot = 0.0F;
		this.left_wing.yRot = -2.7053F;
		this.left_wing.zRot = 0.0F;
		this.right_arm.xRot = 0.0F;
		this.right_arm.yRot = 0.0F;
		this.right_arm.zRot = 0.0F;
		this.left_arm.xRot = 0.0F;
		this.left_arm.yRot = 0.0F;
		this.left_arm.zRot = 0.0F;
		if (angel.getAngelPose() == 0) {
			this.right_arm.xRot = -1.5708F;
			this.left_arm.xRot = -1.5708F;
		} else if (angel.getAngelPose() == 2) {
			this.right_arm.xRot = -0.7854F;
			this.right_arm.yRot = -0.5236F;
			this.left_arm.xRot = -0.7854F;
			this.left_arm.yRot = 0.5236F;
		} else if (angel.getAngelPose() == 3) {
			this.head.xRot = 0.2618F;
			this.hat.xRot = 0.2618F;
			this.right_arm.xRot = -1.9199F;
			this.right_arm.yRot = -0.4363F;
			this.left_arm.xRot = -1.9199F;
			this.left_arm.yRot = 0.4363F;
		} else if (angel.getAngelPose() == 4) {
			this.right_arm.xRot = -1.5708F;
		} else if (angel.getAngelPose() == 5) {
			this.head.xRot = -0.2618F;
			this.hat.xRot = -0.2618F;
			this.right_arm.xRot = -3.0543F;
			this.right_arm.zRot = -0.0873F;
			this.left_arm.xRot = -3.0543F;
			this.left_arm.zRot = 0.0873F;
		} else if (angel.getAngelPose() == 6) {
			this.right_arm.xRot = -0.0436F;
			this.right_arm.zRot = 1.5708F;
			this.left_arm.xRot = -0.0436F;
			this.left_arm.zRot = -1.5708F;
		} else if (angel.getAngelPose() == 7) {
			this.head.xRot = 0.2618F;
			this.head.yRot = 0.7854F;
			this.hat.xRot = 0.2618F;
			this.hat.yRot = 0.7854F;
			this.right_arm.xRot = -1.9199F;
			this.right_arm.yRot = 0.7854F;
			this.left_arm.xRot = -1.9199F;
			this.left_arm.yRot = 0.8727F;
		}
	}
}