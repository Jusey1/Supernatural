package net.salju.supernatural.init;

import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.SupernaturalMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.client.Minecraft;

public class SupernaturalTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SupernaturalMod.MODID);
	public static final RegistryObject<CreativeModeTab> SUPERNATURAL = REGISTRY.register("supernatural",
			() -> CreativeModeTab.builder().title(Component.translatable("itemGroup.supernatural")).icon(() -> new ItemStack(SupernaturalItems.SOULGEM.get())).displayItems((parameters, tabData) -> {
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
				tabData.accept(SupernaturalItems.ECTOPLASM.get());
				tabData.accept(SupernaturalItems.VAMPIRE_DUST.get());
				tabData.accept(SupernaturalItems.ANGEL_STATUE.get());
				tabData.accept(SupernaturalBlocks.RITUAL_ALTAR.get().asItem());
				tabData.accept(SupernaturalManager.setSoul(new ItemStack(SupernaturalItems.SOULGEM.get()), new Villager(EntityType.VILLAGER, Minecraft.getInstance().level)));
				tabData.accept(SupernaturalManager.setUUID(new ItemStack(SupernaturalItems.PLAYER_BLOOD.get()), Minecraft.getInstance().player));
				tabData.accept(SupernaturalItems.VAMPIRISM_CONTRACT.get());
				tabData.accept(SupernaturalItems.WEREWOLFISM_CONTRACT.get());
				tabData.accept(SupernaturalItems.VEXATION_CONTRACT.get());
				tabData.accept(SupernaturalItems.PUMPKIN_CONTRACT.get());
				tabData.accept(SupernaturalItems.REANIMATE_CONTRACT.get());
				tabData.accept(SupernaturalItems.KNOWLEDGE_CONTRACT.get());
				tabData.accept(SupernaturalItems.FORTUNE_CONTRACT.get());
			}).build());
}