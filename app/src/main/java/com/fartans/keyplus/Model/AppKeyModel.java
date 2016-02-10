package com.fartans.keyplus.Model;

/**
 * @author hitesh.sethiya on 07/02/16.
 */
public class AppKeyModel {

    public static final String SHARED_PREFERENCE_KEY = "app_key_preferences";

    private int imageResource;
    private String appName;
    private String packageName;
    private int keyCode;

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
}
