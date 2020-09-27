package com.example.dream_team.common_activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {
    public String TAG = "Dream_Team | LoginScreen";
    private TextInputLayout userMobileNumber, userPassword;
    private TextInputEditText mobileNumberEditText, passwordEditText;
    public Button login_Button;
    public TextView forgotPassword, createAccount;
    public CheckBox rememberMeCheckbox;
    private DATABASE database;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor myEdit;
    private String mobile = "";
    private String password = "";
    public static boolean rememberMeCheckedOrNot;
    private ProgressDialogFragment progressBar;
    private static CustomToast customToast;
    private int Activity_Code = 102;
    private CONSTANTS constants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Log.d(TAG, "onCreate | Activity created ");

        constants = new CONSTANTS();

        //creation of object of customtoast class
        if (customToast == null) {
            customToast = new CustomToast(this);
        }
        initialization();
        checkAlreadyLogin();
    }

    private void checkAlreadyLogin() {
        String userToken = LoginScreen.getSharedData(constants.TOKEN());
        String docName = LoginScreen.getSharedData(constants.DOCUMENT_NAME());
        String remember = LoginScreen.getSharedData(constants.REMEMBER());
        String userType = LoginScreen.getSharedData(constants.TYPE());

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

    public void initialization() {
        userMobileNumber = findViewById(R.id.userName);
        userPassword = findViewById(R.id.userPassword);

        sharedPreferences = getSharedPreferences("DREAM_TEAM_DATA", MODE_PRIVATE);
        myEdit = sharedPreferences.edit();

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
        database = new DATABASE();
        if (!MainActivity.isNetworkAvailable(this)) {
            //toast called by our custom method toast() by passing simple string.
            customToast.toast("Please check your Internet Connection!");
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


//        Intent fpIntent = new Intent(LoginScreen.this, ForgotPasswordDialog.class);
//        startActivity(fpIntent);
        //check the validation for mobile number
        //get that mobile number and send otp
        // call otpDialog activity using intent
        //get result from that activity and then call forgotPasswordDialog.show()
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

            progressBar.show(getSupportFragmentManager(), "Sign up");                    //Progress Bar Start
//            database.checkUserExist(mobile, password, new CALLBACK() {
//                @Override
//                public void callBackMethod(int result) {
//                    Log.d(TAG, "login | callBackMethod | Result => " + result);
//                    if (result == 0) {
//                        Intent intent = new Intent(getApplicationContext(), OwnerLoginScreen.class);
//                        intent.putExtra("DOC_NAME", getSharedData("DOC_NAME"));
//                        intent.putExtra("TOKEN", getSharedData("TOKEN"));
//                        mobileNumberEditText.setText("");
//                        passwordEditText.setText("");
//                        userMobileNumber.setErrorEnabled(false);
//                        userPassword.setErrorEnabled(false);
//                        customToast.toast("LOGGED IN SUCCESSFULLY");
//                        startActivity(intent);
//                    } else if (result == 1) {
//                        customToast.toast("USER DOES NOT EXIST");
//                        Log.d(TAG, "login | callBackMethod | User not exist ");
//                    } else if (result == 2) {
//                        Log.e(TAG, "login | callBackMethod | Exception while checking user ");
//                    }
//                    progressBar.dismiss();   //Progress Bar End
//                }
//            });

            database.checkMobilePassword(mobile, password, new CheckingNewInterface() {
                @Override
                public void callbackWithData(int result, final Map<String, Object> data) {
                    Log.d(TAG, "callbackWithData | Data => " + data);
                    Log.d(TAG, "callbackWithData| Contains key => " + data.containsKey("TYPE"));
                    Log.d(TAG, "callbackWithData | Type => " + data.get("TYPE"));

                    if (result == 0 && data.containsKey("TYPE")) {
                        if (data.get("TYPE").equals("OWNER")) {
                            Log.d(TAG, "callbackWithData | Owner is logged in.... ");
                            Intent i = new Intent(LoginScreen.this, OwnerLoginScreen.class);
                            startActivity(i);
                        } else {
                            Log.d(TAG, "callbackWithData | Not owner. ");
                        }
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
