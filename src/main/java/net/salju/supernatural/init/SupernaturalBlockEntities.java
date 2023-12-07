package net.salju.supernatural.init;

import net.salju.supernatural.block.RitualBlockEntity;
import net.salju.supernatural.SupernaturalMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class SupernaturalBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SupernaturalMod.MODID);
	public static final RegistryObject<BlockEntityType<RitualBlockEntity>> RITUAL = REGISTRY.register("ritual_altar", () -> BlockEntityType.Builder.of(RitualBlockEntity::new, SupernaturalBlocks.RITUAL_ALTAR.get()).build(null));
}