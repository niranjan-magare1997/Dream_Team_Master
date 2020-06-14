package com.example.dream_team.owner.dialog_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.dream_team.R;
import com.example.dream_team.constants.Constant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddMenuDialog extends DialogFragment implements View.OnClickListener {
    private byte status;
    private EditText mainCategoryEditText;
    private Button addMainButton;

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

    }
}