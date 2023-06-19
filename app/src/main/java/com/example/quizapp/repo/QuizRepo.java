package com.example.quizapp.repo;
import android.content.Context;
import androidx.lifecycle.MutableLiveData;

import com.example.quizapp.MainApplication;
import com.example.quizapp.features.dashboard.dto.Quiz;
import com.example.quizapp.network.FlowResponse;
import com.example.quizapp.network.RetroCallback;
import com.example.quizapp.network.RetroHelper;
import com.example.quizapp.network.RetrofitService;
import javax.inject.Inject;
import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Call;

public class QuizRepo {
        private final RetrofitService retrofitService;
        private final Context context;

        @Inject
        public QuizRepo(RetrofitService retrofitService, @ApplicationContext Context context) {
        this.retrofitService = retrofitService;
        this.context = context;
    }


    public void sendQuizListRequest(MutableLiveData<FlowResponse<Quiz>> liveData){
        RetroHelper<Quiz> retroHelper = new RetroHelper<>();



        Call<Quiz> call =  retrofitService.sendQuizListRequest(10);
        FlowResponse<Quiz> flowResponse = new FlowResponse<>();

        if (!MainApplication.hasNetwork()){
            flowResponse.setInternetAvailable(true);
            liveData.postValue(flowResponse);
        }

        retroHelper.enqueue(call, new RetroCallback() {
            @Override
            public void onLoading() {
                flowResponse.setLoading(true);
                liveData.postValue(flowResponse);

            }

            @Override
            public void onFinished() {
                flowResponse.setLoading(false);
                liveData.postValue(flowResponse);
            }

            @Override
            public void onSuccess(int code, Object response) {
                flowResponse.setLdData((Quiz) response);
                liveData.postValue(flowResponse);


            }

            @Override
            public void onError(int code, String message) {
                flowResponse.setError(message);
                liveData.postValue(flowResponse);

            }

            @Override
            public void onHttpException(String error, String errorBody) {
                flowResponse.setError(error);
                liveData.postValue(flowResponse);
            }

            @Override
            public void onSocketTimeoutException(String error) {
                flowResponse.setError(error);
                liveData.postValue(flowResponse);
            }

            @Override
            public void onIOException(String error) {
                flowResponse.setError(error);
                liveData.postValue(flowResponse);
            }

            @Override
            public void OnTokenExpired(String error) {
                flowResponse.setTokenExpired(true);
                flowResponse.setMessage(error);
                liveData.postValue(flowResponse);
            }
        });

    }

}
