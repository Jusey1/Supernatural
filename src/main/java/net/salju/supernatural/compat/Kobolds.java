package net.salju.supernatural.compat;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.client.model.GothicKoboldArmorModel;
import net.salju.kobolds.init.KoboldsTags;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class Kobolds  {
	public static final ModelLayerLocation GOTHIC_KOBOLD = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "gothic_kobold"), "main");

	public static void registerKoboldArmor(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(GOTHIC_KOBOLD, GothicKoboldArmorModel::createBodyLayer);
	}

	public static HumanoidModel<?> getKoboldModel(LivingEntity target, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> gothic) {
		if (target.getType().is(KoboldsTags.KOBOLD)) {
            return new GothicKoboldArmorModel<>(GothicKoboldArmorModel.createBodyLayer().bakeRoot(), stack, slot);
		}
		return gothic;
	}
}