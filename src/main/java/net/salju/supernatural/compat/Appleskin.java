package net.salju.supernatural.compat;

import net.salju.supernatural.events.SupernaturalManager;
import squeek.appleskin.api.event.HUDOverlayEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class Appleskin  {
    @SubscribeEvent
    public void onExhaustion(HUDOverlayEvent.Exhaustion event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && SupernaturalManager.hasVampirism(player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onSaturation(HUDOverlayEvent.Saturation event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && SupernaturalManager.hasVampirism(player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onHunger(HUDOverlayEvent.HungerRestored event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && SupernaturalManager.hasVampirism(player)) {
            event.setCanceled(true);
        }
    }
}