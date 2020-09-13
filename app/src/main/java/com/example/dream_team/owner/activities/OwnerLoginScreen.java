package com.example.dream_team.owner.activities;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dream_team.R;
import com.example.dream_team.common_activities.LoginScreen;
import com.example.dream_team.common_activities.ProfileScreenActivity;
import com.example.dream_team.constants.OwnerScreenConstant;
import com.example.dream_team.owner.Adapter.OwnerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class OwnerLoginScreen extends AppCompatActivity {
    public Toolbar toolbar;
    ViewPager viewPager;
    OwnerAdapter adapter;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    List<OwnerScreenConstant> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login_screen);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setTitle("Owner DashBoard");
        Initialization();
    }

    private void Initialization() {
        models = new ArrayList<>();
        models.add(new OwnerScreenConstant(R.drawable.place_order_img,"Place Order"));
        models.add(new OwnerScreenConstant(R.drawable.add_emp_img,"Add  Employee"));
        models.add(new OwnerScreenConstant(R.drawable.add_menu_img,"Add  Category And Dish"));
        models.add(new OwnerScreenConstant(R.drawable.add_table_img,"Add Table"));

        adapter = new OwnerAdapter(models,this);
        viewPager =findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(105,0,105,0);
        viewPager.setPageMargin(50);
        Integer[] clr = {
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorBlueAccent),
                getResources().getColor(R.color.colorBlueAccentDark),
                getResources().getColor(R.color.colorGreenAccent)
        };
        colors = clr;
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if(position < (adapter.getCount()-1) && position < (colors.length-1))
//                    viewPager.setBackgroundColor((Integer)argbEvaluator.evaluate(positionOffset,colors[position],colors[position+1]));
//                else
//                    viewPager.setBackgroundColor(colors[colors.length-1]);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.profile:
                Intent intent1 = new Intent(this, ProfileScreenActivity.class);
                startActivity(intent1);
                return true;
            case R.id.logout:
                Intent intent = new Intent(this, LoginScreen.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
