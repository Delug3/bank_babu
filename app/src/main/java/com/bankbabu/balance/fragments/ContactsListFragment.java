package com.bankbabu.balance.fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.adapters.account.AccountAdapter;
import com.bankbabu.balance.adapters.account.ContactAdapter;
import com.bankbabu.balance.database.DatabaseHelper;
import com.bankbabu.balance.models.Account;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ContactsListFragment extends BaseFragment {

    private static final String ARG_OWNER_TYPE = "ownerType";
    private RecyclerView recyclerView;
    private Account.OwnerType ownerType;
    private TextView placeholder;

    public static ContactsListFragment newInstance(Account.OwnerType ownerType) {
        Bundle args = new Bundle();
        args.putString(ARG_OWNER_TYPE, ownerType.getDbValue());

        ContactsListFragment fragment = new ContactsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            ownerType = Account.OwnerType.fromDbValue(arguments.getString(ARG_OWNER_TYPE));
        }
    }

    @Override
    protected void initUi() {
        recyclerView = findViewById(R.id.recycler_view);
        placeholder = findViewById(R.id.placeholder);
    }

    @Override
    protected void setUi(@Nullable Bundle savedInstanceState) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

        final List<Account> accounts = databaseHelper.getAllAccounts();
        final List<Account> sanitizedAccounts = new ArrayList<>();

        for (Account account : accounts) {
            if (account.getOwnerType() == ownerType) {
                sanitizedAccounts.add(account);
            }
        }

        placeholder.setTextColor(getResources().getColor(
                ownerType == Account.OwnerType.ACCOUNT
                        ? R.color.keppel
                        : R.color.colorPrimary));

        if (sanitizedAccounts.isEmpty()) {
            placeholder.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerView.Adapter adapter = ownerType == Account.OwnerType.CONTACT
                ? new ContactAdapter(sanitizedAccounts)
                : new AccountAdapter(sanitizedAccounts);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_contacts_list;
    }
}
