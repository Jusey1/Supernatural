package net.salju.supernatural.client.renderer;

import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.block.EbonsteelWeaponBlock;
import net.salju.supernatural.block.entity.WeaponEntity;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

public class WeaponDisplayRenderer implements BlockEntityRenderer<WeaponEntity, SupernaturalBlockState> {
	private final ItemModelResolver item;

	public WeaponDisplayRenderer(BlockEntityRendererProvider.Context context) {
		this.item = context.itemModelResolver();
	}

	@Override
	public SupernaturalBlockState createRenderState() {
		return new SupernaturalBlockState();
	}

	@Override
	public void extractRenderState(WeaponEntity target, SupernaturalBlockState state, float f1, Vec3 v, ModelFeatureRenderer.CrumblingOverlay progress) {
		BlockEntityRenderState.extractBase(target, state, progress);
		state.main = f1;
		state.item = target.getItem(0);
		state.time = target.getLevel().getGameTime();
		this.item.updateForTopItem(state.itemState, state.item, state.item.is(ItemTags.SPEARS) || state.item.is(Items.SHIELD) ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND : ItemDisplayContext.FIXED, target.getLevel(), null, 0);
		if (target.getLevel() != null && target.getLevel().getBlockState(target.getBlockPos()).is(SupernaturalBlocks.EBONSTEEL_WEAPON_DISPLAY)) {
			state.direction = target.getLevel().getBlockState(target.getBlockPos()).getValue(EbonsteelWeaponBlock.FACING);
			state.check = this.isClearForSpear(target.getLevel(), target.getBlockPos());
		}
	}

	@Override
	public void submit(SupernaturalBlockState state, PoseStack pose, SubmitNodeCollector buffer, CameraRenderState c) {
		if (state.direction != null) {
			if (state.item.is(Items.SHIELD)) {
				this.renderShieldItem(state, pose, buffer);
			}else if (state.item.is(ItemTags.SPEARS)) {
				this.renderSpearItem(state, pose, buffer);
			} else {
				this.renderItem(state, pose, buffer);
			}
		}
	}

	public void renderItem(SupernaturalBlockState state, PoseStack pose, SubmitNodeCollector buffer) {
		pose.pushPose();
		pose.translate(this.getX(state.item, state.direction), this.getY(state.item), this.getZ(state.item, state.direction));
		pose.mulPose(Axis.YN.rotationDegrees(this.getYRotation(state.direction)));
		pose.mulPose(Axis.ZN.rotationDegrees(this.getZRotation(state.item)));
		state.itemState.submit(pose, buffer, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
		pose.popPose();
	}

	public void renderSpearItem(SupernaturalBlockState state, PoseStack pose, SubmitNodeCollector buffer) {
		pose.pushPose();
		pose.translate(this.getX(state.item, state.direction), state.check ? -0.15F : 0.15F, this.getZ(state.item, state.direction));
		pose.mulPose(Axis.YN.rotationDegrees(this.getYRotation(state.direction) + 90.0F));
		state.itemState.submit(pose, buffer, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
		pose.popPose();
	}

	public void renderShieldItem(SupernaturalBlockState state, PoseStack pose, SubmitNodeCollector buffer) {
		pose.pushPose();
		pose.translate(this.getX(state.item, state.direction), 0.625F, this.getZ(state.item, state.direction));
		pose.mulPose(Axis.YN.rotationDegrees(this.getYRotation(state.direction) + 90.0F));
		state.itemState.submit(pose, buffer, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
		pose.popPose();
	}

	private float getX(ItemStack stack, Direction dir) {
		if (dir.equals(Direction.EAST)) {
			return stack.is(Items.SHIELD) ? 0.15F : 0.05F;
		} else if (dir.equals(Direction.WEST)) {
			return stack.is(Items.SHIELD) ? 0.85F : 0.95F;
		}
		return this.getPositionByItem(stack, dir);
	}

	private float getY(ItemStack stack) {
		if (stack.is(SupernaturalItems.EBONSTEEL_MIRROR)) {
			return 0.76F;
		} else if (stack.is(Items.BRUSH) || stack.is(Items.SPYGLASS)) {
			return 0.62F;
		} else if (stack.is(Items.MACE) || stack.getItem().getDescriptionId().contains("goblet") || stack.getItem().getDescriptionId().contains("supplementaries.key")) {
			return 0.54F;
		}
		return 0.5F;
	}

	private float getZ(ItemStack stack, Direction dir) {
		if (dir.equals(Direction.SOUTH)) {
			return stack.is(Items.SHIELD) ? 0.15F : 0.05F;
		} else if (dir.equals(Direction.NORTH)) {
			return stack.is(Items.SHIELD) ? 0.85F : 0.95F;
		}
		return this.getPositionByItem(stack, dir);
	}

	private float getPositionByItem(ItemStack stack, Direction dir) {
		if (stack.is(ItemTags.MINING_ENCHANTABLE) && !stack.is(Items.BRUSH)) {
			return this.isOpposite(dir) ? 0.54F : 0.46F;
		} else if (stack.is(Items.BOW)) {
			return this.isOpposite(dir) ? 0.3F : 0.7F;
		} else if (stack.is(Items.SHIELD)) {
			return this.isOpposite(dir) ? 0.75F : 0.25F;
		} else if (stack.is(ItemTags.SPEARS)) {
			return this.isOpposite(dir) ? 0.62F : 0.38F;
		} else if (stack.getItem().getDescriptionId().contains("supplementaries") || stack.getItem().getDescriptionId().contains("suppsquared")) {
			return this.getPositionBySupplmentariesItem(stack, dir);
		}
		return 0.5F;
	}

	private float getPositionBySupplmentariesItem(ItemStack stack, Direction dir) {
		if (stack.getItem().getDescriptionId().contains("key")) {
			return this.isOpposite(dir) ? 0.47F : 0.53F;
		} else if (stack.getItem().getDescriptionId().contains("goblet") || stack.getItem().getDescriptionId().contains("slingshot")) {
			return this.isOpposite(dir) ? 0.53F : 0.47F;
		}
		return 0.5F;
	}

	private float getYRotation(Direction dir) {
		if (dir.equals(Direction.NORTH)) {
			return 180.0F;
		} else if (dir.equals(Direction.EAST)) {
			return 270.0F;
		} else if (dir.equals(Direction.WEST)) {
			return 90.0F;
		}
		return 0.0F;
	}

	private float getZRotation(ItemStack stack) {
		if (stack.is(ItemTags.SWORDS)) {
			return 225.0F;
		} else if (stack.is(Items.CROSSBOW)) {
			return 315.0F;
		}
		return stack.is(SupernaturalTags.DISPLAY_ZERO_ITEMS) ? 0.0F : 45.0F;
	}

	private boolean isClearForSpear(Level world, BlockPos pos) {
		return world.isEmptyBlock(pos.below()) && world.isEmptyBlock(pos.below(2));
	}

	private boolean isOpposite(Direction dir) {
		return dir.equals(Direction.SOUTH) || dir.equals(Direction.WEST);
	}
}