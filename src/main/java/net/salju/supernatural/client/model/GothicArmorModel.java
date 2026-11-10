package net.salju.supernatural.client.model;

import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import java.util.Set;
import java.util.function.Function;

public class GothicArmorModel extends HumanoidModel {
    public static ArmorModelSet<LayerDefinition> GOTHIC_ARMOR_LAYER = GothicArmorModel.createArmorSet().map(mesh -> LayerDefinition.create(mesh, 64, 32));

	public GothicArmorModel(ModelPart root) {
		super(root);
	}

    public static LayerDefinition createHeadLayer() {
        return GOTHIC_ARMOR_LAYER.head();
    }

    public static LayerDefinition createBodyLayer() {
        return GOTHIC_ARMOR_LAYER.chest();
    }

    public static LayerDefinition createLegsLayer() {
        return GOTHIC_ARMOR_LAYER.legs();
    }

    public static LayerDefinition createBootsLayer() {
        return GOTHIC_ARMOR_LAYER.feet();
    }

    public static ArmorModelSet<MeshDefinition> createArmorSet() {
        return createArmorSet(GothicArmorModel::createBaseArmor);
    }

    public static ArmorModelSet<MeshDefinition> createArmorSet(Function<CubeDeformation, MeshDefinition> base) {
        MeshDefinition head = base.apply(new CubeDeformation(1.0F));
        head.getRoot().retainPartsAndChildren(Set.of("head"));
        MeshDefinition body = base.apply(new CubeDeformation(1.0F));
        body.getRoot().retainExactParts(Set.of("body", "left_arm", "right_arm"));
        MeshDefinition legs = base.apply(new CubeDeformation(1.0F));
        legs.getRoot().retainExactParts(Set.of("left_leg", "right_leg", "body"));
        MeshDefinition boots = base.apply(new CubeDeformation(1.0F));
        boots.getRoot().retainExactParts(Set.of("left_leg", "right_leg"));
        return new ArmorModelSet(head, body, legs, boots);
    }

    private static MeshDefinition createBaseArmor(CubeDeformation cube) {
		MeshDefinition mesh = HumanoidModel.createMesh(cube, 0.0F);
		PartDefinition root = mesh.getRoot();
		root.getChild("head").addOrReplaceChild("feather", CubeListBuilder.create().texOffs(25, -7).addBox(0.0F, -14.0F, 2.0F, 0.0F, 7.0F, 7.0F, cube.extend(-1.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		return mesh;
	}
}