package com.example.quizapp.network;

import com.example.quizapp.features.dashboard.dto.Quiz;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RetrofitService {
//    @Headers("isCacheAvailable : yes")
    @Headers({"isCacheAvailable: yes"})
    @GET("api.php")
    Call<Quiz> sendQuizListRequest(@Query("amount") int amount);
}
