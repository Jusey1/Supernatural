package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.item.component.*;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.component.DataComponentType;
import java.util.function.UnaryOperator;

public class SupernaturalData {
	public static final DeferredRegister<DataComponentType<?>> REGISTRY = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Supernatural.MODID);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<SoulgemData>> SOULGEM = register("soulgem", (stuffs) -> {return stuffs.persistent(SoulgemData.CODEC).networkSynchronized(SoulgemData.STREAM_CODEC).cacheEncoding();});
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<RitualBookData>> BOOK = register("ritual_book", (stuffs) -> {return stuffs.persistent(RitualBookData.CODEC).networkSynchronized(RitualBookData.STREAM_CODEC).cacheEncoding();});
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<RitualCompassData>> COMPASS = register("ritual_compass", (stuffs) -> {return stuffs.persistent(RitualCompassData.CODEC).networkSynchronized(RitualCompassData.STREAM_CODEC).cacheEncoding();});

	private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> stuffs) {
		return REGISTRY.register(name, () -> stuffs.apply(DataComponentType.builder()).build());
	}
}