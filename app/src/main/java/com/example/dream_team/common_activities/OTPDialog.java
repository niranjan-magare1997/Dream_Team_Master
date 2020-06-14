package com.example.dream_team.common_activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.example.dream_team.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class OTPDialog extends DialogFragment {
    private PinEntryEditText pinEntryEditText;
    private Button verify;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.otp_dialog, container);
        pinEntryEditText = rootView.findViewById(R.id.pin_entry);
        verify = rootView.findViewById(R.id.verify);
        return rootView;
    }

}
