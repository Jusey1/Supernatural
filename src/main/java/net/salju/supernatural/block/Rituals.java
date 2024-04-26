package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.entity.Angel;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import java.util.List;
import com.google.common.collect.Lists;

public class Rituals {
	public static void doRitual(ItemStack stack, ItemStack offer, ServerLevel lvl, Player player, BlockPos pos) {
		if (lvl.getBlockEntity(pos) instanceof RitualBlockEntity target) {
			BlockPos top = BlockPos.containing((pos.getX() + 3), (pos.getY() - 1), (pos.getZ() + 3));
			BlockPos bot = BlockPos.containing((pos.getX() - 3), (pos.getY() - 1), (pos.getZ() - 3));
			if (lvl.getBrightness(LightLayer.BLOCK, pos) < 6 && (lvl.getBrightness(LightLayer.SKY, pos) < 6 || !lvl.isDay())) {
				int i = SupernaturalManager.getPower(lvl, pos);
				int e = SupernaturalManager.getSoulLevel(SupernaturalManager.getSoulgem(offer));
				if (i == 28 && e >= 5 && stack.is(Items.TOTEM_OF_UNDYING) && SupernaturalManager.isVampire(player)) {
					defaultResult(target, offer, lvl, player, pos);
					List<Item> list = getContracts();
					int r = Mth.nextInt(lvl.random, 0, (list.size() - 1));
					target.setItem(0, new ItemStack(list.get(r)));
				} else if (i == 20 && e >= 4 && stack.is(Items.GLASS_BOTTLE) && SupernaturalManager.isVampire(player)) {
					defaultResult(target, offer, lvl, player, pos);
					Player randy = getRandomPlayer(lvl, (ServerPlayer) player);
					target.setItem(0, SupernaturalManager.setUUID(new ItemStack(SupernaturalItems.PLAYER_BLOOD.get()), randy));
				} else if (i == 12 && e >= 3 && stack.is(Items.IRON_INGOT)) {
					defaultResult(target, offer, lvl, player, pos);
					target.setItem(0, new ItemStack(Items.GOLD_INGOT));
				} else if (i == 12 && e >= 2 && stack.is(Items.COPPER_INGOT)) {
					defaultResult(target, offer, lvl, player, pos);
					target.setItem(0, new ItemStack(Items.IRON_INGOT));
				} else if (i == 4 && e >= 1 && stack.is(SupernaturalItems.VAMPIRE_DUST.get())) {
					defaultResult(target, offer, lvl, player, pos);
					for (BlockPos poz : BlockPos.betweenClosed(top, bot)) {
						if (lvl.getBlockState(poz).is(Blocks.SOUL_SAND) || lvl.getBlockState(poz).is(Blocks.SOUL_SOIL)) {
							lvl.setBlock(poz, SupernaturalBlocks.GRAVE_SOIL.get().defaultBlockState(), 3);
						}
					}
				} else {
					target.dropItem(0);
				}
			} else {
				target.dropItem(0);
			}
		}
	}

	private static void defaultResult(RitualBlockEntity target, ItemStack stack, ServerLevel lvl, Player player, BlockPos pos) {
		target.clearContent();
		lvl.playSound(null, pos, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
		lvl.sendParticles(ParticleTypes.SOUL, (pos.getX() + 0.5), (pos.getY() + 0.5), (pos.getZ() + 0.5), 21, 3, 1, 3, 0);
		if (!player.isCreative()) {
			stack.shrink(1);
		}
		for (Angel statue : lvl.getEntitiesOfClass(Angel.class, target.getRenderBoundingBox().inflate(64.85D))) {
			if (Mth.nextInt(lvl.getRandom(), 0, 25) >= 24 && !statue.isCursed()) {
				statue.getEntityData().set(Angel.CURSED, true);
				lvl.sendParticles(ParticleTypes.SOUL, (statue.getX() + 0.5), (statue.getY() + 0.5), (statue.getZ() + 0.5), 8, 0.25, 0.35, 0.25, 0);
			}
		}
	}

	private static ServerPlayer getRandomPlayer(ServerLevel lvl, ServerPlayer ply) {
		List<ServerPlayer> list = lvl.getPlayers(LivingEntity::isAlive);
		List<ServerPlayer> hit = Lists.newArrayList();
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).hasEffect(SupernaturalEffects.SUPERNATURAL.get())) {
				hit.add(list.get(i));
			}
		}
		return hit.isEmpty() ? ply : hit.get(lvl.random.nextInt(hit.size()));
	}

	private static List<Item> getContracts() {
		List<Item> list = Lists.newArrayList();
		list.add(SupernaturalItems.VEXATION_CONTRACT.get());
		list.add(SupernaturalItems.MISFORTUNE_CONTRACT.get());
		list.add(SupernaturalItems.PUMPKIN_CONTRACT.get());
		list.add(SupernaturalItems.REANIMATE_CONTRACT.get());
		list.add(SupernaturalItems.KNOWLEDGE_CONTRACT.get());
		list.add(SupernaturalItems.FORTUNE_CONTRACT.get());
		return list;
	}
}