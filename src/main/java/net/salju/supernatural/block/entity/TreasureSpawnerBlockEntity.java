package net.salju.supernatural.block.entity;

import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.block.misc.SoulSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.Spawner;
import com.mojang.datafixers.util.Either;
import javax.annotation.Nullable;

public class TreasureSpawnerBlockEntity extends BlockEntity implements Spawner {
    private final SoulSpawner spawner = new SoulSpawner() {
        @Override
        public void broadcastEvent(Level world, BlockPos pos, int i) {
            world.blockEvent(pos, SupernaturalBlocks.TREASURE_SPAWNER.get(), i, 0);
        }

        @Override
        public void setNextSpawnData(@Nullable Level world, BlockPos pos, SpawnData data) {
            super.setNextSpawnData(world, pos, data);
            if (world != null) {
                world.sendBlockUpdated(pos, world.getBlockState(pos), world.getBlockState(pos), 4);
            }
        }

        @Override
        public Either<BlockEntity, Entity> getOwner() {
            return Either.left(TreasureSpawnerBlockEntity.this);
        }
    };

	public TreasureSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(SupernaturalBlocks.TP.get(), pos, state);
	}

	@Override
    public void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        this.spawner.save(tag);
	}

	@Override
    public void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        this.spawner.load(this.getLevel(), this.getBlockPos(), tag);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider regs) {
        CompoundTag tag = this.saveCustomOnly(regs);
        tag.remove("SpawnPotentials");
        return tag;
	}

    @Override
    public boolean triggerEvent(int i, int e) {
        return this.spawner.onEventTriggered(this.getLevel(), i) ? true : super.triggerEvent(i, e);
    }

    @Override
    public void setEntityId(EntityType<?> type, RandomSource rng) {
        this.spawner.setEntityId(type, this.getLevel(), rng, this.getBlockPos());
        this.setChanged();
    }

    public SoulSpawner getSpawner() {
        return this.spawner;
    }

    public static void clientTick(Level world, BlockPos pos, BlockState state, TreasureSpawnerBlockEntity target) {
        target.spawner.clientTick(world, pos);
    }

    public static void serverTick(Level world, BlockPos pos, BlockState state, TreasureSpawnerBlockEntity target) {
        if (world instanceof ServerLevel lvl && !lvl.hasNeighborSignal(pos)) {
            target.spawner.serverTick(lvl, pos);
        }
    }
}