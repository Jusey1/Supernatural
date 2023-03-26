package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalModEntities;
import net.minecraftforge.network.PlayMessages;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;

public class MerDiamondEntity extends AbstractMerEntity {
	public MerDiamondEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(SupernaturalModEntities.MER_DIAMOND.get(), world);
	}

	public MerDiamondEntity(EntityType<MerDiamondEntity> type, Level world) {
		super(type, world);
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 24);
		builder = builder.add(Attributes.ARMOR, 4);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 4);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.25);
		return builder;
	}
}