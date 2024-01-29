package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.ChatFormatting;
import java.util.UUID;
import java.util.List;
import java.lang.reflect.Type;

public class ContractItem extends Item {
	private final ContractItem.Type contract;
	private final String name;
	private final Item item;

	public ContractItem(ContractItem.Type type, Item offer, String n, Item.Properties props) {
		super(props);
		this.contract = type;
		this.name = n;
		this.item = offer;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, world, list, flag);
		UUID target = SupernaturalManager.getUUID(stack);
		list.add(Component.translatable(this.name).withStyle(ChatFormatting.DARK_PURPLE));
		if (Screen.hasShiftDown()) {
			list.add(Component.empty());
			if (SupernaturalConfig.CONTRACT.get()) {
				list.add(Component.translatable((this.name + ".desc")).withStyle(ChatFormatting.GRAY));
			} else {
				list.add(Component.translatable(this.item.getDescriptionId(stack)).withStyle(ChatFormatting.GRAY));
				if (this.requiresSacrifice()) {
					list.add(Component.translatable("item.supernatural.contract.sacrifice").withStyle(ChatFormatting.GRAY));
				}
			}
		}
		if (target != null) {
			list.add(Component.literal(world.getPlayerByUUID(target).getName().getString()).withStyle(ChatFormatting.GOLD));
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (SupernaturalManager.getUUID(stack) == null) {
			if (SupernaturalManager.getUUID(player.getOffhandItem()) != null) {
				SupernaturalManager.setUUID(stack, world.getPlayerByUUID(SupernaturalManager.getUUID(player.getOffhandItem())));
				player.playSound(SoundEvents.INK_SAC_USE);
				return InteractionResultHolder.consume(stack);
			} else if (!SupernaturalManager.isVampire(player)) {
				player.hurt(player.damageSources().generic(), 1.0F);
				SupernaturalManager.setUUID(stack, player);
				player.playSound(SoundEvents.INK_SAC_USE);
				return InteractionResultHolder.consume(stack);
			}
		}
		return InteractionResultHolder.fail(stack);
	}

	@Override
	public Component getName(ItemStack stack) {
		return Component.translatable("item.supernatural.contract");
	}

	public boolean requiresSacrifice() {
		return (this.getContractType() == Types.REANIMATE && SupernaturalConfig.REANIMATE.get());
	}

	public Item getRitualItem() {
		return this.item;
	}

	public ContractItem.Type getContractType() {
		return this.contract;
	}

	public interface Type {
	}

	public static enum Types implements ContractItem.Type {
		VAMPIRISM, REANIMATE, VEXATION, PUMPKIN, KNOWLEDGE, FORTUNE;
	}
}