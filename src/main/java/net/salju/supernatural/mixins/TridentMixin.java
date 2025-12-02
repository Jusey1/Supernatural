package net.salju.supernatural.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import net.salju.supernatural.entity.AbstractMerfolkEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;

@Mixin(ThrownTrident.class)
public abstract class TridentMixin {
	@Inject(method = "isAcceptibleReturnOwner", at = @At("HEAD"), cancellable = true)
	private void checkOwner(CallbackInfoReturnable<Boolean> ci) {
		ThrownTrident trident = (ThrownTrident) (Object) this;
        if (trident.getOwner() != null && trident.getOwner().isAlive() && trident.getOwner() instanceof AbstractMerfolkEntity) {
            ci.setReturnValue(true);
        }
	}
}
