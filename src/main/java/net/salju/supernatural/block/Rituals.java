package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.entity.Angel;
import net.salju.supernatural.SupernaturalMod;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.Mob;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import java.util.List;
import com.google.common.collect.Lists;

public class Rituals {
	public static void doRitual(ItemStack stack, ServerLevel lvl, Player player, BlockPos pos) {
		if (lvl.getBlockEntity(pos) instanceof RitualBlockEntity target) {
			ItemStack offer = target.getItem(0);
			BlockPos top = BlockPos.containing((pos.getX() + 3), (pos.getY() - 1), (pos.getZ() + 3));
			BlockPos bot = BlockPos.containing((pos.getX() - 3), (pos.getY() - 1), (pos.getZ() - 3));
			if (offer.is(SupernaturalItems.SOULGEM.get()) && lvl.getBrightness(LightLayer.BLOCK, pos) < 6 && (lvl.getBrightness(LightLayer.SKY, pos) < 6 || !lvl.isDay())) {
				int i = getPower(lvl, pos);
				int e = SupernaturalManager.getSoulLevel(SupernaturalManager.getSoulgem(offer));
				if (i == 28 && e >= 5 && stack.is(Items.TOTEM_OF_UNDYING)) {
					defaultResult(target, stack, lvl, player, pos);
					List<Item> list = getContracts();
					int r = Mth.nextInt(lvl.random, 0, (list.size() - 1));
					target.setItem(0, new ItemStack(list.get(r)));
				} else if (i == 12 && e >= 3 && stack.is(Items.TOTEM_OF_UNDYING)) {
					defaultResult(target, stack, lvl, player, pos);
					List<Mob> mobs = getSacrifice(lvl, target.getRenderBoundingBox().inflate(12.85D));
					Player randy = lvl.getRandomPlayer();
					int a = 0;
					int v = 0;
					for (Mob bob : mobs) {
						if (bob instanceof Animal) {
							a = (a + (int) bob.getHealth());
							bob.hurt(bob.damageSources().magic(), Float.MAX_VALUE);
						} else if (bob instanceof Villager) {
							v = (v + (int) bob.getHealth());
							bob.hurt(bob.damageSources().magic(), Float.MAX_VALUE);
						}
					}
					if (a > 0) {
						a = (a / 10);
						for (int r = 0; r < a; ++r) {
							SupernaturalMod.queueServerWork((r * 10), () -> {
								lvl.sendParticles(ParticleTypes.SOUL, (pos.getX() + 0.5), (pos.getY() + 0.5), (pos.getZ() + 0.5), 4, 0.35, 1, 0.35, 0);
								target.cloneItem(new ItemStack(SupernaturalItems.ANIMAL_BLOOD.get()));
							});
						}
					}
					if (v > 0) {
						v = (v / 10);
						for (int r = 0; r < v; ++r) {
							SupernaturalMod.queueServerWork((r * 10), () -> {
								lvl.sendParticles(ParticleTypes.SOUL, (pos.getX() + 0.5), (pos.getY() + 0.5), (pos.getZ() + 0.5), 4, 0.35, 1, 0.35, 0);
								target.cloneItem(new ItemStack(SupernaturalItems.VILLAGER_BLOOD.get()));
							});
						}
					}
					if (randy != null && !SupernaturalManager.isVampire(randy)) {
						target.cloneItem(SupernaturalManager.setUUID(new ItemStack(SupernaturalItems.PLAYER_BLOOD.get()), randy));
					}
				} else if (i == 4 && e >= 1 && stack.is(SupernaturalItems.VAMPIRE_DUST.get())) {
					defaultResult(target, stack, lvl, player, pos);
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
			}
		}
	}

	private static List<Mob> getSacrifice(ServerLevel lvl, AABB box) {
		List<Mob> list = Lists.newArrayList();
		for (Mob sacrifice : lvl.getEntitiesOfClass(Mob.class, box)) {
			if ((sacrifice.getMaxHealth() <= 10 && sacrifice instanceof Animal) || sacrifice instanceof Villager) {
				list.add(sacrifice);
			}
		}
		return list;
	}

	private static int getPower(ServerLevel lvl, BlockPos pos) {
		int i = 0;
		List<BlockPos> list = getCircle(pos);
		for (BlockPos poz : list) {
			BlockState state = lvl.getBlockState(poz);
			if (state.getBlock() instanceof CandleBlock && state.getValue(CandleBlock.LIT)) {
				i = (i + state.getValue(CandleBlock.CANDLES));
				lvl.setBlock(poz, state.getBlock().defaultBlockState().setValue(CandleBlock.LIT, false).setValue(CandleBlock.CANDLES, state.getValue(CandleBlock.CANDLES)), 3);
			}
		}
		return i;
	}

	private static List<Item> getContracts() {
		List<Item> list = Lists.newArrayList();
		list.add(SupernaturalItems.VAMPIRISM_CONTRACT.get());
		list.add(SupernaturalItems.VEXATION_CONTRACT.get());
		list.add(SupernaturalItems.PUMPKIN_CONTRACT.get());
		list.add(SupernaturalItems.REANIMATE_CONTRACT.get());
		list.add(SupernaturalItems.KNOWLEDGE_CONTRACT.get());
		list.add(SupernaturalItems.FORTUNE_CONTRACT.get());
		return list;
	}

	private static List<BlockPos> getCircle(BlockPos pos) {
		List<BlockPos> list = Lists.newArrayList();
		list.add(pos.north(4));
		list.add(pos.west(4));
		list.add(pos.south(4));
		list.add(pos.east(4));
		list.add(BlockPos.containing((pos.getX() + 3), pos.getY(), (pos.getZ() + 2)));
		list.add(BlockPos.containing((pos.getX() + 3), pos.getY(), (pos.getZ() - 2)));
		list.add(BlockPos.containing((pos.getX() - 3), pos.getY(), (pos.getZ() + 2)));
		list.add(BlockPos.containing((pos.getX() - 3), pos.getY(), (pos.getZ() - 2)));
		list.add(BlockPos.containing((pos.getX() + 2), pos.getY(), (pos.getZ() + 3)));
		list.add(BlockPos.containing((pos.getX() + 2), pos.getY(), (pos.getZ() - 3)));
		list.add(BlockPos.containing((pos.getX() - 2), pos.getY(), (pos.getZ() + 3)));
		list.add(BlockPos.containing((pos.getX() - 2), pos.getY(), (pos.getZ() - 3)));
		return list;
	}
}