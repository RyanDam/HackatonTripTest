package com.rstudio.hackatontrip.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.rstudio.hackatontrip.R;
import com.rstudio.hackatontrip.model.User;
import com.rstudio.hackatontrip.utils.UserUtils;
import com.rstudio.hackatontrip.view.behaviour.OnSwipeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ShowActivity extends AppCompatActivity {

    private Context mContext;

    private ImageView mainImg;
    private String userId;
    private ParseUser currentUser;
    private ParseUser currentTargetUser;
    private VoiceCall call;
    private ProgressBar mainProgess;
    private ArrayList<String> previousUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        mContext = this;


        previousUser = new ArrayList<>();

        currentUser = ParseUser.getCurrentUser();

        userId = currentUser.getObjectId();

        call = new VoiceCall(this,userId);

        call.listeningCall();

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
        mainProgess = (ProgressBar) findViewById(R.id.main_progess);
        mainProgess.setMax(100);
        mainProgess.setVisibility(View.GONE);

        ImageButton userBt = (ImageButton) findViewById(R.id.accout_bt);
        userBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, UserActivity.class));
            }
        });

        nextUser();
    }

    private void nextUser() {
        // get some other random user
        // set current user
        if (currentTargetUser != null) previousUser.add(currentTargetUser.getObjectId());

        Log.d("USEr", "nextUser");

        HashMap<String, Object> params = new HashMap<>();
        params.put("code", userId);
        params.put("recentChoose", previousUser);
        ParseCloud.callFunctionInBackground("ramdomUser", params, new FunctionCallback<String>() {
            @Override
            public void done(String object, ParseException e) {
                if (null == e) {
                    setTargetUser("" + object);
                } else Log.d("USEr", "nextUser " + "fails");
            }
        });
    }

    private void setTargetUser(final String id) {
        // set current img
        Log.d("USEr", "setTargetUser " + id);
        ParseQuery<ParseUser> query = User.getQuery();
        query.getInBackground(id, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (null == e) {
                    currentTargetUser = object;
                    resetUser(currentTargetUser);
                } else {
                    Log.d("USEr", "setTargetUser " + id + " fails");
                    e.printStackTrace();
                }
            }
        });
    }

    private void resetUser(ParseUser u) {
        Log.d("Current Target", "" + u.getObjectId());
        ParseFile img = (ParseFile) u.get("avatar");
        img.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                mainImg.setImageBitmap(UserUtils.getBitmapFromBytes(data));
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer percentDone) {
                Log.d("Load img", "" + percentDone + "/100");
                if (percentDone == 0) mainProgess.setVisibility(View.VISIBLE);
                else if (percentDone == 100) mainProgess.setVisibility(View.GONE);
                mainProgess.setProgress(percentDone);
            }
        });
    }

    private void makeACall() {
        // make a dirrect call to that user
        String recipientId = "";
        call.callUser(recipientId);
    }

    @Override
    public void onBackPressed() {
        setResult(LoginActivity.EXIT_CODE, new Intent());
        finish();
    }
}
