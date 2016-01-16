package com.rstudio.hackatontrip.controller;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.parse.ParseUser;
import com.rstudio.hackatontrip.R;
import com.rstudio.hackatontrip.model.User;
import com.rstudio.hackatontrip.view.behaviour.OnSwipeListener;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends AppCompatActivity {

    public static final int CALL_CODE = 5646;
    public static final int HANGUP_CODE = 2232;
    private ImageView mainImg;
    private String userId;
    private VoiceCall voiceCall;
    List<String> listRecentCall;

    public void setmCall(Call mCall) {
        this.mCall = mCall;
    }

    private Call mCall;

    public static final String KEY_ONLINE = "isOnline";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        listRecentCall = new ArrayList<String>();
        ParseUser user = ParseUser.getCurrentUser();
        userId = user.getObjectId();

        user.put(KEY_ONLINE,true);
        user.saveInBackground();

        userId = "xyz";

        voiceCall = new VoiceCall(userId,this);

        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

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
        String recipientId = "abc";
        voiceCall.callUser(recipientId);
    }


    public class VoiceCall{
        private Call call;
        private SinchClient sinchClient;
        ShowActivity context;

        public static final String APP_KEY = "69b5b187-c93d-4ea7-8ac1-b41d59ab11ad";
        public static final String APP_SECRET = "+3vdCTyJVEGmBPwFSkJF+g==";
        public static final String HOST = "sandbox.sinch.com";

        public Call getCall(){
            return call;
        }

        public VoiceCall(String userId, final ShowActivity context){
            this.context = context;
            sinchClient = Sinch.getSinchClientBuilder()
                    .context(context)
                    .userId(userId)
                    .applicationKey(APP_KEY)
                    .applicationSecret(APP_SECRET)
                    .environmentHost(HOST)
                    .build();

            sinchClient.setSupportCalling(true);
            sinchClient.startListeningOnActiveConnection();

            sinchClient.start();

            sinchClient.getCallClient().addCallClientListener(new CallClientListener() {
                @Override
                public void onIncomingCall(CallClient callClient, Call incomingCall) {
                    call = incomingCall;
                    //call.answer();

                    InComingCallActivity.call = call;

                    // InComingCallActivity.voiceCall = voiceCall;
                    startActivity(new Intent(context, InComingCallActivity.class));
                }
            });
        }

        public void callUser(String recipientId){
            call = sinchClient.getCallClient().callUser(recipientId);
            Log.d("test", "calling");

            context.setmCall(call);

            CallingActivity.call = call;

            Intent intent = new Intent(context,CallingActivity.class);
            startActivityForResult(intent, CALL_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CALL_CODE:
                if (HANGUP_CODE == resultCode) mCall.hangup();
                    break;
        }
    }
}
