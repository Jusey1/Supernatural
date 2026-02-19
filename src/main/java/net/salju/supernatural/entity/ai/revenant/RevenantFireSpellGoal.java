package net.salju.supernatural.entity.ai.revenant;

import net.salju.supernatural.entity.Revenant;
import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.init.SupernaturalSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;

public class RevenantFireSpellGoal extends AbstractRevenantSpellGoal {
	public RevenantFireSpellGoal(Revenant target) {
		super(target);
	}

	@Override
	protected void performSpellCasting() {
        BlockPos top = BlockPos.containing((this.getTarget().getX() + 1), this.getTarget().getY(), (this.getTarget().getZ() + 1));
        BlockPos bot = BlockPos.containing((this.getTarget().getX() - 1), this.getTarget().getY(), (this.getTarget().getZ() - 1));
        for (BlockPos pos : BlockPos.betweenClosed(top, bot)) {
            if (this.getTarget().level().isEmptyBlock(pos) && this.getTarget().level().getBlockState(pos.below()).isSolid()) {
                this.getTarget().level().setBlock(pos, SupernaturalBlocks.REVENANT_FLAME.get().defaultBlockState(), 3);
            }
        }
        this.getTarget().level().playSound(null, this.getTarget().blockPosition(), SupernaturalSounds.WRAITH_FIRE.get(), SoundSource.BLOCKS);
	}

	@Override
	protected int getCastingInterval() {
		return 420;
	}
}