package com.bankbabu.balance.events;


public class OnInitialAmountIsEmptyEvent {

  private final boolean empty;

  public OnInitialAmountIsEmptyEvent(final boolean empty) {
    this.empty = empty;
  }

  public boolean isEmpty() {
    return empty;
  }
}
