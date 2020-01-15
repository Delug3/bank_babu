package com.bankbabu.balance.adapters.holiday;

import android.view.View;
import android.widget.TextView;
import com.bankbabu.balance.R;
import com.bankbabu.balance.adapters._core.BaseViewHolder;
import com.bankbabu.balance.models.Holiday;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/29/19.
 */
public class HolidayViewHolder extends BaseViewHolder<Holiday> {

  private TextView textViewTitle;
  private TextView textViewDate;

  HolidayViewHolder(final View view) {
    super(view);
  }

  @Override
  protected void initUI() {
    textViewTitle = findViewById(R.id.text_view_title);
    textViewDate = findViewById(R.id.text_view_date);
  }

  @Override
  public void bind(final Holiday item) {
    super.bind(item);

    textViewTitle.setText(item.getHolidayName());

    try {
      final DateFormat input = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
      final DateFormat output = new SimpleDateFormat("MMM,  dd/MM/yyyy,  EEE", Locale.getDefault());
      final Date convertedDate = input.parse(item.getHolidayDate());

      textViewDate.setText(output.format(convertedDate));
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}
