package com.rstudio.hackatontrip.controller;

import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rstudio.hackatontrip.R;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

public class InComingCallActivity extends AppCompatActivity {

    private Button btnAnswer;
    private Button btnReject;
    private Button btnHangup;

    //public static ShowActivity.VoiceCall voiceCall;
    public static Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_coming_call);

        String recipientId = getIntent().getStringExtra(ShowActivity.KEY_RECIPIENTID);

        final TextView txt = (TextView) findViewById(R.id.textView_UserId);
        if (txt != null){
            txt.setText(recipientId);
        }

        call.addCallListener(new CallListener() {
            @Override
            public void onCallProgressing(Call call) {
                Log.d("test", "Call on progress");
            }

            @Override
            public void onCallEstablished(Call call) {
                Log.d("test", "call Establish");

                //AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                //audioManager.setSpeakerphoneOn(true);
                setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                //setVolumeControlStream(AudioManager.MODE_IN_COMMUNICATION);
                
               // audioManager.adjustStreamVolume();
            }

            @Override
            public void onCallEnded(Call endCall) {
                Log.d("test", "call end");
                //AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                //audioManager.setSpeakerphoneOn(false);
                setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
                finish();
            }

            @Override
            public void onShouldSendPushNotification(Call call, List<PushPair> list) {
            }
        });


        btnHangup = (Button) findViewById(R.id.button_hangup);
        btnHangup.setVisibility(View.INVISIBLE);
        btnReject = (Button) findViewById(R.id.button_reject);


        btnAnswer = (Button) findViewById(R.id.button_answer);


        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.answer();
                btnAnswer.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
                btnHangup.setVisibility(View.VISIBLE);
            }
        });

        btnHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.hangup();
                finish();
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.hangup();
                finish();
            }
        });

        final Button btnSpeakerPhone = (Button) findViewById(R.id.btnSpeakerPhone);
        btnSpeakerPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                if (audioManager.isSpeakerphoneOn()){
                    audioManager.setSpeakerphoneOn(false);
                    btnSpeakerPhone.setText("TURN ON SPEAKER PHONE");
                }else{
                    audioManager.setSpeakerphoneOn(true);
                    btnSpeakerPhone.setText("TURN OFF SPEAKER PHONE");
                }
            }
        });

    }
}
