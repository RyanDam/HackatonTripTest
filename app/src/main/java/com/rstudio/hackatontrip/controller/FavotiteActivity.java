package com.rstudio.hackatontrip.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;
import com.rstudio.hackatontrip.R;

import java.util.ArrayList;

public class FavotiteActivity extends AppCompatActivity {

    private Button _male, _female, _thirdGender, _midAge, _young, _old, _science, _economy, _education, _food, _sport,
            _funny, _ambi, _chat,_white, _red, _blue, _done, _sad;
    private TextView _score;
    ArrayList<String> _array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favotite);

        //initial
        _male = (Button)findViewById(R.id.btnMale);
        _female = (Button)findViewById(R.id.btnFemale);
        _thirdGender = (Button)findViewById(R.id.btnMale);
        _male = (Button)findViewById(R.id.btnThirdGender);
        _midAge = (Button)findViewById(R.id.btnMidAge);
        _young = (Button)findViewById(R.id.btnYoung);
        _old = (Button)findViewById(R.id.btnOld);
        _science = (Button)findViewById(R.id.btnScience);
        _economy = (Button)findViewById(R.id.btnEconomy);
        _education = (Button)findViewById(R.id.btnEducation);
        _food = (Button)findViewById(R.id.btnFood);
        _sport = (Button)findViewById(R.id.btnSport);
        _funny = (Button)findViewById(R.id.btnFunny);
        _ambi = (Button)findViewById(R.id.btnAmbition);
        _sad = (Button)findViewById(R.id.btnSad);
        _chat = (Button)findViewById(R.id.btnChatterbox);
        _white = (Button)findViewById(R.id.btnWhite);
        _red = (Button)findViewById(R.id.btnRed);
        _blue = (Button)findViewById(R.id.btnBlue);
        _done = (Button)findViewById(R.id.btnDone);

        _array = new ArrayList<String>();

        _clickButton(_male);
        _clickButton(_female);
        _clickButton(_thirdGender);
        _clickButton(_midAge);
        _clickButton(_young);
        _clickButton(_old);
        _clickButton(_science);
        _clickButton(_economy);
        _clickButton(_education);
        _clickButton(_food);
        _clickButton(_sport);
        _clickButton(_sad);
        _clickButton(_funny);
        _clickButton(_ambi);
        _clickButton(_chat);
        _clickButton(_blue);
        _clickButton(_red);
        _clickButton(_white);
        _clickButton(_done);

    }

    private int haveItInArray (String item){
        for (int i=0; i<_array.size(); i++){
            if (item.compareTo(_array.get(i))==0){
                return i;
            }
        }
        return -1;
    }

    private void _clickButton (final Button temp){
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp.equals(_done)){
                    handleDone();
                }else{
                    int check =haveItInArray((String) temp.getText());
                    if (check!=-1){
                        _array.remove(check);
                        changeColor(temp, R.drawable.favo_not_select);
                    }else{
                        _array.add(temp.getText().toString());
                        changeColor(temp, R.drawable.favo_selected);
                    }
                }
            }
        });
    }

    public void changeColor(final Button b1, int id){
        //b1.setBackgroundColor(color);
        b1.setBackgroundResource(id);
        //b1.setBackground(getResources().getDrawable(R.drawable.favo_selected));
    }

    public void handleDone (){
        ParseUser u = ParseUser.getCurrentUser();
        u.put("favorites",getFinalData());
        u.saveInBackground();
        startActivityForResult(new Intent(this, ShowActivity.class), ShowActivity.USER_CODE);
    }

    public String getFinalData() {
        String up="";
        for (int i=0; i<_array.size(); i++){

            up=up+_array.get(i).toLowerCase()+";";
        }
        return up;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ShowActivity.USER_CODE:
                if (ShowActivity.LOGOUT_CODE == resultCode) {
                    setResult(ShowActivity.LOGOUT_CODE);
                    finish();
                }
                break;
        }
    }

}

