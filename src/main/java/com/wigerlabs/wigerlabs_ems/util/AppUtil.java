package com.wigerlabs.wigerlabs_ems.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppUtil {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Date.class, new TypeAdapter<Date>() {
                @Override
                public void write(JsonWriter out, Date value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                    } else {
                        out.value(value.toString()); // Returns yyyy-MM-dd format
                    }
                }

                @Override
                public Date read(JsonReader in) throws IOException {
                    if (in != null) {
                        String dateStr = in.nextString();
                        return Date.valueOf(dateStr); // Expects yyyy-MM-dd format
                    }
                    return null;
                }
            })
            .registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
                @Override
                public void write(JsonWriter out, LocalDate value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                    } else {
                        out.value(value.toString()); // yyyy-MM-dd
                    }
                }

                @Override
                public LocalDate read(JsonReader in) throws IOException {
                    if (in != null) {
                        String dateStr = in.nextString();
                        return LocalDate.parse(dateStr); // yyyy-MM-dd
                    }
                    return null;
                }
            })
            .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                @Override
                public void write(JsonWriter out, LocalDateTime value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                    } else {
                        out.value(value.toString()); // yyyy-MM-ddTHH:mm:ss
                    }
                }

                @Override
                public LocalDateTime read(JsonReader in) throws IOException {
                    if (in != null) {
                        String dateStr = in.nextString();
                        // Accept both yyyy-MM-ddTHH:mm:ss and yyyy-MM-dd
                        if (dateStr.length() == 10) {
                            return LocalDate.parse(dateStr).atStartOfDay();
                        } else {
                            return LocalDateTime.parse(dateStr);
                        }
                    }
                    return null;
                }
            })
            .create();

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateCode() {
        int randomNumber = SECURE_RANDOM.nextInt(1_000_000);
        return String.format("%6d", randomNumber);
    }
}
