package com.eng.osos.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.eng.osos.bakingapp.adapters.RecipeAdapter;
import com.eng.osos.bakingapp.interfaces.RetrofitAPI;
import com.eng.osos.bakingapp.models.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    final String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.rv_main)
    RecyclerView recyclerView;
    @BindView(R.id.cl)
    ConstraintLayout constraintLayout;
    int spancount = 1;

    @Nullable
    private RecipesIdlingResource idlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spancount = 3;
        }
        if (isOnline()) {
            //create Retrofit Object
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            //use retrofit object by API
            RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
            Call<List<Recipe>> connection = retrofitAPI.getJson();
            connection.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    List<Recipe> recipeList = response.body();
                    mProgressBar.setVisibility(View.GONE);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, spancount);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(new RecipeAdapter(MainActivity.this, recipeList));
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    Snackbar.make(constraintLayout, getString(R.string.networkstate), Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            mProgressBar.setVisibility(View.GONE);
            Snackbar.make(constraintLayout, getString(R.string.networkstate), Snackbar.LENGTH_LONG).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new RecipesIdlingResource();
        }
        return idlingResource;
    }
}
