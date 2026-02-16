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

public class RevenantFlameBlock extends BaseFireBlock {
	public static final MapCodec<RevenantFlameBlock> CODEC = simpleCodec(RevenantFlameBlock::new);

	public RevenantFlameBlock(BlockBehaviour.Properties props) {
		super(props, 2.0F);
	}

	@Override
	public MapCodec<RevenantFlameBlock> codec() {
		return CODEC;
	}

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity target, InsideBlockEffectApplier apply, boolean check) {
        apply.apply(InsideBlockEffectType.CLEAR_FREEZE);
        if (target.getType().is(EntityTypeTags.UNDEAD)) {
            apply.runAfter(InsideBlockEffectType.CLEAR_FREEZE, RevenantFlameBlock::applyUndead);
        } else {
            apply.runAfter(InsideBlockEffectType.CLEAR_FREEZE, RevenantFlameBlock::applyLiving);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tick, BlockPos pos, Direction dir, BlockPos nearbyPos, BlockState nearbyState, RandomSource randy) {
        return this.canSurvive(state, world, pos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).isFaceSturdy(world, pos.below(), Direction.UP);
    }

    @Override
    protected boolean canBurn(BlockState state) {
        return false;
    }

    public static void applyUndead(Entity ent) {
        if (ent instanceof LivingEntity target && target.level() instanceof ServerLevel lvl) {
            target.heal(getFireDamage(lvl));
        }
    }

    public static void applyLiving(Entity target) {
        if (target.level() instanceof ServerLevel lvl) {
            target.hurtServer(lvl, lvl.damageSources().magic(), getFireDamage(lvl));
        }
    }

    public static float getFireDamage(ServerLevel lvl) {
        if (lvl.getDifficulty().equals(Difficulty.HARD)) {
            return 3.0F;
        } else if (lvl.getDifficulty().equals(Difficulty.NORMAL)) {
            return 2.0F;
        }
        return 1.0F;
    }
}