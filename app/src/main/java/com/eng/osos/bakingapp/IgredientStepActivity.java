package com.eng.osos.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.eng.osos.bakingapp.adapters.StepsAdapter;
import com.eng.osos.bakingapp.models.Ingredient;
import com.eng.osos.bakingapp.models.Step;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IgredientStepActivity extends AppCompatActivity {
    boolean mTwoPane;

    private Intent intent;

    @BindView(R.id.recipe_details_ingredients)
    TextView txtIngredients;
    @BindView(R.id.recipe_details_steps)
    RecyclerView rv_step;
    @BindString(R.string.recipe_details_ingredients_header)
    String ingredientsListHeader;
    ArrayList<Step> stepArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_igredient_step);
        ButterKnife.bind(this);
        if (findViewById(R.id.recipe_step_container) == null) {
            mTwoPane = false;
        } else {
            mTwoPane = true;
        }
        intent = getIntent();
        ArrayList<Ingredient> ingredients = intent.getExtras().getParcelableArrayList("ingredients");
        shoeIngredients(ingredients);
        stepArrayList = intent.getExtras().getParcelableArrayList("steps");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_step.setLayoutManager(layoutManager);
        rv_step.setAdapter(new StepsAdapter(this, stepArrayList, mTwoPane));

    }

    private void shoeIngredients(ArrayList<Ingredient> ingredients) {
        StringBuilder sb = new StringBuilder();
        sb.append("* " + ingredientsListHeader);

        for (Ingredient ingredient : ingredients) {

            String name = ingredient.getIngredient();
            double quantity = ingredient.getQuantity();
            String measure = ingredient.getMeasure();

            sb.append("\n - ");
            sb.append(name + " " + quantity + " " + measure);
        }
        txtIngredients.setText(sb);
    }

    public void viewStepDetailfragment(ArrayList<Step> stepArrayList, int position) {
        StepDetailFragment detailFragment = StepDetailFragment.getInstance(stepArrayList, position);
        getSupportFragmentManager().beginTransaction().replace(R.id.recipe_step_container, detailFragment).commit();
    }


}
