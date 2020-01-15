package com.bankbabu.balance.adapters.holiday;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bankbabu.balance.R;
import com.bankbabu.balance.models.Holiday;
import java.util.List;


public class HolidayRecyclerViewAdapter extends RecyclerView.Adapter<HolidayViewHolder> {

  private List<Holiday> holidays;

  public HolidayRecyclerViewAdapter(List<Holiday> holidays) {
    this.holidays = holidays;
  }

  @NonNull
  @Override
  public HolidayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_holiday, parent, false);
    return new HolidayViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final HolidayViewHolder holder, final int position) {
    holder.bind(holidays.get(holder.getAdapterPosition()));
  }

  @Override
  public int getItemCount() {
    return holidays.size();
  }
}
