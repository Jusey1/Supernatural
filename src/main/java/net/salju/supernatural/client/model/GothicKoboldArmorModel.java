package net.salju.supernatural.client.model;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.HumanoidModel;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class GothicKoboldArmorModel<T extends LivingEntity> extends HumanoidModel<T> {
	public static final ModelLayerLocation KOBOLD_GOTHIC_ARMOR = new ModelLayerLocation(new ResourceLocation("supernatural", "gothic_kobold_armor"), "main");
	private final EquipmentSlot slot;
	private final ModelPart Head;
	private final ModelPart Feather;
	private final ItemStack stack;

	public GothicKoboldArmorModel(ModelPart root, ItemStack stack, EquipmentSlot slot) {
		super(root);
		this.stack = stack;
		this.slot = slot;
		this.Head = root.getChild("Head");
		this.Feather = root.getChild("Feather");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition Head = root.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Head_r1 = Head.addOrReplaceChild("Head_r1", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -7.5F, -7.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.48F, 0.0F, 0.0F));
		PartDefinition Feather = root.addOrReplaceChild("Feather", CubeListBuilder.create().texOffs(25, -7).addBox(0.0F, -13.2F, 0.65F, 0.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		if (this.slot == EquipmentSlot.HEAD) {
			int decimal = ((DyeableArmorItem) this.stack.getItem()).getColor(this.stack);
			float r = (decimal >> 16 & 0xFF) / 255.0F;
			float g = (decimal >> 8 & 0xFF) / 255.0F;
			float b = (decimal & 0xFF) / 255.0F;
			poseStack.pushPose();
			this.Head.copyFrom(this.head);
			this.Feather.copyFrom(this.head);
			if (this.young) {
				poseStack.scale(0.75F, 0.75F, 0.75F);
				this.Head.setPos(0.0F, 15.0F, 0.0F);
			}
			this.Head.render(poseStack, buffer, packedLight, packedOverlay);
			this.Feather.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, 1.0F);
			poseStack.popPose();
		}
	}
}