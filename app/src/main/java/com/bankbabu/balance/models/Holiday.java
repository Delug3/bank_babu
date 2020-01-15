package com.bankbabu.balance.models;

public class Holiday {

  private int holidayId;
  private String holidayName;
  private String holidayDate;

  public int getHolidayId() {
    return holidayId;
  }

  public void setHolidayId(final int holidayId) {
    this.holidayId = holidayId;
  }

  public String getHolidayName() {
    return holidayName;
  }

  public void setHolidayName(final String holidayName) {
    this.holidayName = holidayName;
  }

  public String getHolidayDate() {
    return holidayDate;
  }

  public void setHolidayDate(final String holidayDate) {
    this.holidayDate = holidayDate;
  }
}
