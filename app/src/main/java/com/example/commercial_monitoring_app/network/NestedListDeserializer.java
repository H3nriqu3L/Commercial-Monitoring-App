package com.example.commercial_monitoring_app.network;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class NestedListDeserializer<T> implements JsonDeserializer<List<T>> {

    private final Class<T> clazz;

    public NestedListDeserializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public List<T> deserialize(JsonElement json, Type typeOfT,
                               JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonObject root = json.getAsJsonObject();
            JsonObject dados = root.getAsJsonObject("dados");
            JsonArray array = dados.getAsJsonArray("dados");

            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            return new Gson().fromJson(array, listType);
        } catch (Exception e) {
            throw new JsonParseException("Erro ao desserializar lista aninhada: " + e.getMessage(), e);
        }
    }
}
