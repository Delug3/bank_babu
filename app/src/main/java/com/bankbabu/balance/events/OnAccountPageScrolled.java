package com.bankbabu.balance.events;

public class OnAccountPageScrolled {

    private final int pageIndex;

    public OnAccountPageScrolled(final int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageIndex() {
        return pageIndex;
    }
}
