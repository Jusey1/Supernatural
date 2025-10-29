package net.salju.supernatural.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.salju.supernatural.entity.AbstractMerfolkEntity;

public class MerfolkMoveControl extends MoveControl {
	public final AbstractMerfolkEntity merfolk;

	public MerfolkMoveControl(AbstractMerfolkEntity target) {
		super(target);
		this.merfolk = target;
	}

	@Override
	public void tick() {
        if (this.operation == Operation.MOVE_TO && !this.merfolk.getNavigation().isDone()) {
            float f = (float)(this.speedModifier * this.merfolk.getAttributeValue(Attributes.MOVEMENT_SPEED));
            this.merfolk.setSpeed(Mth.lerp(0.125F, this.merfolk.getSpeed(), f));
            double x = this.wantedX - this.merfolk.getX();
            double y = this.wantedY - this.merfolk.getY();
            double z = this.wantedZ - this.merfolk.getZ();
            double w = Math.sqrt(x * x + y * y + z * z);
            if (y != 0.0) {
                this.merfolk.setDeltaMovement(this.merfolk.getDeltaMovement().add(0.0, this.merfolk.getSpeed() * (y / w) * (this.merfolk.isAggressive() ? 0.06 : 0.035), 0.0));
            }
            if (x != 0.0 || z != 0.0) {
                float f1 = (float) (Mth.atan2(z, x) * (double) 180.0F / (double) (float) Math.PI) - 90.0F;
                this.merfolk.setYRot(this.rotlerp(this.merfolk.getYRot(), f1, 90.0F));
                this.merfolk.yBodyRot = this.merfolk.getYRot();
                this.merfolk.setDeltaMovement(this.merfolk.getDeltaMovement().add(this.merfolk.getSpeed() * x / w * (this.merfolk.isAggressive() ? 0.06 : 0.035), 0.0D, this.merfolk.getSpeed() * z / w * (this.merfolk.isAggressive() ? 0.06 : 0.035)));
            }
        } else {
            this.merfolk.setSpeed(0.0F);
        }
	}
}