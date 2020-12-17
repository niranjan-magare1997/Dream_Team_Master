package com.example.dream_team.owner.activities;

import android.os.Bundle;
import android.view.View;

import com.example.dream_team.R;
import com.example.dream_team.constants.Constant;
import com.example.dream_team.owner.Adapter.TableAdapter;
import com.example.dream_team.owner.dialog_fragments.AddUpdateTableDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddTableScreen extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton addTablefab;
    LinearLayoutManager linearLayoutManager;
    TableAdapter tableAdapter;
    ArrayList<String> list = new ArrayList<>();
    private RecyclerView table_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_table_screen);
        initialization();

    }

    private void initialization() {
        list.add("1");
        list.add("2");
        list.add("3");
        table_rv = findViewById(R.id.table_rv);
        addTablefab = findViewById(R.id.fab);
        addTablefab.setOnClickListener(this);
        //setting adapter
        linearLayoutManager = new LinearLayoutManager(this);
        table_rv.setLayoutManager(linearLayoutManager);
        tableAdapter = new TableAdapter(this, list);
        table_rv.setAdapter(tableAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                AddUpdateTableDialog addUpdateTableDialog = AddUpdateTableDialog.newInstance(null, Constant.ADD);
                addUpdateTableDialog.show(getSupportFragmentManager(), "Add employeee");
        }

    }
}