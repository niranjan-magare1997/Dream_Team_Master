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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.List;
import java.util.Map;

public class AddSubMenuDialog extends DialogFragment implements View.OnClickListener {

    private String TAG = "Dream_Team | AddSubMenuDialog";
    private EditText subCategoryEditText, rateEditText;
    private Button addSubButton;
    private byte status;
    private Spinner spinner;
    private DATABASE database;

    public static AddSubMenuDialog newInstance(byte addMainCategory) {
        AddSubMenuDialog addSubMenuDialog = new AddSubMenuDialog();
        addSubMenuDialog.status = addMainCategory;
        return addSubMenuDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = null;
        if (status == Constant.AddSubCategory) {
            database = new DATABASE();
            rootView = inflater.inflate(R.layout.add_sub_menu_dialog, container);
            spinner = rootView.findViewById(R.id.menuSpinner);
            subCategoryEditText = rootView.findViewById(R.id.subMenuEditText);
            rateEditText = rootView.findViewById(R.id.rateEditText);
            addSubButton = rootView.findViewById(R.id.addSubButton);
            addSubButton.setOnClickListener(this);
            fillSpinner();
            return rootView;
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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        String dish_name = subCategoryEditText.getText().toString().trim();
        String dish_rate = rateEditText.getText().toString().trim();
        String selected_category = spinner.getSelectedItem().toString();

        database.add_dish(dish_name, dish_rate, selected_category, new CheckingNewInterface() {
            @Override
            public void callbackWithData(int result, Map<String, Object> data) {
                if (result == 0) {
                    Toast.makeText(getContext(), "Dish added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to add dish", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
