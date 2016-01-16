package com.rstudio.hackatontrip.controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.rstudio.hackatontrip.R;
import com.rstudio.hackatontrip.model.User;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    public static final String USERNAME_KEY = "uesrname";
    public static final String PASSWORD_KEY = "password";
    public static final String BUNDLE_KEY = "register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    protected void init() {
        Button registerBt = (Button) findViewById(R.id.regist_bt);
        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RegisterActivity.this.checkConnection()) {
                    Toast.makeText(RegisterActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                } else {
                    final String username = ((EditText)findViewById(R.id.user_email)).getText().toString();
                    final String password = ((EditText)findViewById(R.id.user_pass)).getText().toString();
                    if (User.validate(username, password)) {
                        // check email exist
                        ParseQuery<User> query = ParseQuery.getQuery(User.class);
                        query.whereEqualTo(USERNAME_KEY, username);

                        query.findInBackground(new FindCallback<User>() {
                            @Override
                            public void done(List<User> objects, ParseException e) {
                                if (objects.size() == 0) {
                                    Log.d("SIZE", objects.size() + "");
                                    Intent intent = new Intent(RegisterActivity.this, ConfirmPass.class);
                                    // package username and password
                                    Bundle bundle = new Bundle();
                                    bundle.putCharSequence(USERNAME_KEY, username);
                                    bundle.putCharSequence(PASSWORD_KEY, password);
                                    intent.putExtra(BUNDLE_KEY, bundle);

                                    startActivity(intent);
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Email is exist", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * check network state
     * @return true if online else false
     */
    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
