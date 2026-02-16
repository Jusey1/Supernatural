package net.salju.supernatural.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.salju.supernatural.block.entity.RitualAltarEntity;

public class RitualEvent extends Event {
    private final ItemStack stack;
    private final ServerLevel lvl;
    private final Player player;
    private final BlockPos pos;
    private final RitualAltarEntity target;
    private final int power;
    private final int soulLevel;
    private boolean check = false;

	public RitualEvent(ItemStack stack, ServerLevel lvl, Player player, BlockPos pos, RitualAltarEntity target, int i, int e) {
        this.stack = stack;
        this.lvl = lvl;
        this.player = player;
        this.pos = pos;
        this.target = target;
        this.power = i;
        this.soulLevel = e;
    }

    public ItemStack getRitualItem() {
        return this.stack;
    }

    public Player getRitualPlayer() {
        return this.player;
    }

    public BlockPos getRitualPosition() {
        return this.pos;
    }

    public RitualAltarEntity getRitualAltar() {
        return this.target;
    }

    public int getPower() {
        return this.power;
    }

    public int getSoulLevel() {
        return this.soulLevel;
    }

    public ServerLevel getServerLevel() {
        return this.lvl;
    }

    public boolean isRitualSuccessful() {
        return this.check;
    }

    public void setRitualSuccess(boolean target) {
        this.check = target;
    }
}