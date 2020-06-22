package modfest.lacrimis.mixin;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.client.CustomOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(GameOptions.class)
public abstract class OptionsMixin implements CustomOptions {
    @Mutable
    @Shadow @Final public KeyBinding[] keysAll;

    public KeyBinding keyTarot;

    @Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/client/MinecraftClient;Ljava/io/File;)V")
    public void init(MinecraftClient client, File optionsFile, CallbackInfo ci) {
        keyTarot = new KeyBinding(Lacrimis.MODID + ".key.tarotAbility", 86, "key.categories.gameplay");

        //Copy array
        KeyBinding[] temp = new KeyBinding[keysAll.length + 1];
        System.arraycopy(keysAll, 0, temp, 0, keysAll.length);
        temp[temp.length - 1] = keyTarot;
        keysAll = temp;

        Lacrimis.LOGGER.info("Game options added");
    }

    public KeyBinding getKeyTarot() {
        return keyTarot;
    }
}
