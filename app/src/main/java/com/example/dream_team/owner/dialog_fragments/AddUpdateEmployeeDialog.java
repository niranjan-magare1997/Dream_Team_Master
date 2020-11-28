package com.example.dream_team.owner.dialog_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.dream_team.R;
import com.example.dream_team.constants.Constant;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddUpdateEmployeeDialog extends DialogFragment implements View.OnClickListener {
    private EditText nameEditText, numberEditText;
    private TextInputLayout nameLayout, numberLayout;
    private Switch statusSwitch;
    private Button saveEmployee;
    private Spinner spinner;
    private byte addUpdateStauts;
    private ArrayList list;

    public static AddUpdateEmployeeDialog newInstance(ArrayList list, byte flag) {
        AddUpdateEmployeeDialog addUpdateEmployeeDialog = new AddUpdateEmployeeDialog();
        addUpdateEmployeeDialog.addUpdateStauts = flag;
        addUpdateEmployeeDialog.list = list;
        return addUpdateEmployeeDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_update_employee, container);

        nameEditText = view.findViewById(R.id.emp_name);
        numberEditText = view.findViewById(R.id.emp_number);
        nameLayout = view.findViewById(R.id.input_layout_name);
        numberLayout = view.findViewById(R.id.input_layout_number);
        statusSwitch = view.findViewById(R.id.statusSwitch);
        saveEmployee = view.findViewById(R.id.btn_save);
        saveEmployee.setOnClickListener(this);
        spinner = view.findViewById(R.id.typeChoice);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.typeChoice, R.layout.spinner_choose_color);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_text_color);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //get String here
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        }));


        //for update button
        if (Constant.UPDATE == addUpdateStauts) {
            saveEmployee.setText("Update");
            saveEmployee.setOnClickListener(this);
            nameEditText.setText(list.toString());
            numberEditText.setText(list.toString());
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
