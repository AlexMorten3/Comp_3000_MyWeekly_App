package com.example.comp_3000_myweekly_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (findViewById(R.id.weeklyFragment) != null) {

            if (savedInstanceState != null) {
                return;
            }



            CreateWeeklyFragment firstFragment = new CreateWeeklyFragment();

            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.weeklyFragment, firstFragment).commit();

        }


    }
}