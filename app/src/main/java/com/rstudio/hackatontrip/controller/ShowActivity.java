package com.rstudio.hackatontrip.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.media.AudioManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ShowActivity extends AppCompatActivity {

    public static final int CALL_CODE = 5646;
    public static final int HANGUP_CODE = 2232;
    public static final int LOGOUT_CODE = 1241;
    public static final int USER_CODE = 14124;

    private Context mContext;

    private ImageView mainImg;
    private ParseUser currentUser;
    private ParseUser currentTargetUser;
    private VoiceCall call;
    private ProgressBar mainProgess;
    private ArrayList<String> previousUser;
    private String userId;
    private VoiceCall voiceCall;
    private TextView targetName;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        previousUser = new ArrayList<>();
        currentUser = ParseUser.getCurrentUser();
        userId = currentUser.getObjectId();
        listRecentCall = new ArrayList<String>();
        ParseUser user = ParseUser.getCurrentUser();
        userId = user.getObjectId();

        user.put(KEY_ONLINE, true);
        user.saveInBackground();

        userId = "xyz";

        voiceCall = new VoiceCall(userId, this);

        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        targetName = (TextView) findViewById(R.id.main_name);
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
                startActivityForResult(new Intent(mContext, UserActivity.class), USER_CODE);
            }
        });

        currentUser.put("isOnline", true);
        currentUser.saveInBackground();

        nextUser();
    }

    private void nextUser() {
        // get some other random user
        // set current user
        if (currentTargetUser != null) previousUser.add(currentTargetUser.getObjectId());

        Log.d("USEr", "nextUser");

        HashMap<String, Object> params = new HashMap<>();
        params.put("code", ParseUser.getCurrentUser().getObjectId());
        params.put("recentChoose", previousUser);
        ParseCloud.callFunctionInBackground("ramdomUser", params, new FunctionCallback<String>() {
            @Override
            public void done(String object, ParseException e) {
                if (null == e) {
                    setTargetUser("" + object);
                } else {
                    Log.d("USEr", "nextUser " + "fails");
                    e.printStackTrace();
                }
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
        userId = u.getObjectId();
        voiceCall = new VoiceCall(userId, this);
        if (null != img) {
            img.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    mainImg.setImageBitmap(UserUtils.getBitmapFromBytes(data));
                }
            }, new ProgressCallback() {
                @Override
                public void done(Integer percentDone) {
                    Log.d("Load img", "" + percentDone + "/100");
                    if (percentDone < 20
                            && mainProgess.getVisibility() == View.GONE)
                        mainProgess.setVisibility(View.VISIBLE);
                    else if (percentDone == 100
                            && mainProgess.getVisibility() == View.VISIBLE)
                        mainProgess.setVisibility(View.GONE);
                    mainProgess.setProgress(percentDone);
                }
            });
        }
        targetName.setText((String) currentTargetUser.get("name"));
    }

    private void makeACall() {
        // make a dirrect call to that user
        String recipientId = "abc";
        voiceCall.callUser(recipientId);
    }

    @Override
    public void onBackPressed() {
        setResult(LoginActivity.EXIT_CODE, new Intent());
        finish();
    }

    public class VoiceCall {
        private Call call;
        private SinchClient sinchClient;
        ShowActivity context;

        public static final String APP_KEY = "69b5b187-c93d-4ea7-8ac1-b41d59ab11ad";
        public static final String APP_SECRET = "+3vdCTyJVEGmBPwFSkJF+g==";
        public static final String HOST = "sandbox.sinch.com";

        public Call getCall() {
            return call;
        }

        public VoiceCall(String userId, final ShowActivity context) {
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

        public void callUser(String recipientId) {
            call = sinchClient.getCallClient().callUser(recipientId);
            Log.d("test", "calling");

            context.setmCall(call);

            CallingActivity.call = call;

            Intent intent = new Intent(context, CallingActivity.class);
            startActivityForResult(intent, CALL_CODE);
        }
    }

    @Override
    protected void onDestroy() {
        currentUser.put("isOnline", false);
        currentUser.saveInBackground();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CALL_CODE:
                if (HANGUP_CODE == resultCode) mCall.hangup();
                break;
            case USER_CODE:
                if (LOGOUT_CODE == resultCode) finish();
                break;
        }
    }
}
