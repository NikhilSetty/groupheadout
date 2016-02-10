package com.fartans.keyplus.commons;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fartans.keyplus.Model.AppKeyModel;

import java.io.IOException;
import java.util.List;

/**
 * @author hitesh.sethiya on 06/02/16.
 * This is a singleton handling all the shared preferences save,get,update and delete for the entire app.
 */
public class SecureKeySharedPreferences {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static SecureKeySharedPreferences secureKeySharedPreferences;
    private static SharedPreferences SHARED_PREFERENCES;
    private final String SECURE_KEY_PREFERENCES_NAME = "SECURE_KEY_PREFERENCES_IDENTIFIER";
    private static List<AppKeyModel> appKeyModels;

    private SecureKeySharedPreferences(Context context) {
        SHARED_PREFERENCES = context.getSharedPreferences(SECURE_KEY_PREFERENCES_NAME,Context.MODE_PRIVATE);
    }

    /**
     * Synchronized method to avoid multiple threads initializing the same object twice or more,
     * because each thread creates its own copy of static objects.
     * @param context the app context to get the shared preferences in MODE_PRIVATE
     * @return the single instance of the class.
     */
    public static synchronized SecureKeySharedPreferences getInstance(Context context) {
        if(secureKeySharedPreferences == null) {
            secureKeySharedPreferences = new SecureKeySharedPreferences(context);
        }
        return secureKeySharedPreferences;
    }


    public List<AppKeyModel> getAppKeyPreferences() {
        if(appKeyModels != null) {
            return appKeyModels;
        }
        String sharedPrefString = SHARED_PREFERENCES.getString(AppKeyModel.SHARED_PREFERENCE_KEY,null);
        if(sharedPrefString != null) {
            try {
                appKeyModels = objectMapper.readValue(sharedPrefString, objectMapper.getTypeFactory().constructCollectionType(List.class, AppKeyModel.class));
            } catch (IOException e) {
                Log.e(LOG_TAG,"IOException exception for getAppKeyPreferences ",e);
            }
        }
        return appKeyModels;
    }

    public void saveAppKeyPreferences(List<AppKeyModel> appKeyModels) {
        final SharedPreferences.Editor editor = SHARED_PREFERENCES.edit();
        try {
            String toPut = objectMapper.writeValueAsString(appKeyModels);
            editor.putString(AppKeyModel.SHARED_PREFERENCE_KEY,toPut);
            editor.apply();
        } catch (JsonProcessingException e) {
            Log.e(LOG_TAG,"Mapping exception for saveAppKeyPreferences ",e);
        }
    }

    private static final String LOG_TAG = SecureKeySharedPreferences.class.getName();
}
