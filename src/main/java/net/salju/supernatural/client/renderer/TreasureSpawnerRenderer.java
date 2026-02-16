package net.salju.supernatural.client.renderer;

import net.salju.supernatural.block.entity.TreasureSpawnerBlockEntity;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.SpawnerRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.mojang.blaze3d.vertex.PoseStack;

public class TreasureSpawnerRenderer implements BlockEntityRenderer<TreasureSpawnerBlockEntity, SupernaturalBlockState> {
    private final EntityRenderDispatcher render;

	public TreasureSpawnerRenderer(BlockEntityRendererProvider.Context context) {
        this.render = context.entityRenderer();
	}

    @Override
    public SupernaturalBlockState createRenderState() {
        return new SupernaturalBlockState();
    }

    @Override
    public AABB getRenderBoundingBox(TreasureSpawnerBlockEntity target) {
        return new AABB(target.getBlockPos().getX() - 1.0, target.getBlockPos().getY() - 1.0, target.getBlockPos().getZ() - 1.0, target.getBlockPos().getX() + 2.0, target.getBlockPos().getY() + 2.0, target.getBlockPos().getZ() + 2.0);
    }

    @Override
    public void extractRenderState(TreasureSpawnerBlockEntity target, SupernaturalBlockState state, float f1, Vec3 v, ModelFeatureRenderer.CrumblingOverlay progress) {
        BlockEntityRenderState.extractBase(target, state, progress);
        state.time = target.getLevel().getGameTime();
        state.main = f1;
        if (target.getLevel() != null) {
            Entity mob = target.getSpawner().getOrCreateDisplayEntity(target.getLevel(), target.getBlockPos());
            if (mob != null) {
                state.displayEntity = this.render.extractEntity(mob, f1);
                state.displayEntity.lightCoords = state.lightCoords;
                state.spin = (float) Mth.lerp(f1, target.getSpawner().getoSpin(), target.getSpawner().getSpin()) * 10.0F;
                state.scale = 0.53125F;
                float f = Math.max(mob.getBbWidth(), mob.getBbHeight());
                if (f > 1.0F) {
                    state.scale /= f;
                }
            }
        }
    }

    @Override
    public void submit(SupernaturalBlockState state, PoseStack pose, SubmitNodeCollector buffer, CameraRenderState c) {
        if (state.displayEntity != null) {
            SpawnerRenderer.submitEntityInSpawner(pose, buffer, state.displayEntity, this.render, state.spin, state.scale, c);
        }
    }
}