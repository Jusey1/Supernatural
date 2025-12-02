package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.block.*;
import net.salju.supernatural.block.entity.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import com.google.common.collect.ImmutableSet;
import java.util.Set;

public class SupernaturalBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(Registries.BLOCK, Supernatural.MODID);
	public static final DeferredRegister<BlockEntityType<?>> BE = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Supernatural.MODID);
	public static final DeferredRegister<PoiType> POI = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, Supernatural.MODID);
    public static final DeferredHolder<Block, Block> WRAITH_SOUL_FIRE = REGISTRY.register("wraith_soul_fire", () -> new WraithSoulFireBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).replaceable().noCollission().instabreak().lightLevel(state -> 10).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)));
	public static final DeferredHolder<Block, Block> GRAVE_SOIL = REGISTRY.register("grave_soil", () -> new GraveSoilBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).sound(SoundType.SOUL_SOIL).strength(0.5f, 2f).randomTicks()));
	public static final DeferredHolder<Block, Block> RITUAL_ALTAR = REGISTRY.register("ritual_altar", () -> new RitualBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).sound(SoundType.DEEPSLATE_BRICKS).strength(1.2f, 8f).requiresCorrectToolForDrops()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RitualBlockEntity>> RITUAL = BE.register("ritual_altar", () -> BlockEntityType.Builder.of(RitualBlockEntity::new, RITUAL_ALTAR.get()).build(null));
	public static final DeferredHolder<PoiType, PoiType> RITUAL_POI = POI.register("ritual_altar_poi", () -> new PoiType(getBlockStates(RITUAL_ALTAR.get()), 0, 1));

	private static Set<BlockState> getBlockStates(Block block) {
		return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
	}
}