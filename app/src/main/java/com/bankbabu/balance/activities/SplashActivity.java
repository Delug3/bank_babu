package com.bankbabu.balance.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.utils.SharedPreferenceUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;


public class SplashActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initUi() {
        MobileAds.initialize(this, getString(R.string.google_app_ad_id));
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("splash");

        final CountDownTimer countDownTimer = new CountDownTimer(300, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                final Intent intent = new Intent(SplashActivity.this,
                        SharedPreferenceUtils.getInstance().isFirstTimeLaunch()
                                ? OnBoardingActivity.class
                                : WelcomeActivity.class);

                InterstitialAd googleInterstitialAd = new InterstitialAd(SplashActivity.this);
                googleInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

                final AdRequest adRequest = new AdRequest.Builder().build();

                googleInterstitialAd.loadAd(adRequest);
                googleInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        startActivity(intent);
                    }

                    @Override
                    public void onAdFailedToLoad(final int i) {
                        super.onAdFailedToLoad(i);
                        startActivity(intent);
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        if (googleInterstitialAd.isLoaded()) {
                            googleInterstitialAd.show();
                        }
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }
                });
            }
        }.start();

        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler(countDownTimer))
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

        private CountDownTimer countDownTimer;

        ExampleNotificationOpenedHandler(CountDownTimer countDownTimer) {
            this.countDownTimer = countDownTimer;
        }

        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            countDownTimer.cancel();
            final JSONObject data = result.notification.payload.additionalData;
            String customKey;

            if (data != null) {
                customKey = data.optString("key", null);
                if (customKey != null) {
                    if (customKey.equals("0") || customKey.equals("1") || customKey.equals("2") || customKey
                            .equals("3") || customKey.equals("4") || customKey.equals("5") || customKey
                            .equals("6") || customKey.equals("7") || customKey.equals("8")) {
                        launchNextActivity(customKey);
                    }
                }
            }
        }

        private void launchNextActivity(@NonNull final String post) {
            Intent intent;
            switch (post) {
                default:
                case "0": {
                    intent = new Intent(SplashActivity.this, StateActivity.class);
                    logEvent("push-state");
                    break;
                }
                case "1": {
                    intent = new Intent(SplashActivity.this, HomeActivity.class);
                    logEvent("push-home");
                    break;
                }
                case "2": {
                    intent = new Intent(SplashActivity.this, AddEditFormActivity.class);
                    logEvent("push-reminder");
                    break;
                }
                case "3": {
                    intent = new Intent(SplashActivity.this, PaymentsActivity.class);
                    logEvent("push-payment");
                    break;
                }
                case "4": {
                    intent = new Intent(SplashActivity.this, NearByActivity.class);
                    logEvent("push-nearby");
                    break;
                }
                case "5": {
                    intent = new Intent(SplashActivity.this, GstCalculatorActivity.class);
                    logEvent("push-gst");
                    break;
                }
                case "6": {
                    intent = new Intent(SplashActivity.this, PfStatusActivity.class);
                    logEvent("push-pf");
                    break;
                }
                case "7": {
                    intent = new Intent(SplashActivity.this, SipCalculatorActivity.class);
                    logEvent("push-sip");
                    break;
                }
                case "8": {
                    intent = new Intent(SplashActivity.this, EmiCalculatorActivity.class);
                    logEvent("push-emi");
                    break;
                }
            }
            intent.putExtra(Constants.EXTRA_PUSH, 1);
            startActivity(intent);
            finish();
        }
    }
}
