package com.eng.osos.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eng.osos.bakingapp.IgredientStepActivity;
import com.eng.osos.bakingapp.R;
import com.eng.osos.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;



public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;

    List<Recipe> recipeList;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_step_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.bindTo(recipeList.get(position));
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cv_recipe)
        CardView cardView;
        @BindView(R.id.list_recipe_name)
        TextView recipeName;
        @BindView(R.id.list_recipe_img)
        ImageView imageView;
        @BindView(R.id.list_recipe_servings)
        TextView recipeServings;

        @BindString(R.string.recipe_list_servings_text)
        String servingsText;

        private int currentId;

        MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void bindTo(@NonNull final Recipe recipe) {

            currentId = recipe.getId();
            String imageUrl = recipe.getImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.with(context)
                        .load(imageUrl)
                        .into(imageView);
            }
            String name = recipe.getName();
            recipeName.setText(name);
            int servings = recipe.getServings();
            recipeServings.setText(String.format(Locale.US, servingsText, servings));
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, IgredientStepActivity.class);
                    intent.putParcelableArrayListExtra("ingredients", recipe.getIngredients());
                    intent.putParcelableArrayListExtra("steps", recipe.getSteps());
                    context.startActivity(intent);
                }
            });
        }

    }
}
