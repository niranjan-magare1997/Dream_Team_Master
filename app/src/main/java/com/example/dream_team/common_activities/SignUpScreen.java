package com.example.dream_team.common_activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dream_team.R;
import com.example.dream_team.interfaces.CALLBACK;
import com.example.dream_team.modal_class.CONSTANTS;
import com.example.dream_team.owner.activities.OwnerLoginScreen;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String TAG = "Dream_Team | SignUpScreen ";
    private Spinner spinner;
    private String signUpType = "";
    private DATABASE database;
    private CONSTANTS constants;
    private TextInputLayout usernameLayout, mobileLayout, passwordLayout, retypePasswordLayout, addressLayout, adharNumberLayout, GSTLayout, hotelNameLayout;
    private TextInputEditText userNameEditText, mobileEditText, passwordEditText, addressEditText, adharNumberEditText, GSTEditText, hotelNameEditText, retypePasswordEditTest;
    private RelativeLayout relativeLayout;
    private ProgressDialogFragment progressBar;
    private CustomToast customToast;
    private int Activity_Code = 101;
    private final Map<String, Object> insertData = new HashMap<>();
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        if (customToast == null) {
            customToast = new CustomToast(this);
        }
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
        retypePasswordEditTest = findViewById(R.id.retypePassword);
        addressEditText = findViewById(R.id.addressEditText);
        adharNumberEditText = findViewById(R.id.adharNumberEditText);
        GSTEditText = findViewById(R.id.GSTEditText);
        hotelNameEditText = findViewById(R.id.hotelNameEditText);

        relativeLayout = findViewById(R.id.relativelayout);

        database = new DATABASE();
        constants = new CONSTANTS();
        progressBar = new ProgressDialogFragment();

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

    public void signUp(View view) {
        Log.d(TAG, "signUp | In sign up with type " + signUpType);

        if (!MainActivity.isNetworkAvailable(this)) {
            customToast.toast("Please check your Internet Connection!");
        } else {
            if (validation()) {
                mobile = mobileEditText.getText().toString();
                insertData.put(constants.MOBILE(), mobileEditText.getText().toString());
                insertData.put(constants.NAME(), userNameEditText.getText().toString());
                insertData.put(constants.PASSWORD(), passwordEditText.getText().toString());
                insertData.put(constants.ADDRESS(), addressEditText.getText().toString());

                switch (signUpType) {
                    case "Owner":
                        insertData.put(constants.AADHAR(), adharNumberEditText.getText().toString());
                        insertData.put(constants.GST_NO(), GSTEditText.getText().toString());
                        insertData.put(constants.HOTEL_NAME(), hotelNameEditText.getText().toString());

                        //Call to check if number exist in our database or not.
                        database.checkOwnerNumberExist(mobile, new CALLBACK() {
                            @Override
                            public void callBackMethod(int result) {
                                Log.d(TAG, "signUp | callBackMethod | Result is => "+result);
                                if(result == 0){
                                    Intent i = new Intent(SignUpScreen.this,OTPDialogActivity.class);
                                    i.putExtra("Mobile",mobile);
                                    startActivityForResult(i,Activity_Code);
                                }else {
                                    //Number Not found
                                }
                            }
                        });
//                        database.checkNumberWithType(mobile, signUpType, insertData, new CALLBACK() {
//                            @Override
//                            public void callBackMethod(int result) {
//                                Log.d(TAG, "signUp | callBackMethod: " + result);
//                                  progressBar.dismiss();   //Progress Bar End
//                            }
//                        });
                        break;
                    case "Waiter":
                    case "Chef":
                        database.checkNumberWithType(mobile, signUpType, insertData, new CALLBACK() {
                            @Override
                            public void callBackMethod(int result) {
                                Log.d(TAG, "signUp | callBackMethod: " + result);
                                //progressBar.dismiss();   //Progress Bar End
                            }
                        });
                        break;
                    default:
                        Toast.makeText(this, "Please Choose the type", Toast.LENGTH_SHORT).show();
                        //progressBar.dismiss();   //Progress Bar End
                        break;
                }
            }
        }
    }

    private boolean isValidGSTNumber() {
        String gst = GSTEditText.getText().toString().trim();
        Log.d(TAG, "isValidGSTNumber | Number " + gst + " Length => " + gst.length());
        if (gst.equals("")) return true;
        String regex = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(gst);
        return m.matches();
    }

    private boolean isValidAadharNumber() {
        String aadhar = adharNumberEditText.getText().toString().trim();
        Log.d(TAG, "isValidAadharNumber | Number " + aadhar + " Length => " + aadhar.length());
        if (aadhar.equals("")) return false;
        String regex = "^[2-9]{1}[0-9]{11}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(aadhar);
        return m.matches();
    }

    //validation for all data
    public boolean validation() {
        usernameLayout.setErrorEnabled(false);
        mobileLayout.setErrorEnabled(false);
        passwordLayout.setErrorEnabled(false);
        retypePasswordLayout.setErrorEnabled(false);
        addressLayout.setErrorEnabled(false);

        Log.d(TAG, "validation | isValidAadharNumber() " + isValidAadharNumber());
        if (signUpType == "Owner") {
            adharNumberLayout.setErrorEnabled(false);
            GSTLayout.setErrorEnabled(false);
            hotelNameLayout.setErrorEnabled(false);
        }

        if (userNameEditText.getText().toString().trim().equals("")) {
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
        } else if (passwordEditText.getText().toString().trim().equals("")) {
            passwordLayout.setError("Required Field");
            passwordLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            passwordLayout.setErrorEnabled(true);
            return false;
        } else if (retypePasswordEditTest.getText().toString().trim().equals("")) {
            retypePasswordLayout.setError("Required Field");
            retypePasswordLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            retypePasswordLayout.setErrorEnabled(true);
            return false;
        } else if (!passwordEditText.getText().toString().trim().equals(retypePasswordEditTest.getText().toString().trim())) {
            passwordLayout.setError("Enter same password please");
            passwordLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            passwordLayout.setErrorEnabled(true);
            retypePasswordLayout.setError("Enter same password please");
            retypePasswordLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            retypePasswordLayout.setErrorEnabled(true);
            return false;
        } else if (addressEditText.getText().toString().trim().equals("")) {
            addressLayout.setError("Required Field");
            addressLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            addressLayout.setErrorEnabled(true);
            return false;
        } else if (isValidAadharNumber() == false) {
            adharNumberLayout.setError("Required Field");
            adharNumberLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            adharNumberLayout.setErrorEnabled(true);
            return false;
        } else if (signUpType == "Owner" && isValidGSTNumber() == false) {
            GSTLayout.setError("Not valid");
            GSTLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            GSTLayout.setErrorEnabled(true);
            return false;
        } else if (signUpType == "Owner" && (hotelNameEditText.getText().toString().trim().equals(""))) {
            hotelNameLayout.setError("Required Field");
            hotelNameLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            hotelNameLayout.setErrorEnabled(true);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult | Request => "+requestCode);
        Log.d(TAG, "onActivityResult | Result => "+resultCode);

        if(requestCode == Activity_Code){
            if(resultCode == Activity.RESULT_OK){
                //Valid user so Sign up user
                Log.d(TAG, "onActivityResult | Sign up success ");
                progressBar.show(getSupportFragmentManager(), "Sign up");                    //Progress Bar Start
                database.checkNumberWithType(mobile, signUpType, insertData, new CALLBACK() {
                    @Override
                    public void callBackMethod(int result) {
                        Log.d(TAG, "signUp | callBackMethod: " + result);
                        progressBar.dismiss();   //Progress Bar End
                        if(result == 0){
                            Intent i = new Intent(SignUpScreen.this,OwnerLoginScreen.class);
                            startActivity(i);
                        }
                    }
                });
            }else {
                //User not verified
                Log.d(TAG, "onActivityResult | Sign up failed ");
            }
        }
    }
}
