package com.example.dream_team.owner.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dream_team.R;
import com.example.dream_team.constants.Constant;
import com.example.dream_team.owner.Adapter.EmployeeAdapter;
import com.example.dream_team.owner.dialog_fragments.AddUpdateEmployeeDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddEmployeeScreen extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView employee_rv;
    FloatingActionButton addEmpFab;
    SearchView searchBar;
    ImageView edit;
    LinearLayoutManager linearLayoutManager;
    EmployeeAdapter employeeAdapter;
    ArrayList<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee_screen);
        initialization();

    }

    private void initialization() {

        list.add("Joy");
        list.add("Flash");
        list.add("Happy");
        employee_rv = findViewById(R.id.employee_rv);
        addEmpFab = findViewById(R.id.fab);
       // edit = findViewById(R.id.editEmployee);

       // edit.setOnClickListener(this);
        addEmpFab.setOnClickListener(this);
        searchBar = findViewById(R.id.search_view);
        //seachbar
        searchBar.setQueryHint("Search...");
        searchBar.clearFocus();
        searchBar.onActionViewExpanded();
        //setting adapter
        linearLayoutManager = new LinearLayoutManager(this);
        employee_rv.setLayoutManager(linearLayoutManager);
        employeeAdapter = new EmployeeAdapter(this, list);
        employee_rv.setAdapter(employeeAdapter);


        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                employeeAdapter.getFilter().filter(s);
                return false;
            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                AddUpdateEmployeeDialog addUpdateEmployeeDialog = AddUpdateEmployeeDialog.newInstance(null, Constant.ADD);
                addUpdateEmployeeDialog.show(getSupportFragmentManager(), "Add employeee");

        }

    }


}