package net.salju.supernatural.client.compass;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;

public class RitualCompassAngle implements RangeSelectItemModelProperty {
    public static final MapCodec<RitualCompassAngle> MAP_CODEC = MapCodec.unit(new RitualCompassAngle());
    private final RitualCompassState state;

    public RitualCompassAngle() {
        this(new RitualCompassState(true));
    }

    private RitualCompassAngle(RitualCompassState state) {
        this.state = state;
    }

    @Override
    public float get(ItemStack stack, @Nullable ClientLevel lvl, @Nullable ItemOwner target, int i) {
        return state.get(stack, lvl, target, i);
    }

    @Override
    public MapCodec<RitualCompassAngle> type() {
        return MAP_CODEC;
    }
}