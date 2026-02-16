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
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;

public class SpiritModel<T extends SupernaturalRenderState> extends EntityModel<T> {
    private final ModelPart head;
	private final ModelPart body;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightWing;
	private final ModelPart leftWing;

	public SpiritModel(ModelPart root) {
		super(root.getChild("root"), RenderTypes::entityTranslucent);
        this.head = this.root.getChild("head");
        this.body = this.root.getChild("body");
        this.rightArm = this.body.getChild("right_arm");
        this.leftArm = this.body.getChild("left_arm");
        this.rightWing = this.body.getChild("right_wing");
        this.leftWing = this.body.getChild("left_wing");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition base = mesh.getRoot();
		PartDefinition root = base.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, -2.5F, 0.0F));
		root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 16).addBox(-1.5F, 1.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 20.0F, 0.0F));
		body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(23, 0).addBox(-1.25F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(-1.75F, 0.25F, 0.0F));
		body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(23, 6).addBox(-0.75F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(1.75F, 0.25F, 0.0F));
		body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(16, 14).mirror().addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 1.0F, 1.0F));
		body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(16, 14).addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 1.0F, 1.0F));
		return LayerDefinition.create(mesh, 32, 32);
	}

	@Override
	public void setupAnim(T target) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		float f = target.ageInTicks * 20.0F * ((float) Math.PI / 180F) + target.walkAnimationSpeed;
		float f1 = Mth.cos(f) * (float) Math.PI * 0.15F;
		float f3 = target.ageInTicks * 9.0F * ((float) Math.PI / 180F);
		float f4 = Math.min(target.walkAnimationSpeed / 0.3F, 1.0F);
		float f5 = 1.0F - f4;
		this.head.xRot = target.xRot * ((float) Math.PI / 180F);
		this.head.yRot = target.yRot * ((float) Math.PI / 180F);
		this.rightWing.xRot = 0.43633232F;
		this.rightWing.yRot = -0.61086524F + f1;
		this.leftWing.xRot = 0.43633232F;
		this.leftWing.yRot = 0.61086524F - f1;
		float f12 = f4 * 0.6981317F;
		this.body.xRot = f12;
		float f13 = Mth.lerp(f4, f12, Mth.lerp(f4, (-(float) Math.PI / 3F), (-(float) Math.PI / 4F)));
		this.root.y += (float) Math.cos(f3) * 0.25F * f5;
		this.rightArm.xRot = f13;
		this.leftArm.xRot = f13;
		float f14 = f5 * (1.0F - f4);
		float f15 = 0.43633232F - Mth.cos(f3 + ((float) Math.PI * 1.5F)) * (float) Math.PI * 0.075F * f14;
		this.leftArm.zRot = -f15;
		this.rightArm.zRot = f15;
		this.rightArm.yRot = 0.27925268F * f4;
		this.leftArm.yRot = -0.27925268F * f4;
		if (target.isCastingSpell) {
			this.rightArm.xRot = ((float) Math.PI * 1.65F);
			this.leftArm.xRot = ((float) Math.PI * 1.65F);
		}
	}
}