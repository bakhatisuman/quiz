package com.example.quizapp.features.dashboard.fragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.quizapp.R;
import com.example.quizapp.features.dashboard.dto.QuizQuestion;
import com.example.quizapp.viewmodel.QuizViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }
    private QuizViewModel quixVm = null;

    ArrayList<QuizQuestion> questions;
    QuizQuestion question;
    String correctAnswer;



    private TextView option1, option2, option3, option4, questionCounter, tv_question, tv_timer;
    private Button nxtBtn , quizBtn;
    int index = 0;
    CountDownTimer timer;
    int totalCorrectAnswers = 0;




    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        quixVm = new ViewModelProvider(this).get(QuizViewModel.class);

        option1 = view.findViewById(R.id.option_1);
        option2 = view.findViewById(R.id.option_2);
        option3 = view.findViewById(R.id.option_3);
        option4 = view.findViewById(R.id.option_4);

        nxtBtn = view.findViewById(R.id.nextBtn);
        quizBtn = view.findViewById(R.id.quizBtn);

        option1.setOnClickListener(view15 -> makeSelected(view15));

        option2.setOnClickListener(view14 -> makeSelected(view14));


        option3.setOnClickListener(view13 -> makeSelected(view13));

        option4.setOnClickListener(view12 -> makeSelected(view12));

        nxtBtn.setOnClickListener(view1 -> {
            reset();
            if (index < questions.size()) {
                index++;
                setNextQuestion();
            } else {
                // todo show result fragment
            }
        });

        quizBtn.setOnClickListener(view16 -> {
            // todo get out of here.
        });








        questionCounter = view.findViewById(R.id.questionCounter);
        tv_question = view.findViewById(R.id.question);
        tv_timer = view.findViewById(R.id.timer);

        Random random = new Random();
        final int rand = random.nextInt(12);


        ObserverData();


        // Inflate the layout for this fragment
        return view;
    }


    private void makeSelected(View view){
        if (timer != null) {
            timer.cancel();
            TextView selected = (TextView) view;
            checkAnswer(selected);
        }

    }

    void showAnswer() {
        if (correctAnswer.equals(option1.getText().toString()))
            option1.setBackground(ContextCompat.getDrawable(requireContext(),(R.drawable.option_right)));
        else if (correctAnswer.equals(option2.getText().toString()))
            option2.setBackground(ContextCompat.getDrawable(requireContext(),(R.drawable.option_right)));
        else if (correctAnswer.equals(option3.getText().toString()))
            option3.setBackground(ContextCompat.getDrawable(requireContext(),(R.drawable.option_right)));
        else if (correctAnswer.equals(option4.getText().toString()))
            option4.setBackground(ContextCompat.getDrawable(requireContext(),(R.drawable.option_right)));
    }

    void reset() {
        option1.setBackground(ContextCompat.getDrawable(requireContext(),(R.drawable.option_unselected)));
        option2.setBackground(ContextCompat.getDrawable(requireContext(),(R.drawable.option_unselected)));
        option3.setBackground(ContextCompat.getDrawable(requireContext(),(R.drawable.option_unselected)));
        option4.setBackground(ContextCompat.getDrawable(requireContext(),(R.drawable.option_unselected)));
    }

    void checkAnswer(TextView textView) {
        String selectedAnswer = textView.getText().toString();
        if (selectedAnswer.equals(correctAnswer)) {
            totalCorrectAnswers++;
            textView.setBackground(ContextCompat.getDrawable(requireContext(),(R.drawable.option_right)));
        } else {
            showAnswer();
            textView.setBackground(ContextCompat.getDrawable(requireContext(),(R.drawable.option_wrong)));
        }
    }

    void setNextQuestion() {
        if (timer != null)
            timer.cancel();

        assert timer != null;
        timer.start();
        if (index < questions.size()) {
            questionCounter.setText(String.format("%d/%d", (index + 1), questions.size()));
            question = questions.get(index);
            tv_question.setText(question.getQuestion());
            correctAnswer = question.getCorrect_answer().trim();
            List<String> options=new ArrayList<>();
            options.addAll(question.getIncorrect_answers());
            options.add(question.getCorrect_answer().trim());

            Collections.shuffle(options);

            option1.setText(options.get(0));
            option2.setText(options.get(2));
            option3.setText(options.get(3));
            option4.setText(options.get(1));
        }
    }


    void resetTimer() {
        timer = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_timer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                // todo show result fragment.

            }
        };
    }







    private void ObserverData(){
        quixVm.sendQuizRequest().observe(getViewLifecycleOwner(), data -> {
            if (data.getLoading()){
                Timber.v("HomeFragment loading.");
            }
            if (data.getInternetAvailable()){
                Timber.v("HomeFragment Internet is available.");
            }

            if (data.getTokenExpired()){
                Timber.v("HomeFragment token is expired");
            }

            if (data.getError() != null && !data.getError().isEmpty() ){
                Timber.v("HomeFragment error" + data.getError());
            }

            if (data.getMessage() != null && !data.getMessage().isEmpty()){
                Timber.v("HomeFragment message" + data.getMessage());
            }


            if (data.getLdData() != null){
                questions = data.getLdData().getResults();
                resetTimer();
                setNextQuestion();

                Timber.v("HomeFragment mutableData" + data);
                Timber.v("HomeFragment mutableData" + data.getLdData().results.size());
            }
        });
    }


}