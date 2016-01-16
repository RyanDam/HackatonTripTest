package com.rstudio.hackatontrip.utils;

import android.app.Application;

import com.rstudio.hackatontrip.R;
import com.rstudio.hackatontrip.model.User;

/**
 * Created by Ryan on 1/16/16.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(User.class);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this
                , getResources().getString(R.string.app_id)
                , getResources().getString(R.string.client_id));

    }

}
