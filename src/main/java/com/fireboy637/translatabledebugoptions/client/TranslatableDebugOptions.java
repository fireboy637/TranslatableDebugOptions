package com.fireboy637.translatabledebugoptions.client;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TranslatableDebugOptions {
    public static final Logger LOGGER = LoggerFactory.getLogger("TranslatableDebugOptions");
    public static final List<String> missingTranslations = new ArrayList<>();

    public static String getKey(ResourceLocation resourceLoc) {
        return "debug." + resourceLoc.getNamespace() + "." + resourceLoc.getPath();
    }
}