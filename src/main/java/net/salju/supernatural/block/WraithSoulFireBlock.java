package net.salju.supernatural.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.MapCodec;

public class WraithSoulFireBlock extends BaseFireBlock {
	public static final MapCodec<WraithSoulFireBlock> CODEC = simpleCodec(WraithSoulFireBlock::new);

	public WraithSoulFireBlock(BlockBehaviour.Properties props) {
		super(props, 2.0F);
	}

	@Override
	public MapCodec<WraithSoulFireBlock> codec() {
		return CODEC;
	}

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity target) {
        target.setTicksFrozen(0);
        if (target.getType().is(EntityTypeTags.UNDEAD)) {
            WraithSoulFireBlock.applyUndead(target);
        } else {
            WraithSoulFireBlock.applyLiving(target);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction dir, BlockState nearbyState, LevelAccessor world, BlockPos pos, BlockPos nearbyPos) {
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

    public static void applyLiving(Entity ent) {
        if (ent.level() instanceof ServerLevel lvl) {
            ent.hurt(lvl.damageSources().magic(), getFireDamage(lvl));
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