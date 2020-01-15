package com.bankbabu.balance.events;

import com.bankbabu.balance.models.Account;

public class OnAccountAddClickEvent {

    private final Account.OwnerType ownerType;

    public OnAccountAddClickEvent(final Account.OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public Account.OwnerType getOwnerType() {
        return ownerType;
    }
}
