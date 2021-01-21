package com.durusappIslamicLecturesApp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.durusappIslamicLecturesApp.R;

import com.durusappIslamicLecturesApp.adapters.CategoriesAdapter;
import com.durusappIslamicLecturesApp.adapters.ScholarAdapter;
import com.durusappIslamicLecturesApp.models.CategoriesResponse;
import com.durusappIslamicLecturesApp.models.ScholarsModel;
import com.durusappIslamicLecturesApp.network.ApiEndPoints;

import com.durusappIslamicLecturesApp.network.RetrofitBuilder;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesActivity extends AppCompatActivity implements CategoriesAdapter.CategoryOnClickListener, ScholarAdapter.ScholarOnClickListener {

    RecyclerView rv_categories_sheiks;
    SpinKitView spinKitView;
    ScholarsModel scholarsModel = new ScholarsModel();
    CategoriesResponse categoriesResponse = new CategoriesResponse();
    LinearLayout categories, scholars;

    boolean wasAtScholars = true;
    MutableLiveData<Boolean>  isCategoriesOpen = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);




        spinKitView = findViewById(R.id.spin_kit);
        rv_categories_sheiks = findViewById(R.id.rv_categories_sheiks);
        categories = findViewById(R.id.ll_categories);
        scholars = findViewById(R.id.ll_scholars);



        isCategoriesOpen.setValue(true);
        isCategoriesOpen.observe(CategoriesActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                {

                    categories.setBackgroundResource(R.drawable.golden_rectangle_rounded_coners);
                    scholars.setBackgroundResource(0);
                    wasAtScholars = false;
                    saveWasAtScholars(wasAtScholars);
                    fetchAllCategories();
                }
                else
                {
                    scholars.setBackgroundResource(R.drawable.golden_rectangle_rounded_coners);
                    categories.setBackgroundResource(0);
                    wasAtScholars = true;
                    saveWasAtScholars(wasAtScholars);
                    fetchAllScholars();
                }
            }
        });

        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               isCategoriesOpen.setValue(true);
                //sendToCategoriesAdapter(categoriesResponse.getData());

            }
        });

        scholars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCategoriesOpen.setValue(false);
                //sendToScholarsAdapter(scholarsModel.getData());

            }
        });

    }

    private void saveWasAtScholars(boolean wasAtScholars) {

        SharedPreferences pref = getSharedPreferences("currentScholar", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("wasAtScholars", wasAtScholars);
        editor.apply();
    }

    private void fetchAllScholars() {
        spinKitView.setVisibility(View.VISIBLE);

        final ApiEndPoints requestInterface = RetrofitBuilder.getRetrofitInstance().create(ApiEndPoints.class);
        Call<ScholarsModel> scholarsModelCall = requestInterface.fetchAllScholars();
        scholarsModelCall.enqueue(new Callback<ScholarsModel>() {
            @Override
            public void onResponse(Call<ScholarsModel> call, Response<ScholarsModel> response) {
                spinKitView.setVisibility(View.GONE);

                scholarsModel = response.body();

                if(scholarsModel != null )
                {
                    sendToScholarsAdapter(scholarsModel.getData());
                }
            }

            @Override
            public void onFailure(Call<ScholarsModel> call, Throwable t) {
                spinKitView.setVisibility(View.GONE);
            }
        });

    }

    private void sendToScholarsAdapter(List<ScholarsModel.Scholar> data) {
        spinKitView.setVisibility(View.GONE);
        ScholarAdapter scholarAdapter = new ScholarAdapter(CategoriesActivity.this, data,this);
        rv_categories_sheiks.setLayoutManager(new GridLayoutManager(CategoriesActivity.this, 2));
        rv_categories_sheiks.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        rv_categories_sheiks.setHasFixedSize(true);
        rv_categories_sheiks.setAdapter(scholarAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        {
            SharedPreferences mPreferences = getSharedPreferences("currentScholar", MODE_PRIVATE);

            wasAtScholars = mPreferences.getBoolean("wasAtScholars", false);

            if ( ( (scholarsModel.getData() != null) && (categoriesResponse.getData() != null) ))
            {
               if(wasAtScholars)
               {
                   sendToScholarsAdapter(scholarsModel.getData());
               }
                else
               {
                   sendToCategoriesAdapter(categoriesResponse.getData());

               }
            }
        }

    }

    private void fetchAllCategories() {

        spinKitView.setVisibility(View.VISIBLE);
        final ApiEndPoints requestInterface = RetrofitBuilder.getRetrofitInstance().create(ApiEndPoints.class);
        Call<CategoriesResponse> categoriesCall = requestInterface.fetchAllCategories();
        categoriesCall.enqueue(new Callback<CategoriesResponse>() {
           @Override
           public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
              categoriesResponse = response.body();
               spinKitView.setVisibility(View.GONE);

               if (categoriesResponse != null)
               {
                   sendToCategoriesAdapter(categoriesResponse.getData());
               }

           }

           @Override
           public void onFailure(Call<CategoriesResponse> call, Throwable t) {
               spinKitView.setVisibility(View.GONE);

           }
       });
    }

    private void sendToCategoriesAdapter(List<CategoriesResponse.Category> data) {
        spinKitView.setVisibility(View.GONE);
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(CategoriesActivity.this, data, this);
        rv_categories_sheiks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_categories_sheiks.setHasFixedSize(true);
        rv_categories_sheiks.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        rv_categories_sheiks.setAdapter(categoriesAdapter);
    }

    @Override
    public void categoryOnClick(CategoriesResponse.Category category) {
        Intent intent = new Intent(CategoriesActivity.this, CourseActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("categoryName", category.getCategory_name());
        bundle.putInt("categoryId", category.getId());
        bundle.putBoolean("isFromCategory", true);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void scholarOnClick(ScholarsModel.Scholar scholar) {

        Intent intent = new Intent(CategoriesActivity.this, CourseActivity.class);
        Bundle b = new Bundle();
        Log.d("TAG2", "onCreate: " +  scholar.getDars().size());
        saveToSharedPref(scholar);
        b.putBoolean("isFromCategory", false);

        intent.putExtras(b);
        startActivity(intent);

    }

    private void saveToSharedPref(ScholarsModel.Scholar scholar) {

            SharedPreferences pref = getSharedPreferences("currentScholar", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            // Convert the object to json, to enable being saved to SharedPreferences.
            Gson gson = new Gson();
            String sch = gson.toJson(scholar);
            // Save the converted object to the sharedPref.
            editor.putString("scholar",sch);
            // commit the changes,
            editor.apply();
    }
}