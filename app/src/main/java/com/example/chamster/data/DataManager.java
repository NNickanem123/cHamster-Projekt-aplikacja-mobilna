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
    private static String currentUser = null;

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
        if (prefs != null) {
            prefs.edit().putString("current_user", username).apply();
        }
    }

    public static String getCurrentUser() {
        if (currentUser == null && prefs != null) {
            currentUser = prefs.getString("current_user", "");
        }
        return currentUser == null ? "" : currentUser;
    }

    private static String getKey(String suffix) {
        String user = getCurrentUser();
        if (user.isEmpty()) {
            return "default_" + suffix;
        }
        return user + "_" + suffix;
    }

    public static int getBalance() {
        if (prefs == null) return 1000;
        return prefs.getInt(getKey("balance"), 1000);
    }

    public static void addBalance(int amount) {
        if (prefs == null) return;
        int current = getBalance();
        prefs.edit().putInt(getKey("balance"), current + amount).apply();
    }

    public static boolean removeBalance(int amount) {
        if (prefs == null) return false;
        int current = getBalance();
        if (current >= amount) {
            prefs.edit().putInt(getKey("balance"), current - amount).apply();
            return true;
        }
        return false;
    }

    public static void resetBalance() {
        if (prefs == null) return;
        prefs.edit().putInt(getKey("balance"), 1000).apply();
    }

    public static String getBaseSkin() {
        if (prefs == null) return "hamster/chomik.png";
        return prefs.getString(getKey("base_skin"), "hamster/chomik.png");
    }

    public static void setBaseSkin(String imagePath) {
        if (prefs == null) return;
        prefs.edit().putString(getKey("base_skin"), imagePath).apply();
    }

    public static Set<String> getOwnedSkins() {
        if (prefs == null) return new HashSet<>();
        return new HashSet<>(prefs.getStringSet(getKey("owned_skins"), new HashSet<>()));
    }

    public static void addOwnedSkin(String imagePath) {
        if (prefs == null) return;
        Set<String> owned = getOwnedSkins();
        owned.add(imagePath);
        prefs.edit().putStringSet(getKey("owned_skins"), owned).apply();
    }

    public static boolean isSkinOwned(String imagePath) {
        return getOwnedSkins().contains(imagePath);
    }

    public static List<String> getAccessories() {
        if (prefs == null) return new ArrayList<>();
        String json = prefs.getString(getKey("accessories"), "[]");
        List<String> list = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                list.add(arr.getString(i));
            }
        } catch (JSONException ignored) {}
        return list;
    }

    public static void toggleAccessory(String imagePath) {
        if (prefs == null) return;
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

    public static void clearAccessories() {
        if (prefs == null) return;
        prefs.edit().putString(getKey("accessories"), "[]").apply();
    }
}