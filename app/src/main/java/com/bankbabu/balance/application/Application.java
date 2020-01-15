package com.bankbabu.balance.application;


import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.bankbabu.balance.utils.LocaleManager;
import com.bankbabu.balance.utils.SharedPreferenceUtils;

public class Application extends MultiDexApplication {

  private boolean showCalculatorAdOnBack = true;

  @Override
  protected void attachBaseContext(Context base) {
    SharedPreferenceUtils.init(base);
    super.attachBaseContext(LocaleManager.setLocale(base));
    MultiDex.install(this);
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    LocaleManager.setLocale(this);
  }

  public boolean isShowCalculatorAdOnBack() {
    return showCalculatorAdOnBack;
  }

  public void setShowCalculatorAdOnBack(final boolean showCalculatorAdOnBack) {
    this.showCalculatorAdOnBack = showCalculatorAdOnBack;
  }
}
