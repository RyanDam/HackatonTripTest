package com.rstudio.hackatontrip.controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.rstudio.hackatontrip.R;
import com.rstudio.hackatontrip.model.User;
import com.rstudio.hackatontrip.utils.AlertWarning;

public class LoginActivity extends AppCompatActivity {

    public static final int EXIT_CODE = 123221;
    public static final int REGIST_CODE = 35452;
    public static final int SHOW_CODE = 31413;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // check if logged
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(this, ShowActivity.class);
            startActivityForResult(intent, SHOW_CODE);
            Log.d("LOGIN", "user have logged");
        }

        init();
        ParseUser u = ParseUser.getCurrentUser();
        Log.d("log", "" + (u == null));
    }

    protected void init() {
        Button loginBt = (Button) findViewById(R.id.login_bt);
        TextView registerText = (TextView) findViewById(R.id.new_acc_bt);
        // set litener of login button
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LoginActivity.this.checkConnection()) {
                    AlertWarning.showAlert(LoginActivity.this, "Fail", "Check your connection");
                } else {
                    String username = ((EditText)findViewById(R.id.user_email)).getText().toString();
                    String password = ((EditText)findViewById(R.id.user_pass)).getText().toString();
                    // login
                    User.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                // start StartActivity when login done
                                Intent intent = new Intent(LoginActivity.this, ShowActivity.class);
                                startActivityForResult(intent, SHOW_CODE);
                                Log.d("LOGIN", "done");
                            } else {
                                AlertWarning.showAlert(LoginActivity.this, "Fail", "Invalid username/password");
                            }
                        }
                    });
                }
            }
        });

        // set litener of register TextView
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (EXIT_CODE == resultCode) {
            finish();
        }
    }
}
