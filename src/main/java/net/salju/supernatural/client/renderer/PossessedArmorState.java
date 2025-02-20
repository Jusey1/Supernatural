package net.salju.supernatural.client.renderer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

@OnlyIn(Dist.CLIENT)
public class PossessedArmorState extends HumanoidRenderState {
	public boolean isAggressive;
	public boolean isLeftHanded;
}