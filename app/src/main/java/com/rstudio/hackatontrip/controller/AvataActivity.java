package com.rstudio.hackatontrip.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.rstudio.hackatontrip.R;

import java.io.ByteArrayOutputStream;

public class AvataActivity extends AppCompatActivity {

    private ImageView avata;

    Bitmap bit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avata);

        bit = BitmapFactory.decodeResource(getResources(), R.drawable.sample_avatar);

        Button addImage;
        //initial
        addImage=(Button)findViewById(R.id.btnFind);
        avata=(ImageView)findViewById(R.id.imgAvt);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFolder();
            }
        });

        Button done = (Button) findViewById(R.id.button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done(bit);
            }
        });
    }

    public void openFolder() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                + "/myFolder/");
        intent.setDataAndType(uri, "jpg/png");
        Intent intentFoder = Intent.createChooser(intent, "Open folder");
        startActivityForResult(intentFoder, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                LowQualtyImage im=new LowQualtyImage(selectedImage.getEncodedPath());
                bit = im.decodeBitmap(100,100);
                avata.setImageBitmap(bit);
            }
    }

    public void done(Bitmap bit) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile f = new ParseFile(byteArray);
        f.saveInBackground();
        ParseUser u = ParseUser.getCurrentUser();
        u.put("avatar", f);
        u.saveInBackground();
        Intent intent = new Intent(this, FavotiteActivity.class);
        startActivity(intent);
    }

}
