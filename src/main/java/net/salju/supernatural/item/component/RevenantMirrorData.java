package net.salju.supernatural.item.component;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;

public record RevenantMirrorData(GlobalPos target) {
	public static final Codec<RevenantMirrorData> CODEC = RecordCodecBuilder.create(codec -> codec.group(GlobalPos.CODEC.fieldOf("target").forGetter(RevenantMirrorData::target)).apply(codec, RevenantMirrorData::new));
	public static final StreamCodec<ByteBuf, RevenantMirrorData> STREAM_CODEC = StreamCodec.composite(GlobalPos.STREAM_CODEC, RevenantMirrorData::target, RevenantMirrorData::new);

	public BlockPos getPos() {
		return this.target.pos();
	}

    public ResourceKey<Level> getDimension() {
        return this.target.dimension();
    }
}