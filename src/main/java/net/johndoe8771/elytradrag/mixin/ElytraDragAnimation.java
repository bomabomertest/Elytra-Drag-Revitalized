package net.johndoe8771.elytradrag.mixin;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.object.equipment.ElytraModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraModel.class)
public class ElytraDragAnimation {
    @Final
    @Shadow
    private ModelPart rightWing;
    @Unique
    LivingEntity spe;
    @Final
    @Shadow
    private ModelPart leftWing;
    @Unique
    float playerVelocity;
    @Unique
    float playerSpeed = playerVelocity * 20.0f;

    public ElytraDragAnimation() {
        playerVelocity = (float) spe.getDeltaMovement().length();
    }

    /**
     * Scuffed way to handle some kind of animation,
     * it just works™
     */
    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
    private void SetAngles(HumanoidRenderState bipedEntityRenderState, CallbackInfo ci) {
        if (!bipedEntityRenderState.isCrouching || !bipedEntityRenderState.isFallFlying)
            return;
        long FLAPPING_SPEED = 3L;
        float progressCycle = (float)(System.currentTimeMillis() * FLAPPING_SPEED % 1000L) / 1000.0F;
        float progress = ((float)Math.sin(progressCycle * Math.PI * 2.0D) + 1.0F) / 2.0F;
        float animSpeedCoef = GetSpeedAnimationCoef(playerSpeed * 20.0F);
        this.rightWing.yRot = -1.5f * animSpeedCoef;
        this.leftWing.yRot = 1.5f * animSpeedCoef;
        this.rightWing.xRot = progress * (1f - animSpeedCoef);
        this.leftWing.xRot = progress * (1f - animSpeedCoef);

        this.rightWing.zRot = 1f;
        this.leftWing.zRot = -1f;
    }

    @Unique
    private float GetSpeedAnimationCoef(float value) {
        value = Math.max(2.0F, Math.min(45.0F, value));
        return (float)Math.pow(((value - 2.0F) / 45.0F), 0.25D);
    }
}
