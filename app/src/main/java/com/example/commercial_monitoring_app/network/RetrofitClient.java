package com.example.commercial_monitoring_app.network;

import com.example.commercial_monitoring_app.model.Pessoa;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    // Custom deserializer for the nested response
    private static class PessoaListDeserializer implements JsonDeserializer<List<Pessoa>> {
        @Override
        public List<Pessoa> deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {

            try {
                JsonObject jsonObject = json.getAsJsonObject();
                JsonObject firstDados = jsonObject.getAsJsonObject("dados");
                JsonArray pessoasArray = firstDados.getAsJsonArray("dados");
                //JsonArray pessoasArray = secondDados.getAsJsonArray("pessoas");

                Type listType = new TypeToken<List<Pessoa>>(){}.getType();
                return new Gson().fromJson(pessoasArray, listType);
            } catch (Exception e) {
                throw new JsonParseException("Error parsing pessoas list: " + e.getMessage(), e);
            }
        }
    }

    public static ApiService getApiService() {
        if (retrofit == null) {
            // Configure logging
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Configure HTTP client
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            // Configure Gson with custom deserializer
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(List.class, new PessoaListDeserializer())
                    .create();

            // Build Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://crmufvgrupo3.apprubeus.com.br/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}