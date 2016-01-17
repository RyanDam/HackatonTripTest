package com.rstudio.hackatontrip.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rstudio.hackatontrip.R;

public class AvataActivity extends AppCompatActivity {

    private ImageView avata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avata);
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
    }

    public void openFolder()
    {
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
                avata.setImageBitmap(im.decodeBitmap(100,100));   //NOT IN REQUIRED FORMAT

            }
    }

}
