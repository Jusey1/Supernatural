package net.salju.supernatural.command;

import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.events.SupernaturalHelpers;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;

@Mod.EventBusSubscriber
public class VampireCurseCommand {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("vampirism").requires(s -> s.hasPermission(4)).then(Commands.literal("give").then(Commands.argument("give", EntityArgument.players()).executes(arguments -> {
			Entity entity = EntityArgument.getEntity(arguments, "give");
			if (entity instanceof Player target) {
				SupernaturalHelpers.setVampire(target, true);
				target.removeEffect(SupernaturalEffects.VAMPIRISM.get());
			}
			return 0;
		}))).then(Commands.literal("cure").then(Commands.argument("cure", EntityArgument.players()).executes(arguments -> {
			Entity entity = EntityArgument.getEntity(arguments, "cure");
			if (entity instanceof Player target) {
				SupernaturalHelpers.setVampire(target, false);
			}
			return 0;
		}))));
	}
}