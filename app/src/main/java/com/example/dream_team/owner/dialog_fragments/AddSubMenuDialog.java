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

public class AddSubMenuDialog extends DialogFragment implements View.OnClickListener {

    private EditText subCategoryEditText, rateEditText;
    private Button addSubButton;
    private byte status;

    public static AddSubMenuDialog newInstance(byte addMainCategory) {
        AddSubMenuDialog addSubMenuDialog = new AddSubMenuDialog();
        addSubMenuDialog.status= addMainCategory;
        return addSubMenuDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = null;
        if (status == Constant.AddSubCategory) {
            rootView = inflater.inflate(R.layout.add_sub_menu_dialog, container);
            subCategoryEditText = rootView.findViewById(R.id.subMenuEditText);
            rateEditText = rootView.findViewById(R.id.rateEditText);
            addSubButton = rootView.findViewById(R.id.addSubButton);
            addSubButton.setOnClickListener(this);
            return rootView;
        }
        return rootView;
    }

    @Override
    public void onClick(View view) {

    }

}
