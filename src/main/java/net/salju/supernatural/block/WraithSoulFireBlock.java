package net.salju.supernatural.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.InsideBlockEffectType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import java.util.UUID;

public class WraithSoulFireBlock extends BaseFireBlock {
	public static final MapCodec<WraithSoulFireBlock> CODEC = simpleCodec(WraithSoulFireBlock::new);
    private UUID owner;

	public WraithSoulFireBlock(BlockBehaviour.Properties props) {
		super(props, 2.0F);
	}

	@Override
	public MapCodec<WraithSoulFireBlock> codec() {
		return CODEC;
	}

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity target, InsideBlockEffectApplier apply, boolean check) {
        apply.apply(InsideBlockEffectType.CLEAR_FREEZE);
        if (target.getType().is(EntityTypeTags.UNDEAD)) {
            apply.runAfter(InsideBlockEffectType.CLEAR_FREEZE, WraithSoulFireBlock::applyUndead);
        } else {
            apply.runAfter(InsideBlockEffectType.CLEAR_FREEZE, WraithSoulFireBlock::applyLiving);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tick, BlockPos pos, Direction dir, BlockPos nearbyPos, BlockState nearybyState, RandomSource randy) {
        return this.canSurvive(state, world, pos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (world instanceof ServerLevel lvl && this.getOwner(lvl) != null && this.getOwner(lvl) instanceof LivingEntity target && !target.isAlive()) {
            return false;
        }
        return world.getBlockState(pos.below()).isFaceSturdy(world, pos.below(), Direction.UP);
    }

    @Override
    protected boolean canBurn(BlockState state) {
        return false;
    }

    @Nullable
    public Entity getOwner(ServerLevel lvl) {
        if (this.owner != null) {
            return lvl.getEntity(this.owner);
        }
        return null;
    }

    public void setOwner(LivingEntity target) {
        this.owner = target.getUUID();
    }

    public static void applyUndead(Entity ent) {
        if (ent instanceof LivingEntity target && target.level() instanceof ServerLevel lvl) {
            target.heal(getFireDamage(lvl));
        }
    }

    public static void applyLiving(Entity ent) {
        if (ent.level() instanceof ServerLevel lvl) {
            if (lvl.getBlockState(ent.blockPosition()).getBlock() instanceof WraithSoulFireBlock fire) {
                if (fire.getOwner(lvl) != null && fire.getOwner(lvl) instanceof LivingEntity target && target.isAlive()) {
                    ent.hurtServer(lvl, lvl.damageSources().indirectMagic(target, target), getFireDamage(lvl));
                } else {
                    ent.hurtServer(lvl, lvl.damageSources().magic(), getFireDamage(lvl));
                }
            }
        }
    }

    public static float getFireDamage(ServerLevel lvl) {
        if (lvl.getDifficulty() == Difficulty.HARD) {
            return 3.0F;
        } else if (lvl.getDifficulty() == Difficulty.NORMAL) {
            return 2.0F;
        }
        return 1.0F;
    }
}