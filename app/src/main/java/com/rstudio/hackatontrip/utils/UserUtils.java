package com.rstudio.hackatontrip.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Ryan on 1/17/16.
 */
public class UserUtils {
    public static Bitmap getBitmapFromBytes(byte[] b) {
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }
}
