package net.salju.supernatural.block.misc;

import net.salju.supernatural.Supernatural;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.List;

public class TreasureVault {
    public static List<ItemStack> getRewards(ServerLevel lvl, BlockPos pos, Player player, String table) {
        return lvl.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(Supernatural.MODID, table))).getRandomItems(new LootParams.Builder(lvl).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player).create(LootContextParamSets.VAULT));
    }

    public static List<ItemStack> getRewards(ServerLevel lvl, BlockPos pos, BlockEntity target, String table) {
        return lvl.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(Supernatural.MODID, table))).getRandomItems(new LootParams.Builder(lvl).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.BLOCK_ENTITY, target).create(LootContextParamSets.VAULT));
    }

    public static void ejectItem(ServerLevel lvl, BlockPos pos, ItemStack stack) {
        DefaultDispenseItemBehavior.spawnItem(lvl, stack, 2, Direction.UP, Vec3.atBottomCenterOf(pos).relative(Direction.UP, 1.2));
    }

	public static void playSound(Level world, BlockPos pos, SoundEvent target) {
        world.playSound(null, pos, target, SoundSource.BLOCKS);
    }
}