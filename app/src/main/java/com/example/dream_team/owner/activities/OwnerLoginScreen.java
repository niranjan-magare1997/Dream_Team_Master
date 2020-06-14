package com.example.dream_team.owner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dream_team.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class OwnerLoginScreen extends AppCompatActivity implements View.OnClickListener {
    public Toolbar toolbar;
    private CardView placeOrderCV, addMenuCV, addEmployeeCV, addTableCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login_screen);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Initialization();
    }

    private void Initialization() {
        addEmployeeCV = findViewById(R.id.addEmployee);
        addMenuCV = findViewById(R.id.addMenu);
        addTableCV = findViewById(R.id.addTable);
        placeOrderCV = findViewById(R.id.placeOrder);

        addTableCV.setOnClickListener(this);
        addMenuCV.setOnClickListener(this);
        addEmployeeCV.setOnClickListener(this);
        placeOrderCV.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.profile:
                return true;
            case R.id.logout:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.placeOrder:
                Toast.makeText(this, "place ordr", Toast.LENGTH_SHORT).show();
                break;
            case R.id.addTable:
                Toast.makeText(this, "add table", Toast.LENGTH_SHORT).show();
                break;
            case R.id.addMenu:
                Intent intent = new Intent(this, AddMenuScreen.class);
                startActivity(intent);
                break;
            case R.id.addEmployee:
                Toast.makeText(this, "add emp", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
