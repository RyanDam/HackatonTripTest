package com.rstudio.hackatontrip.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rstudio.hackatontrip.R;
import com.rstudio.hackatontrip.model.User;
import com.rstudio.hackatontrip.view.behaviour.OnSwipeListener;

public class ShowActivity extends AppCompatActivity {

    private ImageView mainImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        mainImg = (ImageView) findViewById(R.id.main_img);
        mainImg.setOnTouchListener(new OnSwipeListener(this) {
            public void onSwipeRight() {
                nextUser();
            }

            public void onSwipeLeft() {
                nextUser();
            }
        });
        Button okBt = (Button) findViewById(R.id.ok_bt);
        Button nextBt = (Button) findViewById(R.id.next_bt);
        okBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeACall();
            }
        });
        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextUser();
            }
        });
    }

    private void nextUser() {
        // get some other random user
        // set current user
    }

    private void setCurrentUser(User u) {
        // set current img
    }

    private void makeACall() {
        // make a dirrect call to that user
    }

}
