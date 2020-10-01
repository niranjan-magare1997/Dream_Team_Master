package com.example.dream_team.owner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dream_team.R;
import com.example.dream_team.constants.Constant;
import com.example.dream_team.owner.dialog_fragments.AddUpdateCategoryDialog;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private Context activity;
    private List<String> mList;

    public CategoryAdapter(Context activity, List<String> mList) {
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.row_category, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, final int position) {
        holder.categoryName.setText(mList.get(position));
        holder.editCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUpdateCategoryDialog addUpdateEmployeeDialog = AddUpdateCategoryDialog.newInstance(Constant.UPDATE, mList.get(position));
                FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                addUpdateEmployeeDialog.show(fragmentTransaction, "update");
            }
        });
        holder.deleteCat.setOnClickListener(new View.OnClickListener() {
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

    public class CategoryHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView editCat, deleteCat;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            editCat = itemView.findViewById(R.id.editCategory);
            deleteCat = itemView.findViewById(R.id.deleteCategory);
        }
    }
}
