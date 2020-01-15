package com.bankbabu.balance.models;

import android.support.annotation.DrawableRes;


public class Tool {

  @DrawableRes
  private int icon;
  private String title;
  private boolean isDisabled;

  public Tool(final int icon, final String title, boolean isDisabled) {
    this.icon = icon;
    this.title = title;
    this.isDisabled = isDisabled;
  }

  public int getIcon() {
    return icon;
  }

  public void setIcon(final @DrawableRes int icon) {
    this.icon = icon;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public boolean isDisabled() {
    return isDisabled;
  }
}
