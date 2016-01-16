package com.rstudio.hackatontrip.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.ImageView;

import com.rstudio.hackatontrip.model.User;

/**
 * Created by Ryan on 1/16/16.
 */
public class SwipeView extends ImageView{

    private Bitmap img;
    private String name;
    private int id;

    public SwipeView(Context context) {
        super(context);
    }

    public void setUser(User user) {
        img = (Bitmap) user.get("avatar");
        name = (String) user.get("name");
        id = (int) user.get("id");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
