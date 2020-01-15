package com.bankbabu.balance.events;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/30/19.
 */
public class OnMainToolItemClickEvent {

    private final Class intentClass;

    public OnMainToolItemClickEvent(final Class intentClass) {
        this.intentClass = intentClass;
    }

    public Class getIntentClass() {
        return intentClass;
    }
}
