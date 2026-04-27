package com.example.chamster.data;

import android.content.Context;
import android.content.SharedPreferences;

public class BalanceManager {
    private static final String PREFS_NAME = "hamster_prefs";
    private static final String KEY_BALANCE = "user_balance";
    private static final int START_BALANCE = 1000;

    public static int getBalance(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_BALANCE, START_BALANCE);
    }

    public static void addBalance(Context context, int amount) {
        int current = getBalance(context);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_BALANCE, current + amount).apply();
    }

    public static boolean removeBalance(Context context, int amount) {
        int current = getBalance(context);
        if (current >= amount) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().putInt(KEY_BALANCE, current - amount).apply();
            return true;
        }
        return false;
    }

    public static void resetBalance(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_BALANCE, START_BALANCE).apply();
    }
}