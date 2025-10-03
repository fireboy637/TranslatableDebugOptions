package com.fireboy637.translatabledebugoptions.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public interface TranslatableDebugOptions {
    Logger LOGGER = LoggerFactory.getLogger("TranslatableDebugOptions");
    ArrayList<String> missingTranslations = new ArrayList<>();
}