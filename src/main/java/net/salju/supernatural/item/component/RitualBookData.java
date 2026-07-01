package net.salju.supernatural.item.component;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import java.util.Map;

public record RitualBookData(int page) {
	public static final RitualBookData EMPTY = new RitualBookData(0);
	public static final Codec<RitualBookData> CODEC = RecordCodecBuilder.create(codec -> codec.group(Codec.INT.fieldOf("page").forGetter(RitualBookData::page)).apply(codec, RitualBookData::new));
	public static final StreamCodec<ByteBuf, RitualBookData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, RitualBookData::page, RitualBookData::new);

	public int getPage() {
		return this.page;
	}

	public String getRequiredItem() {
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "item.supernatural.vampire_dust");
		map.put(2, "item.supernatural.vampire_dust");
		map.put(3, "desc.book.ingots");
		map.put(4, "item.supernatural.ritual_compass");
		map.put(5, "item.supernatural.ritual_compass");
		map.put(6, "item.supernatural.ritual_compass");
		map.put(7, "item.minecraft.totem_of_undying");
		map.put(8, "item.supernatural.spectral_core");
		map.put(9, "item.supernatural.spectral_core");
		map.put(10, "desc.book.helmets");
		map.put(11, "item.supernatural.ebonsteel_ingot");
		map.put(12, "item.supernatural.plasma");
		map.put(13, "desc.book.enchantable");
        map.put(14, "item.supernatural.grave_soil");
		return map.getOrDefault(this.page, "item.minecraft.writable_book");
	}

	public String getSoulPower() {
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "soulgem.supernatural.petty");
		map.put(2, "soulgem.supernatural.grand");
		map.put(3, "soulgem.supernatural.lesser");
		map.put(4, "soulgem.supernatural.common");
		map.put(5, "soulgem.supernatural.greater");
		map.put(6, "soulgem.supernatural.grand");
		map.put(7, "item.supernatural.soulgem");
		map.put(8, "soulgem.supernatural.greater");
		map.put(9, "soulgem.supernatural.common");
		map.put(10, "soulgem.supernatural.common");
		map.put(11, "soulgem.supernatural.greater");
		map.put(12, "soulgem.supernatural.greater");
		map.put(13, "item.supernatural.soulgem");
        map.put(14, "soulgem.supernatural.lesser");
		return map.getOrDefault(this.page, "soulgem.supernatural.common");
	}

	public String getCandles() {
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "VIII");
		map.put(2, "XXXII");
		map.put(3, "XII");
		map.put(4, "XII");
		map.put(5, "XX");
		map.put(6, "XXVIII");
		map.put(7, "XLII");
		map.put(8, "XVI");
		map.put(9, "XII");
		map.put(10, "XVI");
		map.put(11, "XXXII");
		map.put(12, "XXXV");
		map.put(13, "I+");
        map.put(14, "XXXV");
		return map.getOrDefault(this.page, "XII");
	}

	public String getSacificeTooltip() {
		Map<Integer, String> map = new HashMap<>();
		map.put(2, ".goat");
		map.put(12, ".skeleton");
		return map.getOrDefault(this.page, "");
	}

	public boolean requiresSacrifice() {
		Map<Integer, Boolean> map = new HashMap<>();
		map.put(2, true);
		map.put(12, true);
		return map.getOrDefault(this.page, false);
	}

	public int getMaxRituals() {
		return 15;
	}
}