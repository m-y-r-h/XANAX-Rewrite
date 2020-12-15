package cat.yoink.xanax.external.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

/**
 * @author yoink
 */
@Mixin(AbstractClientPlayer.class)
public final class AbstractClientPlayerPatch
{
    private final ResourceLocation cape = new ResourceLocation("cape.png");

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    private void getLocationCape(CallbackInfoReturnable<ResourceLocation> cape)
    {
        if (Arrays.asList("94b4b37a-7651-4a21-814f-3d4368e312cc", "c503bbbe-ee32-492f-b400-43749928d1c4", "90e514d5-2c4d-4579-85c7-b98aecfdabd6").contains(((AbstractClientPlayer) (Object) this).getUniqueID().toString())) cape.setReturnValue(this.cape);
    }
}

