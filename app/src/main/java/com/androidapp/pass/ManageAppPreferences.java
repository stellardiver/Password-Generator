package com.androidapp.pass;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ManageAppPreferences {



    public static boolean isLowerCaseEnabled(Context ctx){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean stored = pref.getBoolean("isLowerCaseEnabled", false);
        return stored;
    }


    public static boolean isUpperCaseEnabled(Context ctx){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean stored = pref.getBoolean("isUpperCaseEnabled", false);
        return stored;
    }


    public static boolean isNumbersEnabled(Context ctx){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean stored = pref.getBoolean("isNumbersEnabled", false);
        return stored;
    }

    public static boolean isVibrateEnabled(Context ctx){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean stored = pref.getBoolean("isVibrateEnabled", false);
        return stored;
    }
    public static boolean isHistoryEnabled(Context ctx){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean stored = pref.getBoolean("isHistoryEnabled", false);
        return stored;
    }


    public static boolean isSymbolsEnabled(Context ctx){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean stored = pref.getBoolean("isSymbolsEnabled", false);
        return stored;
    }



    public static  int getPasswordLength(Context ctx){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        int stored = pref.getInt("PasswordLength", 4);

        if(stored < 4){
            stored = 4;
        }
        return  stored;
    }

}
