package net.salju.supernatural.client.model;

import net.salju.supernatural.entity.Cannon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.EntityModel;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class ArtCanModel<T extends Cannon> extends EntityModel<T> {
	public static final ModelLayerLocation CANNON_MODEL = new ModelLayerLocation(new ResourceLocation("supernatural", "cannon_model"), "main");
	private final ModelPart head;
	private final ModelPart body;

	public ArtCanModel(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -17.0F, -9.0F, 18.0F, 17.0F, 18.0F, new CubeDeformation(0.0F)).texOffs(72, 11).addBox(-5.0F, -11.0F, -23.0F, 10.0F, 10.0F, 14.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 6.0F, 0.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 35).addBox(-6.0F, -18.0F, -6.0F, 12.0F, 18.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition leg_r1 = body.addOrReplaceChild("leg_r1", CubeListBuilder.create().texOffs(48, 31).addBox(0.0F, -18.0F, -22.0F, 0.0F, 18.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
		PartDefinition leg_r2 = body.addOrReplaceChild("leg_r2", CubeListBuilder.create().texOffs(48, 31).addBox(0.0F, -18.0F, -24.0F, 0.0F, 18.0F, 16.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition leg_r3 = body.addOrReplaceChild("leg_r3", CubeListBuilder.create().texOffs(48, 31).addBox(0.0F, -18.0F, -24.0F, 0.0F, 18.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
		return LayerDefinition.create(meshdefinition, 128, 96);
	}

	@Override
	public void setupAnim(T cannon, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
		this.head.xRot = 0.0F;
		this.head.yRot = headYaw * ((float) Math.PI / 180F);
		this.head.zRot = 0.0F;
		this.body.xRot = 0.0F;
		this.body.yRot = 0.0F;
		this.body.zRot = 0.0F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay);
	}
}