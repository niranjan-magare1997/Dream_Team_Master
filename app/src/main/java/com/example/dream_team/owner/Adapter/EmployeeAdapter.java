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

import com.example.dream_team.R;
import com.example.dream_team.common_activities.DeleteDialogFragment;
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
    private ArrayList<Object> mList;
    private ArrayList<String> mListFull;
    private employeeObject employeeObject;

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<String> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(mListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (String item : mListFull) {
                    if (item.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mList.clear();
            mList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public EmployeeAdapter(Context activity, ArrayList<Object> list) {
        this.activity = activity;
        this.mList = list;
    }

    @NonNull
    @Override
    public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.row_employee, parent, false);
        return new EmployeeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeHolder holder, int position) {
        employeeObject = (com.example.dream_team.owner.Adapter.employeeObject) mList.get(position);

        holder.employeeName.setText(employeeObject.Name);
        holder.employeeNumber.setText(employeeObject.Number);
        holder.employeeType.setText(employeeObject.Type);
        if(employeeObject.Status.equals("true"))
            holder.status.setCardBackgroundColor(Color.GREEN);
        else
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
                DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
                FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                deleteDialogFragment.show(fragmentTransaction, "delete");
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

    public class EmployeeHolder extends RecyclerView.ViewHolder {
        TextView employeeName, employeeNumber, employeeType;
        CardView status;
        ImageView editEmp, deleteEmp;

        public EmployeeHolder(@NonNull View itemView) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.addEmployeeName);
            employeeNumber = itemView.findViewById(R.id.addEmployeeNumber);
            employeeType = itemView.findViewById(R.id.addEmployeeType);
            status = itemView.findViewById(R.id.statusCardView);
            editEmp = itemView.findViewById(R.id.editEmployee);
            deleteEmp = itemView.findViewById(R.id.deleteEmployee);
        }
    }
}
