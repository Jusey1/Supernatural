package net.salju.supernatural.init;

import net.salju.supernatural.entity.*;
import net.salju.supernatural.SupernaturalMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SupernaturalMobs {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SupernaturalMod.MODID);
	public static final RegistryObject<EntityType<Vampire>> VAMPIRE = register("vampire", EntityType.Builder.<Vampire>of(Vampire::new, MobCategory.MONSTER).sized(0.6f, 1.95f));
	public static final RegistryObject<EntityType<Necromancer>> NECROMANCER = register("necromancer", EntityType.Builder.<Necromancer>of(Necromancer::new, MobCategory.MONSTER).sized(0.6f, 1.95f));
	public static final RegistryObject<EntityType<PossessedArmor>> POSSESSED_ARMOR = register("possessed_armor", EntityType.Builder.<PossessedArmor>of(PossessedArmor::new, MobCategory.MISC).sized(0.6f, 1.95f));
	public static final RegistryObject<EntityType<Spooky>> SPOOKY = register("spooky", EntityType.Builder.<Spooky>of(Spooky::new, MobCategory.MISC).sized(0.4f, 0.6f));
	public static final RegistryObject<EntityType<MerA>> MER_AMETHYST = register("mer_amethyst", EntityType.Builder.<MerA>of(MerA::new, MobCategory.MONSTER).sized(0.6f, 1.95f));
	public static final RegistryObject<EntityType<MerE>> MER_EMERALD = register("mer_emerald", EntityType.Builder.<MerE>of(MerE::new, MobCategory.MONSTER).sized(0.6f, 1.95f));
	public static final RegistryObject<EntityType<MerD>> MER_DIAMOND = register("mer_diamond", EntityType.Builder.<MerD>of(MerD::new, MobCategory.MONSTER).sized(0.6f, 1.95f));
	public static final RegistryObject<EntityType<Angel>> ANGEL = register("angel", EntityType.Builder.<Angel>of(Angel::new, MobCategory.MISC).sized(0.6f, 1.95f));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			MerA.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(VAMPIRE.get(), Vampire.createAttributes().build());
		event.put(NECROMANCER.get(), Necromancer.createAttributes().build());
		event.put(POSSESSED_ARMOR.get(), PossessedArmor.createAttributes().build());
		event.put(SPOOKY.get(), Spooky.createAttributes().build());
		event.put(MER_AMETHYST.get(), MerA.createAttributes().build());
		event.put(MER_EMERALD.get(), MerE.createAttributes().build());
		event.put(MER_DIAMOND.get(), MerD.createAttributes().build());
		event.put(ANGEL.get(), Angel.createAttributes().build());
	}
}