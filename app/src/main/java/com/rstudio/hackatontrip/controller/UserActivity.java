package com.rstudio.hackatontrip.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;
import com.rstudio.hackatontrip.R;

public class UserActivity extends AppCompatActivity {

    ParseUser mUser;
    TextView userName;
    Button changeName;
    Button changeAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mUser = ParseUser.getCurrentUser();
        Button logout = (Button) findViewById(R.id.logout_bt);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.put("isOnline", false);
                mUser.saveInBackground();
                mUser.logOut();
                setResult(ShowActivity.LOGOUT_CODE);
                finish();
            }
        });
        userName = (TextView) findViewById(R.id.main_name);
        userName.setText((String)mUser.get("name"));
        changeAvatar = (Button) findViewById(R.id.change_avatar_bt);
        changeName = (Button) findViewById(R.id.change_name_bt);
    }
}
