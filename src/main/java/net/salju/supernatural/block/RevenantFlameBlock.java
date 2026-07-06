package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalDamageTypes;
import net.salju.supernatural.entity.Angel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
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
        if (target instanceof Angel devil) {
            if (!devil.isCursed()) {
                devil.setCursed(true);
            }
        } else if (target.getType().is(EntityTypeTags.UNDEAD)) {
            RevenantFlameBlock.applyUndead(target);
        } else if (target instanceof LivingEntity) {
            RevenantFlameBlock.applyLiving(target);
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

    public static void applyUndead(Entity target) {
        if (target instanceof LivingEntity mob && target.level() instanceof ServerLevel lvl) {
            if (mob.getTicksFrozen() <= 5) {
                mob.setTicksFrozen(mob.getTicksFrozen() + 25);
                mob.heal(2.5F);
            }
        }
    }

    public static void applyLiving(Entity target) {
        if (target.level() instanceof ServerLevel lvl) {
            if (target.getTicksFrozen() <= 25) {
                target.setTicksFrozen(target.getTicksFrozen() + 50);
                target.hurt(SupernaturalDamageTypes.getColdflame(lvl.registryAccess(), null), 3.0F);
            }
        }
    }
}