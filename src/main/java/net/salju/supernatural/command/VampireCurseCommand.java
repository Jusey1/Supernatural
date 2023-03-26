package net.salju.supernatural.command;

import org.checkerframework.checker.units.qual.s;

import net.salju.supernatural.procedures.SupernaturalHelpersProcedure;
import net.salju.supernatural.init.SupernaturalModMobEffects;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.nbt.ByteTag;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;

@Mod.EventBusSubscriber
public class VampireCurseCommand {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("vampirism").requires(s -> s.hasPermission(4)).then(Commands.literal("give").then(Commands.argument("give", EntityArgument.players()).executes(arguments -> {
			Entity entity = EntityArgument.getEntity(arguments, "give");
			if (entity instanceof Player target) {
				SupernaturalHelpersProcedure.setVampire(target, true);
				target.removeEffect(SupernaturalModMobEffects.VAMPIRISM.get());
			}
			return 0;
		}))).then(Commands.literal("cure").then(Commands.argument("cure", EntityArgument.players()).executes(arguments -> {
			Entity entity = EntityArgument.getEntity(arguments, "cure");
			if (entity instanceof Player target) {
				SupernaturalHelpersProcedure.setVampire(target, false);
				target.removeEffect(MobEffects.DAMAGE_BOOST);
				target.removeEffect(MobEffects.DIG_SPEED);
				target.removeEffect(MobEffects.MOVEMENT_SPEED);
			}
			return 0;
		}))));
	}
}