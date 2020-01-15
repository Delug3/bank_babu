package com.bankbabu.balance.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bankbabu.balance.R;
import com.bankbabu.balance.events.OnScrollToFocusDownEvent;
import com.bankbabu.balance.utils.CalculatorHelper;
import com.bankbabu.balance.utils.LocaleHelper;
import com.bankbabu.balance.views.CurrencyEditText;
import com.bankbabu.balance.views.EditTextWithSuffix;

import org.fabiomsr.moneytextview.MoneyTextView;
import org.greenrobot.eventbus.EventBus;

import java.util.Currency;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SipCalculatorFragment extends BaseFragment {

    @BindView(R.id.edit_text_maturity_amount) CurrencyEditText editTextMaturityAmount;
    @BindView(R.id.radio_group) RadioGroup radioGroup;
    @BindView(R.id.button_maturity_amount) ToggleButton buttonMaturityAmount;
    @BindView(R.id.button_investment_period) ToggleButton buttonInvestmentPeriod;
    @BindView(R.id.button_sip_amount) ToggleButton buttonSipAmount;
    @BindView(R.id.edit_text_sip_amount) CurrencyEditText editTextSipAmount;
    @BindView(R.id.edit_text_period) EditTextWithSuffix editTextPeriod;
    @BindView(R.id.sip_amount_wrapper) LinearLayout sipAmountWrapper;
    @BindView(R.id.sip_amount_wrapper_input) LinearLayout sipAmountWrapperInput;
    @BindView(R.id.edit_text_annual_report) EditTextWithSuffix editTextAnnualReport;
    @BindView(R.id.calculator_title) TextView calculatorTitle;
    @BindView(R.id.button_calculate) Button buttonCalculate;
    @BindView(R.id.reset_button) Button resetButton;
    @BindView(R.id.maturity_amount_wrapper) LinearLayout maturityAmountWrapper;
    @BindView(R.id.maturity_amount_result_wrapper_placeholder) LinearLayout maturityAmountResultPlaceholder;
    @BindView(R.id.maturity_amount_wrapper_input) LinearLayout maturityAmountWrapperInput;
    @BindView(R.id.investment_period_wrapper) LinearLayout investmentPeriodWrapper;
    @BindView(R.id.investment_period_wrapper_input) LinearLayout investmentPeriodWrapperInput;
    @BindView(R.id.sip_amount_result_wrapper) LinearLayout sipAmountResultWrapper;
    @BindView(R.id.sip_amount_result_wrapper_placeholder) LinearLayout sipAmountResultWrapperPlaceholder;
    @BindView(R.id.maturity_amount_result_wrapper) LinearLayout maturityAmountResultWrapper;
    @BindView(R.id.period_result_wrapper) LinearLayout investmentPeriodResultWrapper;
    @BindView(R.id.period_result_wrapper_placeholder) LinearLayout investmentPeriodResultWrapperPlaceholder;
    @BindView(R.id.text_view_maturity_amount) MoneyTextView textViewMaturityAmount;
    @BindView(R.id.text_view_sip_amount) MoneyTextView textViewSipAmount;
    @BindView(R.id.text_view_period) TextView textViewPeriod;
    @BindView(R.id.maturity_amount_image) ImageView maturityAmountImage;

    private final OnClickListener onToggleClickListener = view -> {
        ((RadioGroup) view.getParent()).check(view.getId());
        ((ToggleButton) view).setChecked(radioGroup.getCheckedRadioButtonId() == view.getId());

        onToggleButtonChanged(view.getId());
    };

    @Override
    protected void initUi() {
        ButterKnife.bind(this, getActivity());
    }

    @Override
    protected void setUi(@Nullable final Bundle savedInstanceState) {
        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                view.setChecked(view.getId() == i);
            }
        });

        loadAdMobBanner(R.id.ad_view, errorCode -> Log.d(CalculatorFragment.class.getSimpleName(), String.format("Could not load AdMob, Error code: [%s]", errorCode)));

        buttonMaturityAmount.setOnClickListener(onToggleClickListener);
        buttonInvestmentPeriod.setOnClickListener(onToggleClickListener);
        buttonSipAmount.setOnClickListener(onToggleClickListener);

        buttonCalculate.setOnClickListener(v -> {
            resetButton.setBackgroundResource(R.drawable.toggle_button_sip_background);
            resetButton.setTextColor(getResources().getColor(R.color.whisper));
            buttonCalculate.setBackgroundResource(R.drawable.search_border);
            buttonCalculate.setTextColor(getResources().getColor(R.color.colorPrimary));
            calculate();
        });
        resetButton.setOnClickListener(v -> {
            resetButton.setBackgroundResource(R.drawable.search_border);
            resetButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            buttonCalculate.setBackgroundResource(R.drawable.toggle_button_sip_background);
            buttonCalculate.setTextColor(getResources().getColor(R.color.white));
            setDefaultConfiguration();
        });

        setDefaultConfiguration();
    }

    private void calculate() {
        if (!validateFields()) {
            return;
        }

        maturityAmountResultWrapper.setVisibility(View.GONE);
        sipAmountResultWrapper.setVisibility(View.GONE);
        investmentPeriodResultWrapper.setVisibility(View.GONE);

        switch (radioGroup.getCheckedRadioButtonId()) {
            default:
            case R.id.button_maturity_amount:
                calculateMaturityAmount();
                break;
            case R.id.button_investment_period:
                calculateInvestmentPeriod();
                break;
            case R.id.button_sip_amount:
                calculateSipAmount();
                break;
        }

        EventBus.getDefault().post(new OnScrollToFocusDownEvent());
    }

    private void setDefaultConfiguration() {
        editTextPeriod.setText("");
        editTextAnnualReport.setText("");
        editTextSipAmount.setText("");
        editTextSipAmount.setLocale(LocaleHelper.getIndiaLocale());
        editTextMaturityAmount.setText("");
        editTextMaturityAmount.setLocale(LocaleHelper.getIndiaLocale());

        textViewMaturityAmount.setAmount(0);
        textViewMaturityAmount
                .setSymbol(Currency.getInstance(LocaleHelper.getIndiaLocale()).getSymbol());
        textViewSipAmount.setAmount(0);
        textViewSipAmount.setSymbol(Currency.getInstance(LocaleHelper.getIndiaLocale()).getSymbol());
        textViewPeriod.setText("");

        maturityAmountResultPlaceholder.setVisibility(View.VISIBLE);
        maturityAmountResultWrapper.setVisibility(View.GONE);
        sipAmountResultWrapper.setVisibility(View.GONE);
        sipAmountResultWrapperPlaceholder.setVisibility(View.GONE);
        investmentPeriodResultWrapper.setVisibility(View.GONE);

        buttonMaturityAmount.setChecked(true);
        onToggleClickListener.onClick(buttonMaturityAmount);
    }

    private boolean validateFields() {
        switch (radioGroup.getCheckedRadioButtonId()) {
            default:
            case R.id.button_maturity_amount:
                return validateAnnualReport() & validatePeriod() & validateSipAmount();

            case R.id.button_investment_period:
                return validateAnnualReport() & validateMaturityAmount() & validateSipAmount();

            case R.id.button_sip_amount:
                return validateAnnualReport() & validatePeriod() & validateMaturityAmount();
        }
    }

    private void calculateMaturityAmount() {
        final int period = Integer.parseInt(editTextPeriod.getText().toString());
        final int annualReport = Integer.parseInt(editTextAnnualReport.getText().toString());
        final long sipAmount = editTextSipAmount.getRawValue();

        double maturityAmount = CalculatorHelper
                .calculateMaturityAmount(sipAmount, period, annualReport, true);
        textViewMaturityAmount.setAmount((float) maturityAmount);
        maturityAmountResultWrapper.setVisibility(View.VISIBLE);
        textViewMaturityAmount.requestFocus();

        maturityAmountWrapper.setVisibility(View.VISIBLE);
        maturityAmountResultPlaceholder.setVisibility(View.GONE);
    }

    private void calculateInvestmentPeriod() {
        final long sipAmount = editTextSipAmount.getRawValue();
        final int annualReport = Integer.parseInt(editTextAnnualReport.getText().toString());
        final long maturityAmount = editTextMaturityAmount.getRawValue();

        int period = CalculatorHelper
                .calculateInvestmentPeriod(sipAmount, maturityAmount, annualReport);
        int years = period / 12; // 1
        int remainingMonths = period % 12;
        String result = "";
        if (years > 0) {
            result += years + " " +
                    getResources().getQuantityText(R.plurals.year, years).toString();
            if (remainingMonths > 0) {
                result += " " + remainingMonths + " " +
                        getResources().getQuantityText(R.plurals.month, remainingMonths).toString();
            }
        } else {
            result += period + " " +
                    getResources().getQuantityText(R.plurals.month, period).toString();
        }

        textViewPeriod.setText(result);
        investmentPeriodResultWrapper.setVisibility(View.VISIBLE);
        investmentPeriodResultWrapperPlaceholder.setVisibility(View.GONE);
        textViewPeriod.requestFocus();
    }

    private void calculateSipAmount() {
        final int period = Integer.parseInt(editTextPeriod.getText().toString());
        final int annualReport = Integer.parseInt(editTextAnnualReport.getText().toString());
        final long maturityAmount = editTextMaturityAmount.getRawValue();

        double sipAmount = CalculatorHelper
                .calculateSipAmount(maturityAmount, period, annualReport, true);

        textViewSipAmount.setAmount((float) sipAmount);
        sipAmountResultWrapper.setVisibility(View.VISIBLE);
        sipAmountResultWrapperPlaceholder.setVisibility(View.GONE);
        textViewSipAmount.requestFocus();
    }

    private boolean validateAnnualReport() {
        boolean result = true;
        try {
            @SuppressWarnings("unused") final int annualReport = Integer
                    .parseInt(editTextAnnualReport.getText().toString());
        } catch (NumberFormatException e) {
            editTextAnnualReport.setError(getString(R.string.error_expected_annual_return_rate_is_empty));
            editTextAnnualReport.requestFocus();
            result = false;
        }

        return result;
    }

    private boolean validatePeriod() {
        boolean result = true;
        try {
            @SuppressWarnings("unused") final int period = Integer
                    .parseInt(editTextPeriod.getText().toString());
        } catch (NumberFormatException e) {
            editTextPeriod.setError(getString(R.string.error_investment_period_is_empty));
            editTextPeriod.requestFocus();
            result = false;
        }

        return result;
    }

    private boolean validateSipAmount() {
        boolean result = true;
        try {
            @SuppressWarnings("unused") final long sipAmount = editTextSipAmount.getRawValue();
        } catch (NumberFormatException e) {
            editTextSipAmount.setError(getString(R.string.error_sip_amount_is_empty));
            editTextSipAmount.requestFocus();
            result = false;
        }

        return result;
    }

    private boolean validateMaturityAmount() {
        boolean result = true;
        try {
            @SuppressWarnings("unused") final long maturityAmount = editTextMaturityAmount.getRawValue();
        } catch (NumberFormatException e) {
            editTextMaturityAmount.setError(getString(R.string.error_maturity_amount_is_empty));
            editTextMaturityAmount.requestFocus();
            result = false;
        }

        return result;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_sip_calculator;
    }

    private void onToggleButtonChanged(final int id) {
        switch (id) {
            default:
            case R.id.button_maturity_amount:
                calculatorTitle.setText(R.string.maturity_amount);
                maturityAmountWrapper.setVisibility(View.GONE);
                maturityAmountResultWrapper.setVisibility(textViewMaturityAmount.getAmount() == 0 ? View.GONE : View.VISIBLE);
                maturityAmountResultPlaceholder.setVisibility(textViewMaturityAmount.getAmount() == 0 ? View.VISIBLE : View.GONE);
                maturityAmountWrapperInput.setVisibility(View.GONE);
                investmentPeriodWrapper.setVisibility(View.VISIBLE);
                investmentPeriodWrapperInput.setVisibility(View.VISIBLE);
                sipAmountWrapper.setVisibility(View.VISIBLE);
                sipAmountWrapperInput.setVisibility(View.VISIBLE);
                sipAmountResultWrapperPlaceholder.setVisibility(View.GONE);
                sipAmountResultWrapper.setVisibility(View.GONE);
                investmentPeriodResultWrapperPlaceholder.setVisibility(View.GONE);
                investmentPeriodResultWrapper.setVisibility(View.GONE);
                maturityAmountImage.setImageResource(R.drawable.investment_period_icon);
                break;

            case R.id.button_investment_period:
                calculatorTitle.setText(R.string.investment_period_title);
                maturityAmountWrapper.setVisibility(View.VISIBLE);
                maturityAmountResultPlaceholder.setVisibility(View.GONE);
                maturityAmountResultWrapper.setVisibility(View.GONE);
                maturityAmountWrapperInput.setVisibility(View.VISIBLE);
                investmentPeriodWrapper.setVisibility(View.GONE);
                investmentPeriodWrapperInput.setVisibility(View.GONE);
                sipAmountWrapper.setVisibility(View.VISIBLE);
                sipAmountWrapperInput.setVisibility(View.VISIBLE);
                sipAmountResultWrapperPlaceholder.setVisibility(View.GONE);
                sipAmountResultWrapper.setVisibility(View.GONE);
                int length = textViewPeriod.length();
                investmentPeriodResultWrapperPlaceholder.setVisibility(length == 0 ? View.VISIBLE : View.GONE);
                investmentPeriodResultWrapper.setVisibility(textViewPeriod.length() == 0 ? View.GONE : View.VISIBLE);
                maturityAmountImage.setImageResource(R.drawable.investment_period_icon);
                break;

            case R.id.button_sip_amount:
                calculatorTitle.setText(R.string.sip_amount_title);
                maturityAmountWrapper.setVisibility(View.VISIBLE);
                maturityAmountResultPlaceholder.setVisibility(View.GONE);
                maturityAmountResultWrapper.setVisibility(View.GONE);
                maturityAmountWrapperInput.setVisibility(View.VISIBLE);
                investmentPeriodWrapper.setVisibility(View.VISIBLE);
                investmentPeriodWrapperInput.setVisibility(View.VISIBLE);
                sipAmountWrapper.setVisibility(View.GONE);
                sipAmountWrapperInput.setVisibility(View.GONE);
                sipAmountResultWrapperPlaceholder.setVisibility(textViewSipAmount.getAmount() == 0 ? View.VISIBLE : View.GONE);
                sipAmountResultWrapper.setVisibility(textViewSipAmount.getAmount() == 0 ? View.GONE : View.VISIBLE);
                investmentPeriodResultWrapperPlaceholder.setVisibility(View.GONE);
                investmentPeriodResultWrapper.setVisibility(View.GONE);
                maturityAmountImage.setImageResource(R.drawable.sip_amount_icon);
                break;
        }
    }
}
