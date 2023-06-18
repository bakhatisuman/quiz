package com.example.quizapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;


@HiltAndroidApp
public class MainApplication extends Application {

    @Override
    public void onCreate() {
    initTimberForLog();
        super.onCreate();
    }


    private void initTimberForLog() {
//        if (BuildConfig.DEBUG) {
            // DebugTree has all usual logging functionality
            Timber.plant(new Timber.DebugTree() {
                @Nullable
                @Override
                protected String createStackElementTag(@NonNull StackTraceElement element) {
                    return String.format("[L:%s] [%s:%s]",
                            element.getLineNumber(),
                            element.getClassName(),
                            element.getMethodName());

//                    return super.createStackElementTag(element);
                }

                @Override
                protected void log(int priority, String tag, @NonNull String message, Throwable t) {

                    super.log(priority, "test", message, t);
                }

            });
//        }
    }
}

