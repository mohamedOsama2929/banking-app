package com.eng.osos.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.eng.osos.bakingapp.models.Step;

import java.util.ArrayList;

public class StepDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        ArrayList<Step> stepArrayList = intent.getExtras().getParcelableArrayList("steps");
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        StepDetailFragment stepDetailFragment = (StepDetailFragment) supportFragmentManager.findFragmentById(R.id.step_detail_container);
        if (stepDetailFragment == null) {
            stepDetailFragment = StepDetailFragment.getInstance(stepArrayList, position);
            supportFragmentManager.beginTransaction().add(R.id.step_detail_container, stepDetailFragment).commit();
        }
    }
}
