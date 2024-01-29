

import net.salju.supernatural.events.SupernaturalClientManager;


import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;


public class UsedContract {
	private static ItemStack stack;

	public UsedContract(ItemStack item) {
		this.stack = item;
	}

	public static UsedContract reader(FriendlyByteBuf buffer) {
		return new UsedContract(buffer.readItem());
	}

	public static void buffer(UsedContract message, FriendlyByteBuf buffer) {
		buffer.writeItem(stack);
	}

	public static void handler(UsedContract message, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			Player player = SupernaturalClientManager.getPlayer(context.get().getDirection().getReceptionSide());
			if (player != null) {
				SupernaturalClientManager.usedContract(player, stack);
			}
		});
		context.get().setPacketHandled(true);
	}
}