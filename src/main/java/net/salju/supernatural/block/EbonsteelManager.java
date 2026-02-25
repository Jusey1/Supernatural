package net.salju.supernatural.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.SoundType;

public class EbonsteelManager {
    public static final BlockSetType EBONSTEEL = BlockSetType.register(new BlockSetType("ebonsteel", true, false, false, BlockSetType.PressurePlateSensitivity.MOBS, SoundType.NETHERITE_BLOCK, SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
    public static final BooleanProperty LOCKED = BooleanProperty.create("locked");
}