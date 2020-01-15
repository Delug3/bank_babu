package com.bankbabu.balance.models;


public class MainTool {

  private int image;
  private String description;
  private String name;
  private Class intentClass;
  private boolean isAd;

  public MainTool(final String name, final String description, final Class intentClass) {
    this.description = description;
    this.name = name;
    this.intentClass = intentClass;
  }

  public String getName() {
    return name;
  }

  public int getImage() {
    return image;
  }

  public void setImage(final int image) {
    this.image = image;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Class getIntentClass() {
    return intentClass;
  }

  public void setIntentClass(final Class intentClass) {
    this.intentClass = intentClass;
  }

  public boolean isAd() {
    return isAd;
  }

}
