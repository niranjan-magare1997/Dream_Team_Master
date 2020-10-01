package com.example.dream_team.owner.dialog_fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dream_team.R;
import com.example.dream_team.common_activities.DATABASE;
import com.example.dream_team.constants.Constant;
import com.example.dream_team.interfaces.CheckingNewInterface;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddUpdateDishDialog extends DialogFragment implements View.OnClickListener {

    private String TAG = "Dream_Team | AddUpdateDishDialog";
    private EditText dishEditText, rateEditText;
    private Button addDishButton;
    private byte addUpdatestatus;
    private Spinner spinner;
    private DATABASE database;

    public static AddUpdateDishDialog newInstance(byte dishStatus) {
        AddUpdateDishDialog addUpdateDishDialog = new AddUpdateDishDialog();
        addUpdateDishDialog.addUpdatestatus = dishStatus;
        return addUpdateDishDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.add_sub_menu_dialog, container);
        spinner = rootView.findViewById(R.id.menuSpinner);
        dishEditText = rootView.findViewById(R.id.subMenuEditText);
        rateEditText = rootView.findViewById(R.id.rateEditText);
        addDishButton = rootView.findViewById(R.id.addSubButton);
        addDishButton.setOnClickListener(this);
        database = new DATABASE();
        if (addUpdatestatus == Constant.ADD) {
            fillSpinner();
        } else if (addUpdatestatus == Constant.UPDATE) {
            addDishButton.setText("UPDATE");
            addDishButton.setOnClickListener(this);
        }
        return rootView;
    }

    private void fillSpinner() {
        database.addDish(new CheckingNewInterface() {
            @Override
            public void callbackWithData(int result, Map<String, Object> data) {
                Log.d(TAG, "callbackWithData | Result ==> " + result);
                if (result == 0 && data.containsKey("Data")) {
                    List<String> categories = (List<String>) data.get("Data");
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_text_color, categories);
                    arrayAdapter.setDropDownViewResource(R.layout.spinner_choose_color);
                    spinner.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "callbackWithData | Failed to fetch categories.... ");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addSubButton:
                if (addUpdatestatus == Constant.ADD) {
                    String dish_name = dishEditText.getText().toString().trim();
                    String dish_rate = rateEditText.getText().toString().trim();
                    String selected_category = spinner.getSelectedItem().toString();
                    database.add_dish(dish_name, dish_rate, selected_category, new CheckingNewInterface() {
                        @Override
                        public void callbackWithData(int result, Map<String, Object> data) {
                            if (result == 0) {
                                Toast.makeText(getContext(), "Dish added successfully", Toast.LENGTH_SHORT).show();
                                dismiss();
                            } else {
                                Toast.makeText(getContext(), "Failed to add dish", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (addUpdatestatus == Constant.UPDATE) {
                    Toast.makeText(view.getContext(), "update", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
