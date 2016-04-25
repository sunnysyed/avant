package com.sunnysyed.avant.api;

import com.google.gson.Gson;
import com.sunnysyed.avant.api.model.Profile;
import com.sunnysyed.avant.api.model.UserModel;
import com.sunnysyed.avant.helpers.Prefs;

/**
 * Singleton Object type which stores the User Model/ Profile objects
 * Stores Access Token of logged in user
 */
public class UserSingleton {


    private static UserSingleton mInstance = null;

    public UserModel userModel;
    public String accessToken = "";

    /**
     * Hidden constructor
     */
    private UserSingleton(){
    }

    /**
     * Static thread safe way to get a User Singleton Object
     * @return Instance of a UserSingleton
     */
    public static synchronized UserSingleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new UserSingleton();
        }
        return mInstance;
    }


    /**
     * Load the saved user AccessToken from SharePreference using Pref helper class
     */
    public void loadUser ()
    {
        try {
            mInstance = new UserSingleton();
            mInstance.accessToken = Prefs.getString("user", "");

        }catch (Exception e){
            mInstance = new UserSingleton();
        }
    }

    /**
     * Save the user's access token in SharePreference so it can be retrieved later
     */
    public void saveUser ()
    {
        Prefs.putString("user", accessToken);
    }

    /**
     * Reset access token and save it
     */
    public void logout ()
    {
        accessToken = "";
        saveUser();
    }
}

