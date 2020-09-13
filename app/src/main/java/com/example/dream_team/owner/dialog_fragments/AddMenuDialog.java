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

public class AddMenuDialog extends DialogFragment implements View.OnClickListener {
    private byte status;
    private EditText mainCategoryEditText;
    private Button addMainButton;
    private DATABASE database;
    private String TAG = "Dream_Team | DATABASE ";

    public static AddMenuDialog newInstance(byte addMainCategory) {
        AddMenuDialog addMenuDialog = new AddMenuDialog();
        addMenuDialog.status = addMainCategory;
        return addMenuDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = null;
        if (status == Constant.AddMainCategory) {
            database = new DATABASE();
            rootView = inflater.inflate(R.layout.activity_add_menu_dialog, container);
            mainCategoryEditText = rootView.findViewById(R.id.mainMenuEditText);
            addMainButton = rootView.findViewById(R.id.addmainButton);
            addMainButton.setOnClickListener(this);
            return rootView;
        }
        return rootView;
    }

    @Override
    public void onClick(View view) {
        String catagory = mainCategoryEditText.getText().toString().trim();
        database.addMenuCategory(catagory, new CALLBACK() {
            @Override
            public void callBackMethod(int result) {
                Log.d(TAG, "callBackMethod: | Result is :" + result);
                if (result == 0) {
                    Toast.makeText(getContext(), "Category added successfully", Toast.LENGTH_SHORT).show();
                    dismiss();
                }else {
                    Log.d(TAG, "onClick | callBackMethod | Failed to Add Category");
                }
            }
        });
    }
}