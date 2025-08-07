package com.fireboy637.translatabledebugoptions.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net/minecraft/client/gui/screen/DebugOptionsScreen$Entry")
public class DebugOptionScreenMixin {
    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;getPath()Ljava/lang/String;"))
    private String pathToKey(Identifier instance, Operation<String> original) {
        return Text.translatable("debug." + instance.getNamespace() + "." + instance.getPath()).getString();
    }
}
