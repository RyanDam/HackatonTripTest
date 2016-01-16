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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.rstudio.hackatontrip.R;
import com.rstudio.hackatontrip.model.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    protected void init() {
        Button loginBt = (Button) findViewById(R.id.login_bt);
        TextView registerText = (TextView) findViewById(R.id.new_acc_bt);
        // set litener of login button
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LoginActivity.this.checkConnection()) {
                    Toast.makeText(LoginActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
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
                                startActivity(intent);
                                Log.d("LOGIN", "done");
                            } else
                                Toast.makeText(LoginActivity.this, "Invalid username/password", Toast.LENGTH_LONG).show();
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
}
