package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalClient;
import net.salju.supernatural.client.model.ScourgeModel;
import net.salju.supernatural.client.renderer.layers.ScourgeEyesLayer;
import net.salju.supernatural.client.renderer.layers.ScourgeEquipmentLayer;
import net.salju.supernatural.entity.Scourge;
import net.minecraft.client.model.animal.equine.EquineSaddleModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;

public class ScourgeRenderer extends AbstractHorseRenderer<Scourge, EquineRenderState, ScourgeModel<EquineRenderState>> {
	public ScourgeRenderer(EntityRendererProvider.Context context) {
		super(context, new ScourgeModel<>(context.bakeLayer(SupernaturalClient.SCOURGE)), new ScourgeModel<>(context.bakeLayer(SupernaturalClient.SCOURGE)));
        this.addLayer(new ScourgeEquipmentLayer<>(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.HORSE_BODY, (state) -> state.bodyArmorItem, new ScourgeModel(context.bakeLayer(SupernaturalClient.SCOURGE_ARMOR))));
        this.addLayer(new ScourgeEquipmentLayer<>(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.HORSE_SADDLE, (state) -> state.saddle, new EquineSaddleModel(context.bakeLayer(ModelLayers.SKELETON_HORSE_SADDLE))));
        this.addLayer(new ScourgeEyesLayer<>(this));
	}

    @Override
    public EquineRenderState createRenderState() {
        return new EquineRenderState();
    }

    @Override
    public void extractRenderState(Scourge target, EquineRenderState state, float f1) {
        super.extractRenderState(target, state, f1);
        state.bodyArmorItem = target.getBodyArmorItem().copy();
    }

	@Override
	public Identifier getTextureLocation(EquineRenderState target) {
		return Identifier.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/scourge/scourge.png");
	}
}