package net.salju.supernatural.block.misc;

import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.salju.supernatural.entity.Thrall;
import net.salju.supernatural.init.*;
import net.salju.supernatural.events.RitualEvent;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.block.entity.RitualAltarEntity;
import net.salju.supernatural.item.SpectralCoreItem;
import net.salju.supernatural.item.component.AnchorballData;
import net.salju.supernatural.item.RitualCompassItem;
import net.salju.supernatural.entity.Scourge;
import net.salju.supernatural.entity.Wight;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.animal.equine.SkeletonHorse;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.Spawner;

public class SoulMagic {
	public static void performRitual(ItemStack stack, ItemStack offer, ServerLevel lvl, Player player, BlockPos pos) {
		if (lvl.getBlockEntity(pos) instanceof RitualAltarEntity target) {
			if (SupernaturalManager.canSoulMagicWork(lvl, pos)) {
				int i = SupernaturalManager.getPower(lvl, pos, player);
				int e = SupernaturalManager.getSoulLevel(SupernaturalManager.getSoulgem(offer));
				if (stack.is(SupernaturalItems.VAMPIRE_DUST.get())) {
					if (i == 8 && e >= 1) {
						RitualManager.defaultResult(target, offer, lvl, player, pos);
						BlockPos top = BlockPos.containing((pos.getX() + 3), (pos.getY() - 1), (pos.getZ() + 3));
						BlockPos bot = BlockPos.containing((pos.getX() - 3), (pos.getY() - 1), (pos.getZ() - 3));
						for (BlockPos poz : BlockPos.betweenClosed(top, bot)) {
							if (lvl.getBlockState(poz).is(SupernaturalTags.SOIL)) {
								lvl.setBlock(poz, SupernaturalBlocks.GRAVE_SOIL.get().defaultBlockState(), 3);
							}
						}
					} else if (i == 32 && e >= 5 && !SupernaturalManager.isVampire(player)) {
                        Mob goat = RitualManager.getSacrifice(lvl, Goat.class, pos);
                        if (goat != null) {
                            RitualManager.defaultResult(target, offer, lvl, player, pos);
                            player.hurt(player.damageSources().generic(), 0.25F);
                            if (player.isAlive()) {
                                player.setHealth(1.0F);
                            }
                            SupernaturalManager.setVampire(player, true);
                            if (SupernaturalConfig.SACRIFICE.get()) {
                                goat.hurt(goat.damageSources().magic(), goat.getMaxHealth() * 100.0F);
                            }
                        }
					}
				} else if (stack.is(SupernaturalTags.INGOTS) && i == 12 && e >= 2) {
					RitualManager.defaultResult(target, offer, lvl, player, pos);
					target.setItem(0, new ItemStack(stack.is(Items.IRON_INGOT) ? Items.GOLD_INGOT : Items.IRON_INGOT));
				} else if (stack.is(SupernaturalTags.HELMS) && i == 16 && e >= 3) {
					ItemStack copy = stack.transmuteCopy(RitualManager.getHelmet(stack.getItem()));
					RitualManager.defaultResult(target, offer, lvl, player, pos);
					target.setItem(0, copy);
				} else if (stack.is(SupernaturalItems.RITUAL_BOOK) && i == 16 && e >= 2) {
					ItemStack copy = stack.copy();
					RitualManager.defaultResult(target, offer, lvl, player, pos);
					target.setItem(0, copy);
				} else if (stack.is(SupernaturalItems.REVENANT_CORE.get())) {
                    if (i == 12 && e >= 3) {
                        ItemStack copy = stack.copy();
                        AnchorballData data = copy.get(SupernaturalData.ANCHOR.get());
                        RitualManager.defaultResult(target, offer, lvl, player, pos);
                        target.setItem(0, copy);
                        if (RitualManager.canTeleportTo(data, lvl)) {
                            lvl.playSound(null, pos, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
                            lvl.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.getX(), pos.getY() + 0.75, pos.getZ(), 12, 0.5, 0.5, 0.5, 0.65);
                            RitualManager.teleportUser(data, player, lvl);
                        }
                    } else if (i == 16 && e >= 4) {
                        ItemStack copy = stack.copy();
                        RitualManager.defaultResult(target, offer, lvl, player, pos);
                        copy.set(SupernaturalData.ANCHOR, new AnchorballData(GlobalPos.of(lvl.dimension(), pos)));
                        target.setItem(0, copy);
                    }
				} else if (stack.is(Items.WRITABLE_BOOK) && i == 12 && e >= 3) {
					RitualManager.defaultResult(target, offer, lvl, player, pos);
					target.setItem(0, new ItemStack(SupernaturalItems.RITUAL_BOOK.get()));
				} else if (stack.is(Items.TOTEM_OF_UNDYING) && i == 42 && e >= 0) {
					Entity entity = EntityType.loadEntityRecursive(SupernaturalManager.getSoulTag(offer), lvl, EntitySpawnReason.LOAD, o -> o);
					if (lvl.getBlockEntity(pos.above()) instanceof Spawner blok && entity != null && entity.getType().is(SupernaturalTags.SPAWNER)) {
						blok.setEntityId(entity.getType(), lvl.getRandom());
						RitualManager.defaultResult(target, offer, lvl, player, pos);
						lvl.sendBlockUpdated(pos.above(), lvl.getBlockState(pos.above()), lvl.getBlockState(pos.above()), 3);
						lvl.gameEvent(player, GameEvent.BLOCK_CHANGE, pos.above());
					}
				} else if (stack.is(SupernaturalItems.COMPASS.get())) {
					if (i == 12 && e >= 3) {
						RitualManager.defaultResult(target, offer, lvl, player, pos);
						target.setItem(0, RitualCompassItem.getRitualCompass(pos, lvl, 0));
					} else if (i == 20 && e >= 4) {
						RitualManager.defaultResult(target, offer, lvl, player, pos);
						target.setItem(0, RitualCompassItem.getRitualCompass(pos, lvl, 1));
					} else if (i == 28 && e >= 5) {
						RitualManager.defaultResult(target, offer, lvl, player, pos);
						target.setItem(0, RitualCompassItem.getRitualCompass(pos, lvl, 2));
					}
				} else if (stack.is(SupernaturalItems.EBONSTEEL_INGOT.get()) && i == 32 && e >= 4 && SupernaturalManager.hasIronArmor(player)) {
                    RitualManager.defaultResult(target, offer, lvl, player, pos);
                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        ItemStack copy = player.getItemBySlot(slot).copy();
                        if (slot.isArmor() && copy.is(SupernaturalTags.IRON_ARMOR)) {
                            player.setItemSlot(slot, copy.transmuteCopy(RitualManager.getEbonsteel(copy.getItem())));
                            lvl.playSound(null, player.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
                            lvl.playSound(null, player.blockPosition(), SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
                            lvl.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, player.getX(), player.getY() + 1.25, player.getZ(), 15, 0.45, 0.25, 0.45, 0);
                            break;
                        }
                    }
                } else if (stack.is(SupernaturalItems.PLASMA.get()) && i == 35 && e >= 4) {
                    Mob bob = RitualManager.getSacrifice(lvl, SupernaturalTags.PLASMA, pos);
                    if (bob != null) {
                        bob.dropPreservedEquipment(lvl);
                        RitualManager.defaultResult(target, offer, lvl, player, pos);
                        lvl.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, bob.getX(), bob.getY() + 1.25, bob.getZ(), 15, 0.45, 0.25, 0.45, 0);
                        if (bob instanceof SkeletonHorse) {
                            Scourge horse = bob.convertTo(SupernaturalMobs.SCOURGE.get(), ConversionParams.single(bob, true, true), newbie -> { EventHooks.onLivingConvert(bob, newbie); });
                            if (horse != null) {
                                horse.setBodyArmorItem(new ItemStack(SupernaturalItems.EBONSTEEL_HORSE_ARMOR.get()));
                                horse.setOwner(player);
                                horse.setTamed(true);
                                horse.setSaddle();
                            }
                        } else if (bob instanceof AbstractSkeleton) {
                            Wight wyght = bob.convertTo(SupernaturalMobs.WIGHT.get(), ConversionParams.single(bob, true, true), newbie -> { EventHooks.onLivingConvert(bob, newbie); });
                            if (wyght != null) {
                                wyght.setOwner(player);
                                wyght.setItemSlot(EquipmentSlot.MAINHAND, wyght.getSecondary());
                            }
                        } else {
                            Thrall zombo = bob.convertTo(SupernaturalMobs.THRALL.get(), ConversionParams.single(bob, true, true), newbie -> { EventHooks.onLivingConvert(bob, newbie); });
                            if (zombo != null) {
                                zombo.setOwner(player);
                            }
                        }
                    }
				} else if (stack.isEnchantable() && RitualManager.getEnchantments(lvl) != null) {
                    int c = e * SupernaturalConfig.SOULPOWER.get() + i;
                    if (player.isCreative() || player.totalExperience >= c * 2) {
                        ItemStack copy = stack.copy();
                        RitualManager.defaultResult(target, offer, lvl, player, pos);
                        target.setItem(0, EnchantmentHelper.enchantItem(lvl.getRandom(), copy, c, RitualManager.getEnchantments(lvl)));
                        lvl.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
                        if (!player.isCreative()) {
                            player.giveExperiencePoints(-c * 2);
                        }
                    }
				} else {
					RitualEvent event = SupernaturalManager.onRitualEvent(stack, lvl, player, pos, target, i, e);
					if (event.isRitualSuccessful()) {
						RitualManager.defaultResult(target, offer, lvl, player, pos);
					} else {
						target.dropItem(0);
					}
				}
			} else {
				target.dropItem(0);
			}
		}
	}
}