package com.bankbabu.balance.activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bankbabu.balance.R;
import com.bankbabu.balance.activities.core.BaseActivity;
import com.bankbabu.balance.adapters.category.CategoryRecyclerViewAdapter;
import com.bankbabu.balance.adapters.category.NewCategoryRecyclerViewAdapter;
import com.bankbabu.balance.application.Constants;
import com.bankbabu.balance.database.DatabaseHelper;
import com.bankbabu.balance.events.OnDeleteCategoryEvent;
import com.bankbabu.balance.events.OnNewCategoryItemClickEvent;
import com.bankbabu.balance.models.Bill;
import com.bankbabu.balance.models.Bill.RepeatBy;
import com.bankbabu.balance.models.Category;
import com.bankbabu.balance.receivers.AlarmReceiver;
import com.bankbabu.balance.utils.DataContainer;

import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;


public class AddEditFormActivity extends BaseActivity {

    private int flagRepeat = 0;
    private String remindNotification;
    private RepeatBy repeatBy;
    private String billType, categoryName;
    private int categoryIcon;
    private int newCategoryImages = 0;
    //binding views with butterknife
    @Nullable
    @BindView(R.id.image_view_new_category)ImageView imageViewCategoryIcon;
    @Nullable
    @BindView(R.id.text_view_search) EditText editTextSearchCategory;
    @Nullable
    @BindView(R.id.text_view_new_category) TextView textViewNewCategory;
    @Nullable
    @BindView(R.id.edit_text_new_category) EditText editTextNewCategory;
    @BindView(R.id.spinner_repeat) Spinner spinnerRepeat;
    @BindView(R.id.spinner_notification) Spinner spinnerNotification;
    @BindView(R.id.radio_button_payable) AppCompatRadioButton buttonPayable;
    @BindView(R.id.radio_button_receivable) AppCompatRadioButton buttonReceivable;
    @BindView(R.id.radio_button_not_repeat) AppCompatRadioButton buttonNoRepeat;
    @BindView(R.id.radio_button_repeat) AppCompatRadioButton buttonRepeat;
    @BindView(R.id.radio_group_repeat) RadioGroup radioGroupRepeat;
    @BindView(R.id.radio_group_bill_type) RadioGroup radioGroupBillType;
    @BindView(R.id.edit_text_payee_item) EditText editTextPayeeItem;
    @BindView(R.id.edit_text_notes) EditText editTextNotes;
    @BindView(R.id.edit_text_repeat_day) EditText editTextRepeatDay;
    @BindView(R.id.text_view_amount) EditText editTextAmount;
    @BindView(R.id.edit_text_category) EditText editTextCategory;
    @BindView(R.id.edit_text_due_date) EditText editTextDueDate;
    @BindView(R.id.edit_text_next_due_date) EditText editTextNextDueDate;
    @BindView(R.id.image_view_select_category) ImageView  imageViewSelectedCategory;
    @BindView(R.id.check_box_custom_due_date) CheckBox checkBoxCustomDueDate;
    @BindView(R.id.wrapper_date) LinearLayout wrapperDate;
    @BindView(R.id.wrapper_notification) LinearLayout wrapperNotification;
    @BindView(R.id.wrapper_repeat) LinearLayout wrapperRepeat;
    @BindView(R.id.button_cancel) Button buttonCancel;
    @BindView(R.id.buttonSave) Button buttonSave;
    @Nullable
    @BindView(R.id.recycler_view_categories) RecyclerView recyclerViewCategories;


    private DatabaseHelper databaseHelper;
    private int random;
    private int customDueDate = 0;
    private CategoryRecyclerViewAdapter categoryAdapter;
    private List<Category> categories, searchResult;
    private DateFormat dateFormatter;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected boolean useGoogleInterstitialAd() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_edit_form;
    }

    @Override
    protected void initUi() {
        ButterKnife.bind(this);
        }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("payment-states");
        configureToolbar(getString(R.string.add_bill_or_payment), R.drawable.ic_toolbar_arrow);
        loadAdViewGoogle(findViewById(R.id.ad_view));

        repeatBy = RepeatBy.DAY;
        billType = getString(R.string.payable);

        editTextCategory.setFocusable(false);
        editTextCategory.setClickable(true);

        editTextDueDate.setFocusable(false);
        editTextDueDate.setClickable(true);

        editTextNextDueDate.setFocusable(false);
        editTextNextDueDate.setClickable(true);

        categories = new ArrayList<>();
        searchResult = new ArrayList<>();

        databaseHelper = new DatabaseHelper(getApplicationContext());

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{

                        Color.DKGRAY
                        ,getResources().getColor(R.color.colorPrimary),
                }
        );

        buttonPayable.setSupportButtonTintList(colorStateList);
        buttonReceivable.setSupportButtonTintList(colorStateList);
        buttonNoRepeat.setSupportButtonTintList(colorStateList);
        buttonRepeat.setSupportButtonTintList(colorStateList);

        checkBoxCustomDueDate.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                customDueDate = 1;
                editTextNextDueDate.setVisibility(View.VISIBLE);

                editTextRepeatDay.setVisibility(View.GONE);
                spinnerRepeat.setVisibility(View.GONE);
            } else {
                customDueDate = 0;

                editTextNextDueDate.setVisibility(View.GONE);

                editTextRepeatDay.setVisibility(View.VISIBLE);
                spinnerRepeat.setVisibility(View.VISIBLE);
            }
        });

        editTextCategory.setOnClickListener(view -> showDialogCategoryList());

        buttonCancel.setOnClickListener(view -> finish());

        editTextDueDate.setOnClickListener(view -> showDatePickerDialog(editTextDueDate));

        editTextNextDueDate.setOnClickListener(view -> showDatePickerDialog(editTextNextDueDate));

        final Animation myAnim = AnimationUtils.loadAnimation(AddEditFormActivity.this,
                R.anim.bounce);
        buttonSave.startAnimation(myAnim);

        editTextPayeeItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextPayeeItem.getText().toString().length() <= 0) {
                    editTextPayeeItem.setError(getString(R.string.enter_payee));
                } else {
                    editTextPayeeItem.setError(null);
                }
            }
        });

        editTextDueDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextDueDate.getText().toString().length() <= 0) {
                    editTextDueDate.setError(getString(R.string.enter_due_date));
                } else {
                    editTextDueDate.setError(null);
                }
            }
        });

        buttonSave.setOnClickListener(view -> saveBill());

        radioGroupRepeat.setOnCheckedChangeListener((radioGroup, index) -> {
            switch (index) {
                case R.id.radio_button_not_repeat:
                    flagRepeat = 0;
                    wrapperRepeat.setVisibility(View.GONE);
                    break;
                case R.id.radio_button_repeat:
                    flagRepeat = 1;
                    wrapperRepeat.setVisibility(View.VISIBLE);
                    break;
            }
        });

        radioGroupBillType.setOnCheckedChangeListener((radioGroup, index) -> {
            switch (index) {
                case R.id.radio_button_payable:
                    billType = getString(R.string.payable);
                    break;
                case R.id.radio_button_receivable:
                    billType = getString(R.string.receivable);
                    break;
            }
        });

        configureSpinnerRepeats();
        configureSpinnerNotification();

        updateData();
    }

    @SuppressLint("InflateParams")
    private void showDialogCategoryList() {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_category_list, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AddEditFormActivity.this);

        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);

        //recyclerViewCategories = dialogView.findViewById(R.id.recycler_view_categories);
        //final TextView textViewNewCategory = dialogView.findViewById(R.id.text_view_new_category);
        //editTextSearchCategory = dialogView.findViewById(R.id.text_view_search);

        ButterKnife.bind(this, dialogView);

        editTextSearchCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int post, int i1, int i2) {
                String query = editTextSearchCategory.getText().toString();

                if (query.length() > 0) {
                    searchResult.clear();
                    for (int i = 0; i < categories.size(); i++) {

                        final String find = categories.get(i).getName();

                        if (query.length() <= find.length()) {
                            if (find.toLowerCase().contains(query.toLowerCase())) {
                                final Category model = new Category();

                                model.setName(categories.get(i).getName());
                                model.setIcon(categories.get(i).getIcon());
                                model.setId(categories.get(i).getId());

                                searchResult.add(model);
                            }
                        }
                    }

                    if (searchResult.size() > 0) {
                        recyclerViewCategories
                                .setLayoutManager(new GridLayoutManager(AddEditFormActivity.this, 1));
                        categoryAdapter = new CategoryRecyclerViewAdapter(searchResult);
                        recyclerViewCategories.setAdapter(categoryAdapter);
                    } else {
                        categoryAdapter.notifyDataSetChanged();
                    }
                } else {
                    updateCategories();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        random = new Random().nextInt(2) + 1;
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().getAttributes().windowAnimations = random == 1 ? R.style.PauseDialog :
                    R.style.DialogTheme;
        }
        alertDialog.show();

        textViewNewCategory.setOnClickListener(view -> showDialogCategory());
    }

    private void showDatePickerDialog(final EditText editTextDate) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog.OnDateSetListener listener = (datePicker, year1, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year1, monthOfYear, dayOfMonth);
            editTextDate.setText(dateFormatter.format(newDate.getTime()));
        };

        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddEditFormActivity.this, listener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

        random = new Random().nextInt(2) + 1;
        if (datePickerDialog.getWindow() != null) {
            datePickerDialog.getWindow().getAttributes().windowAnimations =
                    random == 1 ? R.style.PauseDialog : R.style.DialogTheme;
        }
        datePickerDialog.show();
    }

    private void saveBill() {
        final String amount = editTextAmount.getText().toString();
        final String payeeName = editTextPayeeItem.getText().toString();
        final String notes = editTextNotes.getText().toString();
        final String dueDate = editTextDueDate.getText().toString();
        final String nextDueDate = editTextNextDueDate.getText().toString();
        final String repeatNumber = editTextRepeatDay.getText().toString();

        if (amount.length() > 0 && payeeName.length() > 0 && notes.length() > 0
                && dueDate.length() > 0) {

            final Bill bill = new Bill(amount, categoryName, categoryIcon, payeeName, notes, dueDate,
                    nextDueDate, repeatNumber, billType, flagRepeat, remindNotification,
                    customDueDate);
            bill.setRepeatByType(repeatBy);

            if (buttonSave.getTag().equals(getString(R.string.tag_save))) {
                createBill(bill);
            } else {
                if (buttonSave.getTag().equals(getString(R.string.tag_next))) {
                    final Intent intent = new Intent(AddEditFormActivity.this, ShareActivity.class);
                    intent.putExtra(Constants.EXTRA_BILL_ID, bill.getId());
                    intent.putExtra(Constants.EXTRA_BILL_PAYEE, bill.getPayee());
                    intent.putExtra(Constants.EXTRA_BILL_AMOUNT, bill.getAmount());
                    intent.putExtra(Constants.EXTRA_BILL_CATEGORY_NAME, bill.getCategoryName());
                    intent.putExtra(Constants.EXTRA_BILL_CATEGORY_ICON, bill.getCategoryIcon());
                    intent.putExtra(Constants.EXTRA_BILL_NOTE, bill.getNotes());
                    intent.putExtra(Constants.EXTRA_BILL_DUE_DATE, bill.getDueDate());
                    intent.putExtra(Constants.EXTRA_BILL_CUSTOM_DUE_DATE, bill.getCustomDueDate());
                    intent.putExtra(Constants.EXTRA_BILL_TYPE, bill.getBillType());
                    intent.putExtra(Constants.EXTRA_BILL_REPEAT, bill.getRepeat());
                    intent.putExtra(Constants.EXTRA_BILL_NEXT_DUE_DATE, bill.getNextDueDate());
                    intent.putExtra(Constants.EXTRA_BILL_REPEAT_EVERY, bill.getRepeatEvery());
                    intent.putExtra(Constants.EXTRA_BILL_REPEAT_BY, bill.getRepeatByType().toString());
                    intent.putExtra(Constants.EXTRA_BILL_STATUS, bill.getStatus());
                    intent.putExtra(Constants.EXTRA_BILL_NOTIFICATION, bill.getNotification());
                    startActivity(intent);
                } else {
                    updateBill(getIntent().getIntExtra(Constants.EXTRA_BILL_ID, -1), bill);
                }
            }
        } else {
            if (editTextAmount.getText().length() <= 0) {
                editTextAmount.setError(getString(R.string.enter_amount));
            }
            if (editTextPayeeItem.getText().length() <= 0) {
                editTextPayeeItem.setError(getString(R.string.enter_payee));
            }
            if (dueDate.length() <= 0) {
                editTextDueDate.setError(getString(R.string.enter_due_date));
            }
            if (notes.length() <= 0) {
                editTextNotes.setError(getString(R.string.enter_notes));
            }
            Toast.makeText(AddEditFormActivity.this, R.string.fill_correct_information,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void configureSpinnerRepeats() {
        final List<String> repeats = getRepeats();
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, repeats);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepeat.setAdapter(dataAdapter);
        spinnerRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                repeatBy = RepeatBy.getFromValue(repeats.get(i), AddEditFormActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void configureSpinnerNotification() {
        final List<String> notification = getNotifications();
        final ArrayAdapter<String> dataAdapter_notification = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, notification);
        dataAdapter_notification.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNotification.setAdapter(dataAdapter_notification);

        spinnerNotification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                remindNotification = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateData() {
        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            final String buttonTitle = intent.getStringExtra(Constants.EXTRA_BILL_BUTTON);
            if (buttonTitle != null) {

                buttonSave.setTag(buttonTitle);
                buttonSave.setText(buttonTitle);

                if (buttonSave.getTag().equals(getString(R.string.tag_next))) {
                    wrapperNotification.setVisibility(View.GONE);
                    wrapperDate.setVisibility(View.GONE);
                }
            }

            editTextPayeeItem.setText(intent.getExtras().getString(Constants.EXTRA_BILL_PAYEE));
            editTextAmount.setText(intent.getExtras().getString(Constants.EXTRA_BILL_AMOUNT));
            categoryName = intent.getExtras().getString(Constants.EXTRA_BILL_CATEGORY_NAME);
            editTextCategory.setText(intent.getExtras().getString(Constants.EXTRA_BILL_CATEGORY_NAME));
            editTextNotes.setText(intent.getExtras().getString(Constants.EXTRA_BILL_NOTE));
            editTextDueDate.setText(intent.getExtras().getString(Constants.EXTRA_BILL_DUE_DATE));
            categoryIcon = intent.getExtras().getInt(Constants.EXTRA_BILL_CATEGORY_ICON);
            imageViewSelectedCategory.setImageResource(intent.getExtras().getInt(
                    Constants.EXTRA_BILL_CATEGORY_ICON));

            if (intent.getExtras().getInt(Constants.EXTRA_BILL_CUSTOM_DUE_DATE) == 1) {
                checkBoxCustomDueDate.setChecked(true);
                editTextNextDueDate.setVisibility(View.VISIBLE);

                editTextRepeatDay.setVisibility(View.GONE);
                spinnerRepeat.setVisibility(View.GONE);
            } else {
                checkBoxCustomDueDate.setChecked(false);
                editTextNextDueDate.setVisibility(View.GONE);

                editTextRepeatDay.setVisibility(View.VISIBLE);
                spinnerRepeat.setVisibility(View.VISIBLE);
            }

            billType = intent.getExtras().getString(Constants.EXTRA_BILL_TYPE);

            if (billType != null) {
                if (billType.equals(getString(R.string.payable))) {
                    radioGroupBillType.check(R.id.radio_button_payable);
                } else {
                    radioGroupBillType.check(R.id.radio_button_receivable);
                }
            }

            flagRepeat = intent.getExtras().getInt(Constants.EXTRA_BILL_REPEAT, 0);
            if (intent.getExtras().getInt(Constants.EXTRA_BILL_REPEAT, 0) > 0) {
                radioGroupRepeat.check(R.id.radio_button_repeat);
                wrapperRepeat.setVisibility(View.VISIBLE);
            } else {
                wrapperRepeat.setVisibility(View.GONE);
                radioGroupRepeat.check(R.id.radio_button_not_repeat);
            }

            editTextNextDueDate.setText(intent.getExtras().getString(Constants.EXTRA_BILL_NEXT_DUE_DATE));
            editTextRepeatDay.setText(intent.getExtras().getString(Constants.EXTRA_BILL_REPEAT_EVERY));

            if (getIntent().getExtras().containsKey(Constants.EXTRA_BILL_REPEAT_BY)) {
                repeatBy =
                        RepeatBy.valueOf(intent.getExtras().getString(Constants.EXTRA_BILL_REPEAT_BY));
                spinnerRepeat
                        .setSelection(getRepeats().indexOf(RepeatBy.getStringValue(repeatBy, this)), true);
            }

            remindNotification = intent.getExtras().getString(Constants.EXTRA_BILL_NOTIFICATION);

            if (remindNotification != null) {
                spinnerNotification.setSelection(Integer.parseInt(remindNotification), true);
            }
        }
    }

    private void updateCategories() {
        categories = databaseHelper.getAllCategories();
        if (categoryAdapter == null) {
            recyclerViewCategories.setLayoutManager(new GridLayoutManager(this, 1));
            categoryAdapter = new CategoryRecyclerViewAdapter(categories);
            recyclerViewCategories.setAdapter(categoryAdapter);
        } else {
            categoryAdapter.setCategories(categories);
        }
    }

    @SuppressLint("InflateParams")
    private void showDialogCategory() {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_category, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AddEditFormActivity.this);

        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);

        //newCategoryRecyclerView = dialogView.findViewById(R.id.recycler_view_categories);
        //imageViewCategoryIcon = dialogView.findViewById(R.id.image_view_new_category);
        //final Button btnCatCancel = dialogView.findViewById(R.id.button_cancel);
        //final Button btnCatSave = dialogView.findViewById(R.id.button_save);
        //editTextNewCategory = dialogView.findViewById(R.id.edit_text_new_category);

        ButterKnife.bind(this,dialogView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        random = new Random().nextInt(2) + 1;
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().getAttributes().windowAnimations =
                    random == 1 ? R.style.PauseDialog : R.style.DialogTheme;
        }
        alertDialog.show();

        buttonSave.setOnClickListener(view -> {

            if (newCategoryImages == 0) {
                newCategoryImages = DataContainer.SUB_CATEGORY_ICONS[0];
            }

            if (editTextNewCategory.getText().toString().length() > 0) {
                Category contact = new Category(editTextNewCategory.getText().toString(),
                        newCategoryImages);
                addNewCategory(contact, alertDialog);

            } else {
                Toast.makeText(AddEditFormActivity.this, R.string.enter_category_name,
                        Toast.LENGTH_SHORT).show();
            }


        });
        buttonCancel.setOnClickListener(view -> alertDialog.dismiss());
        setIconGrid();
    }

    private void createBill(Bill bill) {
        long id = databaseHelper.insertBill(bill);
        if (id > 0) {
            final int alarmId = new Random().nextInt(9999 - 1000) + 1000;

            final Intent alarmIntent = new Intent(AddEditFormActivity.this, AlarmReceiver.class);
            alarmIntent.putExtra(Constants.EXTRA_BILL_ID_ALARM, id);
            alarmIntent.putExtra(Constants.EXTRA_ALARM_ID, alarmId);

            final PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEditFormActivity.this,
                    alarmId, alarmIntent, 0);

            triggerAlarmManager(pendingIntent, bill.getDueDate(), bill.getNotification());
            finish();
        }
    }

    private void updateBill(int bid, Bill bill) {
        long id = databaseHelper.updateBill(bid, bill);
        if (id > 0) {
            finish();
        }
    }

    @NonNull
    private List<String> getRepeats() {
        return new ArrayList<>(
                Arrays.asList(getResources().getStringArray(R.array.repeats)));
    }

    @NonNull
    private List<String> getNotifications() {
        return new ArrayList<>(
                Arrays.asList(getResources().getStringArray(R.array.notifications)));
    }

    private void addNewCategory(Category contact, AlertDialog alertDialog1) {
        long id = databaseHelper.insertCategory(contact);
        if (id > 0) {
            alertDialog1.dismiss();
            updateCategories();
        }
    }

    private void setIconGrid() {
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(this, 5));
        final NewCategoryRecyclerViewAdapter newCategoryAdapter = new NewCategoryRecyclerViewAdapter(
                DataContainer.SUB_CATEGORY_ICONS);
        recyclerViewCategories.setAdapter(newCategoryAdapter);
    }

    //Trigger alarm manager with entered time interval
    public void triggerAlarmManager(PendingIntent pendingIntent, String dueDate,
                                    String notification) {

        String dateArr[] = dueDate.split("-");
        String day = dateArr[0];
        String month = dateArr[1];
        String year = dateArr[2];

        final Calendar current = Calendar.getInstance();
        current.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        current.set(Calendar.YEAR, Integer.parseInt(year));
        current.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        current.set(Calendar.HOUR, 7);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);
        long eventTime = current.getTimeInMillis();

        //Converts 24 Hrs(1 Day) to milliseconds
        long oneDay = AlarmManager.INTERVAL_DAY;
        int noOfDays = Integer.parseInt(notification);
        long reminderTime = eventTime - (noOfDays * oneDay);

        final AlarmManager alarmManager = (AlarmManager) getSystemService(
                android.content.Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, reminderTime, AlarmManager.INTERVAL_DAY,
                    pendingIntent);
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onNewCategoryItemClickEvent(OnNewCategoryItemClickEvent event) {
        imageViewCategoryIcon.setImageResource(event.getItem());
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onDeleteCategoryEvent(OnDeleteCategoryEvent event) {
        deleteCategory(event.getCategory().getId());
    }

    public void deleteCategory(int id) {
        int count = databaseHelper.deleteContact(id);
        if (count > 0) {
            updateCategories();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        new Builder(this)
                .setMessage(R.string.exit_confirmation_payment_reminder)
                .setPositiveButton(android.R.string.yes, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
}
