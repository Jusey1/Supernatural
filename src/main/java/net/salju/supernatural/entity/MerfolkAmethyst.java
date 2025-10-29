package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalMobs;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Difficulty;
import javax.annotation.Nullable;

public class MerfolkAmethyst extends AbstractMerfolkEntity {
	public MerfolkAmethyst(EntityType<MerfolkAmethyst> type, Level world) {
		super(type, world);
	}

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
        SpawnGroupData spawn = super.finalizeSpawn(world, difficulty, reason, data);
        int i = Mth.nextInt(this.getRandom(), 1, 100);
        if (i >= 75) {
            this.convertTo(SupernaturalMobs.MERFOLK_EMERALD.get(), ConversionParams.single(this, true, true), newbie -> { EventHooks.onLivingConvert(this, newbie); });
        } else if (i <= 15) {
            this.convertTo(SupernaturalMobs.MERFOLK_DIAMOND.get(), ConversionParams.single(this, true, true), newbie -> { EventHooks.onLivingConvert(this, newbie); });
        }
        return spawn;
    }

    public static boolean checkMerfolkSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor world, EntitySpawnReason spawn, BlockPos pos, RandomSource randy) {
        boolean flag = world.getDifficulty() != Difficulty.PEACEFUL && (EntitySpawnReason.ignoresLightRequirements(spawn) || isDarkEnoughToSpawn(world, pos, randy)) && (EntitySpawnReason.isSpawner(spawn) || world.getFluidState(pos).is(FluidTags.WATER));
        if (flag && EntitySpawnReason.isSpawner(spawn)) {
            return true;
        } else {
            return isDeepEnoughToSpawn(pos) && randy.nextInt(56) >= 54 && flag;
        }
    }

    private static boolean isDeepEnoughToSpawn(BlockPos pos) {
        return pos.getY() <= 56;
    }
}