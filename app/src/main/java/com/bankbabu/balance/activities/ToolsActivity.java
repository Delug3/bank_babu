package com.bankbabu.balance.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bankbabu.balance.R;
import com.bankbabu.balance._common.navigator.core.FragmentData;
import com.bankbabu.balance._common.navigator.core.FragmentId;
import com.bankbabu.balance._common.navigator.manager.AppManagerUI;
import com.bankbabu.balance._common.navigator.manager.core.ManagerUI;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.utils.InternetConnection;
import com.bankbabu.balance.utils.ScreenshotUtils;
import com.bankbabu.balance.utils.ShareUtils;
import com.bankbabu.balance.utils.SharedPreferenceUtils;

import org.jetbrains.annotations.Nullable;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToolsActivity extends BaseActivity {

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    private static final String FEEDBACK_EMAIL = "bankbabuapp@gmail.com";
    private static final String FEEDBACK_EMAIL_SUBJECT = "Bank Babu Feedback";
    private SharedPreferenceUtils sharedPreferenceUtils;
    private boolean closeOnBackPressed = false;


    @Override
    protected int getContentView() {
        return R.layout.activity_tools;
    }

    @Nullable
    @Override
    public ManagerUI initManagerUI() {
        return new AppManagerUI(this, new FragmentData(FragmentId.TOOLS, new Bundle()));
    }

    @Override
    protected void initUi() {
        ButterKnife.bind(this);
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("tools-activity");
        sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        showRatingDialog();
        setupDrawerItems();
    }

    @SuppressLint("InflateParams")
    private void showRatingDialog() {
        if (SharedPreferenceUtils.getInstance()
                .getBooleanValue(SharedPreferenceUtils.RATE_DIALOG, false) || !shouldShowRateDialog()) {
            return;
        }
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dailog_rating, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        alertDialog.show();

        final Button buttonYes = dialogView.findViewById(R.id.button_yes);
        final Button buttonCancel = dialogView.findViewById(R.id.button_cancel);

        buttonCancel.setOnClickListener(v -> alertDialog.dismiss());

        buttonYes.setOnClickListener(view -> {
            sharedPreferenceUtils.setValue(SharedPreferenceUtils.RATE_DIALOG, true);
            rateApp();
            alertDialog.dismiss();
        });
    }

    private void setupDrawerItems() {
        setDrawerItemOnClickListener(R.id.payment_reminder, () -> startActivity(new Intent(this, HomeActivity.class)));
        setDrawerItemOnClickListener(R.id.check_balance, () -> navigateToItem(LeftMenuNavigation.BALANCE_CHECK));
        setDrawerItemOnClickListener(R.id.provided_funds, () -> navigateToItem(LeftMenuNavigation.PF_STATUS));
        setDrawerItemOnClickListener(R.id.payment_method, () -> startActivity(new Intent(this, PaymentsActivity.class)));
        setDrawerItemOnClickListener(R.id.emi, () -> startActivity(new Intent(this, EmiCalculatorActivity.class)));
        setDrawerItemOnClickListener(R.id.gst, () -> startActivity(new Intent(this, GstCalculatorActivity.class)));
        setDrawerItemOnClickListener(R.id.sip, () -> startActivity(new Intent(this, SipCalculatorActivity.class)));
        setDrawerItemOnClickListener(R.id.rate_us, this::sendFeedback);
        setDrawerItemOnClickListener(R.id.share_app, this::share);
        setDrawerItemOnClickListener(R.id.feedback, this::sendFeedback);
        setDrawerItemOnClickListener(R.id.language, this::showLanguageDialog);
    }

    private boolean shouldShowRateDialog() {
        if (SharedPreferenceUtils.getInstance().getBooleanValue(Constants.KEY_LANGUAGE_DIALOG, true)) {
            return false;
        }
        final int startCount = sharedPreferenceUtils
                .getIntValue(SharedPreferenceUtils.APP_START_COUNT, 0);
        sharedPreferenceUtils.setValue(SharedPreferenceUtils.APP_START_COUNT, startCount + 1);

        return sharedPreferenceUtils.getIntValue(SharedPreferenceUtils.APP_START_COUNT, 0) > 1;
    }

    private void setDrawerItemOnClickListener(@IdRes int viewId, Runnable runnable) {
        findViewById(viewId).setOnClickListener(v -> {
            runnable.run();
            drawer.closeDrawer(GravityCompat.START);
        });
    }

    private void navigateToItem(final LeftMenuNavigation navigation) {
        final Bundle data = new Bundle();
        data.putBoolean("toolbar", true);

        switch (navigation) {
            default:
            case BALANCE_CHECK:
                getManagerUI().changeFragmentTo(new FragmentData(FragmentId.BANK_BALANCE_CHECK, data));
                break;
            case PAYMENT_REMINDER:
                getManagerUI().changeFragmentTo(new FragmentData(FragmentId.REMINDER, data));
                break;
            case PF_STATUS:
                startActivity(new Intent(this, PfStatusActivity.class));
                break;
        }
    }

    private void sendFeedback() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:" + FEEDBACK_EMAIL + "?subject=" + FEEDBACK_EMAIL_SUBJECT);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onToolbarItemClicked(@org.jetbrains.annotations.Nullable final View view) {
        if (view != null) {
            if (view.getId() == R.id.toolbarRate) {
                sendFeedback();
            } else {
                // Means that drawer menu was clicked
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!closeOnBackPressed) {
            closeOnBackPressed = true;
            new Handler().postDelayed(() -> closeOnBackPressed = false, 2000);
            if (InternetConnection.checkConnection(ToolsActivity.this)) {
                Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(ToolsActivity.this)
                        .setMessage(getString(R.string.share_ask))
                        .setPositiveButton(R.string.yes,
                                (dialog, id) -> {
                                    final File file = ScreenshotUtils.takeScreenshot(ToolsActivity.this);
                                    if (file != null) {
                                        ShareUtils.shareScreen(file, ToolsActivity.this,
                                                getString(R.string.app_name) + getString(R.string.share));
                                    }
                                })
                        .setNegativeButton(R.string.no, null)
                        .create().show();
            }
        } else {
            super.onBackPressed();
        }
    }

    public enum LeftMenuNavigation {
        BALANCE_CHECK, PAYMENT_REMINDER, PF_STATUS
    }
}
