package com.rstudio.hackatontrip.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parse.ParseUser;
import com.rstudio.hackatontrip.R;

public class UserActivity extends AppCompatActivity {

    ParseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mUser = ParseUser.getCurrentUser();

    }
}
