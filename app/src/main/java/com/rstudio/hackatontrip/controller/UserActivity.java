package com.rstudio.hackatontrip.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;
import com.rstudio.hackatontrip.R;
import com.rstudio.hackatontrip.utils.AlertWarning;

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

        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChangeName();
            }
        });
    }

    private void showDialogChangeName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change name");
        final EditText input = new EditText(this);
        input.setTextColor(Color.BLACK);
        builder.setView(input);
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().length() == 0) {
                    AlertWarning.showAlert(UserActivity.this, "Warning", "Name empty");
                } else {
                    mUser.put("name", input.getText().toString());
                    mUser.saveInBackground();
                    userName.setText((String) mUser.get("name"));
                    dialog.cancel();
                }
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
