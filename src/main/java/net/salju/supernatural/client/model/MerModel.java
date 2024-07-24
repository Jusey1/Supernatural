package net.salju.supernatural.client.model;

import net.salju.supernatural.entity.AbstractMerEntity;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.ArmedModel;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class MerModel<T extends AbstractMerEntity> extends EntityModel<T> implements ArmedModel {
	private final ModelPart Head;
	private final ModelPart Body;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;
	private final ModelPart UpperTail;
	private final ModelPart LowerTail;

	public MerModel(ModelPart root) {
		this.Head = root.getChild("Head");
		this.Body = root.getChild("Body");
		this.RightArm = root.getChild("RightArm");
		this.LeftArm = root.getChild("LeftArm");
		this.UpperTail = root.getChild("UpperTail");
		this.LowerTail = root.getChild("LowerTail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(4, 0).addBox(-6.0F, -8.0F, 0.0F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(4.0F, -8.0F, 0.0F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(32, 1).addBox(0.0F, -10.0F, -2.0F, 0.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 1.0F, 0.0F));
		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(50, 44).addBox(0.0F, -12.0F, 2.0F, 0.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(28, 28).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 16.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 13.0F, 0.0F));
		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(16, 48).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 2.0F, 0.0F));
		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 48).addBox(0.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 2.0F, 0.0F));
		PartDefinition UpperTail = partdefinition.addOrReplaceChild("UpperTail", CubeListBuilder.create().texOffs(44, 15).addBox(-3.5F, 0.0F, -2.0F, 7.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 0.0F));
		PartDefinition LowerTail = partdefinition.addOrReplaceChild("LowerTail", CubeListBuilder.create().texOffs(32, 48).addBox(-3.0F, 8.0F, -1.5F, 6.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(32, 0).addBox(-8.0F, 15.0F, 0.0F, 16.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T mer, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
		this.Head.yRot = headYaw * ((float) Math.PI / 180F);
		this.Head.xRot = headPitch * ((float) Math.PI / 180F);
		this.RightArm.xRot = 0.0F;
		this.LeftArm.xRot = 0.0F;
		this.RightArm.yRot = 0.0F;
		this.LeftArm.yRot = 0.0F;
		this.RightArm.zRot = 0.0F;
		this.LeftArm.zRot = 0.0F;
		this.RightArm.xRot += Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
		this.LeftArm.xRot -= Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
		this.RightArm.zRot += Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
		this.LeftArm.zRot -= Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
		this.UpperTail.xRot = 0.1491F;
		this.LowerTail.xRot = 0.1627F;
		this.UpperTail.xRot += Mth.cos(ageInTicks * 0.2F) * 0.18F;
		this.LowerTail.xRot += Mth.cos(ageInTicks * 0.2F) * 0.26F;
		if (mer.onGround() && !mer.isInWater() || this.riding) {
			this.UpperTail.xRot = -1.3963F;
			this.LowerTail.xRot = -1.4399F;
		}
		if (mer.getMainHandItem().getItem() instanceof TridentItem) {
			if (mer.isAggressive()) {
				if (mer.isLeftHanded()) {
					this.RightArm.xRot = 0.3491F;
					this.RightArm.xRot += Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
					this.LeftArm.xRot = -2.7925F;
					this.LeftArm.zRot = 0.1745F;
				} else {
					this.LeftArm.xRot = 0.3491F;
					this.LeftArm.xRot += Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
					this.RightArm.xRot = -2.7925F;
					this.RightArm.zRot = -0.1745F;
				}
			}
		} else if (mer.isSwimming()) {
			this.RightArm.xRot = 0.0873F;
			this.LeftArm.xRot = 0.0873F;
			this.RightArm.xRot += Mth.cos(ageInTicks * 0.12F) * 0.08F + 0.04F;
			this.LeftArm.xRot -= Mth.cos(ageInTicks * 0.12F) * 0.08F + 0.04F;
			this.UpperTail.xRot = 0.8727F;
			this.LowerTail.xRot = 0.9163F;
			this.UpperTail.xRot += Mth.cos(ageInTicks * 0.32F) * 0.18F;
			this.LowerTail.xRot += Mth.cos(ageInTicks * 0.32F) * 0.26F;
		}
		if (this.attackTime > 0.0F) {
			if (mer.isLeftHanded()) {
				float progress = this.attackTime;
				this.Body.yRot = Mth.sin(Mth.sqrt(progress) * ((float) Math.PI * 2F)) * 0.2F;
				this.LeftArm.yRot += this.Body.yRot;
				this.RightArm.yRot += this.Body.yRot;
				this.RightArm.xRot += this.Body.yRot;
				progress = 1.0F - this.attackTime;
				progress = progress * progress;
				progress = progress * progress;
				progress = 1.0F - progress;
				float f2 = Mth.sin(progress * (float) Math.PI);
				float f3 = Mth.sin(this.attackTime * (float) Math.PI) * -(this.Head.xRot - 0.7F) * 0.75F;
				LeftArm.xRot = (float) ((double) LeftArm.xRot - ((double) f2 * 1.2D + (double) f3));
				LeftArm.yRot += this.Body.yRot * 2.0F;
				LeftArm.zRot -= Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
			} else {
				float progress = this.attackTime;
				this.Body.yRot = Mth.sin(Mth.sqrt(progress) * ((float) Math.PI * 2F)) * 0.2F;
				this.RightArm.yRot += this.Body.yRot;
				this.LeftArm.yRot += this.Body.yRot;
				this.LeftArm.xRot += this.Body.yRot;
				progress = 1.0F - this.attackTime;
				progress = progress * progress;
				progress = progress * progress;
				progress = 1.0F - progress;
				float f2 = Mth.sin(progress * (float) Math.PI);
				float f3 = Mth.sin(this.attackTime * (float) Math.PI) * -(this.Head.xRot - 0.7F) * 0.75F;
				RightArm.xRot = (float) ((double) RightArm.xRot - ((double) f2 * 1.2D + (double) f3));
				RightArm.yRot += this.Body.yRot * 2.0F;
				RightArm.zRot += Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
			}
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.Head.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		this.Body.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		this.RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		this.LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		this.UpperTail.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		this.LowerTail.render(poseStack, vertexConsumer, packedLight, packedOverlay);
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
		switch (arm) {
			case LEFT -> {
				this.LeftArm.translateAndRotate(poseStack);
				poseStack.translate(0.045, 0.05, 0.0);
				poseStack.scale(0.95F, 0.95F, 0.95F);
			}
			case RIGHT -> {
				this.RightArm.translateAndRotate(poseStack);
				poseStack.translate(-0.045, 0.05, 0.0);
				poseStack.scale(0.95F, 0.95F, 0.95F);
			}
		}
	}
}