package com.example.dream_team.owner.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dream_team.R;
import com.example.dream_team.constants.Constant;
import com.example.dream_team.owner.dialog_fragments.AddUpdateEmployeeDialog;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeHolder> {
    private Context activity;
    private ArrayList<String> mList = new ArrayList<>();
    public EmployeeAdapter(Context activity, ArrayList<String> list) {
        this.activity = activity;
        this.mList = list;
    }

    @NonNull
    @Override
    public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.employee_row,parent,false);
        return new EmployeeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeHolder holder, int position) {
        holder.employeeName.setText(mList.get(position));
        holder.employeeNumber.setText(mList.get(position));
        holder.status.setCardBackgroundColor(Color.RED);
        holder.editEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUpdateEmployeeDialog addUpdateEmployeeDialog = AddUpdateEmployeeDialog.newInstance(mList, Constant.UPDATE);
                FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                addUpdateEmployeeDialog.show(fragmentTransaction,"update");
            }
        });
        holder.deleteEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity,"at delte",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class EmployeeHolder extends RecyclerView.ViewHolder {
        TextView employeeName,employeeNumber;
        CardView status;
        ImageView editEmp,deleteEmp;
        public EmployeeHolder(@NonNull View itemView) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.addEmployeeName);
            employeeNumber = itemView.findViewById(R.id.addEmployeeNumber);
            status = itemView.findViewById(R.id.statusCardView);
            editEmp = itemView.findViewById(R.id.editEmployee);
            deleteEmp = itemView.findViewById(R.id.deleteEmployee);

        }
    }
}
