package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.entity.*;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class SupernaturalMobs {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, Supernatural.MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<Vampire>> VAMPIRE = register("vampire", EntityType.Builder.of(Vampire::new, MobCategory.MONSTER).sized(0.6f, 1.95f).ridingOffset(-0.6F));
	public static final DeferredHolder<EntityType<?>, EntityType<Necromancer>> NECROMANCER = register("necromancer", EntityType.Builder.of(Necromancer::new, MobCategory.MONSTER).sized(0.6f, 1.95f).ridingOffset(-0.6F));
	public static final DeferredHolder<EntityType<?>, EntityType<PossessedArmor>> POSSESSED_ARMOR = register("possessed_armor", EntityType.Builder.of(PossessedArmor::new, MobCategory.MISC).sized(0.6f, 1.95f));
	public static final DeferredHolder<EntityType<?>, EntityType<Spooky>> SPOOKY = register("spooky", EntityType.Builder.of(Spooky::new, MobCategory.MISC).sized(0.4f, 0.6f));
	public static final DeferredHolder<EntityType<?>, EntityType<Angel>> ANGEL = register("angel", EntityType.Builder.of(Angel::new, MobCategory.MISC).sized(0.6f, 1.95f));

	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder) {
		return REGISTRY.register(name, () -> builder.build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, name))));
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(VAMPIRE.get(), Vampire.createAttributes().build());
		event.put(NECROMANCER.get(), Necromancer.createAttributes().build());
		event.put(POSSESSED_ARMOR.get(), PossessedArmor.createAttributes().build());
		event.put(SPOOKY.get(), Spooky.createAttributes().build());
		event.put(ANGEL.get(), Angel.createAttributes().build());
	}
}