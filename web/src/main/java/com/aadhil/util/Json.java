package com.aadhil.util;

import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;

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
}
