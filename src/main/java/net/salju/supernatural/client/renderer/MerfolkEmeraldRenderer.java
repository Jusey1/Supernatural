package net.salju.supernatural.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class MerfolkEmeraldRenderer extends AbstractMerfolkRenderer {
	public MerfolkEmeraldRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

    @Override
    public String getMerfolkType() {
        return "emerald";
    }
}