package net.salju.supernatural.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class MerfolkAmethystRenderer extends AbstractMerfolkRenderer {
	public MerfolkAmethystRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

    @Override
    public String getMerfolkType() {
        return "amethyst";
    }
}