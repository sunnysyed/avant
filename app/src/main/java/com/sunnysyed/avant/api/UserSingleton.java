package com.sunnysyed.avant.api;

import com.google.gson.Gson;
import com.sunnysyed.avant.api.model.Profile;
import com.sunnysyed.avant.api.model.UserModel;
import com.sunnysyed.avant.helpers.Prefs;


public class UserSingleton {


    private static UserSingleton mInstance = null;

    public UserModel userModel;
    public String accessToken = "";

    private UserSingleton(){
    }

    public static synchronized UserSingleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new UserSingleton();
        }
        return mInstance;
    }

    public void loadUser ()
    {
        try {
            mInstance = new UserSingleton();
            mInstance.accessToken = Prefs.getString("user", "");

        }catch (Exception e){

        }
    }

    public void saveUser ()
    {
        Prefs.putString("user", accessToken);
    }

    public void logout ()
    {
        accessToken = "";
        saveUser();
    }
}

