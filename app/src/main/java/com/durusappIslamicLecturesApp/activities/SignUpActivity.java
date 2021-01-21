package com.durusappIslamicLecturesApp.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.developer.kalert.KAlertDialog;

import com.durusappIslamicLecturesApp.R;

import com.durusappIslamicLecturesApp.models.SignUpRequest;
import com.durusappIslamicLecturesApp.models.SignUpResponse;
import com.durusappIslamicLecturesApp.network.ApiEndPoints;
import com.durusappIslamicLecturesApp.network.RetrofitBuilder;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {


    EditText phone, password, confirm_password, username;
    LinearLayout signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        phone = findViewById(R.id.edt_phone);
        password = findViewById(R.id.edt_password);
        confirm_password = findViewById(R.id.edt_confirm_password);
        username = findViewById(R.id.edt_username);
        signup = findViewById(R.id.ll_sign_up);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignUp();
            }
        });



    }

    private boolean validatedFields() {
        boolean allValid = true;

        if (TextUtils.isEmpty(phone.getText())) {

            username.setError("لا يمكن أن يكون رقم الهاتف فارغًا!");
            return  false;
        }else

        if (phone.getText().toString().length() < 10) {

            username.setError("اختر رقم الهاتف يتكون من عشرة أحرف على الأقل!");
            return  false;
        }
        else
        if (TextUtils.isEmpty(username.getText())) {

            username.setError("اسم المستخدم لا يمكن أن يكون فارغا!");
            return  false;
        }
            else
        if (username.getText().toString().length() < 4) {

            username.setError("اختر اسم مستخدم يتكون من أربعة أحرف على الأقل!");
            return  false;
        }
        else
        if (TextUtils.isEmpty(password.getText())) {

            username.setError("كلمه المرور لا يمكن أن يكون فارغا!");
            return  false;
        }
            else
                if (password.getText().toString().equals(confirm_password.getText().toString().trim())) {

            confirm_password.setError("كلمة المرور غير متطابقة!");
            return  false;
        }
        else
        return allValid;
    }

    private void performSignUp() {


        KAlertDialog pDialog = new KAlertDialog(SignUpActivity.this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("تسجيل الدخول...");
        pDialog.setCancelable(false);
        pDialog.show();

        SignUpRequest request = new SignUpRequest(1,
                                                username.getText().toString().trim(),
                                                password.getText().toString().trim(),
                                                phone.getText().toString().trim());

        final ApiEndPoints requestInterface = RetrofitBuilder.getRetrofitInstance().create(ApiEndPoints.class);
        Call<SignUpResponse> call = requestInterface.performSignUp(request);

        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {

                pDialog.dismissWithAnimation();
                SignUpResponse response1 = response.body();
                if(response1.isSuccess())
                {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", response1.getData().getUser_name());
                    editor.putString("password", password.getText().toString().trim());
                    editor.apply();

                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                pDialog.dismissWithAnimation(true);

            }
        });
    }
}