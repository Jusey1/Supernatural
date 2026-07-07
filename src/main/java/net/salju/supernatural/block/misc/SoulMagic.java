package net.salju.supernatural.block.misc;

import net.salju.supernatural.crafting.RitualRecipe;
import net.salju.supernatural.crafting.RitualRecipeInput;
import net.salju.supernatural.init.*;
import net.salju.supernatural.events.RitualEvent;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.block.entity.RitualAltarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Spawner;
import javax.annotation.Nullable;

public class SoulMagic {
	public static void performRitual(ItemStack stack, ItemStack offer, ServerLevel lvl, Player player, BlockPos pos) {
		if (lvl.getBlockEntity(pos) instanceof RitualAltarEntity target) {
			if (SupernaturalManager.canSoulMagicWork(lvl, pos)) {
				int i = SupernaturalManager.getPower(lvl, pos, player);
				int e = SupernaturalManager.getSoulLevel(SupernaturalManager.getSoulgem(offer));
                RitualRecipe recipe = getMatchingRecipe(stack, player, lvl, i, e, lvl.getBlockState(pos.above()));
                if (recipe != null) {
                    ItemStack copy = recipe.getResult(stack, offer);
                    RitualManager.defaultResult(target, offer, lvl, player, pos);
                    if (recipe.getResult().getItem() instanceof BlockItem blok && recipe.getBlockItem().is(lvl.getBlockState(pos.above()).getBlock().asItem())) {
                        lvl.setBlock(pos.above(), blok.getBlock().defaultBlockState(), 3);
                        if (copy.is(SupernaturalItems.SOULGEM)) {
                            Entity entity = EntityType.loadEntityRecursive(SupernaturalManager.getSoulTag(copy), lvl, EntitySpawnReason.MOB_SUMMONED, o -> o);
                            if (lvl.getBlockEntity(pos.above()) instanceof Spawner spawner && entity != null && entity.getType().is(SupernaturalTags.SPAWNER)) {
                                spawner.setEntityId(entity.getType(), lvl.getRandom());
                            }
                        }
                    } else if (copy.is(SupernaturalItems.EBONSTEEL_MIRROR)) {
                        target.setItem(0, RitualManager.createMirror(new ItemStack(recipe.getInput().getValues().get(0).value()), copy, lvl, player, pos));
                    } else if (copy.is(SupernaturalItems.COMPASS)) {
                        target.setItem(0, RitualManager.getRitualCompass(pos, lvl, recipe.getPower() / 12));
                    } else if (copy.is(SupernaturalItems.SOULGEM)) {
                        Entity entity = EntityType.loadEntityRecursive(SupernaturalManager.getSoulTag(copy), lvl, EntitySpawnReason.MOB_SUMMONED, o -> o);
                        if (entity != null) {
                            RitualManager.summonEntity(entity, player, lvl, BlockPos.containing(pos.above().getX() + 0.5, pos.above().getY() + 0.75, pos.above().getZ() + 0.5), pos.above());
                        }
                    } else {
                        target.setItem(0, copy);
                    }
                    if (!player.isCreative() && !recipe.getOffhandItem().isEmpty()) {
                        player.getOffhandItem().shrink(1);
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

    @Nullable
    public static RitualRecipe getMatchingRecipe(ItemStack stack, Player player, ServerLevel lvl, int i, int e, BlockState state) {
        return RecipeManager.createCheck(SupernaturalRecipes.RITUAL.get()).getRecipeFor(new RitualRecipeInput(stack, player.getOffhandItem(), i, e, state), lvl).map(RecipeHolder::value).orElse(null);
    }
}