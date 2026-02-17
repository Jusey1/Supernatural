package net.salju.supernatural.block.misc;

import net.neoforged.neoforge.common.extensions.IOwnedSpawner;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.Difficulty;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import javax.annotation.Nullable;
import java.util.Optional;

public abstract class SoulSpawner implements IOwnedSpawner {
    //Note: This file is mostly the same as Mojang's BaseSpawner, just with a few edits.
    private static final Logger LOGGER = LogUtils.getLogger();
    private int spawnDelay = 20;
    private WeightedList<SpawnData> spawnPotentials = WeightedList.of();
    @Nullable
    private SpawnData nextSpawnData;
    private double spin;
    private double oSpin;
    @Nullable
    private Entity displayEntity;

    public void setEntityId(EntityType<?> type, @Nullable Level level, RandomSource random, BlockPos pos) {
        this.getOrCreateNextSpawnData(level, random, pos).getEntityToSpawn().putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
    }

    public void save(ValueOutput tag) {
        tag.putShort("Delay", (short) this.spawnDelay);
        tag.storeNullable("SpawnData", SpawnData.CODEC, this.nextSpawnData);
        tag.store("SpawnPotentials", SpawnData.LIST_CODEC, this.spawnPotentials);
    }

    public void load(@Nullable Level world, BlockPos pos, ValueInput tag) {
        this.spawnDelay = tag.getShortOr("Delay", (short)20);
        tag.read("SpawnData", SpawnData.CODEC).ifPresent((p_400944_) -> this.setNextSpawnData(world, pos, p_400944_));
        this.spawnPotentials = tag.read("SpawnPotentials", SpawnData.LIST_CODEC).orElseGet(() -> WeightedList.of(this.nextSpawnData != null ? this.nextSpawnData : new SpawnData()));
        this.displayEntity = null;
    }

    public void clientTick(Level world, BlockPos pos) {
        particleTick(world, pos);
        if (!isNearPlayer(world, pos) && !world.hasNeighborSignal(pos)) {
            this.oSpin = this.spin;
        } else if (this.displayEntity != null) {
            if (this.spawnDelay > 0) {
                --this.spawnDelay;
            }
            this.oSpin = this.spin;
            this.spin = (this.spin + (double) (1000.0F / ((float) this.spawnDelay + 200.0F))) % (double) 360.0F;
        }
    }

    public void serverTick(ServerLevel lvl, BlockPos pos) {
        if (isNearPlayer(lvl, pos) && lvl.isSpawnerBlockEnabled()) {
            if (this.spawnDelay == -1) {
                this.delay(lvl, pos);
            }
            if (this.spawnDelay <= 0) {
                boolean flag = false;
                RandomSource rng = lvl.getRandom();
                SpawnData data = this.getOrCreateNextSpawnData(lvl, rng, pos);
                for(int i = 0; i < 2; ++i) {
                    try (ProblemReporter.ScopedCollector problemreporter$scopedcollector = new ProblemReporter.ScopedCollector(this::toString, LOGGER)) {
                        ValueInput tag = TagValueInput.create(problemreporter$scopedcollector, lvl.registryAccess(), data.getEntityToSpawn());
                        Optional<EntityType<?>> optional = EntityType.by(tag);
                        if (optional.isEmpty()) {
                            this.delay(lvl, pos);
                            return;
                        }
                        Vec3 v = tag.read("Pos", Vec3.CODEC).orElseGet(() -> new Vec3(pos.getX() + (rng.nextDouble() - rng.nextDouble()) * 4.0 + 0.5, (pos.getY() + rng.nextInt(3) - 1), pos.getZ() + (rng.nextDouble() - rng.nextDouble()) * 4.0 + 0.5));
                        if (lvl.noCollision((optional.get()).getSpawnAABB(v.x, v.y, v.z))) {
                            BlockPos poz = BlockPos.containing(v);
                            if (data.getCustomSpawnRules().isPresent()) {
                                if (!(optional.get()).getCategory().isFriendly() && lvl.getDifficulty() == Difficulty.PEACEFUL) {
                                    continue;
                                }
                                SpawnData.CustomSpawnRules rules = data.getCustomSpawnRules().get();
                                if (!rules.isValidPosition(poz, lvl)) {
                                    continue;
                                }
                            } else if (!SpawnPlacements.checkSpawnRules(optional.get(), lvl, EntitySpawnReason.SPAWNER, poz, lvl.getRandom())) {
                                continue;
                            }
                            Entity target = EntityType.loadEntityRecursive(tag, lvl, EntitySpawnReason.SPAWNER, (mob) -> {
                                mob.snapTo(v.x, v.y, v.z, mob.getYRot(), mob.getXRot());
                                return mob;
                            });
                            if (target == null) {
                                this.delay(lvl, pos);
                                return;
                            }
                            int j = lvl.getEntities(EntityTypeTest.forExactClass(target.getClass()), (new AABB(pos.getX(), pos.getY(), pos.getZ(), (pos.getX() + 1), (pos.getY() + 1), (pos.getZ() + 1))).inflate(4.0), EntitySelector.NO_SPECTATORS).size();
                            if (j >= 3) {
                                this.delay(lvl, pos);
                                return;
                            }
                            target.snapTo(target.getX(), target.getY(), target.getZ(), rng.nextFloat() * 360.0F, 0.0F);
                            if (target instanceof Mob mob) {
                                boolean flag1 = data.getEntityToSpawn().size() == 1 && data.getEntityToSpawn().getString("id").isPresent();
                                EventHooks.finalizeMobSpawnSpawner(mob, lvl, lvl.getCurrentDifficultyAt(target.blockPosition()), EntitySpawnReason.SPAWNER, null, this, flag1);
                                if (data.getEquipment().isPresent()) {
                                    mob.equip(data.getEquipment().get());
                                }
                            }
                            if (target instanceof Vex ghost) {
                                ghost.setLimitedLife(Mth.nextInt(ghost.getRandom(), 1200, 2400));
                                ghost.setBoundOrigin(poz);
                            }
                            if (!lvl.tryAddFreshEntityWithPassengers(target)) {
                                this.delay(lvl, pos);
                                return;
                            }
                            TrialSpawner.FlameParticle flame = TrialSpawner.FlameParticle.OMINOUS;
                            lvl.levelEvent(3011, pos, flame.encode());
                            lvl.levelEvent(3012, poz, flame.encode());
                            lvl.gameEvent(target, GameEvent.ENTITY_PLACE, poz);
                            if (target instanceof Mob mob) {
                                mob.spawnAnim();
                            }
                            flag = true;
                        }
                    }
                }
                if (flag) {
                    this.delay(lvl, pos);
                }
                return;
            }
            --this.spawnDelay;
        }
    }

    private void delay(Level world, BlockPos pos) {
        this.spawnDelay = 100 + world.getRandom().nextInt(500 - 100);
        this.spawnPotentials.getRandom(world.getRandom()).ifPresent((data) -> this.setNextSpawnData(world, pos, data));
        this.broadcastEvent(world, pos, 1);
    }

    @Nullable
    public Entity getOrCreateDisplayEntity(Level world, BlockPos pos) {
        if (this.displayEntity == null) {
            CompoundTag tag = this.getOrCreateNextSpawnData(world, world.getRandom(), pos).getEntityToSpawn();
            if (tag.getString("id").isEmpty()) {
                return null;
            }
            this.displayEntity = EntityType.loadEntityRecursive(tag, world, EntitySpawnReason.SPAWNER, EntityProcessor.NOP);
        }
        return this.displayEntity;
    }

    public boolean onEventTriggered(Level world, int i) {
        if (i == 1) {
            if (world.isClientSide()) {
                this.spawnDelay = 100;
            }
            return true;
        } else {
            return false;
        }
    }

    protected void setNextSpawnData(@Nullable Level world, BlockPos pos, SpawnData data) {
        this.nextSpawnData = data;
    }

    private SpawnData getOrCreateNextSpawnData(@Nullable Level world, RandomSource rng, BlockPos pos) {
        if (this.nextSpawnData != null) {
            return this.nextSpawnData;
        } else {
            this.setNextSpawnData(world, pos, this.spawnPotentials.getRandom(rng).orElseGet(SpawnData::new));
            return this.nextSpawnData;
        }
    }

    public abstract void broadcastEvent(Level world, BlockPos pos, int i);

    public double getSpin() {
        return this.spin;
    }

    public double getoSpin() {
        return this.oSpin;
    }

    public @Nullable Either<BlockEntity, Entity> getOwner() {
        return null;
    }

    public static void particleTick(Level world, BlockPos pos) {
        RandomSource rng = world.getRandom();
        double x = (double) pos.getX() + rng.nextDouble();
        double y = (double) pos.getY() + rng.nextDouble();
        double z = (double) pos.getZ() + rng.nextDouble();
        world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
        world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 0.0, 0.0, 0.0);
    }

    public static boolean isNearPlayer(Level world, BlockPos pos) {
        return world.hasNearbyAlivePlayer((double) pos.getX() + (double) 0.5F, (double) pos.getY() + (double) 0.5F, (double) pos.getZ() + (double) 0.5F, 16);
    }
}