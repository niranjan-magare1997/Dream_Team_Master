package com.example.dream_team;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

public class LoginScreen extends AppCompatActivity {
    private TextInputLayout userMobileNumber, userPassword;
     private TextInputEditText mobileNumberEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        initialization();
    }

    public void initialization() {
        userMobileNumber = findViewById(R.id.userName);
        userPassword = findViewById(R.id.userPassword);
        mobileNumberEditText = findViewById(R.id.name);
        passwordEditText = findViewById(R.id.password);
    }

    public void forgotPassword(View view) {
    }

    public void createAccount(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUpScreen.class);
        startActivity(intent);
    }

    public void login(View view) {
        if (validation()) {
            Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
            mobileNumberEditText.setText("");
            passwordEditText.setText("");
            userMobileNumber.setErrorEnabled(false);
            userPassword.setErrorEnabled(false);
            startActivity(intent);
        }
    }

    //validation for mob num and password
    public boolean validation() {
        if (mobileNumberEditText.getText().toString().equals("")) {
            userMobileNumber.setError("Required Field");
            userMobileNumber.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            userMobileNumber.setErrorEnabled(true);
            return false;
        } else if (mobileNumberEditText.getText().length()!=10) {
            userMobileNumber.setError("Enter Valid Mobile Number");
            userMobileNumber.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            userMobileNumber.setErrorEnabled(true);
            Toast.makeText(getApplicationContext(),""+mobileNumberEditText.getText().length(),Toast.LENGTH_SHORT);
            return false;
        } else if (passwordEditText.getText().toString().equals("")) {
            userPassword.setError("Required Field");
            userPassword.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            userPassword.setErrorEnabled(true);
            return false;
        } else return true;
    }


}
