package com.example.dream_team.common_activities;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dream_team.R;


public class CustomToast {
    private Activity activity;

    public CustomToast(Activity activity) {
        this.activity = activity;
    }


    public void toast(String toastMessage) {
        // Get the custom layout view.
        View toastView = activity.getLayoutInflater().inflate(R.layout.toastcustomeview, null);
        // Initiate the Toast instance.
        Toast toast = new Toast(activity.getApplicationContext());
        // Set custom view in toast.
        ((TextView) toastView.findViewById(R.id.customToastText)).setText(toastMessage);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 500);
        toast.show();
    }
}