package net.salju.supernatural.item.component;

import net.minecraft.core.GlobalPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import javax.annotation.Nullable;
import java.util.Optional;

public record RitualCompassData(Optional<GlobalPos> target, String type) {
	public static final Codec<RitualCompassData> CODEC = RecordCodecBuilder.create(codec -> codec.group(GlobalPos.CODEC.optionalFieldOf("target").forGetter(RitualCompassData::target), Codec.STRING.fieldOf("type").forGetter(RitualCompassData::type)).apply(codec, RitualCompassData::new));
	public static final StreamCodec<ByteBuf, RitualCompassData> STREAM_CODEC = StreamCodec.composite(GlobalPos.STREAM_CODEC.apply(ByteBufCodecs::optional), RitualCompassData::target, ByteBufCodecs.STRING_UTF8, RitualCompassData::type, RitualCompassData::new);

	@Nullable
	public GlobalPos getGlobalPos() {
		return this.target.orElse(null);
	}

	public String getPower() {
		return this.type;
	}
}