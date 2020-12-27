package com.example.dream_team.owner.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dream_team.R;
import com.example.dream_team.common_activities.COMMON;
import com.example.dream_team.constants.Constant;
import com.example.dream_team.modal_class.CONSTANTS;
import com.example.dream_team.owner.Adapter.EmployeeAdapter;
import com.example.dream_team.owner.Adapter.employeeObject;
import com.example.dream_team.owner.dialog_fragments.AddUpdateEmployeeDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddEmployeeScreen extends AppCompatActivity implements View.OnClickListener {
    public final String TAG = "Dream_Team | AddEmpScrn";
    private RecyclerView employee_rv;
    FloatingActionButton addEmpFab;
    SearchView searchBar;
    ImageView edit;
    LinearLayoutManager linearLayoutManager;
    EmployeeAdapter employeeAdapter;
    ArrayList<Object> list = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee_screen);
        initialization();
    }

    private void initialization() {
        db = FirebaseFirestore.getInstance();

        employee_rv = findViewById(R.id.employee_rv);
        addEmpFab = findViewById(R.id.fab);
       // edit = findViewById(R.id.editEmployee);

       // edit.setOnClickListener(this);
        addEmpFab.setOnClickListener(this);
        searchBar = findViewById(R.id.search_view);

        //seachbar
        searchBar.setQueryHint("Search...");
        searchBar.clearFocus();
        searchBar.onActionViewExpanded();

        //setting adapter
        linearLayoutManager = new LinearLayoutManager(this);
        employee_rv.setLayoutManager(linearLayoutManager);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                employeeAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                AddUpdateEmployeeDialog addUpdateEmployeeDialog = AddUpdateEmployeeDialog.newInstance(null, Constant.ADD);
                addUpdateEmployeeDialog.show(getSupportFragmentManager(), "Add employee");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        final String documentName = COMMON.getSharedData(CONSTANTS.document_name());

        db.collection(CONSTANTS.whole_db()).document(documentName).collection(CONSTANTS.emp_cred())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Toast.makeText(AddEmployeeScreen.this, "Failed to fetch data from firestore", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onEvent: " + error.toString());
                            return;
                        }else if (!queryDocumentSnapshots.isEmpty()){
                            ArrayList<Object> list2 = new ArrayList<>();

                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Log.d(TAG, "onEvent: Data => " + document.getData());
                                String name, type, number, status;

                                name = document.getString(CONSTANTS.name());
                                type = document.getString(CONSTANTS.type());
                                number = document.getString(CONSTANTS.mobile());
                                status = document.getString(CONSTANTS.status());

                                if(!type.equals("OWNER")) list2.add(new employeeObject(name,type,number, status));
                            }

                            list = list2;
                            employeeAdapter = new EmployeeAdapter(AddEmployeeScreen.this, list);
                            employee_rv.setAdapter(employeeAdapter);
                        }else {
                            Log.d(TAG, "onEvent: Data not exists...");
                        }
                    }
                });
    }
}