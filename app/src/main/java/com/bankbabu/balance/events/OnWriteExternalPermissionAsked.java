package com.bankbabu.balance.events;

public class OnWriteExternalPermissionAsked {
    private String shareString;

    public OnWriteExternalPermissionAsked(String shareString) {
        this.shareString = shareString;
    }

    public String getShareString() {
        return shareString;
    }
}
