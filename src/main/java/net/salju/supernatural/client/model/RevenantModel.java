package net.salju.supernatural.client.model;

import net.salju.supernatural.client.renderer.SupernaturalRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.EntityModel;
import net.minecraft.util.Mth;

public class RevenantModel<T extends SupernaturalRenderState> extends EntityModel<T> {
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;

	public RevenantModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.jaw = root.getChild("head").getChild("jaw");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
	}

	public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 12.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        root.getChild("head").addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 14).addBox(-4.0F, 0.0F, -8.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 4.0F));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 24).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(40, 20).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 19.0F, 4.0F, new CubeDeformation(0.25F)).texOffs(17, 40).addBox(-1.0F, 12.0F, 1.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(24, 26).addBox(-1.25F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(52, 43).addBox(-1.25F, -2.0F, -2.0F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.5F)), PartPose.offset(-5.75F, 2.0F, 0.0F));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(24, 26).mirror().addBox(0.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(52, 43).mirror().addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));
		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(T target) {
        this.defaultPose(target);
        this.poseArms(target);
    }

    protected void defaultPose(T target) {
        this.head.xRot = target.xRot * ((float) Math.PI / 180F);
        this.head.yRot = target.yRot * ((float) Math.PI / 180F);
        this.jaw.xRot = 0.0437F;
        this.jaw.yRot = 0.0F;
        this.jaw.zRot = 0.0F;
        this.body.xRot = 0.0F;
        this.body.yRot = 0.0F;
        this.body.zRot = 0.0F;
        this.rightArm.xRot = 0.0F;
        this.leftArm.xRot = 0.0F;
        this.rightArm.yRot = 0.0F;
        this.leftArm.yRot = 0.0F;
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        this.jaw.xRot += Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
        this.rightArm.xRot += Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
        this.leftArm.xRot -= Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
        this.rightArm.zRot += Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
        this.leftArm.zRot -= Mth.cos(target.ageInTicks * 0.04F) * 0.04F + 0.04F;
    }

    protected void poseArms(T target) {
        if (target.isCastingSpell) {
            this.jaw.xRot = 0.2182F;
            this.jaw.xRot += Mth.cos(target.ageInTicks * 0.08F) * 0.08F + 0.08F;
            this.body.xRot = 0.6109F;
            this.body.xRot += Mth.cos(target.ageInTicks * 0.08F) * 0.08F + 0.08F;
            this.rightArm.xRot = -1.6581F;
            this.leftArm.xRot = -1.6581F;
            this.rightArm.yRot = 0.2618F;
            this.leftArm.yRot = -0.2618F;
            this.rightArm.zRot = Mth.cos(target.ageInTicks * 0.4762F) * 0.25F;
            this.leftArm.zRot = Mth.cos(target.ageInTicks * 0.4762F) * -0.25F;
        }
    }
}