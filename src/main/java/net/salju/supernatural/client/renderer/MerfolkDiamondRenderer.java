package net.salju.supernatural.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class MerfolkDiamondRenderer extends AbstractMerfolkRenderer {
	public MerfolkDiamondRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

    @Override
    public String getMerfolkType() {
        return "diamond";
    }
}