package com.example.dream_team.common_activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.example.dream_team.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class OTPDialogActivity extends AppCompatActivity {
    private PinView pinView;
    private Button verify;
    private String TAG = "Dream_Team";
    private TextView resendOTP, timer;
    private String mobileNumber;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider phoneAuthProvider;
    private String verificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_dialog);
        pinView = findViewById(R.id.firstPinView);
        resendOTP = findViewById(R.id.resendOTP);
        timer = findViewById(R.id.timer);
        phoneAuthProvider = PhoneAuthProvider.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent in = getIntent();
        mobileNumber = (in.getStringExtra("Mobile"));
        Log.d(TAG, "onCreate | Mobile "+mobileNumber);
        sendOTP(mobileNumber);
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

    private void sendOTP(String phNo) {
        phoneAuthProvider.verifyPhoneNumber("+91" + phNo.trim(), 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.d(TAG, "Verification code sent. "+s);
                verificationID = s;
                new CountDownTimer(60000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        timer.setText("Seconds remaining: " + millisUntilFinished / 1000);
                    }
                    public void onFinish() {
                        timer.setText("Done!");
                    }
                }.start();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Log.d(TAG, "Verification code autoRetrieval Timeout. ");
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "Verification code verified successfully. Code is "+phoneAuthCredential.getSmsCode());
                pinView.setText(phoneAuthCredential.getSmsCode());
                Intent intent = new Intent();
                intent.putExtra("Result", true);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG, "Code verification failed " + e.toString());
            }
        });
    }

    private void senDataBack() {
        Intent intent = new Intent();
        intent.putExtra("Result", true);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
