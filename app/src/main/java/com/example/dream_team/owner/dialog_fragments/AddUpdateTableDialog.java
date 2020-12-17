package com.example.dream_team.owner.dialog_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dream_team.R;
import com.example.dream_team.constants.Constant;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddUpdateTableDialog extends DialogFragment implements View.OnClickListener {
    private TextView text;
    private EditText tableNumberEditText, capcityEditText;
    private TextInputLayout nameLayout, numberLayout;
    private Button saveTableBtn;
    private byte addUpdateStauts;
    private ArrayList list;

    public static AddUpdateTableDialog newInstance(ArrayList list, byte flag) {
        AddUpdateTableDialog addUpdateTableDialog = new AddUpdateTableDialog();
        addUpdateTableDialog.addUpdateStauts = flag;
        addUpdateTableDialog.list = list;
        return addUpdateTableDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_update_table_dialog, container);
        text = view.findViewById(R.id.text);
        tableNumberEditText = view.findViewById(R.id.tableNumber);
        capcityEditText = view.findViewById(R.id.capacity);
        nameLayout = view.findViewById(R.id.input_layout_name);
        numberLayout = view.findViewById(R.id.input_layout_number);
        saveTableBtn = view.findViewById(R.id.btn_save);
        saveTableBtn.setOnClickListener(this);
        //for update button
        if (Constant.UPDATE == addUpdateStauts) {
            saveTableBtn.setText("Update");
            text.setText("Update Details");
            saveTableBtn.setOnClickListener(this);
            tableNumberEditText.setText(list.toString());
            capcityEditText.setText(list.toString());
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if (Constant.ADD == addUpdateStauts)
                    Toast.makeText(getContext(), "At add", Toast.LENGTH_SHORT).show();
                else if (Constant.UPDATE == addUpdateStauts)
                    Toast.makeText(getContext(), "At update", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
