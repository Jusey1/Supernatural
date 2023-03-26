package net.salju.supernatural.init;

import net.salju.supernatural.block.GraveSoilBlock;
import net.salju.supernatural.SupernaturalMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

public class SupernaturalBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, SupernaturalMod.MODID);
	public static final RegistryObject<Block> GRAVE_SOIL = REGISTRY.register("grave_soil", () -> new GraveSoilBlock());
}