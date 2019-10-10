package com.project.android_kidstories.DataStore;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPref {
    private static final String LAST_SUN_ACCESS ="LAST_SUN_ACCESS";
    private static final String ID_KEY="com.example.solarcalculator_ID_KEY";

    private static SharedPref INSTANCE;

    public static synchronized SharedPref getINSTANCE(Context context) {
        if(INSTANCE==null){
            //noinspection deprecation
            INSTANCE = new SharedPref(PreferenceManager.getDefaultSharedPreferences(context));
        }
        return INSTANCE;
    }



    private SharedPreferences sharedPreferences;

    private SharedPref(SharedPreferences sharedPreferences) {
        //this.sharedPreferences = context.getSharedPreferences("SharedPref",Context.MODE_PRIVATE);
        this.sharedPreferences=sharedPreferences;

    }

    public void setLastSunAccess(int hour){
        sharedPreferences.edit().putInt(LAST_SUN_ACCESS,hour).apply();
    }

    public int getLastSunAccessInHour(){
        return sharedPreferences.getInt(LAST_SUN_ACCESS,1);
    }


    public void setLoggedUserId(Long id){
        sharedPreferences.edit().putLong(ID_KEY,id).apply();
    }

    public Long getLoggedUserId(){
        return sharedPreferences.getLong(ID_KEY,-1);
    }

    public void getLoginUserMethod(){

    }


}
