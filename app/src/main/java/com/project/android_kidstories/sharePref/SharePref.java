package com.project.android_kidstories.sharePref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharePref {

    private static final String LAST_LOGGED_IN ="LAST_LOGGED_IN";
    private static final String ID_KEY="com.project.android_kidstories_ID_KEY";
    private static final String USER_LOGIN_STATE = "isUserLoggedIn";

    private static SharePref INSTANCE;

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCr(savedInstanceState);
//
//        INSTANCE=SharePref.getINSTANCE(this);
//    }
    private SharedPreferences sharedPreferences;

    private SharePref(SharedPreferences sharedPreferences) {
        this.sharedPreferences=sharedPreferences;

    }

    public static synchronized SharePref getINSTANCE(Context context) {
        if(INSTANCE==null){
            //noinspection deprecation
            INSTANCE = new SharePref(PreferenceManager.getDefaultSharedPreferences(context));
        }
        return INSTANCE;
    }

    public SharePref getSharePref() {
        return INSTANCE;
    }

    public void setLastSunAccess(int hour){
        sharedPreferences.edit().putInt(LAST_LOGGED_IN,hour).apply();
    }

    public int getLastLoginInHour(){
        return sharedPreferences.getInt(LAST_LOGGED_IN,1);
    }

    public Long getLoggedUserId(){
        return sharedPreferences.getLong(ID_KEY,-1);
    }

    public void setLoggedUserId(Long id){
        sharedPreferences.edit().putLong(ID_KEY,id).apply();
    }

    public Boolean getIsUserLoggedIn() {
        return sharedPreferences.getBoolean(USER_LOGIN_STATE, false);
    }

    public  void setIsUserLoggedIn(Boolean isUserLoggedIn) {
        sharedPreferences.edit().putBoolean(USER_LOGIN_STATE, isUserLoggedIn).apply();
    }


}
