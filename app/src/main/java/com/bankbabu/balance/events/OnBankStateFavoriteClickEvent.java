package com.bankbabu.balance.events;

import com.bankbabu.balance.models.Bank;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/28/19.
 */
public class OnBankStateFavoriteClickEvent {

  private final Bank bank;

  public OnBankStateFavoriteClickEvent(final Bank bank) {
    this.bank = bank;
  }

  public Bank getBank() {
    return bank;
  }
}
