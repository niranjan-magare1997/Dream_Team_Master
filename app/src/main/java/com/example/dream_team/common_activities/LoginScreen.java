package com.example.dream_team.common_activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dream_team.R;
import com.example.dream_team.interfaces.CALLBACK;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {
    public String TAG = "Dream_Team | LoginScreen";
    private TextInputLayout userMobileNumber, userPassword;
    private TextInputEditText mobileNumberEditText, passwordEditText;
    public Button login_Button;
    public TextView forgotPassword, createAccount;
    public CheckBox rememberMeCheckbox;
    private DATABASE database;
    public static final String ALPHANUMERIC_KEY = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor myEdit;
    private String mobile = "";
    private String password = "";
    public static boolean rememberMeCheckedOrNot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Log.d(TAG, "onCreate | Activity created ");
        initialization();
        checkAlreadyLogin();
    }

    private void checkAlreadyLogin() {
        String userToken = LoginScreen.getSharedData("TOKEN");
        String docName = LoginScreen.getSharedData("DOC_NAME");
        String remember = LoginScreen.getSharedData("REMEMBER");
        Log.d(TAG, "checkAlreadyLogin | Token: " + userToken + " Doc Name: " + docName + " Remember: " + remember);

        if (remember.equals("true")) {
            if (userToken.length() > 0) {
                Log.d(TAG, "checkAlreadyLogin | Token exist. Move to next screen ");
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                intent.putExtra("DOC_NAME", docName);
                intent.putExtra("TOKEN", userToken);
                startActivity(intent);
            }
        }
    }

    public void initialization() {
        userMobileNumber = findViewById(R.id.userName);
        userPassword = findViewById(R.id.userPassword);

        database = new DATABASE();
        sharedPreferences = getSharedPreferences("DREAM_TEAM_DATA", MODE_PRIVATE);
        myEdit = sharedPreferences.edit();

        mobileNumberEditText = findViewById(R.id.name);
        passwordEditText = findViewById(R.id.password);

        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);

        forgotPassword = findViewById(R.id.forgotPasswordLabel);
        createAccount = findViewById(R.id.createAccountLabel);

        login_Button = findViewById(R.id.login_Button);

        login_Button.setOnClickListener(LoginScreen.this);
        forgotPassword.setOnClickListener(LoginScreen.this);
        createAccount.setOnClickListener(LoginScreen.this);

        Log.d(TAG, "onCreate | Views and Listeners are set. ");
    }

    @Override
    public void onClick(View v) {
        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "onClick | Checkbox Status => " + rememberMeCheckbox.isChecked());
            rememberMeCheckedOrNot = rememberMeCheckbox.isChecked();
            switch (v.getId()) {
                case R.id.login_Button:
                    login();
                    break;
                case R.id.forgotPasswordLabel:
                    forgotPassword();
                    break;
                case R.id.createAccountLabel:
                    createAccount();
                    break;
            }
        }
    }

    public static void setSharedData(String key, String data) {
        myEdit.putString(key, data);
        myEdit.apply();
    }

    public static String getSharedData(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void forgotPassword() {
        Log.d(TAG, "forgotPassword | onCreate | In function forgotPassword ");
    }

    public void createAccount() {
        Log.d(TAG, "createAccount | onCreate | In function createAccount ");
        Intent intent = new Intent(getApplicationContext(), SignUpScreen.class);
        startActivity(intent);
    }

    public void login() {
        //Toast.makeText(this, "Clicked on Forgot Password", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "login | In function login ");
        if (validation()) {
            mobile = mobileNumberEditText.getText().toString();
            password = passwordEditText.getText().toString();

            database.checkUserExist(mobile, password, new CALLBACK() {
                @Override
                public void callBackMethod(int result) {
                    Log.d(TAG, "login | callBackMethod | Result => " + result);
                    if (result == 0) {
                        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                        intent.putExtra("DOC_NAME", getSharedData("DOC_NAME"));
                        intent.putExtra("TOKEN", getSharedData("TOKEN"));
                        mobileNumberEditText.setText("");
                        passwordEditText.setText("");
                        userMobileNumber.setErrorEnabled(false);
                        userPassword.setErrorEnabled(false);
                        startActivity(intent);
                    } else if (result == 1) {
                        Log.d(TAG, "login | callBackMethod | User not exist ");
                    } else if (result == 2) {
                        Log.e(TAG, "login | callBackMethod | Exception while checking user ");
                    }
                }
            });
        }
    }

    //validation for mob num and password
    public boolean validation() {
        if (mobileNumberEditText.getText().toString().equals("")) {
            userMobileNumber.setError("Required Field");
            userMobileNumber.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            userMobileNumber.setErrorEnabled(true);
            return false;
        } else if (mobileNumberEditText.getText().length() != 10) {
            userMobileNumber.setError("Enter Valid Mobile Number");
            userMobileNumber.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            userMobileNumber.setErrorEnabled(true);
            Toast.makeText(getApplicationContext(), "" + mobileNumberEditText.getText().length(), Toast.LENGTH_SHORT);
            return false;
        } else if (passwordEditText.getText().toString().equals("")) {
            userPassword.setError("Required Field");
            userPassword.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            userPassword.setErrorEnabled(true);
            return false;
        } else {
            userMobileNumber.setErrorEnabled(false);
            userPassword.setErrorEnabled(false);
            return true;
        }
    }
}
