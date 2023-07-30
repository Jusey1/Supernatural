package net.salju.supernatural.init;

import net.salju.supernatural.block.*;
import net.salju.supernatural.SupernaturalMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class SupernaturalBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, SupernaturalMod.MODID);
	public static final RegistryObject<Block> GRAVE_SOIL = REGISTRY.register("grave_soil", () -> new GraveSoilBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).sound(SoundType.SOUL_SOIL).strength(1.2f, 8f).requiresCorrectToolForDrops().randomTicks()));
}