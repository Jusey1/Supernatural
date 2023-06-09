package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalModEntities;
import net.minecraftforge.network.PlayMessages;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;

public class MerEmeraldEntity extends AbstractMerEntity {
	public MerEmeraldEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(SupernaturalModEntities.MER_EMERALD.get(), world);
	}

	public MerEmeraldEntity(EntityType<MerEmeraldEntity> type, Level world) {
		super(type, world);
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 20);
		builder = builder.add(Attributes.ARMOR, 2);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		return builder;
	}
}