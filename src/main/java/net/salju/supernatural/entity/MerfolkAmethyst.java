package net.salju.supernatural.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.Difficulty;

public class MerfolkAmethyst extends AbstractMerfolkEntity {
	public MerfolkAmethyst(EntityType<MerfolkAmethyst> type, Level world) {
		super(type, world);
	}

    public static boolean checkMerfolkSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor world, EntitySpawnReason spawn, BlockPos pos, RandomSource randy) {
        boolean flag = world.getDifficulty() != Difficulty.PEACEFUL && (EntitySpawnReason.ignoresLightRequirements(spawn) || isDarkEnoughToSpawn(world, pos, randy)) && (EntitySpawnReason.isSpawner(spawn) || world.getFluidState(pos).is(FluidTags.WATER));
        if (flag && EntitySpawnReason.isSpawner(spawn)) {
            return true;
        } else {
            return pos.getY() <= 54 && randy.nextInt(99) <= 0 && flag;
        }
    }
}