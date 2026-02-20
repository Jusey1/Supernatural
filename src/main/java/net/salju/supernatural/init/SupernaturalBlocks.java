package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.block.*;
import net.salju.supernatural.block.entity.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import com.google.common.collect.ImmutableSet;
import java.util.Set;

public class SupernaturalBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(Registries.BLOCK, Supernatural.MODID);
	public static final DeferredRegister<BlockEntityType<?>> BE = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Supernatural.MODID);
	public static final DeferredRegister<PoiType> POI = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, Supernatural.MODID);
	public static final DeferredHolder<Block, Block> REVENANT_FLAME = REGISTRY.register("revenant_flame", () -> new RevenantFlameBlock(createBaseProps("revenant_flame").mapColor(MapColor.COLOR_LIGHT_BLUE).replaceable().noCollision().instabreak().lightLevel(state -> 10).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)));
	public static final DeferredHolder<Block, Block> GRAVE_SOIL = REGISTRY.register("grave_soil", () -> new GraveSoilBlock(createBaseProps("grave_soil").mapColor(MapColor.PODZOL).sound(SoundType.SOUL_SOIL).strength(0.5F, 2.0F).randomTicks()));
    public static final DeferredHolder<Block, Block> TREASURE_SPAWNER = REGISTRY.register("treasure_spawner", () -> new TreasureSpawnerBlock(createBaseProps("treasure_spawner").mapColor(MapColor.DEEPSLATE).sound(SoundType.TRIAL_SPAWNER).strength(25.0F, 1200.0F).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredHolder<Block, Block> TREASURE_VAULT = REGISTRY.register("treasure_vault", () -> new TreasureVaultBlock(createBaseProps("treasure_vault").mapColor(MapColor.DEEPSLATE).sound(SoundType.VAULT).strength(25.0F, 1200.0F).requiresCorrectToolForDrops().noOcclusion()));
	public static final DeferredHolder<Block, Block> RITUAL_ALTAR = REGISTRY.register("ritual_altar", () -> new RitualAltarBlock(createBaseProps("ritual_altar").mapColor(MapColor.DEEPSLATE).sound(SoundType.DEEPSLATE_BRICKS).strength(1.2F, 8.0F).requiresCorrectToolForDrops()));
    public static final DeferredHolder<Block, Block> RITUAL_CANDLE = REGISTRY.register("ritual_candle", () -> new RitualCandleBlock(createBaseProps("ritual_candle").mapColor(MapColor.DEEPSLATE).sound(SoundType.CANDLE).strength(0.1F).pushReaction(PushReaction.DESTROY).lightLevel((state) -> state.getValue(RitualCandleBlock.LIT) ? 2 * state.getValue(RitualCandleBlock.CANDLES) : 0).noOcclusion()));
    public static final DeferredHolder<Block, Block> EBONSTEEL_BARS = REGISTRY.register("ebonsteel_bars", () -> new IronBarsBlock(createBaseProps("ebonsteel_bars").mapColor(MapColor.DEEPSLATE).sound(SoundType.NETHERITE_BLOCK).strength(50.0F, 1200.0F).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredHolder<Block, Block> EBONSTEEL_BLOCK = REGISTRY.register("ebonsteel_block", () -> new Block(createBaseProps("ebonsteel_block").mapColor(MapColor.DEEPSLATE).sound(SoundType.NETHERITE_BLOCK).strength(50.0F, 1200.0F).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops()));
    public static final DeferredHolder<Block, Block> EBONSTEEL_CHAIN = REGISTRY.register("ebonsteel_chain", () -> new ChainBlock(createBaseProps("ebonsteel_chain").mapColor(MapColor.DEEPSLATE).sound(SoundType.NETHERITE_BLOCK).strength(25.0F, 1200.0F).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredHolder<Block, Block> EBONSTEEL_DOOR = REGISTRY.register("ebonsteel_door", () -> new EbonsteelDoorBlock(createBaseProps("ebonsteel_door").mapColor(MapColor.DEEPSLATE).sound(SoundType.NETHERITE_BLOCK).strength(50.0F, 1200.0F).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().noOcclusion()));
    public static final DeferredHolder<Block, Block> EBONSTEEL_LANTERN = REGISTRY.register("ebonsteel_lantern", () -> new LanternBlock(createBaseProps("ebonsteel_lantern").mapColor(MapColor.DEEPSLATE).sound(SoundType.NETHERITE_BLOCK).strength(25.0F, 1200.0F).pushReaction(PushReaction.DESTROY).lightLevel((state) -> 10).requiresCorrectToolForDrops().noOcclusion().forceSolidOn()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RitualAltarEntity>> RITUAL = BE.register("ritual_altar", () -> new BlockEntityType<>(RitualAltarEntity::new, RITUAL_ALTAR.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TreasureSpawnerBlockEntity>> TP = BE.register("treasure_spawner", () -> new BlockEntityType<>(TreasureSpawnerBlockEntity::new, TREASURE_SPAWNER.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TreasureVaultBlockEntity>> TV = BE.register("treasure_vault", () -> new BlockEntityType<>(TreasureVaultBlockEntity::new, TREASURE_VAULT.get()));
	public static final DeferredHolder<PoiType, PoiType> RITUAL_POI = POI.register("ritual_altar_poi", () -> new PoiType(getBlockStates(RITUAL_ALTAR.get()), 0, 1));

	public static BlockBehaviour.Properties createBaseProps(String name) {
		return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Supernatural.MODID, name)));
	}

	private static Set<BlockState> getBlockStates(Block block) {
		return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
	}
}