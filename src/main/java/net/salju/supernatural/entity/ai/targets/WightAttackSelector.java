package net.salju.supernatural.entity.ai.targets;

import net.salju.supernatural.entity.Wight;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class WightAttackSelector implements Predicate<LivingEntity> {
	private final Wight kevin;

	public WightAttackSelector(Wight source) {
		this.kevin = source;
	}

    @Override
	public boolean test(@Nullable LivingEntity target) {
        return target instanceof Player || target instanceof Animal || target instanceof AbstractVillager;
	}
}