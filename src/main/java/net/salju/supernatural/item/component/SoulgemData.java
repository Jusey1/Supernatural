package net.salju.supernatural.item.component;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;

public record SoulgemData(CompoundTag mobster, String power) {
	public static final SoulgemData EMPTY = new SoulgemData(new CompoundTag(), "soulgem.supernatural.grand");
	public static final Codec<SoulgemData> CODEC = RecordCodecBuilder.create(codec -> codec.group(CompoundTag.CODEC.fieldOf("mobster").forGetter(SoulgemData::mobster), Codec.STRING.fieldOf("power").forGetter(SoulgemData::power)).apply(codec, SoulgemData::new));
	public static final StreamCodec<ByteBuf, SoulgemData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.COMPOUND_TAG, SoulgemData::mobster, ByteBufCodecs.STRING_UTF8, SoulgemData::power, SoulgemData::new);

	public SoulgemData(CompoundTag mobster, String power) {
		this.mobster = mobster;
		this.power = power;
	}

	public CompoundTag getSoul() {
		return this.mobster;
	}

	public String getSoulPower() {
		return this.power;
	}
}