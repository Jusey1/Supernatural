package net.salju.supernatural.init;

import net.salju.supernatural.enchantment.LeechingEnchantment;
import net.salju.supernatural.SupernaturalMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.enchantment.Enchantment;

public class SupernaturalEnchantments {
	public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, SupernaturalMod.MODID);
	public static final RegistryObject<Enchantment> LEECHING = REGISTRY.register("leeching", () -> new LeechingEnchantment());
}