package com.bankbabu.balance.events;


public class OnResetButtonSetEnabledEvent {

  private final boolean enabled;

  public OnResetButtonSetEnabledEvent(final boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }
}
