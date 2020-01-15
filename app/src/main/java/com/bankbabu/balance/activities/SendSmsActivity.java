package com.bankbabu.balance.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.application.Constants;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendSmsActivity extends BaseActivity {

    private String phoneNumber;
    @BindView(R.id.button_send) Button buttonSend;
    @BindView(R.id.edit_text_message) EditText editText;
    @BindView(R.id.text_view_info) TextView textViewInfo;
    @BindView(R.id.ad_view) AdView adView;


    @Override
    protected int getContentView() {
        return R.layout.activity_sms;

    }

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }

    @Override
    protected void initUi() {
        //testting and implementing butterknife
        ButterKnife.bind(this);
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("sms-activity");

        final Intent intent = getIntent();
        final String bankName = intent.getStringExtra(Constants.EXTRA_BANK_NAME);
        final String message = intent.getStringExtra(Constants.EXTRA_BANK_MESSAGE);
        final String info = intent.getStringExtra(Constants.EXTRA_BANK_INFO);
        phoneNumber = intent.getStringExtra(Constants.EXTRA_BANK_PHONE_NUMBER);

        configureToolbar(bankName, R.drawable.ic_toolbar_arrow);

        loadAdViewGoogle(adView);

        editText.setText(message);
        textViewInfo.setText(info);

        buttonSend.setOnClickListener(v -> sendSms());
    }

    private void sendSms() {
        final String msg = editText.getText().toString();

        if (msg.length() > 0) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + Uri.encode(phoneNumber)));
            intent.putExtra("sms_body", msg);
            startActivity(intent);
        } else {
            Toast.makeText(SendSmsActivity.this, R.string.enter_text_message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bank_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.share:
                share();
                return true;

            case R.id.home:
                launchHomeScreen();
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(SendSmsActivity.this, ToolsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
