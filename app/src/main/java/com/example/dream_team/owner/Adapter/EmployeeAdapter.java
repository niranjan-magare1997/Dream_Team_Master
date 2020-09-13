package com.example.dream_team.owner.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dream_team.R;
import com.example.dream_team.constants.Constant;
import com.example.dream_team.owner.dialog_fragments.AddUpdateEmployeeDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeHolder> implements Filterable {
    private Context activity;
    private ArrayList<String> mList;
    private ArrayList<String> mListFull;

    public EmployeeAdapter(Context activity, ArrayList<String> list) {
        this.activity = activity;
        this.mList = list;
        this.mListFull = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.row_employee, parent, false);
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
                addUpdateEmployeeDialog.show(fragmentTransaction, "update");
            }
        });
        holder.deleteEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "at delte", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<String> filteredList=new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(mListFull);
            }
            else {
                String filterPattern=charSequence.toString().toLowerCase().trim();

                for(String item:mListFull){
                    if (item.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mList.clear();
            mList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };



public class EmployeeHolder extends RecyclerView.ViewHolder {
    TextView employeeName, employeeNumber;
    CardView status;
    ImageView editEmp, deleteEmp;

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
