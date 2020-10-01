package com.example.dream_team.owner.dialog_fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dream_team.R;
import com.example.dream_team.common_activities.DATABASE;
import com.example.dream_team.constants.Constant;
import com.example.dream_team.interfaces.CALLBACK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddUpdateCategoryDialog extends DialogFragment implements View.OnClickListener {
    private byte status;
    private EditText categoryEditText;
    private Button addCategoryButton;
    private DATABASE database;
    private String TAG = "Dream_Team | DATABASE ";
    private String categoryName;

    public static AddUpdateCategoryDialog newInstance(byte addMainCategory, String str) {
        AddUpdateCategoryDialog addUpdateCategoryDialog = new AddUpdateCategoryDialog();
        addUpdateCategoryDialog.status = addMainCategory;
        addUpdateCategoryDialog.categoryName = str;
        return addUpdateCategoryDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = null;
        database = new DATABASE();
        rootView = inflater.inflate(R.layout.activity_add_menu_dialog, container);
        categoryEditText = rootView.findViewById(R.id.categoryEditText);
        addCategoryButton = rootView.findViewById(R.id.addCategoryButton);
        addCategoryButton.setOnClickListener(this);
        if (status == Constant.UPDATE) {
            addCategoryButton.setText("Update");
            addCategoryButton.setOnClickListener(this);
            categoryEditText.setText(categoryName);
        }
        return rootView;
    }

    @Override
    public void onClick(View view) {
        String catagory = categoryEditText.getText().toString().trim();
        if (Constant.ADD == status)
            database.addMenuCategory(catagory, new CALLBACK() {
                @Override
                public void callBackMethod(int result) {
                    Log.d(TAG, "callBackMethod: | Result is :" + result);
                    if (result == 0) {
                        Toast.makeText(getContext(), "Category added successfully", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Log.d(TAG, "onClick | callBackMethod | Failed to Add Category");
                    }
                }
            });
        else if (Constant.UPDATE == status)
            Toast.makeText(getContext(), "At update", Toast.LENGTH_SHORT).show();

    }

}
