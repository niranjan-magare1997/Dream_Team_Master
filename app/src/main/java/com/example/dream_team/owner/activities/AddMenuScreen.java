package com.example.dream_team.owner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dream_team.R;
import com.example.dream_team.constants.Constant;
import com.example.dream_team.owner.dialog_fragments.AddUpdateCategoryDialog;
import com.example.dream_team.owner.dialog_fragments.AddUpdateDishDialog;

import androidx.appcompat.app.AppCompatActivity;

public class AddMenuScreen extends AppCompatActivity implements View.OnClickListener {
    private TextView addMain, addSub, editMain, editSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_screen);
        initialization();
    }

    private void initialization() {
        addMain = findViewById(R.id.addCategory);
        addSub = findViewById(R.id.addDish);

        editMain = findViewById(R.id.editCategory);
        editSub = findViewById(R.id.editDish);

        addMain.setOnClickListener(this);
        addSub.setOnClickListener(this);
        editSub.setOnClickListener(this);
        editMain.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addCategory:
                AddUpdateCategoryDialog addUpdateCategoryDialog = AddUpdateCategoryDialog.newInstance(Constant.ADD,null);
                addUpdateCategoryDialog.show(getSupportFragmentManager(), "Add Menu");
                break;
            case R.id.addDish:
                AddUpdateDishDialog addsubDialog = AddUpdateDishDialog.newInstance(Constant.ADD);
                addsubDialog.show(getSupportFragmentManager(), "Add Sub Menu");
                break;
            case R.id.editCategory:
              Intent intent = new Intent(this,EditCategoryScreen.class);
              startActivity(intent);
                break;
            case R.id.editDish:
                Intent intent1 = new Intent(this,EditDishScreen.class);
                startActivity(intent1);
                break;
        }
    }
}