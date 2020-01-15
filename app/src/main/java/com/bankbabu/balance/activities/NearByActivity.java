package com.bankbabu.balance.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.fragments.NearByFragment;

public class NearByActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_near_by;
    }

    @Override
    protected void initUi() {
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        configureToolbar(getString(R.string.near_by_search), R.drawable.ic_toolbar_arrow);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new NearByFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
