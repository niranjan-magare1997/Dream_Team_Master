package com.example.dream_team.common_activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.example.dream_team.R;

import androidx.appcompat.app.AppCompatActivity;

public class OTPDialogActivity extends AppCompatActivity {
    PinView pinView;
    Button verify;
    TextView resendOTP, timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_dialog);
        pinView = findViewById(R.id.firstPinView);
        resendOTP = findViewById(R.id.resendOTP);
        timer = findViewById(R.id.timer);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timer.setText("Done!");
            }
        }.start();


    }

    public void verifyOTP(View view) {
        String OTP = pinView.getText().toString();
        if (OTP.equals("123456")) {
            pinView.setLineColor(Color.GREEN);
            resendOTP.setText("OTP Verified");
            resendOTP.setTextColor(Color.GREEN);
        } else {
            pinView.setLineColor(Color.RED);
            resendOTP.setText("Incorrect OTP");
            resendOTP.setTextColor(Color.RED);
        }
    }
}
