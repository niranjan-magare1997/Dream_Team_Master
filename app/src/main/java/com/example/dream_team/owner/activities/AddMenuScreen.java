package com.example.dream_team.owner.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dream_team.R;
import com.example.dream_team.constants.Constant;
import com.example.dream_team.owner.dialog_fragments.AddMenuDialog;
import com.example.dream_team.owner.dialog_fragments.AddSubMenuDialog;

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
        addMain = findViewById(R.id.addMainCategory);
        addSub = findViewById(R.id.addSubCategory);

        editMain = findViewById(R.id.editMainCategory);
        editSub = findViewById(R.id.editSubCategory);

        addMain.setOnClickListener(this);
        addSub.setOnClickListener(this);
        editSub.setOnClickListener(this);
        editMain.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addMainCategory:
                AddMenuDialog addMenuDialog = AddMenuDialog.newInstance(Constant.AddMainCategory);
                addMenuDialog.show(getSupportFragmentManager(), "Add Menu");
                break;
            case R.id.addSubCategory:
                AddSubMenuDialog addsubDialog = AddSubMenuDialog.newInstance(Constant.AddSubCategory);
                addsubDialog.show(getSupportFragmentManager(), "Add Sub Menu");
                break;
        }
    }
}