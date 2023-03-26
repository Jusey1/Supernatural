package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalModEntities;

import net.minecraftforge.network.PlayMessages;

import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

public class NewVexEntity extends Vex {
	public NewVexEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(SupernaturalModEntities.NEW_VEX.get(), world);
	}

	public NewVexEntity(EntityType<NewVexEntity> type, Level world) {
		super(type, world);
	}

	protected float getStandingEyeHeight(Pose pose, EntityDimensions ent) {
		return ent.height * 0.6F;
	}

	public static void init() {
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
		SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		this.setBoundOrigin(new BlockPos(x, y, z));
		this.setLimitedLife(2000);
		for (AbstractIllager target : this.level.getEntitiesOfClass(AbstractIllager.class, this.getBoundingBox().inflate(12.0D))) {
			this.setOwner(target);
		}
		return retval;
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}
}