package com.example.quizapp.viewmodel;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.quizapp.features.dashboard.dto.Quiz;
import com.example.quizapp.network.FlowResponse;
import com.example.quizapp.repo.QuizRepo;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class QuizViewModel extends ViewModel {
    private final QuizRepo quizRepo;
    private final Context context;

    @Inject
    public QuizViewModel(QuizRepo quizRepo, @ApplicationContext Context context) {
        this.quizRepo = quizRepo;
        this.context = context;
    }

    public MutableLiveData< FlowResponse<Quiz>> sendQuizRequest(){
        MutableLiveData< FlowResponse<Quiz>> mutableLiveData = new MutableLiveData<>();
        quizRepo.sendQuizListRequest(mutableLiveData);
        return mutableLiveData;
    }

}
