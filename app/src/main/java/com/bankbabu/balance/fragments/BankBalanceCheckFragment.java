package com.bankbabu.balance.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bankbabu.balance.R;
import com.bankbabu.balance._common.navigator.core.FragmentData;
import com.bankbabu.balance._common.navigator.core.FragmentId;
import com.bankbabu.balance._common.navigator.core.ToolbarId;
import com.bankbabu.balance.activities.BankMenuActivity;
import com.bankbabu.balance.activities.core.BaseActivity.AdLoadedListener;
import com.bankbabu.balance.adapters.bank.BankStateRecyclerViewAdapter;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.database.AssetDatabase;
import com.bankbabu.balance.events.OnBankItemClickEvent;
import com.bankbabu.balance.events.OnBankStateFavoriteClickEvent;
import com.bankbabu.balance.events.OnBankStateItemClickEvent;
import com.bankbabu.balance.events.OnMainToolItemClickEvent;
import com.bankbabu.balance.models.Bank;
import com.bankbabu.balance.utils.BankListSorter;
import com.bankbabu.balance.utils.LocaleManager;
import com.bankbabu.balance.utils.SharedPreferenceUtils;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class BankBalanceCheckFragment extends BaseFragment {

    private final int VOICE_RECOGNITION_REQUEST_CODE = 756;

    @BindView(R.id.text_view_search) TextView textViewSearch;
    @BindView(R.id.recycler_view_more_banks) RecyclerView recyclerViewMoreBanks;
    @BindView(R.id.voice_search_container) CardView voiceSearchContainer;
    @BindView(R.id.ad_view) AdView adView;

    private AssetDatabase assetDatabase;
    private List<Bank> moreBanks;
    private BankStateRecyclerViewAdapter moreBanksAdapter;
    private boolean showFavoriteBanks;


    @Override
    protected boolean withToolbar() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_bank_balance_check;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == Constants.RC_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getContext(), R.string.user_denied_permissions, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }

    @Override
    protected void initUi() {
        initToolbar(ToolbarId.TOOLBAR_BOOKMARK, R.string.bank_balance_check);
        toolbarContainer.setVisibility(View.VISIBLE);
        ButterKnife.bind(this,getActivity());
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onBankItemClickEvent(OnBankItemClickEvent event) {
        launchBankMenu(event.getBank());
    }

    private void launchBankMenu(final Bank bank) {
        final Intent intent = new Intent(getContext(), BankMenuActivity.class);
        intent.putExtra(Constants.EXTRA_BANK_NAME, bank.getName());
        intent.putExtra(Constants.EXTRA_BANK_BALANCE, bank.getInquiry());
        intent.putExtra(Constants.EXTRA_BANK_STATEMENT, bank.getMiniStatement());
        intent.putExtra(Constants.EXTRA_CUSTOMER_CARE, bank.getCare());
        intent.putExtra(Constants.EXTRA_INTERNET_BANKING, bank.getNetBankApi());

        startActivity(intent);
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        assetDatabase = new AssetDatabase(getContext());
        loadAdViewGoogle(adView);

        ((SwitchCompat) toolbarContainer.findViewById(R.id.switch_bookmark)).setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    showFavoriteBanks = isChecked;
                    SharedPreferenceUtils.getInstance().setValue(SharedPreferenceUtils.BOOKMARK, isChecked);
                    updateMoreBanks();
                });

        updateMoreBanks();

        textViewSearch
                .setOnClickListener(v -> changeFragmentTo(new FragmentData(FragmentId.SEARCH, null)));
        voiceSearchContainer.setOnClickListener(v -> startVoiceRecognitionActivity());
    }

    public void updateMoreBanks() {
        if (showFavoriteBanks) {
            moreBanks = assetDatabase.getFavoriteBanks();
        } else {
            moreBanks = BankListSorter.sortSuggestions(assetDatabase.getBanks());
        }

        recyclerViewMoreBanks.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerViewMoreBanks.setItemAnimator(new DefaultItemAnimator());
        moreBanksAdapter = new BankStateRecyclerViewAdapter(moreBanks);
        recyclerViewMoreBanks.setAdapter(moreBanksAdapter);
    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, LocaleManager.getLanguage());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak_now));
        try {
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(), R.string.device_not_supported, Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onStateItemClickEvent(OnBankStateItemClickEvent event) {
        launchBankMenu(event.getBank());
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onStateFavoriteClickEvent(OnBankStateFavoriteClickEvent event) {
        int count = assetDatabase
                .setBankFavorite(event.getBank().getId(), event.getBank().getFav() == 0);
        if (count > 0) {
            updateMoreBanks();
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onMainToolItemClickEvent(final OnMainToolItemClickEvent event) {
        loadDefaultGoogleInterstitialAd(new AdLoadedListener() {
            @Override
            public void onAdFailedToLoad() {
                super.onAdFailedToLoad();
                startActivity(new Intent(getContext(), event.getIntentClass()));
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                startActivity(new Intent(getContext(), event.getIntentClass()));
            }
        }, R.string.interstitial_tool);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> recognizedWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                final Bundle searchFragmentData = new Bundle();
                searchFragmentData.putString("searchText", recognizedWords.get(0));

                changeFragmentTo(new FragmentData(FragmentId.SEARCH, searchFragmentData));
            }
        }
    }
}


