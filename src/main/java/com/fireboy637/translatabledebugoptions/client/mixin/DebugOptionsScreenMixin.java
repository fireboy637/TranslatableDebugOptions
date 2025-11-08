package com.fireboy637.translatabledebugoptions.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

import static com.fireboy637.translatabledebugoptions.client.TranslatableDebugOptions.*;

/**
 * {@link  net.minecraft.client.gui.screens.debug.DebugOptionsScreen}
 */
public class DebugOptionsScreenMixin {
    // Make getPath() return translated string. That's all.
    // Fabric API is required, as it loads the translations.
    @Mixin(targets = "net/minecraft/client/gui/screens/debug/DebugOptionsScreen$OptionEntry", priority = 999999)
    public static class OptionEntryMixin {
        // Need to use @WarpOperation instead of @Redirect here, or it will be broken by Fabric API
        @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;getPath()Ljava/lang/String;"))
        private String entryNameHandler(ResourceLocation instance, Operation<String> original) {
            String key = getKey(instance);
            String translatedStr = Component.translatable(key).getString();

            // Log missing translations
            if (translatedStr.equals(key) && !missingTranslations.contains(key)) {
                LOGGER.warn("[TranslatableDebugOptions] Missing translation for key: {}", key);
                // Prevent logging the same missing translation multiple times
                missingTranslations.add(key);
            }

            return translatedStr;
        }
    }

    @Mixin(targets = "net/minecraft/client/gui/screens/debug/DebugOptionsScreen$OptionList", priority = 999999)
    public static class OptionsListMixin {
        // Allow searching with both original and translated strings, and it's case-insensitive!
        @WrapOperation(method = "updateSearch", at = @At(value = "INVOKE", target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"))
        private boolean searchHandler(String originalStr, CharSequence searchCharSeq, Operation<Boolean> original, @Local Map.Entry<ResourceLocation, DebugScreenEntry> entry) {
            String searchStrLower = ((String) searchCharSeq).toLowerCase();
            String translatedStrLower = Component.translatable(getKey(entry.getKey())).getString().toLowerCase();

            return original.call(originalStr.toLowerCase(), searchStrLower) ||
                    original.call(translatedStrLower, searchStrLower);
        }
    }
}