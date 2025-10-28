package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.entity.*;
import net.salju.supernatural.events.SupernaturalManager;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

@EventBusSubscriber
public class SupernaturalMobs {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, Supernatural.MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<Vampire>> VAMPIRE = register("vampire", EntityType.Builder.of(Vampire::new, MobCategory.MONSTER).sized(0.6F, 1.95F).ridingOffset(-0.6F));
	public static final DeferredHolder<EntityType<?>, EntityType<Necromancer>> NECROMANCER = register("necromancer", EntityType.Builder.of(Necromancer::new, MobCategory.MONSTER).sized(0.6F, 1.95F).ridingOffset(-0.6F));
	public static final DeferredHolder<EntityType<?>, EntityType<PossessedArmor>> POSSESSED_ARMOR = register("possessed_armor", EntityType.Builder.of(PossessedArmor::new, MobCategory.MISC).sized(0.6F, 1.95F));
	public static final DeferredHolder<EntityType<?>, EntityType<Spooky>> SPOOKY = register("spooky", EntityType.Builder.of(Spooky::new, MobCategory.MISC).sized(0.4F, 0.75F).eyeHeight(0.52F));
	public static final DeferredHolder<EntityType<?>, EntityType<Angel>> ANGEL = register("angel", EntityType.Builder.of(Angel::new, MobCategory.MISC).sized(0.6F, 1.95F));

	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder) {
		return REGISTRY.register(name, () -> builder.build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, name))));
	}

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(VAMPIRE.get(), SupernaturalManager.createAttributes(24, 3, 2, 0.3).build());
        event.put(NECROMANCER.get(), SupernaturalManager.createAttributes(52, 3, 2, 0.3).build());
        event.put(POSSESSED_ARMOR.get(), SupernaturalManager.createAttributes(20, 1, 0, 0.25).add(Attributes.KNOCKBACK_RESISTANCE, 0.5).build());
        event.put(SPOOKY.get(), SupernaturalManager.createAttributes(12, 1, 0, 0.2).add(Attributes.FLYING_SPEED, 0.35).build());
        event.put(ANGEL.get(), SupernaturalManager.createAttributes(24, 7, 0, 0.25).add(Attributes.KNOCKBACK_RESISTANCE, 1).add(Attributes.FOLLOW_RANGE, 32).add(Attributes.STEP_HEIGHT, 1.25).build());
    }
}