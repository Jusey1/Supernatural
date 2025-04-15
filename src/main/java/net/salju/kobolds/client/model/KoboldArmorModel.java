package net.salju.kobolds.client.model;

import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;

public class KoboldArmorModel extends HumanoidModel {
	public KoboldArmorModel(ModelPart part) {
		super(part);
	}

	public static LayerDefinition createOuterArmorLayer() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.3F)), PartPose.offset(0.0F, 4.0F, 0.0F));
		root.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -7.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 4.0F, 0.0F));
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 11.0F, 4.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, 4.0F, 0.0F));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, -1.55F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(-0.35F)).mirror(false), PartPose.offset(1.5F, 15.0F, 0.0F));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -1.55F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(-0.35F)), PartPose.offset(-1.5F, 15.0F, 0.0F));
		root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(0.0F, -1.55F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(-0.35F)).mirror(false), PartPose.offset(4.5F, 5.0F, 0.0F));
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-4.0F, -1.55F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(-0.35F)), PartPose.offset(-4.5F, 5.0F, 0.0F));
		return LayerDefinition.create(mesh, 64, 32);
	}

	public static LayerDefinition createInnerArmorLayer() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.4F)), PartPose.offset(0.0F, 4.0F, 0.0F));
		root.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -7.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.35F)), PartPose.offset(0.0F, 4.0F, 0.0F));
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, -1.55F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(-0.45F)).mirror(false), PartPose.offset(1.5F, 15.0F, 0.0F));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -1.55F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(-0.45F)), PartPose.offset(-1.5F, 15.0F, 0.0F));
		root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(0.0F, -1.55F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(-0.45F)).mirror(false), PartPose.offset(4.5F, 5.0F, 0.0F));
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-4.0F, -1.55F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(-0.45F)), PartPose.offset(-4.5F, 5.0F, 0.0F));
		return LayerDefinition.create(mesh, 64, 32);
	}
}