package com.bankbabu.balance.activities.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bankbabu.balance.R;
import com.bankbabu.balance._common.core.BaseNavigationActivity;
import com.bankbabu.balance._common.navigator.manager.core.ManagerUI;
import com.bankbabu.balance.activities.WelcomeActivity;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.utils.LocaleManager;
import com.bankbabu.balance.utils.SharedPreferenceUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

import butterknife.ButterKnife;


public abstract class BaseActivity extends BaseNavigationActivity {

    private InterstitialAd googleInterstitialAd;
    private FirebaseAnalytics firebaseAnalytics;
    private Toolbar toolbar;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getContentView());
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        initUi();
        setUi(savedInstanceState);

    }

    @LayoutRes
    protected abstract int getContentView();

    /**
     * find views in this method.
     */
    protected abstract void initUi();

    /**
     * set listeners or data in this method.
     */
    protected abstract void setUi(final Bundle savedInstanceState);

    @Nullable
    @Override
    public ManagerUI initManagerUI() {
        return null;
    }

    @Override
    public void onLeftMenuClicked() {

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    protected boolean useEventBus() {
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissAdProgressDialog();
    }

    public void dismissAdProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected boolean useFacebookInterstitialAd() {
        return false;
    }

    public void loadDefaultGoogleInterstitialAd(final AdLoadedListener adListener,
                                                @StringRes final int adUnitId) {
        if (!useGoogleInterstitialAd()) {
            return;
        }

        showAdProgressBar();
        googleInterstitialAd = new InterstitialAd(this);
        googleInterstitialAd.setAdUnitId(getString(adUnitId));

        final AdRequest adRequest = new AdRequest.Builder().build();

        googleInterstitialAd.loadAd(adRequest);
        googleInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                adListener.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(final int i) {
                super.onAdFailedToLoad(i);
                dismissAdProgressDialog();
                adListener.onAdFailedToLoad();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (googleInterstitialAd.isLoaded()) {
                    googleInterstitialAd.show();
                }
                dismissAdProgressDialog();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });
    }

    protected boolean useGoogleInterstitialAd() {
        return false;
    }

    public void showAdProgressBar() {
        if (isDestroyed()) {
            return;
        }
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setMessage(getString(R.string.loading_ads));
            dialog.setCancelable(false);
        }
        dialog.show();
    }

    protected void logEvent(@NonNull String event) {
        firebaseAnalytics.logEvent(event.replace("-", "_"), null);
//    logger.logEvent(event);
    }

    protected void loadAdViewGoogle(final AdView adView) {
        if (!useGoogleInterstitialAd()) {
            return;
        }

        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setVisibility(View.VISIBLE);
    }

    protected void configureToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    protected void configureToolbar(final String title, @DrawableRes final int navigationIcon) {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(navigationIcon);
        setSupportActionBar(toolbar);
    }

    protected void configureToolbar(String title) {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }

    protected void showMap(String place) {
        try {
            final Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + place);
            final Intent mapIntent = new Intent("android.intent.action.VIEW", gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            safeStartActivity(mapIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Install this First", Toast.LENGTH_LONG).show();
            final String marketLink = "market://details?id=";
            startActivity(new Intent("android.intent.action.VIEW",
                    Uri.parse(marketLink + "com.google.android.apps.maps")));
        }
    }

    private void safeStartActivity(final Intent intent) {
        PackageManager packageManager = getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.intent_cannot_resolve, Toast.LENGTH_SHORT).show();
        }
    }

    protected void share() {
        final String appName = getString(R.string.app_name);
        final String externalString = getString(R.string.string);
        final Intent sendIntent = new Intent()
                .setAction(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, appName + "\n" + externalString + "\n"
                        + "https://play.google.com/store/apps/details?id=" + getPackageName())
                .setType("text/plain");
        safeStartActivity(sendIntent);
    }

    public void rateApp() {
        safeStartActivity(
                new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
    }

    protected void makeCall(final String number) {
        final Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        safeStartActivity(intent);
    }

    protected void askWriteExternalStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, R.string.enable_write_external_permission,
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this,
                    Constants.WRITE_EXTERNAL_STORAGE_PERMISSIONS, Constants.RC_WRITE_EXTERNAL_STORAGE);
        }
    }

    protected void showLanguageDialog() {
        if (isDestroyed()) {
            return;
        }
//    if (!SharedPreferenceUtils.getInstance().getBooleanValue(Constants.KEY_LANGUAGE_DIALOG, true)) {
//      return;
//    }
        final String[] languages = getResources().getStringArray(R.array.languages);
        final String[] languageCodes = getResources().getStringArray(R.array.language_codes);

        new Builder(this)
                .setSingleChoiceItems(languages, -1, (dialog, which) -> {
                    final String code = languageCodes[which];
                    LocaleManager.setNewLocale(this, code);

                    final Intent i = new Intent(this, WelcomeActivity.class);
                    startActivity(
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                    SharedPreferenceUtils.getInstance().setValue(Constants.KEY_LANGUAGE_DIALOG, false);
                    if (!LocaleManager.getLanguage().equals(code)) {
                        System.exit(0);
                    }
                })
                .setTitle(R.string.choose_language)
                .setCancelable(true)
                .setOnCancelListener(dialog -> {
                    SharedPreferenceUtils.getInstance()
                            .setValue(Constants.KEY_LANGUAGE_DIALOG, false);
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    public interface OnAdMobError {

        void onError(int errorCode);
    }

    public static class AdLoadedListener {

        public void onAdFailedToLoad() {

        }

        public void onAdClosed() {

        }
    }
}
