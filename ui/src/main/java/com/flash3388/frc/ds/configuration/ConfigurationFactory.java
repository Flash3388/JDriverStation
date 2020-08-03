package com.flash3388.frc.ds.configuration;

import com.flash3388.frc.ds.configuration.store.JsonFileStore;
import com.flash3388.frc.ds.configuration.store.JsonInMemoryStore;
import com.flash3388.frc.ds.configuration.store.Store;
import com.flash3388.frc.ds.util.ErrorHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ConfigurationFactory {

    public static Configuration create(Path path, Logger logger, ErrorHandler errorHandler, boolean useFiles) {
        boolean fileNotExists = !Files.exists(path);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        Store store;
        if (useFiles) {
            JsonParser jsonParser = new JsonParser();
            store = new JsonFileStore(path, jsonParser, gson,
                    new AtomicReference<>(fileNotExists ? new JsonObject() : null));
        } else {
            store = new JsonInMemoryStore(gson);
        }

        ConfigurationValues configurationValues = new ConfigurationValues();
        Map<String, Value> values = configurationValues.load();

        Configuration configuration = new Configuration(
                store,
                logger,
                errorHandler, values.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().getValue()
                    )),
                values.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().getType()
                        )));

        if (useFiles && fileNotExists) {
            try {
                configuration.putDefaultValues();
            } catch (IOException e) {
                logger.warn("Failed to save default values in file", e);
            }
        }

        return configuration;
    }
}
