package net.salju.supernatural.client.renderer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

@OnlyIn(Dist.CLIENT)
public class AngelState extends HumanoidRenderState {
	public int angelPose;

	public int getAngelPose() {
		return angelPose;
	}
}