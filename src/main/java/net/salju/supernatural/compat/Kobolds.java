package net.salju.supernatural.compat;

import net.salju.supernatural.init.SupernaturalClient;
import net.salju.supernatural.client.model.GothicKoboldArmorModel;
import net.salju.kobolds.client.model.KoboldModel;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.ArmorModelSet;

public class Kobolds  {
	public static final ArmorModelSet<ModelLayerLocation> GOTHIC_KOBOLD = SupernaturalClient.registerArmorSet("gothic_kobold");

	public static void registerKoboldArmor(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GOTHIC_KOBOLD.head(), GothicKoboldArmorModel::createHeadLayer);
        event.registerLayerDefinition(GOTHIC_KOBOLD.chest(), GothicKoboldArmorModel::createBodyLayer);
        event.registerLayerDefinition(GOTHIC_KOBOLD.legs(), GothicKoboldArmorModel::createLegsLayer);
        event.registerLayerDefinition(GOTHIC_KOBOLD.feet(), GothicKoboldArmorModel::createBootsLayer);
	}

	public static HumanoidModel<?> getKoboldModel(Model basic, HumanoidModel<?> gothic) {
		if (basic instanceof KoboldModel) {
			return new GothicKoboldArmorModel(GothicKoboldArmorModel.createHeadLayer().bakeRoot());
		}
		return gothic;
	}
}