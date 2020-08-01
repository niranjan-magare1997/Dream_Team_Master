package com.example.dream_team.common_activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dream_team.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ForgotPasswordDialog extends DialogFragment implements View.OnClickListener {

    private TextInputLayout newPasswordLayout, confirmPasswordLayout;
    private TextInputEditText newPaswordEditText, confirmPasswordEditText;
    private Button setPwd;
    private CustomToast customToast;
    private TextView forgotPasswordTextView;

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
        if (customToast == null) {
            customToast = new CustomToast(getActivity());
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        customToast.toast("Password Set Successfully!");
    }
}