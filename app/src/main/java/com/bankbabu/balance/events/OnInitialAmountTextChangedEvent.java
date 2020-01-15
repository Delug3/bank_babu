package com.bankbabu.balance.events;


public class OnInitialAmountTextChangedEvent {

  private final double initialAmount;

  public OnInitialAmountTextChangedEvent(final double initialAmount) {
    this.initialAmount = initialAmount;
  }

  public double getInitialAmount() {
    return initialAmount;
  }
}
