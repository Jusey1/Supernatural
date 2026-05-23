package net.salju.supernatural.item.component;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import javax.annotation.Nullable;
import java.util.List;

public record CommanderWandData(int type, GlobalPos target, List<BlockPos> poz) {
    public static final CommanderWandData EMPTY = new CommanderWandData(List.of());
	public static final Codec<CommanderWandData> CODEC = BlockPos.CODEC.listOf().xmap(CommanderWandData::new, b -> b.poz);
	public static final StreamCodec<ByteBuf, CommanderWandData> STREAM_CODEC = BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()).map(CommanderWandData::new, b -> b.poz);

    public CommanderWandData(List<BlockPos> poz) {
        this(null, poz);
    }

    public CommanderWandData(GlobalPos target, List<BlockPos> poz) {
        this(0, target, poz);
    }

	public List<BlockPos> getPlacements() {
		return this.poz;
	}

    @Nullable
    public BlockPos getCommandingPiecePosition() {
        if (this.target != null) {
            return target.pos();
        }
        return null;
    }

    public boolean hasNoValidPlacements() {
        return this.getPlacements().isEmpty();
    }

    public BlockPos getValidPlacement(int i) {
        return this.getPlacements().get(i);
    }

	public int getPromotionType() {
		return this.type;
	}
}