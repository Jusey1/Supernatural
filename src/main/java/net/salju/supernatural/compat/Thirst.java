package net.salju.supernatural.compat;

import net.salju.supernatural.init.SupernaturalItems;
import dev.ghen.thirst.content.thirst.PlayerThirst;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Thirst  {
    public static void vampireBite(Player player) {
        PlayerThirst.drink(new ItemStack(SupernaturalItems.BLOOD.get()), player);
    }
}