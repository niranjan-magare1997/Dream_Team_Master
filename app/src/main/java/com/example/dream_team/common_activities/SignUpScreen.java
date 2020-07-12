package com.example.dream_team.common_activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dream_team.R;
import com.example.dream_team.interfaces.CALLBACK;
import com.example.dream_team.modal_class.CONSTANTS;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String TAG = "Dream_Team | SignUpScreen ";
    private Spinner spinner;
    private String signUpType = "";
    private DATABASE database;
    private CONSTANTS constants;
    private TextInputLayout usernameLayout, mobileLayout, passwordLayout, retypePasswordLayout, addressLayout, adharNumberLayout, GSTLayout, hotelNameLayout;
    private TextInputEditText userNameEditText, mobileEditText, passwordEditText, addressEditText, adharNumberEditText, GSTEditText, hotelNameEditText;
    private Button nextFormButton, dialActionButton;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        initialization();
    }

    public void initialization() {
        usernameLayout = findViewById(R.id.usernameLayout);
        mobileLayout = findViewById(R.id.mobileLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        retypePasswordLayout = findViewById(R.id.retypePasswordLayout);
        addressLayout = findViewById(R.id.addressLayout);
        adharNumberLayout = findViewById(R.id.adharNumberLayout);
        GSTLayout = findViewById(R.id.GSTLayout);
        hotelNameLayout = findViewById(R.id.hotelNameLayout);

        userNameEditText = findViewById(R.id.userNameEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        addressEditText = findViewById(R.id.addressEditText);
        adharNumberEditText = findViewById(R.id.adharNumberEditText);
        GSTEditText = findViewById(R.id.GSTEditText);
        hotelNameEditText = findViewById(R.id.hotelNameEditText);

        relativeLayout = findViewById(R.id.relativelayout);

        database = new DATABASE();
        constants = new CONSTANTS();

        spinner = findViewById(R.id.spinnerChoice);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.dropdownChoice, R.layout.spinner_choose_color);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_text_color);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getSelectedItem().toString().equals("Owner")) {
            findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
            signUpType = "Owner";
        } else if (adapterView.getSelectedItem().toString().equals("Waiter")) {
            signUpType = "Waiter";
            findViewById(R.id.linearLayout).setVisibility(View.INVISIBLE);
        } else if (adapterView.getSelectedItem().toString().equals("Cheff")) {
            signUpType = "Chef";
            findViewById(R.id.linearLayout).setVisibility(View.INVISIBLE);
        } else {
            signUpType = "";
            findViewById(R.id.linearLayout).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void showOtp() {
        OTPDialog otpdialog = new OTPDialog();
        otpdialog.show(getSupportFragmentManager(), "OTP");
        otpdialog.setCancelable(false);
    }

    public void signUp(View view) {
        Log.d(TAG, "signUp | In sign up with type " + signUpType);
        final Map<String, Object> insertData = new HashMap<>();
        String mobile = mobileEditText.getText().toString();

        //if (validation()) {
        insertData.put(constants.MOBILE(), mobileEditText.getText().toString());
        insertData.put(constants.NAME(), userNameEditText.getText().toString());
        insertData.put(constants.PASSWORD(), passwordEditText.getText().toString());
        insertData.put(constants.ADDRESS(), addressEditText.getText().toString());
        switch (signUpType) {
            case "Owner":
                insertData.put(constants.AADHAR(), adharNumberEditText.getText().toString());
                insertData.put(constants.GST_NO(), GSTEditText.getText().toString());
                insertData.put(constants.HOTEL_NAME(), hotelNameEditText.getText().toString());
                database.checkNumberWithType(mobile, signUpType, insertData, new CALLBACK() {
                    @Override
                    public void callBackMethod(int result) {
                        Log.d(TAG, "signUp | callBackMethod: " + result);
                    }
                });
                break;
            case "Waiter":
            case "Chef":
                database.checkNumberWithType(mobile, signUpType, insertData, new CALLBACK() {
                    @Override
                    public void callBackMethod(int result) {
                        Log.d(TAG, "signUp | callBackMethod: " + result);
                    }
                });
                break;
            default:
                Toast.makeText(this, "Please Choose the type", Toast.LENGTH_SHORT).show();
                break;
        }
        //}
    }

    //validation for mob num and password
    public boolean validation() {
        if (userNameEditText.getText().toString().equals("")) {
            usernameLayout.setError("Required Field");
            usernameLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            usernameLayout.setErrorEnabled(true);
            return false;
        } else if (mobileEditText.getText().length() != 10) {
            mobileLayout.setError("Enter Valid Mobile Number");
            mobileLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            mobileLayout.setErrorEnabled(true);
            Toast.makeText(getApplicationContext(), mobileEditText.getText().length(), Toast.LENGTH_SHORT);
            return false;
        } else if (passwordEditText.getText().toString().equals("")) {
            passwordLayout.setError("Required Field");
            passwordLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            passwordLayout.setErrorEnabled(true);
            return false;
        } else if (addressEditText.getText().toString().equals("")) {
            addressLayout.setError("Required Field");
            addressLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            addressLayout.setErrorEnabled(true);
            return false;
        } else return true;
    }

    public void proceedNext(View view) {
        relativeLayout.setVisibility(View.VISIBLE);
    }

    public void actionDialDirect(View view) {
        String phoneNumber = "9999999999";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
