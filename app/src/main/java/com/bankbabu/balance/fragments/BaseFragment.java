package com.bankbabu.balance.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.activities.core.BaseActivity.AdLoadedListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public abstract class BaseFragment extends com.bankbabu.balance._common.core.BaseFragment {

    private InterstitialAd googleInterstitialAd;
    private FirebaseAnalytics firebaseAnalytics;
    private ProgressDialog dialog;

    public void loadDefaultGoogleInterstitialAd(final AdLoadedListener adListener,
                                                @StringRes final int adUnitId) {
        if (!useGoogleInterstitialAd()) {
            return;
        }

        googleInterstitialAd = new InterstitialAd(getParentActivity());
        googleInterstitialAd.setAdUnitId(getString(adUnitId));

        final AdRequest adRequest = new AdRequest.Builder().build();

        showAdProgressBar();
        googleInterstitialAd.loadAd(adRequest);
        googleInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (isAdded()) {
                    adListener.onAdClosed();
                }
            }

            @Override
            public void onAdFailedToLoad(final int i) {
                super.onAdFailedToLoad(i);
                if (isAdded()) {
                    adListener.onAdFailedToLoad();
                }
                dismissAdProgressDialog();
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

    @SuppressWarnings("unused")
    protected BaseActivity getParentActivity() {
        return ((BaseActivity) getActivity());
    }

    public void showAdProgressBar() {
        if (isDetached()) {
            return;
        }

        if (dialog == null) {
            dialog = new ProgressDialog(getContext());
            dialog.setMessage(getString(R.string.loading_ads));
            dialog.setCancelable(false);
        }
        dialog.show();
    }

    public void dismissAdProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onViewCreated(@NotNull final View view, @Nullable final Bundle savedInstanceState) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(getParentActivity());
        super.onViewCreated(view, savedInstanceState);
    }

    public void loadAdMobBanner(final int viewId, @Nullable BaseActivity.OnAdMobError listener) {
        AdView adView = findViewById(viewId);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.setAdListener(new AdListener() {


            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (listener != null && isAdded()) {
                    listener.onError(errorCode);
                }
            }
        });

        adView.loadAd(adRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callOnActivityResultOnChildFragments(this, requestCode, resultCode, data);
    }

    private static void callOnActivityResultOnChildFragments(Fragment parent, int requestCode,
                                                             int resultCode, Intent data) {
        final FragmentManager childFragmentManager = parent.getChildFragmentManager();
        final List<Fragment> childFragments = childFragmentManager.getFragments();
        for (Fragment child : childFragments) {
            if (child != null && !child.isDetached() && !child.isRemoving()) {
                child.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    boolean useEventBus() {
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    protected void logEvent(@NonNull String event) {
        firebaseAnalytics.logEvent(event.replace("-", "_"), null);
    }

    protected void showMap(String place) {
        try {
            final Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + place);
            final Intent mapIntent = new Intent("android.intent.action.VIEW", gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            safeStartActivity(mapIntent, getContext());
        } catch (Exception e) {
            Toast.makeText(getContext(), "Install this First", Toast.LENGTH_LONG).show();
            final String marketLink = "market://details?id=";
            startActivity(new Intent("android.intent.action.VIEW",
                    Uri.parse(marketLink + "com.google.android.apps.maps")));
        }
    }

    protected void safeStartActivity(final Intent intent, final Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, R.string.intent_cannot_resolve, Toast.LENGTH_SHORT).show();
        }
    }

    protected void loadAdViewGoogle(final AdView adView) {
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
