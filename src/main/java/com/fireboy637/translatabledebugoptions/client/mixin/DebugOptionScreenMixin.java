package com.fireboy637.translatabledebugoptions.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

public abstract class DebugOptionScreenMixin {
    // Make getPath() return translated string. That's all.
    // But for some reason, translation file can't be applied in dev environment.
    @Mixin(targets = "net/minecraft/client/gui/screen/DebugOptionsScreen$Entry")
    public static abstract class DebugOptionScreenEntryMixin {
        @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;getPath()Ljava/lang/String;"))
        private String pathToKey(Identifier identifier, Operation<String> original) {
            return Text.translatable("debug." + identifier.getNamespace() + "." + identifier.getPath()).getString();
        }
    }

    @Mixin(targets = "net/minecraft/client/gui/screen/DebugOptionsScreen$OptionsListWidget")
    public static abstract class DebugOptionScreenOptionsListWidgetMixin {
        @WrapOperation(method = "fillEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;getPath()Ljava/lang/String;"))
        private String searchPathToKey(Identifier identifier, Operation<String> operation) {
            return Text.translatable("debug." + identifier.getNamespace() + "." + identifier.getPath()).getString();
        }
    }
}