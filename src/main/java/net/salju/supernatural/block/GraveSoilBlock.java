package net.salju.supernatural.block;

import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

public class GraveSoilBlock extends Block {
	public GraveSoilBlock(BlockBehaviour.Properties props) {
		super(props);
	}

    @Override
    public InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rez) {
        if (world instanceof ServerLevel lvl) {
            if (rez.getDirection().equals(Direction.UP) && stack.is(Items.FLINT_AND_STEEL) && SupernaturalManager.canSoulMagicWork(lvl, pos.above()) && lvl.getDifficulty() != Difficulty.PEACEFUL) {
                BlockPos poz = BlockPos.containing(pos.getX() + 0.5, pos.getY() + 2, pos.getZ() + 0.5);
                if (lvl.isEmptyBlock(poz)) {
                    if (Math.random() <= 0.65 || !SupernaturalConfig.VEX.get()) {
                        SupernaturalMobs.SPOOKY.get().spawn(lvl, poz, EntitySpawnReason.MOB_SUMMONED);
                    } else {
                        Vex ghost = EntityType.VEX.spawn(lvl, poz, EntitySpawnReason.MOB_SUMMONED);
                        if (ghost != null) {
                            ghost.setLimitedLife(Mth.nextInt(ghost.getRandom(), 1200, 2400));
                            ghost.setBoundOrigin(poz);
                        }
                    }
                    lvl.sendParticles(ParticleTypes.SOUL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 8, 0.75, 0.5, 0.75, 0);
                    lvl.playSound(null, pos, SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
                    lvl.setBlock(pos, Blocks.SOUL_SOIL.defaultBlockState(), 2);
                }
            }
        }
        return InteractionResult.PASS;
    }
}