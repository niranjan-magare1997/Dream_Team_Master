package com.example.dream_team.owner.Adapter;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dream_team.R;
import com.example.dream_team.constants.CategoryConstant;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class CategoryVH extends GroupViewHolder {
    TextView categoryTv;
    ImageView imageView;

    public CategoryVH(View itemView) {
        super(itemView);
        categoryTv = itemView.findViewById(R.id.comp_tv);
        imageView = itemView.findViewById(R.id.arrow);
    }

    public void bind(CategoryConstant categoryConstant) {
        categoryTv.setText(categoryConstant.getTitle());
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotateAnimation = new RotateAnimation(360, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(300);
        rotateAnimation.setFillAfter(true);
        imageView.setAnimation(rotateAnimation);
    }

    private void animateCollapse() {
        RotateAnimation rotateAnimation = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(300);
        rotateAnimation.setFillAfter(true);
        imageView.setAnimation(rotateAnimation);
    }
}
