package com.flash3388.frc.ds.configuration.store;

import com.flash3388.frc.ds.configuration.ValueType;
import com.flash3388.frc.ds.configuration.store.exceptions.NoEntryException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class JsonStoreBase implements Store {

    private final Gson mGson;

    public JsonStoreBase(Gson gson) {
        mGson = gson;
    }

    @Override
    public void put(String name, Object value) throws IOException {
        try {
            JsonElement element = mGson.toJsonTree(value);
            addToRoot(name, element);
        } catch (IOException e) {
            throw e;
        } catch (Throwable t) {
            throw new IOException(t);
        }
    }

    @Override
    public Object get(String name, ValueType type) throws IOException {
        try {
            JsonObject root = getRoot();
            JsonElement element = root.get(name);
            if (element == null) {
                throw new NoEntryException(name);
            }

            return mGson.fromJson(element, type.javaClass());
        } catch (IOException e) {
            throw e;
        } catch (Throwable t) {
            throw new IOException(t);
        }
    }

    @Override
    public Map<String, String> getAllLeafs() throws IOException {
        try {
            Map<String, String> allLeafs = new HashMap<>();

            JsonObject root = getRoot();
            putLeafs("", root, allLeafs);

            return allLeafs;
        } catch (IOException e) {
            throw e;
        } catch (Throwable t) {
            throw new IOException(t);
        }
    }

    private void putLeafs(String keyPrefix, JsonObject root, Map<String, String> leafs) {
        for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
            JsonElement element = entry.getValue();

            String key;
            if (keyPrefix.isEmpty()) {
                key = entry.getKey();
            } else {
                key = keyPrefix.concat(".").concat(entry.getKey());
            }

            if (element.isJsonObject()) {
                putLeafs(key, element.getAsJsonObject(), leafs);
            } else if (element.isJsonPrimitive()) {
                leafs.put(key, element.getAsString());
            } else if (element.isJsonArray()) {
                leafs.put(key, element.toString());
            } else { // null
                leafs.put(key, "NULL");
            }
        }
    }

    private synchronized void addToRoot(String name, JsonElement element) throws IOException {
        JsonObject root = getRoot();
        root.add(name, element);

        storeRoot(root);
    }

    protected abstract JsonObject getRoot() throws IOException;
    protected abstract void storeRoot(JsonObject root) throws IOException;
}
