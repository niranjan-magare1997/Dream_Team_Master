package com.example.dream_team.owner.activities;

import android.os.Bundle;

import com.example.dream_team.R;
import com.example.dream_team.constants.CategoryConstant;
import com.example.dream_team.constants.DishConstant;
import com.example.dream_team.owner.Adapter.DishAdapter;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EditDishScreen extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dish_screen);
        initialization();
    }

    private void initialization() {
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<CategoryConstant> categoryConstants = new ArrayList<>();

        ArrayList<DishConstant> dishConstants = new ArrayList<>();
        dishConstants.add(new DishConstant("Schezwan rice"));
        dishConstants.add(new DishConstant("Tripple Schezwan rice"));
        dishConstants.add(new DishConstant("Manchurian Rice"));
        CategoryConstant google = new CategoryConstant("Chinese",dishConstants);
        categoryConstants.add(google);

        ArrayList<DishConstant> dishConstants1 = new ArrayList<>();
        dishConstants1.add(new DishConstant("Masala Dosa"));
        dishConstants1.add(new DishConstant("Idali sambhar"));
        CategoryConstant ms = new CategoryConstant("South",dishConstants1);
        categoryConstants.add(ms);

        DishAdapter productAdapter = new DishAdapter(categoryConstants);
        recyclerView.setAdapter(productAdapter);

    }
}