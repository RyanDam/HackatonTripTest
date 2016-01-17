package com.rstudio.hackatontrip.controller;

import android.content.Intent;
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

public class CallingActivity extends AppCompatActivity {

    public static Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        String recipientId = getIntent().getStringExtra(ShowActivity.KEY_RECIPIENTID);
        final TextView txt = (TextView) findViewById(R.id.textView_UserId);
        if (txt != null){
            txt.setText(recipientId);
        }

        final Button btnHangup = (Button) findViewById(R.id.btnHangup);
        btnHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(ShowActivity.HANGUP_CODE);
                finish();
            }
        });

        call.addCallListener(new CallListener() {
            public void onCallProgressing(Call call) {
                Log.d("test", "Call on progress");
            }

            @Override
            public void onCallEstablished(Call call) {
                Log.d("test", "call Establish");
                setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            }

            @Override
            public void onCallEnded(Call endCall) {
                Log.d("test", "call end");
                setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
                setResult(3487);
                finish();
            }

            @Override
            public void onShouldSendPushNotification(Call call, List<PushPair> list) {
            }
        });

    }
}
