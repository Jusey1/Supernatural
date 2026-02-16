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
	public static final DeferredHolder<EntityType<?>, EntityType<Necromancer>> NECROMANCER = register("necromancer", EntityType.Builder.of(Necromancer::new, MobCategory.MONSTER).sized(0.6F, 1.95F).ridingOffset(-0.6F));
	public static final DeferredHolder<EntityType<?>, EntityType<PossessedArmor>> POSSESSED_ARMOR = register("possessed_armor", EntityType.Builder.of(PossessedArmor::new, MobCategory.MISC).sized(0.6F, 1.95F));
	public static final DeferredHolder<EntityType<?>, EntityType<Spooky>> SPOOKY = register("spooky", EntityType.Builder.of(Spooky::new, MobCategory.MISC).sized(0.4F, 0.75F).eyeHeight(0.52F).fireImmune());
    public static final DeferredHolder<EntityType<?>, EntityType<Angel>> ANGEL = register("angel", EntityType.Builder.of(Angel::new, MobCategory.MISC).sized(0.6F, 1.95F));
    public static final DeferredHolder<EntityType<?>, EntityType<Merfolk>> MERFOLK_AMETHYST = register("merfolk_amethyst", EntityType.Builder.of(Merfolk::new, MobCategory.MONSTER).sized(0.6F, 1.95F).ridingOffset(-0.15F));
    public static final DeferredHolder<EntityType<?>, EntityType<Merfolk>> MERFOLK_EMERALD = register("merfolk_emerald", EntityType.Builder.of(Merfolk::new, MobCategory.MONSTER).sized(0.6F, 1.95F).ridingOffset(-0.15F));
    public static final DeferredHolder<EntityType<?>, EntityType<Merfolk>> MERFOLK_DIAMOND = register("merfolk_diamond", EntityType.Builder.of(Merfolk::new, MobCategory.MONSTER).sized(0.6F, 1.95F).ridingOffset(-0.15F));
    public static final DeferredHolder<EntityType<?>, EntityType<Wight>> WIGHT = register("wight", EntityType.Builder.of(Wight::new, MobCategory.MONSTER).sized(0.6F, 1.95F).ridingOffset(-0.6F).fireImmune());
    public static final DeferredHolder<EntityType<?>, EntityType<Revenant>> REVENANT = register("revenant", EntityType.Builder.of(Revenant::new, MobCategory.MONSTER).sized(0.75F, 2.05F).ridingOffset(-0.6F).fireImmune());

	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder) {
		return REGISTRY.register(name, () -> builder.build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, name))));
	}

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(VAMPIRE.get(), SupernaturalManager.createAttributes(24, 3, 2, 0.3).build());
        event.put(NECROMANCER.get(), SupernaturalManager.createAttributes(96, 3, 2, 0.3).build());
        event.put(POSSESSED_ARMOR.get(), SupernaturalManager.createAttributes(20, 1, 0, 0.25).add(Attributes.KNOCKBACK_RESISTANCE, 0.5).build());
        event.put(SPOOKY.get(), SupernaturalManager.createAttributes(12, 1, 0, 0.2).add(Attributes.FLYING_SPEED, 0.35).build());
        event.put(ANGEL.get(), SupernaturalManager.createAttributes(24, 7, 0, 0.25).add(Attributes.KNOCKBACK_RESISTANCE, 1).add(Attributes.FOLLOW_RANGE, 32).add(Attributes.STEP_HEIGHT, 1.25).build());
        event.put(MERFOLK_AMETHYST.get(), SupernaturalManager.createAttributes(18, 3, 0, 0.3).build());
        event.put(MERFOLK_EMERALD.get(), SupernaturalManager.createAttributes(21, 3, 2, 0.3).build());
        event.put(MERFOLK_DIAMOND.get(), SupernaturalManager.createAttributes(24, 4, 4, 0.3).add(Attributes.KNOCKBACK_RESISTANCE, 0.25).build());
        event.put(WIGHT.get(), SupernaturalManager.createAttributes(30, 3, 0, 0.3).build());
        event.put(REVENANT.get(), SupernaturalManager.createAttributes(50, 1, 0, 0.3).add(Attributes.FLYING_SPEED, 0.45).add(Attributes.KNOCKBACK_RESISTANCE, 1.0).add(Attributes.FOLLOW_RANGE, 32).build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(VAMPIRE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(NECROMANCER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(POSSESSED_ARMOR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(SPOOKY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(MERFOLK_AMETHYST.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SupernaturalManager::checkMerfolkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(MERFOLK_EMERALD.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SupernaturalManager::checkMerfolkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(MERFOLK_DIAMOND.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SupernaturalManager::checkMerfolkSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(WIGHT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SupernaturalManager::checkVaultSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(REVENANT.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SupernaturalManager::checkVaultSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}