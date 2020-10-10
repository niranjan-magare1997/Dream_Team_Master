package com.example.dream_team.common_activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.example.dream_team.R;
import com.example.dream_team.interfaces.CheckingNewInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class OTPDialogActivity extends AppCompatActivity {
    private PinView pinView;
    private String TAG = "Dream_Team | OTPDialogActivity";
    private TextView resendOTP, timer, setNumber;
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
        setNumber = findViewById(R.id.empMobileNumber);
        Intent in = getIntent();
        mobileNumber = (in.getStringExtra("Mobile"));
        Log.d(TAG, "onCreate | Mobile " + mobileNumber);
        sendOTP(mobileNumber);
        setNumber.setText(mobileNumber);
    }

    @SuppressLint("SetTextI18n")
    public void verifyOTP(View view) {
        String OTP = pinView.getText().toString();
        if (OTP.length() != 6) {
            pinView.setLineColor(Color.RED);
            resendOTP.setTextColor(Color.RED);
            resendOTP.setText("Incorrect OTP");
        } else {
            verifyOTPManually(OTP, new CheckingNewInterface() {
                @Override
                public void callbackWithData(int result, Map<String, Object> data) {
                    if (result == 0) {
                        pinView.setLineColor(Color.GREEN);
                        resendOTP.setText("OTP Verified");
                        resendOTP.setTextColor(Color.GREEN);
                        Intent intent = new Intent();
                        intent.putExtra("Result", true);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        pinView.setLineColor(Color.RED);
                        resendOTP.setTextColor(Color.RED);
                        resendOTP.setText("Incorrect OTP");
                    }
                }
            });
        }
    }

    private void sendOTP(String phNo) {
        phoneAuthProvider.verifyPhoneNumber("+91" + phNo.trim(), 120L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.d(TAG, "Verification code sent. " + s);
                verificationID = s;
                new CountDownTimer(120000, 1000) {
                    @SuppressLint("SetTextI18n")
                    public void onTick(long millisUntilFinished) {
                        timer.setText("Seconds remaining: " + millisUntilFinished / 1000);
                    }

                    @SuppressLint("SetTextI18n")
                    public void onFinish() {
                        timer.setText("Done!");
                    }
                }.start();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Log.d(TAG, "Verification code autoRetrieval Timeout. ");
                finish();
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "Verification code verified successfully. Code is " + phoneAuthCredential.getSmsCode());
                pinView.setText(phoneAuthCredential.getSmsCode());
                Intent intent = new Intent();
                intent.putExtra("Result", true);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG, "Code verification failed " + e.toString());
            }
        });
    }

    private void verifyOTPManually(String userOTP, final CheckingNewInterface newInterface) {
        try {
            final Map<String, Object> responseObj = new HashMap<>();
            Log.d(TAG, "verifyOTPManually | Verifying OTP... " + userOTP);
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, userOTP);
            mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "verifyOTPManually | onComplete | Verification done");
                        newInterface.callbackWithData(0, responseObj);
                    } else {
                        Log.e(TAG, "verifyOTPManually | onComplete | Verification Failed");
                        newInterface.callbackWithData(1, responseObj);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "verifyOTPManually | Exception in verifyOTPManually " + e.getMessage());
            newInterface.callbackWithData(2, null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
