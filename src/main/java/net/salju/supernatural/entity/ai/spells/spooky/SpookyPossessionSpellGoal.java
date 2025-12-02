package net.salju.supernatural.entity.ai.spells.spooky;

import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.entity.ai.spells.AbstractTargetSpellGoal;
import net.salju.supernatural.entity.PossessedArmor;
import net.salju.supernatural.entity.Spooky;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import java.util.Optional;

public class SpookyPossessionSpellGoal extends AbstractTargetSpellGoal {
	public SpookyPossessionSpellGoal(Spooky target) {
		super(target);
	}

	@Override
	protected void performSpellCasting() {
        if (this.hasTarget() && this.user instanceof Spooky ghost) {
            if (this.getTarget() instanceof ArmorStand target) {
                if (SupernaturalManager.hasArmor(target)) {
                    ghost.playSound(SupernaturalSounds.SPOOK_POOF.get(), 1.0F, 1.0F);
                    if (target.level() instanceof ServerLevel lvl) {
                        PossessedArmor armor = SupernaturalManager.convertArmor(target, SupernaturalMobs.POSSESSED_ARMOR.get(), true);
                        if (ghost.getOwner() != null) {
                            ItemStack sword = new ItemStack(Items.IRON_SWORD);
                            EnchantmentHelper.enchantItem(ghost.getRandom(), sword, 32, lvl.registryAccess(), Optional.empty());
                            armor.setOwner(ghost.getOwner());
                            armor.setItemSlot(EquipmentSlot.MAINHAND, sword);
                        }
                        double r = ghost.getRandom().nextGaussian() * 0.02D;
                        lvl.sendParticles(ParticleTypes.POOF, ghost.getRandomX(1.0D), ghost.getRandomY(), ghost.getRandomZ(1.0D), 10, r, r, r, 0.25);
                        ghost.discard();
                    }
                }
            } else if (this.getTarget() instanceof LivingEntity target) {
                target.addEffect(new MobEffectInstance(SupernaturalEffects.POSSESSION, 6000, 0));
                target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 32, 0, false, false));
                if (ghost.level() instanceof ServerLevel lvl) {
                    ghost.playSound(SupernaturalSounds.SPOOK_POOF.get(), 1.0F, 1.0F);
                    ghost.discard();
                    double r = ghost.getRandom().nextGaussian() * 0.02D;
                    lvl.sendParticles(ParticleTypes.POOF, ghost.getRandomX(1.0D), ghost.getRandomY(), ghost.getRandomZ(1.0D), 10, r, r, r, 0.25);
                }
            }
        }
	}

	@Override
	protected int getCastingTime() {
		return 30;
	}

	@Override
	protected int getCastingInterval() {
		return 120;
	}

	@Override
	protected int getSpell() {
		return 0;
	}

	@Override
	protected SoundEvent getSpellPrepareSound() {
		return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
	}
}