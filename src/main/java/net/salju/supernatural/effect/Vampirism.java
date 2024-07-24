package net.salju.supernatural.effect;

import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class Vampirism extends MobEffect {
	public Vampirism(MobEffectCategory cate, int i) {
		super(cate, i);
	}

	@Override
	public String getDescriptionId() {
		return "effect.supernatural.vampirism";
	}

	@Override
	public boolean isDurationEffectTick(int d, int a) {
		return true;
	}

	@Override
	public void initializeClient(java.util.function.Consumer<IClientMobEffectExtensions> consumer) {
		consumer.accept(new IClientMobEffectExtensions() {
			@Override
			public boolean isVisibleInInventory(MobEffectInstance effect) {
				return false;
			}

			@Override
			public boolean isVisibleInGui(MobEffectInstance effect) {
				return false;
			}
		});
	}
}