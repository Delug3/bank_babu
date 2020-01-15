package com.bankbabu.balance.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.adapters.account.ShareAccountAdapter;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.database.DatabaseHelper;
import com.bankbabu.balance.events.OnAccountItemClickEvent;
import com.bankbabu.balance.models.Account;
import com.bankbabu.balance.utils.ScreenshotUtils;
import com.bankbabu.balance.utils.ShareUtils;
import java.io.File;
import java.util.List;
import java.util.Random;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareActivity extends BaseActivity {

    @BindView(R.id.check_box_add_bank_details) CheckBox checkBoxAddBankDetails;
    @BindView(R.id.button_go_payment) Button buttonGoPayment ;
    @BindView(R.id.button_share) Button buttonShare;
    @BindView(R.id.card_view) CardView cardView;
    @BindView(R.id.check_box_pay) CheckBox checkBoxPay;
    @BindView(R.id.wrapper_add_bank_details) LinearLayout wrapperAddBankDetails;
    @BindView(R.id.layout_bank_details) LinearLayout wrapperBankDetails;
    @BindView(R.id.text_view_account_name) TextView textViewAccountName;
    @BindView(R.id.text_view_add_your_account) TextView textViewPayName;
    @BindView(R.id.text_view_date) TextView textViewDate;
    @BindView(R.id.text_view_category) TextView textViewCategory;
    @BindView(R.id.text_view_amount) TextView textViewAmount;
    @BindView(R.id.text_view_payment) TextView textViewPayment;
    @BindView(R.id.text_view_account_number) TextView textViewAccountNumber;
    @BindView(R.id.text_view_bank_name) TextView textViewBankName;
    @BindView(R.id.text_view_account_ifsc) TextView textViewIFSC;
    @BindView(R.id.text_view_account_type) TextView textViewAccountType;
    @BindView(R.id.text_view_account_contact) TextView textViewContact;
    @BindView(R.id.image_view_icon)ImageView imageViewIcon;

    private DatabaseHelper databaseHelper;
    private String paymentDetail = "";
    private String share = "";
    private AlertDialog alertDialog;

  @Override
  protected void initUi() {
      ButterKnife.bind(this);
  }

  @Override
  protected boolean useEventBus() {
    return true;
  }

  @Override
  protected int getContentView() {
    return R.layout.activity_share;
  }

  @Override
  protected void setUi(final Bundle savedInstanceState) {
    logEvent("reminder-shareScreen-activity");

    updateUI();

    databaseHelper = new DatabaseHelper(getApplicationContext());

    scaleAnimation(cardView);

    checkBoxPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
          paymentDetail = getString(R.string.paymentMethod);
        } else {
          paymentDetail = "";
        }
      }
    });

    buttonGoPayment.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final Intent intent = new Intent(ShareActivity.this, PaymentsActivity.class);
        loadDefaultGoogleInterstitialAd(new AdLoadedListener() {
          @Override
          public void onAdClosed() {
            super.onAdClosed();
            startActivity(intent);
          }

          @Override
          public void onAdFailedToLoad() {
            startActivity(intent);
          }
        }, R.string.interstitial_full_screen);
      }
    });

    checkBoxAddBankDetails.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
          showAccountDialog();
        } else {
          wrapperBankDetails.setVisibility(View.GONE);
        }
      }
    });

    final Animation myAnim = AnimationUtils.loadAnimation(ShareActivity.this, R.anim.bounce);
    buttonShare.startAnimation(myAnim);

    buttonShare.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        checkBoxPay.setVisibility(View.GONE);
        final File file = ScreenshotUtils.takeScreenshot(ShareActivity.this);
        if (file != null) {
          ShareUtils.shareScreen(file, ShareActivity.this, buildAccountShare());
        }

        if (checkBoxPay.isChecked()) {
          checkBoxPay.setVisibility(View.VISIBLE);
        }

      }
    });
  }

  @Override
  protected boolean useGoogleInterstitialAd() {
    return true;
  }

  private void updateUI() {
    final Intent intent = getIntent();
    if (intent.getExtras() == null) {
      return;
    }

    final String payeeItem = intent.getExtras().getString(Constants.EXTRA_BILL_PAYEE);
    final String amount = intent.getExtras().getString(Constants.EXTRA_BILL_AMOUNT);
    final String categoryName = intent.getExtras().getString(Constants.EXTRA_BILL_CATEGORY_NAME);
    final String dueDate = intent.getExtras().getString(Constants.EXTRA_BILL_DUE_DATE);
    final int categoryIcon = intent.getExtras().getInt(Constants.EXTRA_BILL_CATEGORY_ICON);
    final String billType = intent.getExtras().getString(Constants.EXTRA_BILL_TYPE);

    textViewPayment.setText(billType);

    if (billType != null) {
      if (billType.equals(getString(R.string.payable))) {
        share = getString(R.string.sharePayable);
        checkBoxPay.setVisibility(View.VISIBLE);
        wrapperAddBankDetails.setVisibility(View.GONE);
      } else {
        share = getString(R.string.shareReceive);
        checkBoxPay.setVisibility(View.GONE);
        wrapperAddBankDetails.setVisibility(View.VISIBLE);
      }
    }

    textViewPayName.setText(payeeItem);
    textViewAmount.setText(String.format("%s %s", amount, getString(R.string.usd)));
    textViewCategory.setText(categoryName);
    textViewDate.setText(getString(R.string.format_date, dueDate));
    imageViewIcon.setImageResource(categoryIcon);
  }

  private void scaleAnimation(View viewToAnimate) {
    final ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    anim.setDuration(new Random().nextInt(1000));//to make duration random number between [0,501)
    viewToAnimate.startAnimation(anim);
  }

  @SuppressLint("InflateParams")
  private void showAccountDialog() {
    View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_account, null);

    final int min = 1;
    final int max = 2;

    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShareActivity.this)
        .setView(dialogView);

    final ImageView imageViewDetailAdd = dialogView.findViewById(R.id.image_view_no_payment);

    final RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view_accounts);
    recyclerView.setLayoutManager(new LinearLayoutManager(ShareActivity.this));

    final List<Account> contactArrayList = databaseHelper.getAllAccounts();

    if (contactArrayList.size() > 0) {
      dialogBuilder.setCancelable(false);

      imageViewDetailAdd.setVisibility(View.GONE);
      ShareAccountAdapter adapter = new ShareAccountAdapter(contactArrayList);
      recyclerView.setAdapter(adapter);
    } else {
      dialogBuilder.setCancelable(true);
      checkBoxAddBankDetails.setChecked(false);
      imageViewDetailAdd.setVisibility(View.VISIBLE);
    }

    alertDialog = dialogBuilder.create();
    if (alertDialog.getWindow() != null) {
      int random = new Random().nextInt((max - min) + 1) + min;
      if (random == 1) {
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialog;
      } else {
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
      }
    }
    alertDialog.show();

    imageViewDetailAdd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        alertDialog.dismiss();
        checkBoxAddBankDetails.setChecked(false);
        startActivity(new Intent(ShareActivity.this, AddEditPaymentActivity.class));
      }
    });
  }

  private String buildAccountShare() {
    if (checkBoxAddBankDetails.isChecked()) {
      return paymentDetail + "\n" + "*" + textViewAccountName.getText().toString() + "\n" +
          "*" + textViewAccountNumber.getText().toString() + "\n" +
          "*" + textViewBankName.getText().toString() + "\n" +
          "*" + textViewIFSC.getText().toString() + "\n" +
          "*" + textViewAccountType.getText().toString() + "\n" +
          "*" + textViewContact.getText().toString() + "\n\n" + share;
    } else {
      return "";
    }
  }

  @Subscribe
  @SuppressWarnings("unused")
  public void onShareAccountClickEvent(OnAccountItemClickEvent event) {
    alertDialog.dismiss();

    wrapperBankDetails.setVisibility(View.VISIBLE);

    final String name = getString(R.string.format_account_name, event.getItem().getName());
    textViewAccountName.setText(name);
    final String number = getString(R.string.format_account_number, event.getItem().getNumber());
    textViewAccountNumber.setText(number);
    final String bankName = getString(R.string.format_bank_name, event.getItem().getBankName());
    textViewBankName.setText(bankName);
    final String ifsc = getString(R.string.format_ifsc, event.getItem().getCode());
    textViewIFSC.setText(ifsc);
    final String accountType = getString(R.string.format_account_type, event.getItem().getType());
    textViewAccountType.setText(accountType);
    final String contactNumber = getString(R.string.format_mobile,
        event.getItem().getContactNumber());
    textViewContact.setText(contactNumber);
  }

}
