
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.salju.supernatural.init;

import net.salju.supernatural.entity.VampireEntity;
import net.salju.supernatural.entity.SpookyEntity;
import net.salju.supernatural.entity.PossessedArmorEntity;
import net.salju.supernatural.entity.NecromancerEntity;
import net.salju.supernatural.entity.MerEmeraldEntity;
import net.salju.supernatural.entity.MerDiamondEntity;
import net.salju.supernatural.entity.MerAmethystEntity;
import net.salju.supernatural.entity.AngelEntity;
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
public class SupernaturalModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SupernaturalMod.MODID);
	public static final RegistryObject<EntityType<VampireEntity>> VAMPIRE = register("vampire",
			EntityType.Builder.<VampireEntity>of(VampireEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(VampireEntity::new)

					.sized(0.6f, 1.95f));
	public static final RegistryObject<EntityType<NecromancerEntity>> NECROMANCER = register("necromancer",
			EntityType.Builder.<NecromancerEntity>of(NecromancerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(NecromancerEntity::new)

					.sized(0.6f, 1.95f));
	public static final RegistryObject<EntityType<PossessedArmorEntity>> POSSESSED_ARMOR = register("possessed_armor",
			EntityType.Builder.<PossessedArmorEntity>of(PossessedArmorEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(PossessedArmorEntity::new)

					.sized(0.6f, 1.95f));
	public static final RegistryObject<EntityType<SpookyEntity>> SPOOKY = register("spooky",
			EntityType.Builder.<SpookyEntity>of(SpookyEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(SpookyEntity::new)

					.sized(0.4f, 0.6f));
	public static final RegistryObject<EntityType<MerAmethystEntity>> MER_AMETHYST = register("mer_amethyst",
			EntityType.Builder.<MerAmethystEntity>of(MerAmethystEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(MerAmethystEntity::new)

					.sized(0.6f, 1.95f));
	public static final RegistryObject<EntityType<MerEmeraldEntity>> MER_EMERALD = register("mer_emerald",
			EntityType.Builder.<MerEmeraldEntity>of(MerEmeraldEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(MerEmeraldEntity::new)

					.sized(0.6f, 1.95f));
	public static final RegistryObject<EntityType<MerDiamondEntity>> MER_DIAMOND = register("mer_diamond",
			EntityType.Builder.<MerDiamondEntity>of(MerDiamondEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(MerDiamondEntity::new)

					.sized(0.6f, 1.95f));
	public static final RegistryObject<EntityType<AngelEntity>> ANGEL = register("angel",
			EntityType.Builder.<AngelEntity>of(AngelEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(AngelEntity::new)

					.sized(0.6f, 1.95f));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			VampireEntity.init();
			NecromancerEntity.init();
			PossessedArmorEntity.init();
			SpookyEntity.init();
			MerAmethystEntity.init();
			MerEmeraldEntity.init();
			MerDiamondEntity.init();
			AngelEntity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(VAMPIRE.get(), VampireEntity.createAttributes().build());
		event.put(NECROMANCER.get(), NecromancerEntity.createAttributes().build());
		event.put(POSSESSED_ARMOR.get(), PossessedArmorEntity.createAttributes().build());
		event.put(SPOOKY.get(), SpookyEntity.createAttributes().build());
		event.put(MER_AMETHYST.get(), MerAmethystEntity.createAttributes().build());
		event.put(MER_EMERALD.get(), MerEmeraldEntity.createAttributes().build());
		event.put(MER_DIAMOND.get(), MerDiamondEntity.createAttributes().build());
		event.put(ANGEL.get(), AngelEntity.createAttributes().build());
	}
}
