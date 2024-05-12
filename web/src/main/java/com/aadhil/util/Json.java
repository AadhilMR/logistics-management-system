package com.aadhil.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Json {
    public <T> T getData(BufferedReader reader, Class<T> tClass) throws IOException {
        Gson gson = new Gson();

        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return gson.fromJson(stringBuilder.toString(), tClass);
    }

    public String getJsonString(List list) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(list);
    }
}
