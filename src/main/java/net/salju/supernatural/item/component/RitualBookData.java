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
		map.put(2, "desc.book.helmets");
		map.put(3, "desc.book.ingots");
		map.put(4, "item.supernatural.ritual_compass");
		map.put(5, "item.supernatural.ritual_compass");
		map.put(6, "item.supernatural.ritual_compass");
		map.put(7, "block.supernatural.grave_soil");
		map.put(8, "item.minecraft.totem_of_undying");
		map.put(9, "item.supernatural.vampire_dust");
		map.put(10, "desc.book.money");
		map.put(11, "item.supernatural.core_darkness");
		map.put(12, "item.supernatural.core_darkness");
		return map.getOrDefault(this.page, "item.minecraft.writable_book");
	}

	public String getSoulPower() {
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "soulgem.supernatural.petty");
		map.put(2, "soulgem.supernatural.common");
		map.put(3, "soulgem.supernatural.lesser");
		map.put(4, "soulgem.supernatural.common");
		map.put(5, "soulgem.supernatural.greater");
		map.put(6, "soulgem.supernatural.grand");
		map.put(7, "item.supernatural.soulgem");
		map.put(8, "item.supernatural.soulgem");
		map.put(9, "soulgem.supernatural.greater");
		map.put(10, "soulgem.supernatural.lesser");
		map.put(11, "soulgem.supernatural.greater");
		map.put(12, "soulgem.supernatural.common");
		return map.getOrDefault(this.page, "soulgem.supernatural.common");
	}

	public String getCandles() {
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "VIII");
		map.put(2, "XVI");
		map.put(3, "XII");
		map.put(4, "XII");
		map.put(5, "XX");
		map.put(6, "XXVIII");
		map.put(7, "XXVIII");
		map.put(8, "XXVIII");
		map.put(9, "XVI");
		map.put(10, "XVI");
		map.put(11, "XVI");
		map.put(12, "XII");
		return map.getOrDefault(this.page, "XII");
	}

	public boolean requiresSacrifice() {
		Map<Integer, Boolean> map = new HashMap<>();
		map.put(7, true);
		return map.getOrDefault(this.page, false);
	}

	public int getMaxRituals() {
		return 13;
	}
}