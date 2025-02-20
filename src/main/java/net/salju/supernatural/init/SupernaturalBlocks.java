package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.block.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class SupernaturalBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(Registries.BLOCK, Supernatural.MODID);
	public static final DeferredHolder<Block, Block> GRAVE_SOIL = REGISTRY.register("grave_soil", () -> new GraveSoilBlock(createBaseProps("grave_soil").mapColor(MapColor.PODZOL).sound(SoundType.SOUL_SOIL).strength(0.5f, 2f).randomTicks()));
	public static final DeferredHolder<Block, Block> RITUAL_ALTAR = REGISTRY.register("ritual_altar", () -> new RitualBlock(createBaseProps("ritual_altar").mapColor(MapColor.DEEPSLATE).sound(SoundType.DEEPSLATE_BRICKS).strength(1.2f, 8f).requiresCorrectToolForDrops()));

	public static BlockBehaviour.Properties createBaseProps(String name) {
		return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, name)));
	}
}