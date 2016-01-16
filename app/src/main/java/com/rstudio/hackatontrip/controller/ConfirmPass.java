package com.rstudio.hackatontrip.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SignUpCallback;
import com.rstudio.hackatontrip.R;
import com.rstudio.hackatontrip.model.User;

public class ConfirmPass extends AppCompatActivity {

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pass);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(RegisterActivity.BUNDLE_KEY);

        username = bundle.getString(RegisterActivity.USERNAME_KEY);
        password = bundle.getString(RegisterActivity.PASSWORD_KEY);
        Log.d("TEST", username + " " + password);
        init();
    }

    private void init() {
        Button confirm = (Button) findViewById(R.id.comfirm_bt);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView confirmTextView = (TextView) findViewById(R.id.user_pass);
                String passwordConfrim = confirmTextView.getText().toString();
                Log.d("TEST", passwordConfrim);
                if (passwordConfrim.compareTo(ConfirmPass.this.password) == 0) {
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                Intent intent = new Intent(ConfirmPass.this, FavotiteActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ConfirmPass.this,"Some error occur", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(ConfirmPass.this, "Password Confirm not match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
