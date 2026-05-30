package net.salju.supernatural.client.renderer.layers;

import net.salju.supernatural.Supernatural;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Function;

public class ScourgeEquipmentLayer<S extends EquineRenderState, RM extends EntityModel<? super S>, EM extends EntityModel<? super S>> extends RenderLayer<S, RM> {
    private final EquipmentLayerRenderer render;
    private final EquipmentClientInfo.LayerType layer;
    private final Function<S, ItemStack> stack;
    private final EM model;

	public ScourgeEquipmentLayer(RenderLayerParent<S, RM> parent, EquipmentLayerRenderer equipment, EquipmentClientInfo.LayerType layer, Function<S, ItemStack> stack, EM model) {
		super(parent);
        this.render = equipment;
        this.layer = layer;
        this.stack = stack;
        this.model = model;
	}

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector buffer, int i, S state, float f1, float f2) {
        ItemStack stack = this.stack.apply(state);
        Equippable target = stack.get(DataComponents.EQUIPPABLE);
        if (target != null && target.assetId().isPresent()) {
            this.render.renderLayers(this.layer, getAssetId(target.assetId().get(), stack), this.model, state, stack, pose, buffer, i, null, state.outlineColor, 2);
        }
    }

    public static ResourceKey<EquipmentAsset> getAssetId(ResourceKey<EquipmentAsset> target, ItemStack stack) {
        if (stack.is(Items.SADDLE)) {
            return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Supernatural.MODID, "scourge_saddle"));
        }
        return target;
    }
}