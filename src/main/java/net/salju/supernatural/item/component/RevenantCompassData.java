package net.salju.supernatural.item.component;

import net.minecraft.core.GlobalPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import javax.annotation.Nullable;
import java.util.Optional;

public record RevenantCompassData(Optional<GlobalPos> target, String type) {
	public static final Codec<RevenantCompassData> CODEC = RecordCodecBuilder.create(codec -> codec.group(GlobalPos.CODEC.optionalFieldOf("target").forGetter(RevenantCompassData::target), Codec.STRING.fieldOf("type").forGetter(RevenantCompassData::type)).apply(codec, RevenantCompassData::new));
	public static final StreamCodec<ByteBuf, RevenantCompassData> STREAM_CODEC = StreamCodec.composite(GlobalPos.STREAM_CODEC.apply(ByteBufCodecs::optional), RevenantCompassData::target, ByteBufCodecs.STRING_UTF8, RevenantCompassData::type, RevenantCompassData::new);

	@Nullable
	public GlobalPos getGlobalPos() {
		return this.target.orElse(null);
	}

	public String getPower() {
		return this.type;
	}
}