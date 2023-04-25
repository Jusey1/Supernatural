package net.salju.supernatural.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.CreativeModeTabEvent;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SupernaturalTabs {
	@SubscribeEvent
	public static void buildTabContentsModded(CreativeModeTabEvent.Register event) {
		event.registerCreativeModeTab(new ResourceLocation("supernatural_tab"), builder -> builder.title(Component.translatable("itemGroup.supernatural_tab")).icon(() -> new ItemStack(Items.ENDER_EYE)).displayItems((parameters, tabData) -> {
			tabData.accept(SupernaturalItems.VAMPIRE_SPAWN_EGG.get());
			tabData.accept(SupernaturalItems.NECROMANCER_SPAWN_EGG.get());
			tabData.accept(SupernaturalItems.POSSESSED_ARMOR_SPAWN_EGG.get());
			tabData.accept(SupernaturalItems.SPOOKY_SPAWN_EGG.get());
			tabData.accept(SupernaturalItems.MER_AMETHYST_SPAWN_EGG.get());
			tabData.accept(SupernaturalItems.GOTHIC_IRON_HELMET.get());
			tabData.accept(SupernaturalItems.GOTHIC_DIAMOND_HELMET.get());
			tabData.accept(SupernaturalItems.GOTHIC_GOLDEN_HELMET.get());
			tabData.accept(SupernaturalItems.GOTHIC_NETHERITE_HELMET.get());
			tabData.accept(SupernaturalBlocks.GRAVE_SOIL.get().asItem());
			tabData.accept(SupernaturalItems.GOTHIC_TEMPLATE.get());
			tabData.accept(SupernaturalItems.NECRO_TEMPLATE.get());
			tabData.accept(SupernaturalItems.ANIMAL_BLOOD.get());
			tabData.accept(SupernaturalItems.VILLAGER_BLOOD.get());
			tabData.accept(SupernaturalItems.PLAYER_BLOOD.get());
			tabData.accept(SupernaturalItems.ECTOPLASM.get());
			tabData.accept(SupernaturalItems.VAMPIRE_DUST.get());
			tabData.accept(SupernaturalItems.ANGEL_STATUE.get());
		}));
	}
}