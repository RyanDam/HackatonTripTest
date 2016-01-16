package com.rstudio.hackatontrip.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;

import com.rstudio.hackatontrip.R;

public class InComingCallActivity extends AppCompatActivity {

    private Button btnAnswer;
    private Button btnReject;
    private Button btnHangup;


    public static VoiceCall call ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_coming_call);

        btnHangup = (Button) findViewById(R.id.button_hangup);
        btnHangup.setVisibility(View.INVISIBLE);
        btnReject = (Button) findViewById(R.id.button_reject);


        btnAnswer = (Button) findViewById(R.id.button_answer);
        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.pickupCall();
                btnAnswer.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
                btnHangup.setVisibility(View.VISIBLE);
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.rejectCall();
                finish();
            }
        });

        btnHangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.hangup();
                finish();
            }
        });

    }
}
