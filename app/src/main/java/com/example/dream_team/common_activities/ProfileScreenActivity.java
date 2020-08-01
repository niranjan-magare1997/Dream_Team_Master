package com.example.dream_team.common_activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dream_team.R;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileScreenActivity extends AppCompatActivity {
    TextView name, phone, address, GST, adhar, hotelName, changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        name = findViewById(R.id.empName);
        phone = findViewById(R.id.empMobileNumber);
        address = findViewById(R.id.empAddress);
        GST = findViewById(R.id.empGST);
        adhar = findViewById(R.id.empAdhar);
        hotelName = findViewById(R.id.hotelName);
        changePassword = findViewById(R.id.changePasswordLabel);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileScreenActivity.this, "changeP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editProfile(View view) {
    }
}