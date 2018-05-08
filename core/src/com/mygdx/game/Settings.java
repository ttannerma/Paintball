package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


/**
 * @author Teemu Tannerma
 * @version 1.6
 * @since 3.4.2018
 *
 * Contains getters and setters for setting screen.
 */
public class Settings {

    private static Settings settings;
    private Preferences prefs;

    /**
     * Constructor for Settings.
     */
    private Settings() {
        prefs = Gdx.app.getPreferences("Paintball.config");
    }

    /**
     * Checks if the prefs file contains key
     * @param key
     * @return true if contains.
     */
    private boolean hasKey(String key) {
        return prefs.contains(key);
    }

    /**
     * Gets float from prefs file that contains key.
     * @param key
     * @param defaultValue
     * @return
     */
    public float getFloat(String key, float defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return prefs.getFloat(key);
    }

    /**
     * Sets float value in the prefs file with key name.
     * @param key
     * @param value
     */
    public void setFloat(String key, float value) {
        prefs.putFloat(key, value);
    }

    /**
     * Gets int value from prefs file with key name.
     * @param key
     * @param defaultValue
     * @return int value.
     */
    public int getInteger(String key, int defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return prefs.getInteger(key);
    }

    /**
     * Sets int value in the prefs file with key name.
     * @param key
     * @param value
     */
    public void setInteger(String key, int value) {
        prefs.putInteger(key, value);
    }

    /**
     * Gets boolean value from prefs file with key name.
     * @param key
     * @param defaultValue
     * @return boolean value.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return prefs.getBoolean(key);
    }

    /**
     * Sets String value in the prefs file with key name.
     * @param key
     * @param value
     */
    public void setString(String key, String value) {
        prefs.putString(key, value);
    }

    /**
     * Gets String value from the prefs file with key name.
     * @param key
     * @param defaultValue
     * @return String value.
     */
    public String  getString(String key, String defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return prefs.getString(key);
    }

    /**
     * Sets boolean value in the prefs file with key name.
     * @param key
     * @param value
     */
    public void setBoolean(String key, boolean value) {
        prefs.putBoolean(key, value);
    }

    /**
     * Saves settings.
     */
    public void saveSettings() {
        prefs.flush();
    }

    /**
     * Gets an instance of Settings.
     * @return Settings instance.
     */
    public static Settings getInstance() {
        if (settings == null) {
            settings = new Settings();
        }
        return settings;
    }
}