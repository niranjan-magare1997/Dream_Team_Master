package com.example.dream_team.owner.activities;

import android.os.Bundle;
import android.util.Log;

import com.example.dream_team.R;
import com.example.dream_team.common_activities.DATABASE;
import com.example.dream_team.interfaces.CheckingNewInterface;
import com.example.dream_team.owner.Adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EditCategoryScreen extends AppCompatActivity {
    private String TAG = "Dream_Team | AddUpdateDishDialog";
    private RecyclerView recyclerView;
    private DATABASE database;
    LinearLayoutManager linearLayoutManager;
    CategoryAdapter categoryAdapter;
    List<String> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_category_activity);
        initialization();
    }

    private void initialization() {
        recyclerView = findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        getCategories();
    }

    private void getCategories() {
        database = new DATABASE();
        database.addDish(new CheckingNewInterface() {
            @Override
            public void callbackWithData(int result, Map<String, Object> data) {
                if (result == 0 && data.containsKey("Data")) {
                    categoryList = (List<String>) data.get("Data");
                    categoryAdapter = new CategoryAdapter(getBaseContext(), categoryList);
                    recyclerView.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(categoryAdapter);
                } else {
                    Log.d(TAG, "callbackWithData | Failed to fetch categories.... ");
                }
            }
        });
    }
}
