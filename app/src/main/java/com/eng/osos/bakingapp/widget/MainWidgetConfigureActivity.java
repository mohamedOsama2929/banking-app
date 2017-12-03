package com.eng.osos.bakingapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eng.osos.bakingapp.R;
import com.eng.osos.bakingapp.interfaces.RetrofitAPI;
import com.eng.osos.bakingapp.models.Ingredient;
import com.eng.osos.bakingapp.models.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The configuration screen for the {@link MainWidget MainWidget} AppWidget.
 */
public class MainWidgetConfigureActivity extends Activity {
    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.button)
    Button btn_ok;
    @BindView(R.id.widget_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.textView)
    TextView textView;
    List<Recipe> recipeList;
    private static final String PREFS_NAME = "com.eng.osos.bakingapp.widget.MainWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private RadioButton radioButton;
//    EditText mAppWidgetText;
//    View.OnClickListener mOnClickListener = new View.OnClickListener() {
//        public void onClick(View v) {
    //   final Context context = MainWidgetConfigureActivity.this;
//
//            // When the button is clicked, store the string locally
//            String widgetText = mAppWidgetText.getText().toString();
//            saveTitlePref(context, mAppWidgetId, widgetText);
//
//            // It is the responsibility of the configuration activity to update the app widget
//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//            MainWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);
//
//            // Make sure we pass back the original appWidgetId
//            Intent resultValue = new Intent();
//            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
//            setResult(RESULT_OK, resultValue);
//            finish();
//        }
//    };

    public MainWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.main_widget_configure);
        ButterKnife.bind(this);
        getRecipe();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = mRadioGroup.getCheckedRadioButtonId();
              if (selectedId==-1){
                  selectedId=0;
              }
                StringBuilder sb = new StringBuilder();
                sb.append("* " + recipeList.get(selectedId).getName());
                for (Ingredient ingredient : recipeList.get(selectedId).getIngredients()) {
                    String name = ingredient.getIngredient();
                    double quantity = ingredient.getQuantity();
                    String measure = ingredient.getMeasure();
                    sb.append("\n - ");
                    sb.append(name + " " + quantity + " " + measure);
                }
                final Context context = MainWidgetConfigureActivity.this;
                saveTitlePref(context, mAppWidgetId, sb.toString());
                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                MainWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);
                //            // Make sure we pass back the original appWidgetId
         Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
            }
        });
        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

    }

    private void getRecipe() {
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
                recipeList = response.body();
                btn_ok.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                for (int i = 0; i < recipeList.size(); i++) {
                    String name = recipeList.get(i).getName();
                    RadioButton radioButton = new RadioButton(getApplicationContext());
                    radioButton.setText(name);
                    radioButton.setTextColor(Color.parseColor("#FF000000"));
                    radioButton.setId(i);
                    RadioGroup.LayoutParams rprms = new RadioGroup.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    mRadioGroup.addView(radioButton, rprms);
                }

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                btn_ok.setVisibility(View.GONE);
                textView.setText(getString(R.string.networkstate));
                Toast.makeText(getApplicationContext(), getString(R.string.networkstate), Toast.LENGTH_SHORT).show();

            }
        });
    }
}

