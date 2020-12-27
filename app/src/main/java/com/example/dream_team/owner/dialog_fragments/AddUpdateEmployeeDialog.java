package com.example.dream_team.owner.dialog_fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.service.voice.AlwaysOnHotwordDetector;
import android.util.Log;
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
import com.example.dream_team.common_activities.DATABASE;
import com.example.dream_team.constants.Constant;
import com.example.dream_team.interfaces.CheckingNewInterface;
import com.example.dream_team.modal_class.CONSTANTS;
import com.example.dream_team.owner.activities.AddEmployeeScreen;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddUpdateEmployeeDialog extends DialogFragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    private final String TAG = "Dream_Team";
    private EditText nameEditText, numberEditText;
    private TextInputLayout nameLayout, numberLayout;
    private Switch statusSwitch;
    private Button saveEmployee;
    private byte addUpdateStauts;
    private ArrayList list;
    private String empName, empNumber, empStatus, empType;
    private Spinner spinner;
    private DATABASE database;

    public static AddUpdateEmployeeDialog newInstance(ArrayList list, byte flag) {
        AddUpdateEmployeeDialog addUpdateEmployeeDialog = new AddUpdateEmployeeDialog();
        addUpdateEmployeeDialog.addUpdateStauts = flag;
        addUpdateEmployeeDialog.list = list;
        return addUpdateEmployeeDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_update_employee_dialog, container);
        nameEditText = view.findViewById(R.id.emp_name);
        numberEditText = view.findViewById(R.id.emp_number);
        nameLayout = view.findViewById(R.id.input_layout_name);
        numberLayout = view.findViewById(R.id.input_layout_number);
        statusSwitch = view.findViewById(R.id.statusSwitch);
        saveEmployee = view.findViewById(R.id.btn_save);
        saveEmployee.setOnClickListener(this);

        //for update button
        if (Constant.UPDATE == addUpdateStauts) {
            saveEmployee.setText("Update");
            saveEmployee.setOnClickListener(this);
            nameEditText.setText(list.toString());
            numberEditText.setText(list.toString());
        }

        spinner = view.findViewById(R.id.typeChoice);
        spinner.setOnItemSelectedListener(this);

        database = new DATABASE();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if (Constant.ADD == addUpdateStauts) {
                    if (validateInputs()) {
                        Log.d(TAG, "onClick: Name => " + nameEditText.getText().toString());
                        Log.d(TAG, "onClick: Number => " + numberEditText.getText().toString());
                        Log.d(TAG, "onClick: statusSwitch => " + statusSwitch.isChecked());
                        Log.d(TAG, "onClick: Type => " + empType);

                        Map<String, Object> empInfo = new HashMap<>();
                        empInfo.put(CONSTANTS.name(), empName);
                        empInfo.put(CONSTANTS.mobile(), empNumber);
                        empInfo.put(CONSTANTS.status(), empStatus);
                        empInfo.put(CONSTANTS.type(), empType);

                        Log.d(TAG, "onClick: EMP Info => " + empInfo);

                        database.addEmployee(empInfo, new CheckingNewInterface() {
                            @Override
                            public void callbackWithData(int result, Map<String, Object> data) {
                                Log.d(TAG, "callbackWithData: Result => "+result);
                                Log.d(TAG, "callbackWithData: Data => "+data);

                                switch (result) {
                                    case 0:
                                        Log.d(TAG, "callbackWithData: Employee information inserted successfully => "+data);
                                        dismiss();
                                        break;
                                    case 1:
                                    case 2:
                                        Log.e(TAG, "callbackWithData: Failed to insert employee information");
                                        break;
                                    default:
                                        break;
                                }

                                if(data.containsKey(CONSTANTS.message()))
                                    Toast.makeText(getActivity(), data.get(CONSTANTS.message()).toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else if (Constant.UPDATE == addUpdateStauts)
                    Toast.makeText(getContext(), "At update", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private boolean validateInputs() {
        if (nameEditText.getText().toString().trim().length() == 0) {
            nameLayout.setError("Required Field");
            nameLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            nameLayout.setErrorEnabled(true);
            return false;
        } else if (numberEditText.getText().toString().trim().length() != 10) {
            numberLayout.setError("Required Field");
            numberLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
            numberLayout.setErrorEnabled(true);
            return false;
        } else {
            empName = nameEditText.getText().toString().trim();
            empNumber = numberEditText.getText().toString().trim();
            if (statusSwitch.isChecked()) empStatus = "true";
            else empStatus = "false";
            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getSelectedItem().toString()) {
            case "Owner":
                empType = "Owner";
                break;
            case "Waiter":
                empType = "Waiter";
                break;
            case "Chef":
                empType = "Chef";
                break;
            default:
                empType = "";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
