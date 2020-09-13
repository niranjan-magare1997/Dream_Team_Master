package com.example.dream_team.owner.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dream_team.R;
import com.example.dream_team.constants.OwnerScreenConstant;
import com.example.dream_team.owner.activities.AddEmployeeScreen;
import com.example.dream_team.owner.activities.AddMenuScreen;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class OwnerAdapter extends PagerAdapter {
    private List<OwnerScreenConstant> models;
    private LayoutInflater layoutInflater;
    Context context;

    public OwnerAdapter(List<OwnerScreenConstant> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.row_ownerscreen_cardview, container, false);
        ImageView imageView;
        TextView title;
        Button button;

        title = view.findViewById(R.id.title);
        button = view.findViewById(R.id.button);
        imageView = view.findViewById(R.id.image);

        title.setText(models.get(position).getTitle());
        imageView.setImageResource(models.get(position).getImage());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 0)
                    Toast.makeText(context, "place order", Toast.LENGTH_SHORT).show();
                else if (position == 1) {
                    Intent intent = new Intent(context, AddEmployeeScreen.class);
                    context.startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(context, AddMenuScreen.class);
                    context.startActivity(intent);
                } else if (position == 3)
                    Toast.makeText(context, "add", Toast.LENGTH_SHORT).show();

            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
