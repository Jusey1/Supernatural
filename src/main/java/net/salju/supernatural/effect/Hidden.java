package net.salju.supernatural.effect;

import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class Hidden extends MobEffect {
	private final String descId;

	public Hidden(MobEffectCategory cate, int i, String str) {
		super(cate, i);
		this.descId = str;
	}

	@Override
	public String getDescriptionId() {
		return this.descId;
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