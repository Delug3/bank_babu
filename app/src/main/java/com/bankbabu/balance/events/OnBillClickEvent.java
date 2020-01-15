package com.bankbabu.balance.events;

import com.bankbabu.balance.models.Bill;

public class OnBillClickEvent {

    private final Bill bill;

    public OnBillClickEvent(final Bill bill) {
        this.bill = bill;
    }

    public Bill getBill() {
        return bill;
    }
}
