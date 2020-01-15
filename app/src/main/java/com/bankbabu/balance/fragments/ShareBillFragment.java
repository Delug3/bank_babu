package com.bankbabu.balance.fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.PaymentsActivity;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.database.DatabaseHelper;
import com.bankbabu.balance.events.OnWriteExternalPermissionAsked;
import com.bankbabu.balance.models.Account;
import com.bankbabu.balance.models.Bill;
import com.bankbabu.balance.utils.ScreenshotUtils;
import com.bankbabu.balance.utils.ShareUtils;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareBillFragment extends BaseFragment {

    @BindView(R.id.text_view_amount) EditText amount;
    @BindView(R.id.edit_text_payee_item) EditText payee;
    @BindView(R.id.edit_text_notes) EditText note;
    @BindView(R.id.add_account) Button addAccount;
    @BindView(R.id.button_share) Button buttonConfirm;
    @BindView(R.id.ad_view) AdView adView;
    @BindView(R.id.account_holder) LinearLayout accountHolder;
    @BindView(R.id.account_name) TextView accountName;
    @BindView(R.id.bank_name) TextView bankName;
    @BindView(R.id.bank_number) TextView bankNumber;
    @BindView(R.id.ifsc_number) TextView ifscCode;
    @BindView(R.id.account_type) TextView accountType;

    private final int REQUEST_CODE = 1;
    private Bill bill;
    private DatabaseHelper databaseHelper;
    private Account account;


    public ShareBillFragment() {
    }

    public static ShareBillFragment newInstance(Bill bill) {
        ShareBillFragment fragment = new ShareBillFragment();
        Bundle args = new Bundle();
        addBilToBundle(bill, args);
        fragment.setArguments(args);
        return fragment;
    }

    private static void addBilToBundle(Bill bill, Bundle args) {
        args.putInt(Constants.EXTRA_BILL_ID, bill.getId());
        args.putString(Constants.EXTRA_BILL_AMOUNT, bill.getAmount());
        args.putString(Constants.EXTRA_BILL_PAYEE, bill.getPayee());
        args.putString(Constants.EXTRA_BILL_NOTE, bill.getNotes());
    }

    public static ShareBillFragment newInstance(Bill bill, Account account) {
        ShareBillFragment fragment = new ShareBillFragment();
        Bundle args = new Bundle();
        addBilToBundle(bill, args);

        addAccountToBundle(account, args);
        fragment.setArguments(args);
        return fragment;
    }

    private static void addAccountToBundle(Account account, Bundle args) {
        args.putInt(Constants.EXTRA_ACCOUNT_ID, account.getId());
        args.putString(Constants.EXTRA_ACCOUNT_NAME, account.getName());
        args.putString(Constants.EXTRA_ACCOUNT_NUMBER, account.getNumber());
        args.putString(Constants.EXTRA_BANK_NAME, account.getBankName());
        args.putString(Constants.EXTRA_ACCOUNT_TYPE, account.getType());
        args.putString(Constants.EXTRA_ACCOUNT_IFSC, account.getCode());
    }

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }

    @Override
    protected void initUi() {

        ButterKnife.bind(this, getActivity());
        bill = new Bill();
        Bundle extras = getArguments();
        if (extras != null) {
            bill.setId(extras.getInt(Constants.EXTRA_BILL_ID));
            bill.setAmount(extras.getString(Constants.EXTRA_BILL_AMOUNT));
            bill.setPayee(extras.getString(Constants.EXTRA_BILL_PAYEE));
            bill.setNotes(extras.getString(Constants.EXTRA_BILL_NOTE));

            if (extras.containsKey(Constants.EXTRA_ACCOUNT_ID)) {
                account = new Account();
                account.setId(extras.getInt(Constants.EXTRA_ACCOUNT_ID, 0));
                account.setName(extras.getString(Constants.EXTRA_ACCOUNT_NAME));
                account.setNumber(extras.getString(Constants.EXTRA_ACCOUNT_NUMBER));
                account.setBankName(extras.getString(Constants.EXTRA_BANK_NAME));
                account.setType(extras.getString(Constants.EXTRA_ACCOUNT_TYPE));
                account.setCode(extras.getString(Constants.EXTRA_ACCOUNT_IFSC));
            }
        }

        databaseHelper = new DatabaseHelper(getContext());
        List<Bill> billsById = databaseHelper.getBillsById(bill.getId());
        if (!billsById.isEmpty()) {
            bill = billsById.get(0);
        }

    }

    @Override
    protected void setUi(@Nullable Bundle savedInstanceState) {
        loadAdViewGoogle(adView);
        amount.setText(bill.getAmount());
        payee.setText(bill.getPayee());
        note.setText(bill.getNotes());

        buttonConfirm.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                EventBus.getDefault().post(new OnWriteExternalPermissionAsked(buildAccountShare()));
            } else {

                updateBill();

                final File file = ScreenshotUtils.takeScreenshot(getActivity());
                if (file != null && getActivity() != null) {
                    ShareUtils.shareScreen(file, getActivity(), buildAccountShare());
                }
            }
        });
        showAccount();
    }

    private void updateBill() {
        bill.setPayee(payee.getText().toString());
        bill.setNotes(note.getText().toString());
        bill.setAmount(amount.getText().toString());

        databaseHelper.updateBill(bill.getId(), bill);
    }

    private String buildAccountShare() {
        String title;
        String footer;

        boolean isPayable = bill.getBillType().equals(getContext().getString(R.string.payable));

        footer = getString(isPayable ? R.string.sharePayable : R.string.shareReceive);

        if (account == null) {
            title = isPayable
                    ? getString(R.string.payable_with_account)
                    : getString(R.string.not_payeable_with_account);
        } else {
            title = getString(R.string.no_account);
        }

        String accountDetails = "";

        if (account != null) {
            String contactNumber = account.getContactNumber() == null ? "\n" : "*" + account.getContactNumber() + "\n\n";
            accountDetails = "\n*" + account.getName() + "\n" +
                    "*" + account.getNumber() + "\n" +
                    "*" + account.getBankName() + "\n" +
                    "*" + account.getCode() + "\n" +
                    "*" + account.getType() + "\n" +
                    contactNumber + footer
            ;
        }

        return title + accountDetails;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            account = new Account();
            account.setId(data.getIntExtra(Constants.EXTRA_ACCOUNT_ID, 0));
            account.setName(data.getStringExtra(Constants.EXTRA_ACCOUNT_NAME));
            account.setNumber(data.getStringExtra(Constants.EXTRA_ACCOUNT_NUMBER));
            account.setBankName(data.getStringExtra(Constants.EXTRA_BANK_NAME));
            account.setType(data.getStringExtra(Constants.EXTRA_ACCOUNT_TYPE));
            account.setCode(data.getStringExtra(Constants.EXTRA_ACCOUNT_IFSC));

            showAccount();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showAccount() {
        if (account == null) {
            accountHolder.setVisibility(View.GONE);
            adView.setVisibility(View.VISIBLE);
            buttonConfirm.setText(R.string.request_account);
            addAccount.setText(R.string.add_account);
            addAccount.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), PaymentsActivity.class);
                intent.putExtra(Constants.EXTRA_ACCOUNT_SELECT, true);
                intent.putExtra(Constants.EXTRA_BILL_ID, bill.getId());
                intent.putExtra(Constants.EXTRA_BILL_AMOUNT, bill.getAmount());
                intent.putExtra(Constants.EXTRA_BILL_PAYEE, bill.getPayee());
                intent.putExtra(Constants.EXTRA_BILL_NOTE, bill.getNotes());

                startActivityForResult(intent, REQUEST_CODE);
            });

        } else {
            accountHolder.setVisibility(View.VISIBLE);
            adView.setVisibility(View.GONE);
            buttonConfirm.setText(R.string.btn_share);
            addAccount.setText(R.string.edit);
            addAccount.setOnClickListener(v -> {
                account = null;
                showAccount();
            });

            accountName.setText(account.getName());
            bankName.setText(account.getBankName());
            bankNumber.setText(account.getNumber());
            ifscCode.setText(account.getCode());
            accountType.setText(account.getType());
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_share_bill;
    }
}
