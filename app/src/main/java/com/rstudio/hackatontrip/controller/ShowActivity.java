package com.rstudio.hackatontrip.controller;

import android.content.Context;
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

import java.util.List;

public class ShowActivity extends AppCompatActivity {

    private ImageView mainImg;
    private String userId;
    private VoiceCall voiceCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        //userId = ParseUser.getCurrentUser().getObjectId();
        userId = "abc";

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
        String recipientId = "xyz";
        voiceCall.callUser(recipientId);
    }


    public class VoiceCall{
        private Call call;
        private SinchClient sinchClient;

        public static final String APP_KEY = "69b5b187-c93d-4ea7-8ac1-b41d59ab11ad";
        public static final String APP_SECRET = "+3vdCTyJVEGmBPwFSkJF+g==";
        public static final String HOST = "sandbox.sinch.com";


        public VoiceCall(String userId, Context context){
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

            sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
        }

        public void callUser(String recipientId){
            if (call == null){
                call = sinchClient.getCallClient().callUser(recipientId);
                Log.d("test","calling");
            }
        }

        public void hangupCall(){
            if (call != null){
                call.hangup();
                call = null;
            }
        }



        private class SinchCallListener implements CallListener{

            @Override
            public void onCallProgressing(Call call) {
                Log.d("test","onCallProgressing");
            }

            @Override
            public void onCallEstablished(Call call) {
                Log.d("test","connected");
                setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            }

            @Override
            public void onCallEnded(Call endCall) {
                Log.d("test","Call End");
                call = null;
                setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            }

            @Override
            public void onShouldSendPushNotification(Call call, List<PushPair> list) {

            }
        }

        private class SinchCallClientListener implements CallClientListener {
            @Override
            public void onIncomingCall(CallClient callClient, Call incomingCall) {
                Log.d("test","answer the call");
                call = incomingCall;
                call.answer();
                call.addCallListener(new SinchCallListener());
            }
        }
    }
}
