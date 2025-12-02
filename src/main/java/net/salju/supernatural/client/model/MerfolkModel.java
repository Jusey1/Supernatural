package net.salju.supernatural.client.model;

import net.salju.supernatural.entity.AbstractMerfolkEntity;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.EntityModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.TridentItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class MerfolkModel<T extends AbstractMerfolkEntity> extends EntityModel<T> implements ArmedModel {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart upperTail;
    private final ModelPart lowerTail;

	public MerfolkModel(ModelPart root) {
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
	public void setupAnim(T merfolk, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        this.head.yRot = headYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.rightArm.xRot = 0.0F;
        this.leftArm.xRot = 0.0F;
        this.rightArm.yRot = 0.0F;
        this.leftArm.yRot = 0.0F;
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        this.rightArm.xRot += Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
        this.leftArm.xRot -= Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
        this.rightArm.zRot += Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
        this.leftArm.zRot -= Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
        this.upperTail.xRot = 0.1491F;
        this.lowerTail.xRot = 0.1627F;
        this.upperTail.xRot += Mth.cos(ageInTicks * 0.2F) * 0.18F;
        this.lowerTail.xRot += Mth.cos(ageInTicks * 0.2F) * 0.26F;
        if ((merfolk.onGround() && !merfolk.isInWaterOrBubble()) || merfolk.isPassenger()) {
            this.upperTail.xRot = -1.3963F;
            this.lowerTail.xRot = -1.4399F;
        }
        if (merfolk.getMainHandItem().getItem() instanceof TridentItem) {
            if (merfolk.isAggressive()) {
                if (merfolk.isLeftHanded()) {
                    this.rightArm.xRot = 0.3491F;
                    this.rightArm.xRot += Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
                    this.leftArm.xRot = -2.7925F;
                    this.leftArm.zRot = 0.1745F;
                } else {
                    this.leftArm.xRot = 0.3491F;
                    this.leftArm.xRot += Mth.cos(ageInTicks * 0.04F) * 0.04F + 0.04F;
                    this.rightArm.xRot = -2.7925F;
                    this.rightArm.zRot = -0.1745F;
                }
            }
        } else if (merfolk.isSwimming()) {
            this.rightArm.xRot = 0.0873F;
            this.leftArm.xRot = 0.0873F;
            this.rightArm.xRot += Mth.cos(ageInTicks * 0.12F) * 0.08F + 0.04F;
            this.leftArm.xRot -= Mth.cos(ageInTicks * 0.12F) * 0.08F + 0.04F;
            this.upperTail.xRot = 0.8727F;
            this.lowerTail.xRot = 0.9163F;
            this.upperTail.xRot += Mth.cos(ageInTicks * 0.32F) * 0.18F;
            this.lowerTail.xRot += Mth.cos(ageInTicks * 0.32F) * 0.26F;
        }
        if (this.attackTime > 0.0F) {
            if (merfolk.isLeftHanded()) {
                float progress = this.attackTime;
                this.body.yRot = Mth.sin(Mth.sqrt(progress) * ((float) Math.PI * 2F)) * 0.2F;
                this.leftArm.yRot += this.body.yRot;
                this.rightArm.yRot += this.body.yRot;
                this.rightArm.xRot += this.body.yRot;
                progress = 1.0F - this.attackTime;
                progress = progress * progress;
                progress = progress * progress;
                progress = 1.0F - progress;
                float f2 = Mth.sin(progress * (float) Math.PI);
                float f3 = Mth.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
                leftArm.xRot = (float) ((double) leftArm.xRot - ((double) f2 * 1.2D + (double) f3));
                leftArm.yRot += this.body.yRot * 2.0F;
                leftArm.zRot -= Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
            } else {
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

    @Override
    public void renderToBuffer(PoseStack pose, VertexConsumer buffer, int l, int o, int c) {
        this.head.render(pose, buffer, l, o);
        this.body.render(pose, buffer, l, o);
        this.rightArm.render(pose, buffer, l, o);
        this.leftArm.render(pose, buffer, l, o);
        this.upperTail.render(pose, buffer, l, o);
        this.lowerTail.render(pose, buffer, l, o);
    }

    @Override
    public void translateToHand(HumanoidArm arm, PoseStack pose) {
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
}