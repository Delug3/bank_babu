package com.bankbabu.balance.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bankbabu.balance.R;
import com.bankbabu.balance._common.navigator.core.ToolbarId;
import com.bankbabu.balance.activities.BankMenuActivity;
import com.bankbabu.balance.activities.core.BaseActivity.AdLoadedListener;
import com.bankbabu.balance.adapters.search.CustomSuggestionsAdapter;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.database.AssetDatabase;
import com.bankbabu.balance.models.Bank;
import com.bankbabu.balance.utils.AbstractTextWatcher;
import com.bankbabu.balance.utils.BankListSorter;
import com.bankbabu.balance.utils.LocaleManager;
import com.bankbabu.balance.views.searchbar.adapter.NpaLinearLayoutManager;
import com.bankbabu.balance.views.searchbar.adapter.SuggestionsAdapter.OnItemViewClickListener;
import com.google.android.gms.ads.AdView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/15/19.
 */
public class SearchFragment extends BaseFragment {

    @BindView(R.id.suggestion_list) RecyclerView suggestionList;
    @BindView(R.id.search_box) EditText searchBox;
    @BindView(R.id.ad_view) AdView adView;
    @BindView(R.id.voice_search_container) CardView voiceRecognitionContainer;
    private final int VOICE_RECOGNITION_REQUEST_CODE = 756;
    private CustomSuggestionsAdapter customSuggestionsAdapter;
    private String searchText;

    @Override
    protected boolean withToolbar() {
        return true;
    }

    @Override
    protected void initUi() {
        initToolbar(ToolbarId.TOOLBAR_HOME, R.string.bank_balance_check);
        toolbarContainer.setVisibility(View.VISIBLE);
        ButterKnife.bind(this, getActivity());
    }

    @Override
    protected void handleData() {
        if (getArguments() != null && getArguments().containsKey("searchText")) {
            searchText = getArguments().getString("searchText");
        }
    }

    @Override
    protected void setUi(@Nullable final Bundle savedInstanceState) {
        final AssetDatabase assetDatabase = new AssetDatabase(getParentActivity());
        final List<Bank> suggestions = BankListSorter.sortSuggestions(assetDatabase.getBanks());
        loadAdViewGoogle(adView);

        customSuggestionsAdapter = new CustomSuggestionsAdapter(
                LayoutInflater.from(getParentActivity()), suggestions);

        customSuggestionsAdapter.setSuggestions(suggestions);
        customSuggestionsAdapter.setListener(new OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(final int position, final View v) {
                launchBankMenu(customSuggestionsAdapter.getSuggestions().get(position));
            }

            @Override
            public void OnItemDeleteListener(final int position, final View v) {

            }
        });

        suggestionList.setAdapter(customSuggestionsAdapter);
        suggestionList.setLayoutManager(new NpaLinearLayoutManager(getContext()));

        searchBox.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                customSuggestionsAdapter.getFilter().filter(searchBox.getText());
            }
        });

        if (searchText == null) {
            searchBox.clearFocus();
            searchBox.requestFocus();
            showKeyboard(searchBox);
        } else {
            searchBox.setText(searchText);
        }

        searchBox.setOnFocusChangeListener(this::toggleKeyboard);
        voiceRecognitionContainer.setOnClickListener(v -> startVoiceRecognitionActivity());
    }


    private void launchBankMenu(final Bank bank) {
        final Intent intent = new Intent(getContext(), BankMenuActivity.class);
        intent.putExtra(Constants.EXTRA_BANK_NAME, bank.getName());
        intent.putExtra(Constants.EXTRA_BANK_BALANCE, bank.getInquiry());
        intent.putExtra(Constants.EXTRA_BANK_STATEMENT, bank.getMiniStatement());
        intent.putExtra(Constants.EXTRA_CUSTOMER_CARE, bank.getCare());
        intent.putExtra(Constants.EXTRA_INTERNET_BANKING, bank.getNetBankApi());

        getParentActivity().showAdProgressBar();
        loadDefaultGoogleInterstitialAd(new AdLoadedListener() {
            @Override
            public void onAdFailedToLoad() {
                super.onAdFailedToLoad();
                getParentActivity().dismissAdProgressDialog();
                startActivity(intent);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                getParentActivity().dismissAdProgressDialog();
                startActivity(intent);
            }
        }, R.string.interstitial_full_screen);
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void toggleKeyboard(View v, boolean hasFocus) {
        if (hasFocus) {
            showKeyboard(v);
        } else {
            hideKeyboard(v);
        }
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

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_search;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> recognizedWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                searchBox.setText(recognizedWords.get(0));
            }
        }
    }

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }
}
