package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.entity.Angel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EntityType;
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
		if (context.getClickedFace() == Direction.DOWN) {
			return InteractionResult.FAIL;
		} else {
			if (context.getLevel() instanceof ServerLevel lvl) {
				BlockPos pos = new BlockPlaceContext(context).getClickedPos();
				AABB nums = SupernaturalMobs.ANGEL.get().getDimensions().makeBoundingBox(Vec3.atBottomCenterOf(pos).x(), Vec3.atBottomCenterOf(pos).y(), Vec3.atBottomCenterOf(pos).z());
				Consumer<Angel> consumer = EntityType.createDefaultStackConfig(lvl, context.getItemInHand(), context.getPlayer());
				Angel target = SupernaturalMobs.ANGEL.get().create(lvl, context.getItemInHand().getTag(), consumer, pos, MobSpawnType.SPAWN_EGG, true, true);
				if (target != null && lvl.noCollision(target, nums) && lvl.getEntities(target, nums).isEmpty()) {
					target.moveTo(target.getX(), target.getY(), target.getZ());
					float rot = (float) Mth.floor((Mth.wrapDegrees(context.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
					target.setYRot(rot);
					target.setYBodyRot(rot);
					target.setYHeadRot(rot);
					target.yRotO = rot;
					target.yBodyRotO = rot;
					target.yHeadRotO = rot;
					lvl.addFreshEntityWithPassengers(target);
					lvl.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
					target.gameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());
					context.getItemInHand().shrink(1);
					context.getPlayer().swing(context.getHand(), true);
					return InteractionResult.SUCCESS;
				}
			}
			return InteractionResult.PASS;
		}
	}
}