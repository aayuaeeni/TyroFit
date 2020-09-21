package com.raju.tyrofit.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.raju.tyrofit.utils.AppConstats;

public class TyroFitApp extends Application
{

    //app context
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    @SuppressLint("StaticFieldLeak")
    private static TyroFitApp mInstance;


    public static Context getAppContext()
    {
        return context;
    }

    /**
     * called on app created
     */
    @Override
    public void onCreate()
    {
        super.onCreate();
        //app context
        context = getApplicationContext();
        mInstance = this;
        AppConstats.init(this);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    public static synchronized TyroFitApp getInstance()
    {
        return mInstance;
    }

    public static Context getContext()
    {
        return context;
    }

}