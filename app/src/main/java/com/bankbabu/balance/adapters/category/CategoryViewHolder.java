package com.bankbabu.balance.adapters.category;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import com.bankbabu.balance.R;
import com.bankbabu.balance.adapters._core.BaseViewHolder;
import com.bankbabu.balance.events.OnCategoryItemClickEvent;
import com.bankbabu.balance.events.OnDeleteCategoryEvent;
import com.bankbabu.balance.models.Category;
import org.greenrobot.eventbus.EventBus;


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 1/30/19.
 */
public class CategoryViewHolder extends BaseViewHolder<Category> implements OnLongClickListener {

  private TextView textViewCategoryName;

  CategoryViewHolder(final View view) {
    super(view);
  }

  @Override
  protected void initUI() {
    textViewCategoryName = findViewById(R.id.text_view_title);
  }

  @Override
  protected void setListener() {
    itemView.setOnClickListener(this);
    itemView.setOnLongClickListener(this);
  }

  @Override
  public void bind(final Category item) {
    super.bind(item);
    textViewCategoryName.setText(item.getName());
  }

  @Override
  public void onClick(final View view) {
    super.onClick(view);
    EventBus.getDefault().post(new OnCategoryItemClickEvent(getItem()));
  }

  @Override
  public boolean onLongClick(final View v) {
    new AlertDialog.Builder(itemView.getContext())
        .setTitle(R.string.alert)
        .setMessage(R.string.delete_category_confirmation)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            EventBus.getDefault().post(new OnDeleteCategoryEvent(getItem()));
          }
        })
        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        })
        .show();

    return false;
  }
}
