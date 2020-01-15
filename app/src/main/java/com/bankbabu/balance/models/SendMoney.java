package com.bankbabu.balance.models;

import android.support.annotation.DrawableRes;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/30/19.
 */
public class SendMoney {

    private String title;
    private String number;
    private @DrawableRes
    int iconId;

    public SendMoney(final String title, final String number, @DrawableRes int iconId) {
        this.title = title;
        this.number = number;
        this.iconId = iconId;
    }

    public int getIconId() {
        return iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }
}
