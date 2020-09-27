package com.example.dream_team.common_activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dream_team.R;
import com.example.dream_team.interfaces.CheckingNewInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Map;

public class ForgotPasswordDialog extends DialogFragment implements View.OnClickListener {

    private static String mobileNumber;
    private String TAG = "Dream_Team | ForgotPasswordDialog";
    private TextInputLayout newPasswordLayout, confirmPasswordLayout;
    private TextInputEditText newPaswordEditText, confirmPasswordEditText;
    private Button setPwd;
    private CustomToast customToast;
    private TextView forgotPasswordTextView;
    private DATABASE database;
    //  String mobileNumber;

    public static ForgotPasswordDialog newInstance(String number) {
        Log.d("Dream_Team | ", "ForgotPasswordDialog | newInstance | Mobile => " + number);
        ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog();
        ForgotPasswordDialog.mobileNumber = number;
        return forgotPasswordDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_forgot_password_dialog, container);
        newPasswordLayout = view.findViewById(R.id.passwordLayout);
        confirmPasswordLayout = view.findViewById(R.id.retypePasswordLayout);
        newPaswordEditText = view.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = view.findViewById(R.id.retypePassword);
        forgotPasswordTextView = view.findViewById(R.id.forgotPasswordTextView);
        setPwd = view.findViewById(R.id.forgotPasswordButton);
        setPwd.setOnClickListener(this);
        database = new DATABASE();
        if (customToast == null) {
            customToast = new CustomToast(getActivity());
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick | Mobile Number => " + mobileNumber);
        newPasswordLayout.setErrorEnabled(false);
        confirmPasswordLayout.setErrorEnabled(false);

        //customToast.toast("Password Set Successfully!");

        String newPSWD = newPaswordEditText.getText().toString().trim();
        String confirmPSWD = confirmPasswordEditText.getText().toString().trim();

        if (newPSWD.equals("")) {
            newPasswordLayout.setError("Enter password");
            newPasswordLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            newPasswordLayout.setErrorEnabled(true);
        } else if (confirmPSWD.equals("")) {
            confirmPasswordLayout.setError("Enter password");
            confirmPasswordLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            confirmPasswordLayout.setErrorEnabled(true);
        } else if (!newPSWD.equals(confirmPSWD)) {
            newPasswordLayout.setError("Both passwords must be same");
            newPasswordLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            newPasswordLayout.setErrorEnabled(true);
            confirmPasswordLayout.setError("Both passwords must be same");
            confirmPasswordLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            confirmPasswordLayout.setErrorEnabled(true);
        } else {
            database.updatePassword(mobileNumber, newPSWD, new CheckingNewInterface() {
                @Override
                public void callbackWithData(int result, Map<String, Object> data) {
                    Log.d(TAG, "callbackWithData | Result ===> " + result);
                    Log.d(TAG, "callbackWithData | Data ===> " + data);

                    if (result == 0) {
                        //All good
                        customToast.toast("Password updated successfully");
                    } else {
                        //Gadbad zaali re baba
                        customToast.toast("Unable to update password");
                    }

                }
            });
        }
    }
}