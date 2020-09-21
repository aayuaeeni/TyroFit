package com.raju.tyrofit.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppConstats
{
    public static SharedPreferences sharedpreferences;
    public static SharedPreferences.Editor editor;
    public static final String TYRO_FIT = "tyro_fit";
    public static final String FIREBASE_TOKEN = "firebase_token";

    public static void init(Context context)
    {
        sharedpreferences = context.getSharedPreferences(TYRO_FIT, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

}
