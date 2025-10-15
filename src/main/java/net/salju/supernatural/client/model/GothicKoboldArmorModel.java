package net.salju.supernatural.client.model;

import net.salju.kobolds.client.model.KoboldArmorModel;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.ArmorModelSet;

public class GothicKoboldArmorModel extends HumanoidModel {
    public static ArmorModelSet<LayerDefinition> GOTHIC_KOBOLD_ARMOR_LAYER = GothicKoboldArmorModel.createArmorSet().map(mesh -> LayerDefinition.create(mesh, 64, 32));

	public GothicKoboldArmorModel(ModelPart root) {
		super(root);
	}

    public static LayerDefinition createHeadLayer() {
        return GOTHIC_KOBOLD_ARMOR_LAYER.head();
    }

    public static LayerDefinition createBodyLayer() {
        return GOTHIC_KOBOLD_ARMOR_LAYER.chest();
    }

    public static LayerDefinition createLegsLayer() {
        return GOTHIC_KOBOLD_ARMOR_LAYER.legs();
    }

    public static LayerDefinition createBootsLayer() {
        return GOTHIC_KOBOLD_ARMOR_LAYER.feet();
    }

    public static ArmorModelSet<MeshDefinition> createArmorSet() {
        return KoboldArmorModel.createArmorSet(GothicKoboldArmorModel::createBaseArmor);
    }

    private static MeshDefinition createBaseArmor(CubeDeformation cube) {
		MeshDefinition mesh = KoboldArmorModel.createBaseArmor(cube);
		PartDefinition root = mesh.getRoot();
		root.getChild("head").clearChild("hat");
		root.getChild("head").addOrReplaceChild("front", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -7.5F, -7.0F, 8.0F, 8.0F, 8.0F, cube.extend(-0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.48F, 0.0F, 0.0F));
		root.getChild("head").addOrReplaceChild("feather", CubeListBuilder.create().texOffs(25, -7).addBox(0.0F, -13.2F, 0.65F, 0.0F, 7.0F, 7.0F, cube), PartPose.offset(0.0F, 0.0F, 0.0F));
		return mesh;
	}
}