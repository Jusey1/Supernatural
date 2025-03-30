package net.salju.supernatural.item.component;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;

public record AnchorballData(GlobalPos target) {
	public static final Codec<AnchorballData> CODEC = RecordCodecBuilder.create(codec -> codec.group(GlobalPos.CODEC.fieldOf("target").forGetter(AnchorballData::target)).apply(codec, AnchorballData::new));
	public static final StreamCodec<ByteBuf, AnchorballData> STREAM_CODEC = StreamCodec.composite(GlobalPos.STREAM_CODEC, AnchorballData::target, AnchorballData::new);

	public AnchorballData(GlobalPos target) {
		this.target = target;
	}

	public BlockPos getPos() {
		return this.target.pos();
	}
}