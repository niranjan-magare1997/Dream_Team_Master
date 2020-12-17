package com.example.dream_team.owner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dream_team.R;
import com.example.dream_team.common_activities.DeleteDialogFragment;
import com.example.dream_team.constants.Constant;
import com.example.dream_team.owner.dialog_fragments.AddUpdateTableDialog;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableHolder> {
    private Context activity;
    private ArrayList<String> mList;

    public TableAdapter(Context activity, ArrayList<String> list) {
        this.activity = activity;
        this.mList = list;
    }

    @NonNull
    @Override
    public TableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.row_table, parent, false);
        return new TableHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableHolder holder, int position) {
        holder.tableNumber.setText("Table Number : "+mList.get(position)+"   Capacity : "+mList.get(position));
        holder.editTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUpdateTableDialog addUpdateTableDialog = AddUpdateTableDialog.newInstance(mList, Constant.UPDATE);
                FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                addUpdateTableDialog.show(fragmentTransaction, "update");
            }
        });
        holder.deleteTable.setOnClickListener(new View.OnClickListener() {
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

    public class TableHolder extends RecyclerView.ViewHolder {
        TextView tableNumber;
        ImageView editTable, deleteTable;

        public TableHolder(@NonNull View itemView) {
            super(itemView);
            tableNumber = itemView.findViewById(R.id.tableNumber);
            editTable = itemView.findViewById(R.id.editTable);
            deleteTable = itemView.findViewById(R.id.deleteTable);

        }
    }
}

