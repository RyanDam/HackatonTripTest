package com.rstudio.hackatontrip.controller;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by AnLuu on 16/01/2016.
 */
public class VoiceCall {
    public static final String APP_KEY = "69b5b187-c93d-4ea7-8ac1-b41d59ab11ad";
    public static final String APP_SECRET = "+3vdCTyJVEGmBPwFSkJF+g==";
    public static final String HOST = "sandbox.sinch.com";

    private Context context;

    private ShowActivity showActivity;
    private InComingCallActivity inComingCallActivity;

    private SinchClient sinchClient;
    private Call call;
    private String callState;
    private boolean answer;
    private boolean reject;



    public VoiceCall(Context context, String userId){
        this.context = context;
        voiceCall = this;
        resetState();
        sinchClient = Sinch.getSinchClientBuilder()
                .context(context)
                .userId(userId)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(HOST)
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.start();
    }


    //Listen for incoming call
    public void listeningCall(){
        sinchClient.startListeningOnActiveConnection();
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
    }

    //Stop listening
    public void stopListeningCall(){
        sinchClient.stopListeningOnActiveConnection();
    }

    //Call to user who has recipientId
    public boolean callUser(String recipientId){
        if (call == null){
            call = sinchClient.getCallClient().callUser(recipientId);
            return true;
        }
        return false;
    }

    public boolean hangup(){
        if (call != null){
            call.hangup();
            call = null;
            return true;
        }

        return false;
    }

    public void pickupCall(){
        this.answer = true;
    }

    public void rejectCall(){
        this.reject = true;
    }


    public String getCallState() {
        return callState;
    }

    public void resetState(){
        answer = false;
        reject = false;
    }

    private class SinchCallListener implements CallListener{

        @Override
        public void onCallProgressing(Call call) {
            callState = "RINGING";
        }

        @Override
        public void onCallEstablished(Call call) {
            callState = "CONNECTED";
        }

        @Override
        public void onCallEnded(Call endCall) {
            Log.d("VoiceCall","CallEnded");
            call = null;
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {
            //I have no idea for this :v :v :v
        }
    }


    private VoiceCall voiceCall;

    private class SinchCallClientListener implements CallClientListener {

        public static final int maxSecond = 10;
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            call = incomingCall;
            CallTask callTask = new CallTask();

            //Start activity IncomingCall
            InComingCallActivity.call = voiceCall;
            Intent i = new Intent(context,InComingCallActivity.class);
            context.startActivity(i);

            //Waiting for answer
            callTask.execute(maxSecond);
        }


        private class CallTask extends AsyncTask<Integer,Void,Void>{

            @Override
            protected Void doInBackground(Integer... params) {
                int maxSecond = params[0];

                for (int i=0 ; i<maxSecond; i++){
                    if (answer == true || reject == true)
                        break;
                    SystemClock.sleep(1000);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (call != null) {
                    if (answer == true) {
                        call.answer();
                        call.addCallListener(new SinchCallListener());
                    } else {
                        call.hangup();
                    }
                    resetState();
                }

            }
        }
    }

}
