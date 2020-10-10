package com.example.dream_team.common_activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class COMMON {
    private static String TAG = "Dream_Team | COMMON";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor myEdit;

    public static boolean checkConnectivity(Activity context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void showSnackBar(String text, View view) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
//        snackbar.setAction("UNDO", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "Clicked on snack bar action button");
//            }
//        });
        snackbar.show();
    }

    @SuppressLint("CommitPrefEdits")
    public static void setSharedPreference(SharedPreferences sharedPref){
        sharedPreferences = sharedPref;
        myEdit = sharedPreferences.edit();
    }

    public static void setSharedData(String key, String data) {
        myEdit.putString(key, data);
        myEdit.apply();
    }

    public static String getSharedData(String key) {
        return sharedPreferences.getString(key, "");
    }
}
