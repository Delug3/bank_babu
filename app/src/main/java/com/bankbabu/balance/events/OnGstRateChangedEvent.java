package com.bankbabu.balance.events;


public class OnGstRateChangedEvent {

  private final String gstRate;

  public OnGstRateChangedEvent(final String gstRate) {
    this.gstRate = gstRate;
  }

  public String getGstRate() {
    return gstRate;
  }
}
