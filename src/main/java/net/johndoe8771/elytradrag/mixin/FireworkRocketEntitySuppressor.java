package net.johndoe8771.elytradrag.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin (FireworkRocketEntity.class)
public class FireworkRocketEntitySuppressor {

    @Shadow
    private int lifetime;

    @Shadow
    private LivingEntity attachedToEntity;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci)
    {
        if(attachedToEntity != null)
        {
            if(attachedToEntity.isFallFlying() && attachedToEntity.isShiftKeyDown())
                lifetime = 0;
        }
    }
}

