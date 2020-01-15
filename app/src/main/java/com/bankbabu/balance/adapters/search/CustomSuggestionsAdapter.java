package com.bankbabu.balance.adapters.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.models.Bank;
import com.bankbabu.balance.views.searchbar.adapter.SuggestionsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/15/19.
 */
public class CustomSuggestionsAdapter extends
        SuggestionsAdapter<Bank, CustomSuggestionsAdapter.SuggestionHolder> {

    private SuggestionsAdapter.OnItemViewClickListener listener;
    private List<Bank> allSuggestions;

    public CustomSuggestionsAdapter(LayoutInflater inflater, List<Bank> allSuggestions) {
        super(inflater);
        this.allSuggestions = Collections.unmodifiableList(allSuggestions);
    }

    public void setListener(SuggestionsAdapter.OnItemViewClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SuggestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_last_request, parent, false);
        return new SuggestionHolder(view);
    }

    @Override
    public void onBindSuggestionHolder(Bank suggestion, SuggestionHolder holder, int position) {
        holder.text.setText(suggestion.isShowShortName() ? suggestion.getCode() : suggestion.getName());
        holder.favouriteIcon.setImageResource(
                suggestion.getFav() == 1
                        ? R.drawable.ic_heart_on
                        : R.drawable.ic_heart_off);
    }

    @Override
    public int getSingleViewHeight() {
        return 500;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final ArrayList<Bank> suggestions;
                FilterResults results = new FilterResults();
                String term = constraint.toString();
                if (term.isEmpty()) {
                    suggestions = new ArrayList<>(allSuggestions);
                } else {
                    suggestions = new ArrayList<>();
                    for (Bank item : suggestions_clone) {
                        if (item.getName().toLowerCase().contains(term.toLowerCase()) ||
                                (item.getCode() != null && item.getCode().toLowerCase()
                                        .contains(term.toLowerCase()))) {
                            if ((item.getCode() != null && item.getCode().toLowerCase()
                                    .contains(term.toLowerCase()))) {
                                item.setShowShortName(true);
                            } else {
                                item.setShowShortName(false);
                            }
                            suggestions.add(item);
                        }
                    }
                }
                results.values = suggestions;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                suggestions = (ArrayList<Bank>) results.values;
                notifyDataSetChanged();
                notifyDataSetChanged();
            }
        };
    }

    class SuggestionHolder extends RecyclerView.ViewHolder {

        private final TextView text;
        private final ImageView iv_delete;
        private final ImageView favouriteIcon;

        public SuggestionHolder(final View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            favouriteIcon = itemView.findViewById(R.id.image_view_bookmark);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_delete.setVisibility(View.GONE);
            itemView.setOnClickListener(v -> {
                v.setTag(getSuggestions().get(getAdapterPosition()));
                listener.OnItemClickListener(getAdapterPosition(), v);
            });
            iv_delete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position > 0 && position < getSuggestions().size()) {
                    v.setTag(getSuggestions().get(getAdapterPosition()));
                    listener.OnItemDeleteListener(getAdapterPosition(), v);
                }
            });
        }
    }
}
