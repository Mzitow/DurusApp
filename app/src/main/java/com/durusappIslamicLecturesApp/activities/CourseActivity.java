package com.durusappIslamicLecturesApp.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.durusappIslamicLecturesApp.R;
import com.durusappIslamicLecturesApp.adapters.DarsAdapter;
import com.durusappIslamicLecturesApp.models.CategoryModel;
import com.durusappIslamicLecturesApp.models.ScholarsModel;
import com.durusappIslamicLecturesApp.network.ApiEndPoints;

import com.durusappIslamicLecturesApp.network.RetrofitBuilder;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.durusappIslamicLecturesApp.utils.Constants.FROM_CATAGORIES;
import static com.durusappIslamicLecturesApp.utils.Constants.FROM_SCHOLARS;

public class CourseActivity extends AppCompatActivity implements DarsAdapter.DarsOnClick {

    String categoryName;
    int categoryId;
    TextView no_courses;
    boolean isFromCategory;
    ScholarsModel.Scholar currentScholar = new ScholarsModel.Scholar();
    TextView title;

    RecyclerView rv_dars;
    SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        no_courses = findViewById(R.id.tv_no_dars);
        rv_dars = findViewById(R.id.rv_courses);
        spinKitView = findViewById(R.id.spin_kit_2);
        title = findViewById(R.id.tv_category_or_scholar);

        SharedPreferences mPreferences = getSharedPreferences("currentScholar", MODE_PRIVATE);
        Gson gson = new Gson();
        String schola = mPreferences.getString("scholar", "");
        currentScholar = gson.fromJson(schola, ScholarsModel.Scholar.class);

        Bundle bundle = getIntent().getExtras();


        if(bundle != null)
        {
            categoryName =bundle.getString("categoryName","");
            categoryId = bundle.getInt("categoryId",0);
            isFromCategory = bundle.getBoolean("isFromCategory", false);

        }


        if (isFromCategory) {
            spinKitView.setVisibility(View.VISIBLE);
            title.setText(categoryName);
            fetchAllLessonsInCategories(categoryId);
        }
        else
        {
            spinKitView.setVisibility(View.GONE);
            title.setText(currentScholar.getScholarName());
            if (currentScholar != null)
            sendDataToRecyclerview(currentScholar.getDars());
        }
    }

    private void sendDataToRecyclerview(List<ScholarsModel.Dar> dars) {
        if  ( dars.size() <= 0)
        {
            no_courses.setVisibility(View.VISIBLE);
        }
        else {
            DarsAdapter darsAdapter = new DarsAdapter(CourseActivity.this, dars, FROM_SCHOLARS, this);
            rv_dars.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rv_dars.setHasFixedSize(true);
            rv_dars.setAdapter(darsAdapter);
        }

    }

    private void fetchAllLessonsInCategories(int categoryId) {

        final ApiEndPoints requestInterface = RetrofitBuilder.getRetrofitInstance().create(ApiEndPoints.class);
        Call<CategoryModel> coursesInTheCategory = requestInterface.fetchCourseByCategory(categoryId);
        coursesInTheCategory.enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                spinKitView.setVisibility(View.GONE);

                CategoryModel categoryModel = response.body();
                if (categoryModel != null) {
                    if (categoryModel.getData().size() > 0) {
                        sendToRecyclerview(categoryModel.getData());
                    }
                    else
                    {
                        no_courses.setVisibility(View.VISIBLE);
                    }
                }else
                    no_courses.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {

            }
        });


    }

    private void sendToRecyclerview(List<ScholarsModel.Dar> data) {

        DarsAdapter darsAdapter = new DarsAdapter(CourseActivity.this, data, FROM_CATAGORIES, this);
        rv_dars.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_dars.setHasFixedSize(true);
        rv_dars.setAdapter(darsAdapter);
    }

    @Override
    public void darsOnClickListener(ScholarsModel.Dar dar) {

        SharedPreferences pref = getSharedPreferences("currentScholar", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        // Convert the object to json, to enable being saved to SharedPreferences.
        Gson gson = new Gson();
        String sch = gson.toJson(dar);
        // Save the converted object to the sharedPref.
        editor.putString("currentdars",sch);
        // commit the changes,
        editor.apply();

        Intent intent = new Intent(CourseActivity.this, LessonsActivity.class);
        startActivity(intent);

    }
}