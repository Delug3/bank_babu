package com.bankbabu.balance.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toast;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.events.OnWriteExternalPermissionAsked;
import com.bankbabu.balance.fragments.ShareBillFragment;
import com.bankbabu.balance.models.Account;
import com.bankbabu.balance.models.Bill;
import com.bankbabu.balance.utils.ScreenshotUtils;
import com.bankbabu.balance.utils.ShareUtils;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;

public class ShareBillActivity extends BaseActivity {

    private Bill bill;
    private Account account;
    private String shareString;

    @Override
    protected int getContentView() {
        return R.layout.activity_share_bill;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected void initUi() {
        bill = new Bill();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            makeBillFromBundle(extras);

            if (extras.containsKey(Constants.EXTRA_ACCOUNT_ID)) {
                makeAccountFromBundle(extras);
            }
        }
    }

    private void makeBillFromBundle(Bundle extras) {
        bill.setId(extras.getInt(Constants.EXTRA_BILL_ID));
        bill.setAmount(extras.getString(Constants.EXTRA_BILL_AMOUNT));
        bill.setPayee(extras.getString(Constants.EXTRA_BILL_PAYEE));
        bill.setNotes(extras.getString(Constants.EXTRA_BILL_NOTE));
    }

    private void makeAccountFromBundle(Bundle extras) {
        account = new Account();
        account.setId(extras.getInt(Constants.EXTRA_ACCOUNT_ID, 0));
        account.setName(extras.getString(Constants.EXTRA_ACCOUNT_NAME));
        account.setNumber(extras.getString(Constants.EXTRA_ACCOUNT_NUMBER));
        account.setBankName(extras.getString(Constants.EXTRA_BANK_NAME));
        account.setType(extras.getString(Constants.EXTRA_ACCOUNT_TYPE));
        account.setCode(extras.getString(Constants.EXTRA_ACCOUNT_IFSC));
    }

    @Override
    protected void setUi(Bundle savedInstanceState) {
        configureToolbar(getString(R.string.share_bill_or_payment), R.drawable.ic_toolbar_arrow);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, account == null ? ShareBillFragment.newInstance(bill) : ShareBillFragment.newInstance(bill, account))
                .commit();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onScrollToPosition(final OnWriteExternalPermissionAsked event) {
        shareString = event.getShareString();
        askWriteExternalStoragePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == Constants.RC_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, R.string.user_denied_permissions, Toast.LENGTH_LONG).show();
            } else {
                final File file = ScreenshotUtils.takeScreenshot(this);
                if (file != null && shareString != null) {
                    ShareUtils.shareScreen(file, this, shareString);
                    shareString = null;
                }
            }
        }
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
