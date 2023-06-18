package com.example.quizapp.features.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;

import com.example.quizapp.R;
import com.example.quizapp.features.dashboard.fragment.HomeFragment;
import com.example.quizapp.features.splash.SplashFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt("some_int", 0);

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragmentContainer, SplashFragment.class, bundle)
                    .commit();

            waitingTimeForSplash();


        }



        /*QuizViewModel vm = new ViewModelProvider(this).get(QuizViewModel.class);
        vm.sendProvinceListRequest();*/

    }


    private void waitingTimeForSplash(){
        int secondsDelayed = 3;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                // open Home Fragment
                moveFromSplashToHome();
            }
        }, secondsDelayed * 1000);
    }

    private void moveFromSplashToHome(){

        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }









}