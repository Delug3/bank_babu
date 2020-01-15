package com.bankbabu.balance.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.database.DatabaseHelper;
import com.bankbabu.balance.models.Account;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditPaymentActivity extends BaseActivity {

    int id;
    @BindView(R.id.button_submit)Button buttonSubmit;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.input_layout_number)TextInputLayout textInputLayoutNumber;
    @BindView(R.id.text_input_layout_isfc) TextInputLayout textInputLayout_isfc;
    @BindView(R.id.text_input_layout_confirm_number) TextInputLayout textInputLayoutConfirmNumber;
    @BindView(R.id.edit_text_account_name)EditText editTextName;
    @BindView(R.id.edit_text_bank_name) EditText editTextBankName;
    @BindView(R.id.edit_text_account_number) EditText editTextAccountNumber;
    @BindView(R.id.edit_text_isfc_code) EditText editTextIsfcCode;
    @BindView(R.id.edit_text_confirm_number) EditText editTextConfirmNumber;
    @BindView(R.id.edit_text_note) EditText editTextNote;
    @BindView(R.id.edit_text_contact_no) EditText editTextContactNo;
    @BindView(R.id.radio_button_credit)AppCompatRadioButton radioButtonCredit;
    @BindView(R.id.radio_button_current)AppCompatRadioButton radioButtonCurrent;
    @BindView(R.id.radio_button_loan)AppCompatRadioButton radioButtonLoan;
    @BindView(R.id.radio_button_saving)AppCompatRadioButton radioButtonSaving;

    private String accountType = "Current";
    private Account.OwnerType ownerType = Account.OwnerType.UNKNOWN;
    private boolean ifsc = false;
    private boolean account = false;

    private DatabaseHelper databaseHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_edit_payment;
    }

    @Override
    protected void initUi() {
      ButterKnife.bind(this);
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("add-payment-method-activity");
        configureToolbar(getString(R.string.add_contact_title), R.drawable.ic_toolbar_arrow);
        databaseHelper = new DatabaseHelper(getApplicationContext());

        editTextIsfcCode.addTextChangedListener(new MyTextWatcher(editTextIsfcCode));
        editTextConfirmNumber.addTextChangedListener(new MyTextWatcher(editTextConfirmNumber));

        final Intent intent = getIntent();

        if (intent.getExtras() != null && intent.hasExtra(Constants.EXTRA_OWNER_TYPE)) {
            ownerType = Account.OwnerType.fromDbValue(intent.getStringExtra(Constants.EXTRA_OWNER_TYPE));
        }

        if (ownerType == Account.OwnerType.ACCOUNT) {
            int color = getResources().getColor(R.color.keppel);
            toolbar.setBackgroundColor(color);
            buttonSubmit.setBackgroundResource(R.drawable.toggle_button_sip_background_green);
            setRadioButtonsColor(color);
        }

        if (ownerType == Account.OwnerType.CONTACT) {
            int color = getResources().getColor(R.color.colorPrimary);
            toolbar.setBackgroundColor(color);
            buttonSubmit.setBackgroundResource(R.drawable.toggle_button_sip_background);
            setRadioButtonsColor(color);
        }

        if (intent.getExtras() != null && intent.getExtras()
                .containsKey(Constants.EXTRA_BILL_NOTIFICATION)) {
            buttonSubmit.setText(getString(R.string.update));
            buttonSubmit.setTag(getString(R.string.tag_update));

            id = intent.getExtras().getInt(Constants.EXTRA_ACCOUNT_ID);

            editTextName.setText(intent.getExtras().getString(Constants.EXTRA_ACCOUNT_NAME));
            editTextAccountNumber.setText(intent.getExtras().getString(Constants.EXTRA_ACCOUNT_NUMBER));
            editTextConfirmNumber.setText(intent.getExtras().getString(Constants.EXTRA_ACCOUNT_NUMBER));
            editTextIsfcCode.setText(intent.getExtras().getString(Constants.EXTRA_ACCOUNT_IFSC));
            editTextBankName.setText(intent.getExtras().getString(Constants.EXTRA_BANK_NAME));
            editTextNote.setText(intent.getExtras().getString(Constants.EXTRA_BILL_NOTE));
            editTextContactNo.setText(intent.getExtras().getString(Constants.EXTRA_CONTACT));

            final String type = intent.getStringExtra(Constants.EXTRA_ACCOUNT_TYPE);

            if (type != null) {
                switch (type) {
                    case "Current":
                        radioButtonCredit.setChecked(false);
                        radioButtonCurrent.setChecked(true);
                        radioButtonLoan.setChecked(false);
                        radioButtonSaving.setChecked(false);
                        break;

                    case "Credit Card":
                        radioButtonCredit.setChecked(true);
                        radioButtonCurrent.setChecked(false);
                        radioButtonLoan.setChecked(false);
                        radioButtonSaving.setChecked(false);
                        break;

                    case "Saving":
                        radioButtonCredit.setChecked(false);
                        radioButtonCurrent.setChecked(false);
                        radioButtonLoan.setChecked(false);
                        radioButtonSaving.setChecked(true);
                        break;

                    case "Loan account":
                        radioButtonCredit.setChecked(false);
                        radioButtonCurrent.setChecked(false);
                        radioButtonLoan.setChecked(true);
                        radioButtonSaving.setChecked(false);
                        break;
                }
            }

        } else {
            buttonSubmit.setText(R.string.add);
            buttonSubmit.setTag(R.string.tag_save);
        }

        radioButtonCurrent.setOnClickListener(view -> {
            accountType = "Current";
            radioButtonCredit.setChecked(false);
            radioButtonCurrent.setChecked(true);
            radioButtonLoan.setChecked(false);
            radioButtonSaving.setChecked(false);
        });

        radioButtonCredit.setOnClickListener(view -> {
            accountType = "Credit Card";
            radioButtonCredit.setChecked(true);
            radioButtonCurrent.setChecked(false);
            radioButtonLoan.setChecked(false);
            radioButtonSaving.setChecked(false);
        });

        radioButtonSaving.setOnClickListener(view -> {
            accountType = "Saving";
            radioButtonCredit.setChecked(false);
            radioButtonCurrent.setChecked(false);
            radioButtonLoan.setChecked(false);
            radioButtonSaving.setChecked(true);
        });

        radioButtonLoan.setOnClickListener(view -> {
            accountType = "Loan account";
            radioButtonCredit.setChecked(false);
            radioButtonCurrent.setChecked(false);
            radioButtonLoan.setChecked(true);
            radioButtonSaving.setChecked(false);
        });

        buttonSubmit.setOnClickListener(view -> {
            if (account && ifsc && editTextName.length() > 0 && editTextAccountNumber.length() > 0
                    && editTextBankName.length() > 0) {

                final String name = editTextName.getText().toString();
                final String number = editTextAccountNumber.getText().toString();
                final String code = editTextIsfcCode.getText().toString();
                final String bankName = editTextBankName.getText().toString();
                final String contactNo = editTextContactNo.getText().toString();
                final String note = editTextNote.getText().toString();

                final Account account = new Account(name, number, code, bankName, accountType, contactNo,
                        note, ownerType);

                if (buttonSubmit.getTag().equals(R.string.tag_save)) {
                    Intent intent1 = new Intent(this, ConfirmPaymentActivity.class);
                    intent1.putExtra(Constants.EXTRA_ACCOUNT, account);
                    intent1.putExtra(Constants.EXTRA_ACCOUNT_SELECT, getIntent().getBooleanExtra(Constants.EXTRA_ACCOUNT_SELECT, false));
                    intent1.putExtra(Constants.EXTRA_BILL_ID, getIntent().getIntExtra(Constants.EXTRA_BILL_ID, 0));
                    intent1.putExtra(Constants.EXTRA_BILL_AMOUNT, getIntent().getStringExtra(Constants.EXTRA_BILL_AMOUNT));
                    intent1.putExtra(Constants.EXTRA_BILL_PAYEE,getIntent().getStringExtra(Constants.EXTRA_BILL_PAYEE));
                    intent1.putExtra(Constants.EXTRA_BILL_NOTE, getIntent().getStringExtra(Constants.EXTRA_BILL_NOTE));
                    startActivity(intent1);
                } else {
                    updateAccount(id, account);
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.enter_data, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void updateAccount(int accountId, Account contact) {
        long id = databaseHelper.updateAccount(accountId, contact);
        if (id > 0) {
            finish();
        } else {
            Toast.makeText(this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("RestrictedApi")
    private void setRadioButtonsColor(int color) {
        radioButtonCredit.setSupportButtonTintList(ColorStateList.valueOf(color));
        radioButtonCurrent.setSupportButtonTintList(ColorStateList.valueOf(color));
        radioButtonLoan.setSupportButtonTintList(ColorStateList.valueOf(color));
        radioButtonSaving.setSupportButtonTintList(ColorStateList.valueOf(color));
    }

    private void validateAccount() {
        if (editTextAccountNumber.getText().toString()
                .equals(editTextConfirmNumber.getText().toString())) {
            account = true;
            textInputLayoutConfirmNumber.setErrorEnabled(false);
        } else {
            textInputLayoutConfirmNumber.setError(getString(R.string.err_msg_Account));
            requestFocus(editTextConfirmNumber);
            account = false;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void validateIFSC() {
        int ifsc = editTextIsfcCode.length();
        if (ifsc == 0 || ifsc < 11 || ifsc > 11) {
            textInputLayout_isfc.setError(getString(R.string.err_msg_ifsc));
            requestFocus(editTextIsfcCode);
            this.ifsc = false;
        } else {
            this.ifsc = true;
            textInputLayout_isfc.setErrorEnabled(false);
        }
    }

    private void validateMobile() {
        int mob = editTextContactNo.length();
        if (mob == 0 || mob < 10 || mob > 10) {
            textInputLayoutNumber.setError(getString(R.string.err_msg_number));
            requestFocus(editTextContactNo);
        } else {
            textInputLayoutNumber.setErrorEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        new Builder(this)
                .setMessage(R.string.quit_confirmation_payment_method)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> finish())
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_text_contact_no:
                    validateMobile();
                    break;
                case R.id.edit_text_isfc_code:
                    validateIFSC();
                    break;
                case R.id.edit_text_confirm_number:
                    validateAccount();
                    break;
            }
        }
    }

}
