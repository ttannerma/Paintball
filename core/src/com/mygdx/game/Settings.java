package com.mygdx.game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


/**
 * Created by Teemu Tannerma on 2.5.2018.
 */

public class Settings {

    private static Settings settings;
    private Preferences prefs;

    private Settings() {
        prefs = Gdx.app.getPreferences("Paintball.config");
    }

    private boolean hasKey(String key) {
        return prefs.contains(key);
    }

    public float getFloat(String key, float defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return prefs.getFloat(key);
    }
    public void setFloat(String key, float value) {
        prefs.putFloat(key, value);
    }

    public int getInteger(String key, int defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return prefs.getInteger(key);
    }

    public void setInteger(String key, int value) {
        prefs.putInteger(key, value);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return prefs.getBoolean(key);
    }

    public void setString(String key, String value) {
        prefs.putString(key, value);
    }

    public String  getString(String key, String defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return prefs.getString(key);
    }

    public void setBoolean(String key, boolean value) {
        prefs.putBoolean(key, value);
    }

    public void saveSettings() {
        prefs.flush();
    }

    public static Settings getInstance() {
        if (settings == null) {
            settings = new Settings();
        }
        return settings;
    }
}