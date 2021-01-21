package com.durusappIslamicLecturesApp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.kalert.KAlertDialog;

import com.durusappIslamicLecturesApp.R;

import com.durusappIslamicLecturesApp.models.LoginRequest;
import com.durusappIslamicLecturesApp.models.LoginResponse;
import com.durusappIslamicLecturesApp.network.ApiEndPoints;
import com.durusappIslamicLecturesApp.network.RetrofitBuilder;


import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    LinearLayout loginbtn;
    EditText username, password;
    TextView no_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        no_account = findViewById(R.id.tv_no_account);
        loginbtn = findViewById(R.id.ll_login);
        username = findViewById(R.id.edt_username);
        password = findViewById(R.id.edt_password);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        String name = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        username.setText(name);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });

        no_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void performLogin() {


        KAlertDialog pDialog = new KAlertDialog(LoginActivity.this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("تسجيل الدخول...");
        pDialog.setCancelable(false);
        pDialog.show();

        LoginRequest request = new LoginRequest(username.getText().toString().trim(), password.getText().toString().trim());

        final ApiEndPoints requestInterface = RetrofitBuilder.getRetrofitInstance().create(ApiEndPoints.class);
        Call<LoginResponse> loginResponseCall = requestInterface.performLogin(request);

        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                pDialog.dismissWithAnimation();

                LoginResponse myResponse = response.body();

                if (myResponse.isSuccess())
                {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", myResponse.getData().getUser_name());
                    editor.apply();
                    //Toasty.success(LoginActivity.this, myResponse.getMessage(), Toasty.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, CategoriesActivity.class);
                    startActivity(intent);
                }
                else
                    Toasty.error(LoginActivity.this, "خطأ في اسم المستخدم أو كلمة مرور!", Toasty.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                pDialog.dismiss();
            }
        });

    }
}