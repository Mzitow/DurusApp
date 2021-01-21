
package com.durusappIslamicLecturesApp.network;


import com.durusappIslamicLecturesApp.models.CategoriesResponse;
import com.durusappIslamicLecturesApp.models.CategoryModel;
import com.durusappIslamicLecturesApp.models.LoginRequest;
import com.durusappIslamicLecturesApp.models.LoginResponse;

import com.durusappIslamicLecturesApp.models.ScholarsModel;
import com.durusappIslamicLecturesApp.models.SignUpRequest;
import com.durusappIslamicLecturesApp.models.SignUpResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

import static com.durusappIslamicLecturesApp.utils.Constants.CATEGORIES_END_POINT;
import static com.durusappIslamicLecturesApp.utils.Constants.FETCH_ALL_SCHOLARS;
import static com.durusappIslamicLecturesApp.utils.Constants.FETCH_COURSE_BY_CATEGORY;
import static com.durusappIslamicLecturesApp.utils.Constants.LOGIN_END_POINT;
import static com.durusappIslamicLecturesApp.utils.Constants.SIGNUP_END_POINT;



public interface ApiEndPoints {

    @GET(CATEGORIES_END_POINT)
    Call<CategoriesResponse> fetchAllCategories();

    @POST(LOGIN_END_POINT)
    Call<LoginResponse> performLogin(@Body LoginRequest request);

    @POST(SIGNUP_END_POINT)
    Call<SignUpResponse> performSignUp(@Body SignUpRequest request);

    @GET(FETCH_ALL_SCHOLARS)
    Call<ScholarsModel> fetchAllScholars();


    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST(FETCH_COURSE_BY_CATEGORY)
    Call<CategoryModel> fetchCourseByCategory(@Field("category_id") int categoryId);

    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    @Streaming
    @GET
    Call<ResponseBody> downloadYourProgressFile(@Url String fileUrl);



//    @FormUrlEncoded
//    @Headers("Accept: application/json")
//    @POST("login")
//    Call<LoginResponse> loginUser(@Field("user_name") String user, @Field("password") String pass);


//    @Headers("Accept: application/json")
//    @POST("users")
//    Call<SignUpResponse> signUpUser(@Body SignUpReques signUpRequest);

}
