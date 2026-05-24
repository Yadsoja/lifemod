package net.yadsoja.lifemod.mixin;

import net.minecraft.client.render.GameRenderer;
import net.yadsoja.lifemod.client.FreezeClientState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void lockFov(CallbackInfoReturnable<Double> cir) {

        if (FreezeClientState.isFrozen()) {
            cir.setReturnValue(70.0); // FOV fixe
        }
    }
}