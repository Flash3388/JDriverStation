package com.flash3388.frc.ds.configuration.store;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class JsonInMemoryStore extends JsonStoreBase {

    private final AtomicReference<JsonObject> mStoreRoot;

    JsonInMemoryStore(Gson gson, AtomicReference<JsonObject> storeRoot) {
        super(gson);
        mStoreRoot = storeRoot;
    }

    public JsonInMemoryStore(Gson gson) {
        this(gson, new AtomicReference<>(new JsonObject()));
    }

    @Override
    protected JsonObject getRoot() throws IOException {
        return mStoreRoot.get();
    }

    @Override
    protected void storeRoot(JsonObject root) throws IOException {
        mStoreRoot.getAndSet(root);
    }
}
