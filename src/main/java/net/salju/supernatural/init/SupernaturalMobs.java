package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.entity.*;
import net.salju.supernatural.events.SupernaturalManager;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;

@EventBusSubscriber
public class SupernaturalMobs {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, Supernatural.MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<Vampire>> VAMPIRE = register("vampire", EntityType.Builder.of(Vampire::new, MobCategory.MONSTER).sized(0.6F, 1.95F).ridingOffset(-0.6F));
	public static final DeferredHolder<EntityType<?>, EntityType<Angel>> ANGEL = register("angel", EntityType.Builder.of(Angel::new, MobCategory.MISC).sized(0.6F, 1.95F));
	public static final DeferredHolder<EntityType<?>, EntityType<Merfolk>> MERFOLK = register("merfolk", EntityType.Builder.of(Merfolk::new, MobCategory.MONSTER).sized(0.6F, 1.95F).ridingOffset(-0.15F));
    public static final DeferredHolder<EntityType<?>, EntityType<Thrall>> THRALL = register("thrall", EntityType.Builder.of(Thrall::new, MobCategory.MONSTER).sized(0.6F, 1.95F).ridingOffset(-0.7F).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<Wight>> WIGHT = register("wight", EntityType.Builder.of(Wight::new, MobCategory.MONSTER).sized(0.6F, 1.95F).ridingOffset(-0.6F).fireImmune());
	public static final DeferredHolder<EntityType<?>, EntityType<Scourge>> SCOURGE = register("scourge", EntityType.Builder.of(Scourge::new, MobCategory.CREATURE).sized(1.3964844F, 1.6F).eyeHeight(1.52F).passengerAttachments(1.31875F).clientTrackingRange(10).fireImmune());

	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder) {
		return REGISTRY.register(name, () -> builder.build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, name))));
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(VAMPIRE.get(), SupernaturalManager.createAttributes(32, 1, 2, 0.3).build());
		event.put(ANGEL.get(), SupernaturalManager.createAttributes(24, 7, 0, 0.25).add(Attributes.KNOCKBACK_RESISTANCE, 1).add(Attributes.FOLLOW_RANGE, 32).add(Attributes.STEP_HEIGHT, 1.25).build());
		event.put(MERFOLK.get(), SupernaturalManager.createAttributes(21, 3, 2, 0.25).add(Attributes.KNOCKBACK_RESISTANCE, 0.25).build());
        event.put(THRALL.get(), SupernaturalManager.createAttributes(30, 1, 2, 0.20).build());
		event.put(WIGHT.get(), SupernaturalManager.createAttributes(30, 1, 0, 0.25).build());
		event.put(SCOURGE.get(), SupernaturalManager.createAttributes(40, 1, 0, 0.3).add(Attributes.JUMP_STRENGTH, 0.75).add(Attributes.STEP_HEIGHT, 1.0F).add(Attributes.SAFE_FALL_DISTANCE, 7.0F).add(Attributes.FALL_DAMAGE_MULTIPLIER, 0.5F).build());
	}

	@SubscribeEvent
	public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
		event.register(VAMPIRE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(MERFOLK.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SupernaturalManager::checkMerfolkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(THRALL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SupernaturalManager::checkVaultSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(WIGHT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SupernaturalManager::checkVaultSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
		event.register(SCOURGE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SupernaturalManager::checkVaultSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
	}
}