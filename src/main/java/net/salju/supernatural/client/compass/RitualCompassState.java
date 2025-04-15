package net.salju.supernatural.client.compass;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.item.component.RitualCompassData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.NeedleDirectionHelper;
import net.minecraft.core.GlobalPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;

public class RitualCompassState extends NeedleDirectionHelper {
    public static final MapCodec<RitualCompassState> MAP_CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(Codec.BOOL.optionalFieldOf("wobble", Boolean.TRUE).forGetter(RitualCompassState::wobble)).apply(codec, RitualCompassState::new));
    private final NeedleDirectionHelper.Wobbler wobbler = this.newWobbler(0.8F);
    private final RandomSource random = RandomSource.create();

    public RitualCompassState(boolean check) {
        super(check);
    }

    @Override
    protected float calculate(ItemStack stack, ClientLevel lvl, int i, Entity target) {
        RitualCompassData data = stack.get(SupernaturalData.COMPASS);
        GlobalPos pos = null;
        if (data != null && data.target().isPresent()) {
            pos = data.target().get();
        }
        return isValid(target, pos) ? this.getTarget(target, lvl.getGameTime(), pos.pos()) : this.getRandom(i, lvl.getGameTime());
    }

    private float getRandom(int i, long time) {
        if (this.wobbler.shouldUpdate(time)) {
            this.wobbler.update(time, this.random.nextFloat());
        }
        return Mth.positiveModulo(this.wobbler.rotation() + (float) hash(i) / 2.1474836E9F, 1.0F);
    }

    private float getTarget(Entity target, long time, BlockPos pos) {
        float a = (float) getAngle(target, pos);
        float y = getRotY(target);
        if (target instanceof Player player && player.isLocalPlayer() && player.level().tickRateManager().runsNormally()) {
            if (this.wobbler.shouldUpdate(time)) {
                this.wobbler.update(time, 0.5F - (y - 0.25F));
            }
            return Mth.positiveModulo(a + this.wobbler.rotation(), 1.0F);
        }
        return Mth.positiveModulo(0.5F - (y - 0.25F - a), 1.0F);
    }

    private static boolean isValid(Entity target, @Nullable GlobalPos pos) {
        return pos != null && pos.dimension().equals(target.level().dimension()) && !(pos.pos().distToCenterSqr(target.position()) < 1.0E-5F);
    }

    private static double getAngle(Entity target, BlockPos pos) {
        return Math.atan2(Vec3.atCenterOf(pos).z() - target.getZ(), Vec3.atCenterOf(pos).x() - target.getX()) / (float) (Math.PI * 2);
    }

    private static float getRotY(Entity target) {
        return Mth.positiveModulo(target.getVisualRotationYInDegrees() / 360.0F, 1.0F);
    }

    private static int hash(int i) {
        return i * 1327217883;
    }
}