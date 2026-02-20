package net.salju.supernatural.compat;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.init.SupernaturalCandle;
import net.salju.supernatural.init.SupernaturalItems;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CandleHolderBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class Supplementaries  {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Supernatural.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Supernatural.MODID);

    public static final DeferredHolder<Block, Block> EBONSTEEL_CANDLE_HOLDER_BLOCK = BLOCKS.register("ebonsteel_candle_holder", () -> new CandleHolderBlock(null, SupernaturalBlocks.createBaseProps("ebonsteel_candle_holder").mapColor(MapColor.DEEPSLATE).sound(SoundType.NETHERITE_BLOCK).strength(25.0F, 1200.0F).pushReaction(PushReaction.DESTROY).lightLevel((state) -> state.getValue(CandleHolderBlock.LIT) ? 2 * state.getValue(CandleHolderBlock.CANDLES) : 0).requiresCorrectToolForDrops().noOcclusion().forceSolidOn(), SupernaturalCandle.FLAME::get, CandleHolderBlock::getDefaultParticleOffsets));
    public static final DeferredHolder<Item, Item> EBONSTEEL_CANDLE_HOLDER_ITEM = block(EBONSTEEL_CANDLE_HOLDER_BLOCK, "ebonsteel_candle_holder");

    private static DeferredHolder<Item, Item> block(DeferredHolder<Block, Block> block, String name) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), SupernaturalItems.createBaseProps(name)));
    }

    public static Item getCandleHolder() {
        return EBONSTEEL_CANDLE_HOLDER_ITEM.get();
    }
}