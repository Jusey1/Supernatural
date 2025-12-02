package net.salju.supernatural.client.model;

import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class GothicArmorModel<T extends LivingEntity> extends HumanoidModel<T> {
	private final EquipmentSlot slot;
	private final ModelPart feather;
	private final ItemStack stack;

	public GothicArmorModel(ModelPart root, ItemStack stack, EquipmentSlot slot) {
		super(root);
		this.stack = stack;
		this.slot = slot;
		this.feather = root.getChild("feather");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(1.0F), 0.0F);
		PartDefinition root = meshdefinition.getRoot();
        root.getChild("head").addOrReplaceChild("front", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("feather", CubeListBuilder.create().texOffs(25, -7).addBox(0.0F, -15.0F, 2.0F, 0.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack pose, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
		if (this.slot == EquipmentSlot.HEAD) {
			pose.pushPose();
            this.feather.copyFrom(this.head);
			if (this.young) {
				pose.scale(0.75F, 0.75F, 0.75F);
				this.head.setPos(0.0F, 15.0F, 0.0F);
			}
			this.head.render(pose, buffer, packedLight, packedOverlay);
			this.feather.render(pose, buffer, packedLight, packedOverlay, DyedItemColor.getOrDefault(stack, color));
			pose.popPose();
		}
	}
}