package com.example.dream_team.owner.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dream_team.R;
import com.example.dream_team.common_activities.DeleteDialogFragment;
import com.example.dream_team.constants.Constant;
import com.example.dream_team.constants.DishConstant;
import com.example.dream_team.owner.dialog_fragments.AddUpdateDishDialog;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DishVH extends ChildViewHolder implements View.OnClickListener {
    private TextView dishTV;
    private ImageView editDish,deleteDish;

    public DishVH(View itemView) {
        super(itemView);
        dishTV = itemView.findViewById(R.id.dishName);
        editDish = itemView.findViewById(R.id.editDishIV);
        deleteDish = itemView.findViewById(R.id.deleteDishIV);
        editDish.setOnClickListener(this);
        deleteDish.setOnClickListener(this);

    }

    public void bind(DishConstant dishConstant) {
        dishTV.setText(dishConstant.name);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.editDishIV:
                AddUpdateDishDialog addUpdateDishDialog = AddUpdateDishDialog.newInstance(Constant.UPDATE);
                FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                addUpdateDishDialog.show(fragmentTransaction, "updatedish");
                break;
            case R.id.deleteDishIV: DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
                FragmentManager fragmentManagerr = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                deleteDialogFragment.show(fragmentManagerr, "delete");
        }

    }
}
