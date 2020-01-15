package com.bankbabu.balance.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.events.OnResetButtonClickEvent;
import com.bankbabu.balance.events.OnToggleButtonClickEvent;
import com.bankbabu.balance.models.CalculationType;
import com.bankbabu.balance.utils.CalculatorHelper;
import com.bankbabu.balance.utils.InputFilterMinMax;
import com.bankbabu.balance.utils.LocaleHelper;
import com.bankbabu.balance.views.CurrencyEditText;
import com.bankbabu.balance.views.EditTextWithSuffix;

import org.fabiomsr.moneytextview.MoneyTextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Currency;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EmiCalculatorFragment extends BaseFragment {

    @BindView(R.id.edit_text_loan_amount) CurrencyEditText editTextLoanAmount;
    @BindView(R.id.edit_text_period) EditTextWithSuffix editTextPeriod;
    @BindView(R.id.edit_text_monthly_emi) CurrencyEditText editTextMonthlyEmi;
    @BindView(R.id.edit_text_interest_rate) EditTextWithSuffix editTextInterestRate;
    @BindView(R.id.monthly_emi_input_wrapper) LinearLayout monthlyEmiInputWrapper;
    @BindView(R.id.loan_amount_input_wrapper) LinearLayout loanAmountInputWrapper;
    @BindView(R.id.period_input_wrapper) LinearLayout periodInputWrapper;
    @BindView(R.id.interest_rate_input_wrapper) LinearLayout interestRateInputWrapper;
    @BindView(R.id.monthly_emi_wrapper) LinearLayout monthlyEmiWrapper;
    @BindView(R.id.loan_amount_wrapper) LinearLayout loanAmountWrapper;
    @BindView(R.id.total_interest_wrapper) LinearLayout totalInterestWrapper;
    @BindView(R.id.total_payment_wrapper) LinearLayout totalPaymentWrapper;
    @BindView(R.id.period_wrapper) LinearLayout periodWrapper;
    @BindView(R.id.interest_wrapper) LinearLayout interestWrapper;
    @BindView(R.id.text_view_monthly_emi) MoneyTextView textViewMonthlyEmi;
    @BindView(R.id.text_view_loan_amount) MoneyTextView textViewLoanAmount;
    @BindView(R.id.text_view_total_interest) MoneyTextView textViewTotalInterest;
    @BindView(R.id.text_view_total_payment) MoneyTextView textViewTotalPayment;
    @BindView(R.id.text_view_total_payment_placeholder) MoneyTextView textViewTotalPaymentPlaceholder;
    @BindView(R.id.text_view_period) TextView textViewPeriod;
    @BindView(R.id.text_view_interest) TextView textViewInterest;
    @BindView(R.id.total_calculation_title) TextView totalCalculationTitle;
    @BindView(R.id.total_payment_title) TextView totalPaymentTitle;
    @BindView(R.id.reset_button) Button resetButton;
    @BindView(R.id.button_calculate) Button buttonCalculate;

    private CalculationType calculationType;


    @Override
    boolean useEventBus() {
        return true;
    }

    @Override
    protected void initUi() {
        ButterKnife.bind(this,getActivity());
    }

    @Override
    protected void setUi(@Nullable final Bundle savedInstanceState) {
        loadAdMobBanner(R.id.ad_view, errorCode -> Log.d(EmiCalculatorFragment.class.getSimpleName(),
                String.format("Could not load AdMob, Error code: [%s]", errorCode)));

        editTextPeriod.setFilters(new InputFilter[]{new InputFilterMinMax("1", "500")});

        resetButton.setOnClickListener(v -> {
            setDefaultConfiguration();
            EventBus.getDefault().post(new OnResetButtonClickEvent());

            setButtonSelected(resetButton);
            setButtonDeselected(buttonCalculate);

            showTotalPaymentPlaceholder();
        });

        buttonCalculate.setOnClickListener(v -> {
            hideKeyboard();
            if (!areFieldsValid()) {
                return;
            }

            calculate();

            setButtonSelected(buttonCalculate);
            setButtonDeselected(resetButton);
        });

        setDefaultConfiguration();
    }

    private void setDefaultConfiguration() {
        resetEditText(editTextPeriod);
        resetEditText(editTextLoanAmount);
        resetEditText(editTextMonthlyEmi);
        resetEditText(editTextInterestRate);

        resetResult();

        editTextMonthlyEmi.setLocale(LocaleHelper.getIndiaLocale());
        editTextLoanAmount.setLocale(LocaleHelper.getIndiaLocale());

        totalCalculationTitle.setTextColor(getResources().getColor(R.color.fiord));


        onToggleButtonClickEvent(new OnToggleButtonClickEvent(CalculationType.EMI));
        setTotalPaymentFont("font/helvetica_neue_roman.ttf");
    }

    private void setButtonSelected(Button button) {
        button.setBackgroundResource(R.drawable.gst_procent_border);
        button.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void setButtonDeselected(Button button) {
        button.setBackgroundResource(R.drawable.toggle_button_sip_background);
        button.setTextColor(getResources().getColor(R.color.white));
    }

    private void showTotalPaymentPlaceholder() {
        textViewTotalPayment.setVisibility(View.GONE);
        textViewTotalPaymentPlaceholder.setVisibility(View.VISIBLE);
    }

    private void hideKeyboard() {
        View view = getParentActivity().getCurrentFocus();
        if (view != null) {
            final InputMethodManager imm = (InputMethodManager) getParentActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private boolean areFieldsValid() {
        boolean isValid = true;
        switch (calculationType) {
            case EMI:
                if (isAmountEmpty(editTextLoanAmount)) {
                    isValid = false;
                }

                if (isEditTextEmpty(editTextPeriod)) {
                    isValid = false;

                }

                if (isEditTextEmpty(editTextInterestRate)) {
                    isValid = false;
                }
                break;

            case INTEREST: {
                if (isAmountEmpty(editTextLoanAmount)) {
                    isValid = false;
                }

                if (isAmountEmpty(editTextMonthlyEmi)) {
                    isValid = false;
                }

                if (isEditTextEmpty(editTextPeriod)) {
                    isValid = false;
                }
                break;
            }

            case PERIOD:
                if (isAmountEmpty(editTextLoanAmount)) {
                    isValid = false;
                }

                if (isAmountEmpty(editTextMonthlyEmi)) {
                    isValid = false;
                }

                if (isEditTextEmpty(editTextInterestRate)) {
                    isValid = false;
                }
                break;

            case LOAN_AMOUNT:
                if (isAmountEmpty(editTextMonthlyEmi)) {
                    isValid = false;
                }

                if (isEditTextEmpty(editTextPeriod)) {
                    isValid = false;
                }

                if (isEditTextEmpty(editTextInterestRate)) {
                    isValid = false;
                }
                break;
        }

        return isValid;
    }

    private void calculate() {
        hideResultFields();
        totalCalculationTitle.setTextColor(getResources().getColor(R.color.colorPrimary));

        double loanAmount = 0;
        double monthlyEmi = 0;
        int period = 0;
        double interestRate = 0;

        try {
            if (calculationType != CalculationType.LOAN_AMOUNT) {
                loanAmount = editTextLoanAmount.getRawValue();
                if (loanAmount == 0) {
                    return;
                }
            }

            if (calculationType != CalculationType.EMI) {
                monthlyEmi = editTextMonthlyEmi.getRawValue();
                if (monthlyEmi == 0) {
                    return;
                }
            }

            if (calculationType != CalculationType.PERIOD) {
                period = Integer.parseInt(editTextPeriod.getText().toString());
                if (period == 0) {
                    return;
                }
            }

            if (calculationType != CalculationType.INTEREST) {
                interestRate = Double.parseDouble(editTextInterestRate.getText().toString());
                if (interestRate == 0) {
                    return;
                }
            }
        } catch (NumberFormatException e) {
            return;
        }

        final String currencySymbol = Currency.getInstance(LocaleHelper.getIndiaLocale()).getSymbol();

        // DEFAULT VALUE
        if (calculationType == null) {
            calculationType = CalculationType.LOAN_AMOUNT;
        }
        switch (calculationType) {
            case INTEREST:
                interestWrapper.setVisibility(View.VISIBLE);
                final double calculatedInterestRate = CalculatorHelper
                        .calculateInterest(loanAmount, period, monthlyEmi);
                textViewInterest.setText("".concat(String.valueOf(calculatedInterestRate)).concat("%"));

                totalInterestWrapper.setVisibility(View.VISIBLE);
                final double calculatedTotalPayment1 = CalculatorHelper
                        .calculateTotalPayment(monthlyEmi, period);
                textViewTotalInterest.setAmount((float) CalculatorHelper.calculateTotalInterest(
                        calculatedTotalPayment1, loanAmount), currencySymbol);
                totalPaymentWrapper.setVisibility(View.VISIBLE);
                textViewTotalPayment.setAmount((float) calculatedTotalPayment1, currencySymbol);
                break;
            case LOAN_AMOUNT:
                loanAmountWrapper.setVisibility(View.VISIBLE);
                final double calculatedLoanAmount = CalculatorHelper
                        .calculateAmountLoan(monthlyEmi, period, interestRate);
                textViewLoanAmount.setAmount((float) calculatedLoanAmount, currencySymbol);

                totalInterestWrapper.setVisibility(View.VISIBLE);
                final double calculatedTotalPayment2 = CalculatorHelper
                        .calculateTotalPayment(monthlyEmi, period);
                textViewTotalInterest.setAmount((float) CalculatorHelper.calculateTotalInterest(
                        calculatedTotalPayment2, calculatedLoanAmount), currencySymbol);
                totalPaymentWrapper.setVisibility(View.VISIBLE);
                textViewTotalPayment.setAmount((float) calculatedTotalPayment2, currencySymbol);
                break;
            case PERIOD:
                periodWrapper.setVisibility(View.VISIBLE);
                final int calculatedPeriod = CalculatorHelper
                        .calculatePeriod(loanAmount, monthlyEmi, interestRate);

                int years = calculatedPeriod / 12;
                int remainingMonths = calculatedPeriod % 12;
                String result = "";
                if (years > 0) {
                    result += years + " " +
                            getResources().getQuantityText(R.plurals.year, years).toString();
                    if (remainingMonths > 0) {
                        result += " " + remainingMonths + " " +
                                getResources().getQuantityText(R.plurals.month, remainingMonths).toString();
                    }
                } else {
                    result += remainingMonths + " " +
                            getResources().getQuantityText(R.plurals.month, period).toString();
                }
                textViewPeriod.setText(result);

                totalInterestWrapper.setVisibility(View.VISIBLE);
                final double calculatedTotalPayment3 = CalculatorHelper
                        .calculateTotalPayment(monthlyEmi, calculatedPeriod);
                textViewTotalInterest.setAmount((float) CalculatorHelper.calculateTotalInterest(
                        calculatedTotalPayment3, loanAmount), currencySymbol);
                totalPaymentWrapper.setVisibility(View.VISIBLE);
                textViewTotalPayment.setAmount((float) calculatedTotalPayment3, currencySymbol);
                break;
            case EMI:
                monthlyEmiWrapper.setVisibility(View.VISIBLE);
                final double calculatedMonthlyEmi = CalculatorHelper
                        .calculateMonthlyEmi(loanAmount, period, interestRate);
                textViewMonthlyEmi.setAmount((float) calculatedMonthlyEmi, currencySymbol);
                totalInterestWrapper.setVisibility(View.VISIBLE);
                final double calculatedTotalPayment4 = CalculatorHelper
                        .calculateTotalPayment(calculatedMonthlyEmi,
                                period);
                textViewTotalInterest.setAmount((float) CalculatorHelper.calculateTotalInterest(
                        calculatedTotalPayment4, loanAmount), currencySymbol);
                totalPaymentWrapper.setVisibility(View.VISIBLE);
                textViewTotalPayment.setAmount((float) calculatedTotalPayment4, currencySymbol);
                break;
        }

        textViewTotalPayment.setVisibility(View.VISIBLE);
        textViewTotalPaymentPlaceholder.setVisibility(View.GONE);
        setTotalPaymentFont("font/helvetica_neue_bold.ttf");
    }

    private void resetEditText(EditText editTextPeriod) {
        editTextPeriod.setText("");
        editTextPeriod.setError(null);
    }

    private void resetResult() {
        textViewMonthlyEmi.setAmount(0);
        textViewPeriod.setText(getString(R.string.zero_months));
        textViewInterest.setText(getString(R.string.zero_procents));
        textViewLoanAmount.setAmount(0);
        textViewTotalInterest.setAmount(0);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onToggleButtonClickEvent(OnToggleButtonClickEvent event) {
        calculationType = event.getCalculationType();
        hideResultFields();

        if (calculationType == CalculationType.LOAN_AMOUNT) {
            loanAmountInputWrapper.setVisibility(View.GONE);
            monthlyEmiInputWrapper.setVisibility(View.VISIBLE);
            interestRateInputWrapper.setVisibility(View.VISIBLE);
            periodInputWrapper.setVisibility(View.VISIBLE);

            loanAmountWrapper.setVisibility(View.VISIBLE);
            showCommonResultFields();

        } else if (calculationType == CalculationType.EMI) {
            loanAmountInputWrapper.setVisibility(View.VISIBLE);
            monthlyEmiInputWrapper.setVisibility(View.GONE);
            interestRateInputWrapper.setVisibility(View.VISIBLE);
            periodInputWrapper.setVisibility(View.VISIBLE);

            monthlyEmiWrapper.setVisibility(View.VISIBLE);
            showCommonResultFields();
        } else if (calculationType == CalculationType.PERIOD) {
            loanAmountInputWrapper.setVisibility(View.VISIBLE);
            monthlyEmiInputWrapper.setVisibility(View.VISIBLE);
            interestRateInputWrapper.setVisibility(View.VISIBLE);
            periodInputWrapper.setVisibility(View.GONE);

            periodWrapper.setVisibility(View.VISIBLE);
            showCommonResultFields();
        } else if (calculationType == CalculationType.INTEREST) {
            loanAmountInputWrapper.setVisibility(View.VISIBLE);
            monthlyEmiInputWrapper.setVisibility(View.VISIBLE);
            interestRateInputWrapper.setVisibility(View.GONE);
            periodInputWrapper.setVisibility(View.VISIBLE);

            interestWrapper.setVisibility(View.VISIBLE);
            showCommonResultFields();
        }
    }

    private void setTotalPaymentFont(String fontPath) {
        totalPaymentTitle.setTypeface(Typeface.createFromAsset(getContext().getAssets(), fontPath));
    }

    private boolean isAmountEmpty(CurrencyEditText editText) {
        boolean isEmpty = editText.getRawValue() == 0;
        if (isEmpty) {
            editText.setError(getString(R.string.filed_is_empty));
        }
        return isEmpty;
    }

    private boolean isEditTextEmpty(EditText editText) {
        boolean isEmpty = editText.getText() != null && editText.getText().toString().isEmpty();
        if (isEmpty) {
            editText.setError(getString(R.string.filed_is_empty));
        }
        return isEmpty;
    }

    private void hideResultFields() {
        monthlyEmiWrapper.setVisibility(View.GONE);
        loanAmountWrapper.setVisibility(View.GONE);
        totalInterestWrapper.setVisibility(View.GONE);
        totalPaymentWrapper.setVisibility(View.GONE);
        periodWrapper.setVisibility(View.GONE);
        interestWrapper.setVisibility(View.GONE);
    }

    private void showCommonResultFields() {
        totalInterestWrapper.setVisibility(View.VISIBLE);
        totalPaymentWrapper.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_emi_calculator;
    }
}
