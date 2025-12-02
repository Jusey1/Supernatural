package net.salju.supernatural.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.Difficulty;

public class MerfolkAmethyst extends AbstractMerfolkEntity {
	public MerfolkAmethyst(EntityType<MerfolkAmethyst> type, Level world) {
		super(type, world);
	}

    public static boolean checkMerfolkSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor world, MobSpawnType spawn, BlockPos pos, RandomSource randy) {
        boolean flag = world.getDifficulty() != Difficulty.PEACEFUL && (MobSpawnType.ignoresLightRequirements(spawn) || isDarkEnoughToSpawn(world, pos, randy)) && (MobSpawnType.isSpawner(spawn) || world.getFluidState(pos).is(FluidTags.WATER));
        if (flag && MobSpawnType.isSpawner(spawn)) {
            return true;
        } else {
            return isDeepEnoughToSpawn(pos) && randy.nextInt(56) >= 54 && flag;
        }
    }

    private static boolean isDeepEnoughToSpawn(BlockPos pos) {
        return pos.getY() <= 56;
    }
}