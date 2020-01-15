package com.bankbabu.balance.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 9/11/18
 */
public class SharedPreferenceUtils {

  public static final String SETUP = "SETUP";
  public static final String RATE_DIALOG = "RATE";
  public static final String APP_START_COUNT = "APP_START";
  public static final String BOOKMARK = "FLAG";
  private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

  private static SharedPreferenceUtils sharedPreferenceUtils;
  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor sharedPreferencesEditor;

  @SuppressLint("CommitPrefEdits")
  private SharedPreferenceUtils(Context context) {
    sharedPreferences = context.getSharedPreferences("Bank-babu", Context.MODE_PRIVATE);
    sharedPreferencesEditor = sharedPreferences.edit();
  }

  /**
   * Creates single instance of SharedPreferenceUtils.
   *
   * @param context context of Activity or Service
   */
  public static synchronized void init(Context context) {
    if (sharedPreferenceUtils == null) {
      sharedPreferenceUtils = new SharedPreferenceUtils(context);
    }
  }

  public static SharedPreferenceUtils getInstance() {
    return sharedPreferenceUtils;
  }

  public boolean isFirstTimeLaunch() {
    return getBooleanValue(IS_FIRST_TIME_LAUNCH, true);
  }

  public void setFirstTimeLaunch(boolean isFirstTime) {
    setValue(IS_FIRST_TIME_LAUNCH, isFirstTime);
  }

  /**
   * Stores boolean value in preference.
   *
   * @param key key of preference
   * @param value value for that key
   */
  public void setValue(String key, boolean value) {
    sharedPreferencesEditor.putBoolean(key, value);
    sharedPreferencesEditor.commit();
  }

  /**
   * Retrieves boolean value from preference.
   *
   * @param keyFlag key of preference
   * @param defaultValue default value if no key found
   */
  public boolean getBooleanValue(String keyFlag, boolean defaultValue) {
    return sharedPreferences.getBoolean(keyFlag, defaultValue);
  }

  /**
   * Stores int value in preference.
   *
   * @param key key of preference
   * @param value value for that key
   */
  public void setValue(String key, int value) {
    sharedPreferencesEditor.putInt(key, value);
    sharedPreferencesEditor.commit();
  }

  /**
   * Stores Double value in String format in preference.
   *
   * @param key key of preference
   * @param value value for that key
   */
  public void setValue(String key, double value) {
    setValue(key, Double.toString(value));
  }

  /**
   * Stores String value in preference.
   *
   * @param key key of preference
   * @param value value for that key
   */
  public void setValue(String key, String value) {
    sharedPreferencesEditor.putString(key, value);
    sharedPreferencesEditor.commit();
  }

  /**
   * Stores long value in preference.
   *
   * @param key key of preference
   * @param value value for that key
   */
  public void setValue(String key, long value) {
    sharedPreferencesEditor.putLong(key, value);
    sharedPreferencesEditor.commit();
  }

  /**
   * Retrieves String value from preference.
   *
   * @param key key of preference
   * @param defaultValue default value if no key found
   */
  public String getStringValue(String key, String defaultValue) {
    return sharedPreferences.getString(key, defaultValue);
  }

  /**
   * Retrieves int value from preference.
   *
   * @param key key of preference
   * @param defaultValue default value if no key found
   */
  public int getIntValue(String key, int defaultValue) {
    return sharedPreferences.getInt(key, defaultValue);
  }

  /**
   * Retrieves long value from preference.
   *
   * @param key key of preference
   * @param defaultValue default value if no key found
   */
  public long getLongValue(String key, long defaultValue) {
    return sharedPreferences.getLong(key, defaultValue);
  }

  /**
   * Removes key from preference.
   *
   * @param key key of preference that is to be deleted
   */
  private void removeKey(String key) {
    if (sharedPreferencesEditor != null) {
      sharedPreferencesEditor.remove(key);
      sharedPreferencesEditor.commit();
    }
  }

  public boolean contains(String key) {
    return sharedPreferences.contains(key);
  }

  /**
   * Clears all the preferences stored.
   */
  public void clear() {
    sharedPreferencesEditor.clear().commit();
  }
}
