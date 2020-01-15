package com.bankbabu.balance.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import java.util.Locale;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/15/19.
 */
public class LocaleManager {

  private static final String LANGUAGE_ENGLISH = "en";

  private static final String LANGUAGE_KEY = "language_key";

  public static Context setLocale(Context c) {
    return updateResources(c, getLanguage());
  }

  private static Context updateResources(Context context, String language) {
    final Locale locale = new Locale(language);
    Locale.setDefault(locale);

    final Resources res = context.getResources();
    final Configuration config = new Configuration(res.getConfiguration());

    config.setLocale(locale);
    context = context.createConfigurationContext(config);

    return context;
  }

  public static String getLanguage() {
    return SharedPreferenceUtils.getInstance().getStringValue(LANGUAGE_KEY, LANGUAGE_ENGLISH);
  }

  public static void setNewLocale(Context c, String language) {
    persistLanguage(language);
    updateResources(c, language);
  }

  private static void persistLanguage(String language) {
    SharedPreferenceUtils.getInstance().setValue(LANGUAGE_KEY, language);
  }

  @SuppressWarnings("deprecation")
  public static Locale getLocale(Resources res) {
    final Configuration config = res.getConfiguration();
    if (VERSION.SDK_INT >= VERSION_CODES.N) {
      return config.getLocales().get(0);
    } else {
      return config.locale;
    }
  }
}
