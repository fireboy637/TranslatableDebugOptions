package com.fireboy637.translatabledebugoptions.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.hud.debug.DebugHudEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

public abstract class DebugOptionScreenMixin {
    // Make getPath() return translated string. That's all.
    // Though no code here uses Fabric API, but we still need to install it, or we can only see the translation keys.
    @Mixin(targets = "net/minecraft/client/gui/screen/DebugOptionsScreen$Entry")
    public static abstract class DebugOptionScreenEntryMixin {
        @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;getPath()Ljava/lang/String;"))
        private String entryLabelHandler(Identifier identifier, Operation<String> original) {
            return Text.translatable("debug." + identifier.getNamespace() + "." + identifier.getPath()).getString();
        }
    }

    @Mixin(targets = "net/minecraft/client/gui/screen/DebugOptionsScreen$OptionsListWidget")
    public static abstract class DebugOptionScreenOptionsListWidgetMixin {
        @WrapOperation(method = "fillEntries", at = @At(value = "INVOKE", target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"))
        private boolean searchHandler(String string, CharSequence searchCharSequence, Operation<Boolean> original, @Local Map.Entry<Identifier, DebugHudEntry> entry) {
            Identifier identifier = entry.getKey();
            String searchStringLower = searchCharSequence.toString().toLowerCase();
            return original.call(string.toLowerCase(), searchStringLower) ||
                    original.call(Text.translatable("debug." + identifier.getNamespace() + "." + identifier.getPath()).getString().toLowerCase(), searchStringLower);
        }
    }
}