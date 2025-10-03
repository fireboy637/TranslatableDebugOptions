package com.fireboy637.translatabledebugoptions.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.hud.debug.DebugHudEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import static com.fireboy637.translatabledebugoptions.client.TranslatableDebugOptions.LOGGER;
import static com.fireboy637.translatabledebugoptions.client.TranslatableDebugOptions.missingTranslations;

import java.util.Map;

public interface DebugOptionScreenMixin {
    // Make getPath() return translated string. That's all.
    // Though no code here uses Fabric API, we still need to install it, or we can only see the translation keys.
    @Mixin(targets = "net/minecraft/client/gui/screen/DebugOptionsScreen$Entry")
    abstract class EntryMixin {
        // Need to use @WarpOperation instead of @Redirect, or it will be broken by Fabric API.
        @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;getPath()Ljava/lang/String;"))
        private String entryLabelHandler(Identifier identifier, Operation<String> original) {
            String key = "debug." + identifier.getNamespace() + "." + identifier.getPath();
            Text translatedText = Text.translatable(key);
            String translatedString = translatedText.getString();
            if(translatedText.getString().contains(key) && !missingTranslations.contains(key)) {
                LOGGER.warn("Missing translation for key: {}", key);
                // Prevent logging the same missing translation multiple times
                missingTranslations.add(key);
            }
            return translatedString;
        }
    }

    @Mixin(targets = "net/minecraft/client/gui/screen/DebugOptionsScreen$OptionsListWidget")
    abstract class OptionsListWidgetMixin {
        @WrapOperation(method = "fillEntries", at = @At(value = "INVOKE", target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"))
        private boolean searchHandler(String originalString, CharSequence searchCharSequence, Operation<Boolean> original, @Local Map.Entry<Identifier, DebugHudEntry> entry) {
            Identifier identifier = entry.getKey();
            String searchStringLower = searchCharSequence.toString().toLowerCase();
            String translatedStringLower = Text.translatable("debug." + identifier.getNamespace() + "." + identifier.getPath()).getString().toLowerCase();
            return original.call(originalString.toLowerCase(), searchStringLower) || original.call(translatedStringLower, searchStringLower);
        }
    }
}