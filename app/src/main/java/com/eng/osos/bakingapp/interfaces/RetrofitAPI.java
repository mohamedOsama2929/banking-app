package com.eng.osos.bakingapp.interfaces;

import com.eng.osos.bakingapp.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;



public interface RetrofitAPI {
    @GET("baking.json")
    Call<List<Recipe>> getJson();
}
