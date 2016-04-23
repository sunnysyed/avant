package com.sunnysyed.avant;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//
import com.sunnysyed.avant.api.UserSingleton;
import com.sunnysyed.avant.helpers.Prefs;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by SuNnY on 4/21/16.
 */
public class Avant extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .build()
        );
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);


        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
//
        UserSingleton.getInstance().loadUser();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

}
