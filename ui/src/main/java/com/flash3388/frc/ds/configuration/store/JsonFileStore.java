package com.flash3388.frc.ds.configuration.store;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

public class JsonFileStore extends JsonStoreBase {

    private final Path mPath;
    private final JsonParser mParser;
    private final AtomicReference<JsonObject> mStoreRoot;

    public JsonFileStore(Path path, JsonParser parser, Gson gson, AtomicReference<JsonObject> storeRoot) {
        super(gson);

        mPath = path;
        mParser = parser;
        mStoreRoot = storeRoot;
    }

    public JsonFileStore(Path path, JsonParser parser, Gson gson) {
        this(path, parser, gson, new AtomicReference<>());
    }

    protected synchronized JsonObject getRoot() throws IOException {
        JsonObject root = mStoreRoot.get();
        if (root != null) {
            return root;
        }

        try(InputStream inputStream = Files.newInputStream(mPath)) {
            JsonElement element = mParser.parse(new InputStreamReader(inputStream));

            if (!element.isJsonObject()) {
                throw new IOException("Expected root of file to be object");
            }

            JsonObject object = element.getAsJsonObject();
            mStoreRoot.set(object);

            return object;
        }
    }

    protected void storeRoot(JsonObject root) throws IOException {
        try (OutputStream outputStream = Files.newOutputStream(mPath)) {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setLenient(false);
            jsonWriter.setIndent("\t");
            Streams.write(root, jsonWriter);

            outputStream.write(stringWriter.toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}
