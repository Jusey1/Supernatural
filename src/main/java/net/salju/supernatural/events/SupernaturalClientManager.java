package net.salju.supernatural.events;

import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import javax.annotation.Nullable;

public class SupernaturalClientManager {
	@Nullable
	public static Player getPlayer(LogicalSide side) {
		if (side.isClient()) {
			return Minecraft.getInstance().player;
		} else {
			return null;
		}
	}

	public static void usedContract(Player player, ItemStack stack) {
		Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
	}
}