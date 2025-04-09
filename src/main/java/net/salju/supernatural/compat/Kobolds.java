package net.salju.supernatural.compat;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.client.model.GothicKoboldArmorModel;
import net.salju.kobolds.client.model.KoboldArmorModel;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.resources.ResourceLocation;

public class Kobolds  {
	public static final ModelLayerLocation GOTHIC_KOBOLD = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "gothic_kobold"), "main");

	public static void registerKoboldArmor(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(GOTHIC_KOBOLD, GothicKoboldArmorModel::createBodyLayer);
	}

	public static HumanoidModel<?> getKoboldModel(Model basic, HumanoidModel<?> gothic) {
		if (basic instanceof KoboldArmorModel) {
			return new GothicKoboldArmorModel(GothicKoboldArmorModel.createBodyLayer().bakeRoot());
		}
		return gothic;
	}
}