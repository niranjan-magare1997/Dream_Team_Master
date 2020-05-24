package com.example.dream_team;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private TextInputLayout userMobileNumber, userPassword, userAddress, userAdhar, userGST, userHotelName;
    private TextInputEditText mobileNumberEditText, passwordEditText, addressEditText, adharNumberEditText, gstEditText, hotelNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        initialization();
    }

    public void initialization() {
        userMobileNumber = findViewById(R.id.userNumberSignUp);
        userPassword = findViewById(R.id.createPassword);
        userAddress = findViewById(R.id.userAddress);
        userAdhar = findViewById(R.id.userAddress);
        userGST = findViewById(R.id.userGST);

        userHotelName = findViewById(R.id.userHotelName);

        mobileNumberEditText = findViewById(R.id.mobileSignup);
        passwordEditText = findViewById(R.id.passwordSignup);
        addressEditText = findViewById(R.id.address);
        adharNumberEditText = findViewById(R.id.adharNumber);
        gstEditText = findViewById(R.id.gstNumber);
        hotelNameEditText = findViewById(R.id.hotelName);

        spinner = findViewById(R.id.spinnerChoice);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.dropdownChoice, R.layout.spinner_choose_color);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_text_color);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getSelectedItem().toString().equals("Owner")) {
            Toast.makeText(this, "same", Toast.LENGTH_SHORT).show();
            findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
        } else findViewById(R.id.linearLayout).setVisibility(View.INVISIBLE);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void signUp(View view) {
        OTPDialog otpdialog = new OTPDialog();
        otpdialog.show(getSupportFragmentManager(), "OTP");
        otpdialog.setCancelable(false);
    }


    //validation for mob num and password
//    public boolean validation() {
//        if (mobileNumberEditText.getText().toString().equals("")) {
//            userMobileNumber.setError("Required Field");
//            userMobileNumber.setErrorTextColor(ColorStateList.valueOf(Color.RED));
//            userMobileNumber.setErrorEnabled(true);
//            return false;
//        } else if (mobileNumberEditText.getText().length()!=10) {
//            userMobileNumber.setError("Enter Valid Mobile Number");
//            userMobileNumber.setErrorTextColor(ColorStateList.valueOf(Color.RED));
//            userMobileNumber.setErrorEnabled(true);
//            Toast.makeText(getApplicationContext(),""+mobileNumberEditText.getText().length(),Toast.LENGTH_SHORT);
//            return false;
//        } else if (passwordEditText.getText().toString().equals("")) {
//            userPassword.setError("Required Field");
//            userPassword.setErrorTextColor(ColorStateList.valueOf(Color.RED));
//            userPassword.setErrorEnabled(true);
//            return false;
//        } else return true;
//    }


}
