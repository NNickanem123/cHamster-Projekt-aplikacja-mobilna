package com.example.chamster.data;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SkinManager {

    private static final String PREFS_NAME = "hamster_prefs";
    private static final String KEY_BASE = "selected_base_skin";
    private static final String KEY_ACCESSORIES = "selected_accessories";

    public static void saveBaseSkin(Context context, String imagePath) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_BASE, imagePath).apply();
    }

    public static String loadBaseSkin(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_BASE, "hamster/chomik.png");
    }

    public static void toggleAccessory(Context context, String imagePath) {
        List<String> list = loadAccessories(context);

        if (list.contains(imagePath)) {
            list.remove(imagePath);
        } else {
            list.add(imagePath);
        }

        saveAccessories(context, list);
    }

    public static List<String> loadAccessories(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_ACCESSORIES, "[]");

        List<String> list = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                list.add(arr.getString(i));
            }
        } catch (Exception ignored) {}

        return list;
    }

    private static void saveAccessories(Context context, List<String> list) {
        JSONArray arr = new JSONArray();
        for (String s : list) arr.put(s);

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_ACCESSORIES, arr.toString()).apply();
    }
}