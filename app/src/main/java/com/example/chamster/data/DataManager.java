package com.example.chamster.data;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataManager {
    private static SharedPreferences prefs;
    private static String currentUser;

    public static void init(Context context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences("hamster_race_data", Context.MODE_PRIVATE);
        }
        if (currentUser == null) {
            currentUser = prefs.getString("current_user", "");
        }
    }

    public static void setCurrentUser(String username) {
        currentUser = username;
        prefs.edit().putString("current_user", username).apply();
    }

    private static String getKey(String suffix) {
        String user = currentUser == null ? "" : currentUser;
        return user.isEmpty() ? "default_" + suffix : user + "_" + suffix;
    }

    public static int getBalance() {
        return prefs.getInt(getKey("balance"), 1000);
    }

    public static void addBalance(int amount) {
        prefs.edit().putInt(getKey("balance"), getBalance() + amount).apply();
        checkAndResetBalance();
    }

    public static boolean removeBalance(int amount) {
        int current = getBalance();
        if (current >= amount) {
            prefs.edit().putInt(getKey("balance"), current - amount).apply();
            checkAndResetBalance();
            return true;
        }
        return false;
    }

    public static String getBaseSkin() {
        return prefs.getString(getKey("base_skin"), "hamster/chomik.png");
    }

    public static void setBaseSkin(String imagePath) {
        prefs.edit().putString(getKey("base_skin"), imagePath).apply();
    }

    public static Set<String> getOwnedSkins() {
        return new HashSet<>(prefs.getStringSet(getKey("owned_skins"), new HashSet<>()));
    }

    public static void addOwnedSkin(String imagePath) {
        Set<String> owned = getOwnedSkins();
        owned.add(imagePath);
        prefs.edit().putStringSet(getKey("owned_skins"), owned).apply();
    }

    public static boolean isSkinOwned(String imagePath) {
        return getOwnedSkins().contains(imagePath);
    }

    public static List<String> getAccessories() {
        String json = prefs.getString(getKey("accessories"), "[]");
        List<String> list = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) list.add(arr.getString(i));
        } catch (JSONException ignored) {}
        return list;
    }

    public static void toggleAccessory(String imagePath) {
        List<String> list = getAccessories();
        if (list.contains(imagePath)) {
            list.remove(imagePath);
        } else {
            list.add(imagePath);
        }
        JSONArray arr = new JSONArray();
        for (String s : list) arr.put(s);
        prefs.edit().putString(getKey("accessories"), arr.toString()).apply();
    }
    public static void checkAndResetBalance() {
        if (getBalance() <= 0) {
            prefs.edit().putInt(getKey("balance"), 1000).apply();
        }
    }
}