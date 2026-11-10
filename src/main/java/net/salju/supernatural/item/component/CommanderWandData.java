package net.salju.supernatural.item.component;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import javax.annotation.Nullable;
import java.util.Optional;

public record CommanderWandData(int type, Optional<GlobalPos> target) {
    public static final CommanderWandData EMPTY = new CommanderWandData(0, Optional.empty());
	public static final Codec<CommanderWandData> CODEC = RecordCodecBuilder.create(codec -> codec.group(Codec.INT.fieldOf("type").forGetter(CommanderWandData::type), GlobalPos.CODEC.optionalFieldOf("target").forGetter(CommanderWandData::target)).apply(codec, CommanderWandData::new));
	public static final StreamCodec<ByteBuf, CommanderWandData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, CommanderWandData::type, GlobalPos.STREAM_CODEC.apply(ByteBufCodecs::optional), CommanderWandData::target, CommanderWandData::new);

    @Nullable
    public BlockPos getCommandingPiecePosition() {
        if (this.target.isPresent()) {
            return target.get().pos();
        }
        return null;
    }

	public int getPromotionType() {
		return this.type;
	}
}