package com.example.dream_team.common_activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dream_team.R;
import com.example.dream_team.interfaces.CheckingNewInterface;
import com.example.dream_team.modal_class.CONSTANTS;
import com.example.dream_team.owner.activities.OwnerLoginScreen;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Map;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {
    private static View loginView;
    public String TAG = "Dream_Team | LoginScreen";
    private TextInputLayout userMobileNumber, userPassword;
    private TextInputEditText mobileNumberEditText, passwordEditText;
    private String mobile = "";
    private String password = "";
    private int Activity_Code = 102;
    public Button login_Button;
    public TextView forgotPassword, createAccount;
    public CheckBox rememberMeCheckbox;
    private DATABASE database;
    private ProgressDialogFragment progressBar;
    private CONSTANTS constants;
    public static boolean rememberMeCheckedOrNot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Log.d(TAG, "onCreate | Activity created ");
        constants = new CONSTANTS();
        initialization();
        checkAlreadyLogin();
    }

    private void checkAlreadyLogin() {
        String userToken = COMMON.getSharedData(constants.TOKEN());
        String docName = COMMON.getSharedData(constants.DOCUMENT_NAME());
        String remember = COMMON.getSharedData(constants.REMEMBER());
        String userType = COMMON.getSharedData(constants.TYPE());

        Log.d(TAG, "checkAlreadyLogin | Token: " + userToken + " Document Name: " + docName + " Remember: " + remember + " Type: " + userType);

        if (remember.equals("true") && userToken.length() > 0) {
            Log.d(TAG, "checkAlreadyLogin | Token exist. Move to next screen ");
            if (userType.equals("OWNER")) {
                Intent i = new Intent(LoginScreen.this, OwnerLoginScreen.class);
                startActivity(i);
            } else if (userType.equals("CHEFF") || userType.equals("WAITER")) {
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                intent.putExtra(constants.DOCUMENT_NAME(), docName);
                intent.putExtra(constants.TOKEN(), userToken);
                startActivity(intent);
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void initialization() {
        userMobileNumber = findViewById(R.id.userName);
        userPassword = findViewById(R.id.userPassword);

        mobileNumberEditText = findViewById(R.id.name);
        passwordEditText = findViewById(R.id.password);

        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);

        forgotPassword = findViewById(R.id.forgotPasswordLabel);
        createAccount = findViewById(R.id.createAccountLabel);

        login_Button = findViewById(R.id.login_Button);

        progressBar = new ProgressDialogFragment();

        login_Button.setOnClickListener(LoginScreen.this);
        forgotPassword.setOnClickListener(LoginScreen.this);
        createAccount.setOnClickListener(LoginScreen.this);

        Log.d(TAG, "onCreate | Views and Listeners are set. ");
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick | In onclick event");
        loginView = v;
        database = new DATABASE();

        if (COMMON.checkConnectivity(LoginScreen.this)){
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
                    startSignUpActivity();
                    break;
            }
        }else {
            Log.d(TAG, "onClick | No connection");
            COMMON.showSnackBar("Please Check your internet connection!",loginView);
        }
    }

    public void forgotPassword() {
        mobile = mobileNumberEditText.getText().toString().trim();

        if (mobile.length() != 10) {
            userMobileNumber.setError("Required Field");
            userMobileNumber.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            userMobileNumber.setErrorEnabled(true);
        } else {
            userMobileNumber.setErrorEnabled(false);
            Log.d(TAG, "forgotPassword | onCreate | In function forgotPassword ");

            Intent i = new Intent(LoginScreen.this, OTPDialogActivity.class);
            i.putExtra("Mobile", mobile);
            startActivityForResult(i, Activity_Code);

            //For testing
//            ForgotPasswordDialog forgotPasswordDialog = ForgotPasswordDialog.newInstance(mobile);
//            forgotPasswordDialog.show(getSupportFragmentManager(), "Forgot Password");
        }
    }

    public void startSignUpActivity() {
        Log.d(TAG, "createAccount | onCreate | In function createAccount ");
        Intent intent = new Intent(getApplicationContext(), SignUpScreen.class);
        startActivity(intent);
    }

    public void login() {
        //Toast.makeText(this, "Clicked on Forgot Password", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "login | In function login ");
        if (validation()) {

            progressBar.show(getSupportFragmentManager(), "Sign up");                    //Progress Bar Start

            database.checkMobilePassword(mobile, password, new CheckingNewInterface() {
                @Override
                public void callbackWithData(int result, final Map<String, Object> data) {
                    Log.d(TAG, "callbackWithData | Data => " + data);
                    Log.d(TAG, "callbackWithData| Contains key => " + data.containsKey("TYPE"));
                    Log.d(TAG, "callbackWithData | Type => " + data.get("TYPE"));

                    if (result == 0 && data.containsKey("TYPE")) {
                        if (Objects.equals(data.get("TYPE"), "OWNER")) {
                            Log.d(TAG, "callbackWithData | Owner is logged in.... ");
//                            COMMON.showSnackBar("Owner Log in Successful", loginView);
                            Intent i = new Intent(LoginScreen.this, OwnerLoginScreen.class);
                            startActivity(i);
                        } else {
                            Log.d(TAG, "callbackWithData | Not owner. ");
                        }
                    }else{
                        COMMON.showSnackBar("User Not found", loginView);
                    }
                    progressBar.dismiss();   //Progress Bar End
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult | Request => " + requestCode);
        Log.d(TAG, "onActivityResult | Result => " + resultCode);

        if (requestCode == Activity_Code) {
            if (resultCode == Activity.RESULT_OK) {
                //Valid user so Sign up user
                ForgotPasswordDialog forgotPasswordDialog = ForgotPasswordDialog.newInstance(mobile);
                forgotPasswordDialog.show(getSupportFragmentManager(), "Forgot Password");
            } else {
                //User not verified
                Log.d(TAG, "onActivityResult | Otp verification failed ");
            }
        }
    }

    //validation for mobile number and password
    @SuppressLint("ShowToast")
    public boolean validation() {

        mobile = mobileNumberEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if (mobile.equals("")) {
            userMobileNumber.setError("Required Field");
            userMobileNumber.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            userMobileNumber.setErrorEnabled(true);
            return false;
        } else if (mobile.length() != 10) {
            userMobileNumber.setError("Enter Valid Mobile Number");
            userMobileNumber.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            userMobileNumber.setErrorEnabled(true);
            Toast.makeText(getApplicationContext(), "" + mobile, Toast.LENGTH_SHORT);
            return false;
        } else if (password.equals("")) {
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

    public static View getView() {
        return loginView;
    }
}
