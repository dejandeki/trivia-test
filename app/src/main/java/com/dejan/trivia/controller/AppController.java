package com.dejan.trivia.controller;

import android.app.Application;

import com.android.volley.RequestQueue;

public class AppController extends Application {

    private static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;

    
}
