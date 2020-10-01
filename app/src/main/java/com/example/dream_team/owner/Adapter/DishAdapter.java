package com.example.dream_team.owner.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dream_team.R;
import com.example.dream_team.constants.CategoryConstant;
import com.example.dream_team.constants.DishConstant;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class DishAdapter extends ExpandableRecyclerViewAdapter<CategoryVH, DishVH> {
    public DishAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public CategoryVH onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exp_rv_category, parent, false);
        return new CategoryVH(view);
    }

    @Override
    public DishVH onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exp_rv_dish, parent, false);
        return new DishVH(view);
    }

    @Override
    public void onBindChildViewHolder(DishVH holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final DishConstant product = (DishConstant) group.getItems().get(childIndex);
        holder.bind(product);
    }

    @Override
    public void onBindGroupViewHolder(CategoryVH holder, int flatPosition, ExpandableGroup group) {
        final CategoryConstant company = (CategoryConstant) group;
        holder.bind(company);
    }

}