package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.entity.Angel;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResult;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import java.util.function.Consumer;

public class AngelItem extends Item {
	public AngelItem(Item.Properties props) {
		super(props);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Direction direct = context.getClickedFace();
		if (direct == Direction.DOWN) {
			return InteractionResult.FAIL;
		} else {
			Level world = context.getLevel();
			BlockPlaceContext place = new BlockPlaceContext(context);
			BlockPos pos = place.getClickedPos();
			ItemStack stack = context.getItemInHand();
			Vec3 veccy = Vec3.atBottomCenterOf(pos);
			AABB nums = SupernaturalMobs.ANGEL.get().getDimensions().makeBoundingBox(veccy.x(), veccy.y(), veccy.z());
			if (world.noCollision((Entity) null, nums) && world.getEntities((Entity) null, nums).isEmpty()) {
				if (world instanceof ServerLevel server) {
					Consumer<Angel> consumer = EntityType.createDefaultStackConfig(server, stack, context.getPlayer());
					Angel angel = SupernaturalMobs.ANGEL.get().create(server, stack.getTag(), consumer, pos, MobSpawnType.SPAWN_EGG, true, true);
					if (angel == null) {
						return InteractionResult.FAIL;
					}
					angel.moveTo(angel.getX(), angel.getY(), angel.getZ());
					float rot = (float) Mth.floor((Mth.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
					angel.setYRot(rot);
					angel.setYBodyRot(rot);
					angel.setYHeadRot(rot);
					angel.yRotO = rot;
					angel.yBodyRotO = rot;
					angel.yHeadRotO = rot;
					server.addFreshEntityWithPassengers(angel);
					world.playSound((Player) null, angel.getX(), angel.getY(), angel.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
					angel.gameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());
				}
				stack.shrink(1);
				return InteractionResult.sidedSuccess(world.isClientSide);
			} else {
				return InteractionResult.FAIL;
			}
		}
	}
}