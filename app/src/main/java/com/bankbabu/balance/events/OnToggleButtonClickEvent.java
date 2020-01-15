package com.bankbabu.balance.events;

import com.bankbabu.balance.models.CalculationType;


public class OnToggleButtonClickEvent {

  private final CalculationType calculationType;

  public OnToggleButtonClickEvent(final CalculationType calculationType) {
    this.calculationType = calculationType;
  }

  public CalculationType getCalculationType() {
    return calculationType;
  }
}
