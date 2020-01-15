package com.bankbabu.balance.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.bankbabu.balance.R;
import com.bankbabu.balance.events.OnAccountAddClickEvent;
import com.bankbabu.balance.events.OnAccountPageScrolled;
import com.bankbabu.balance.models.Account;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountsFragment extends BaseFragment {

    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.btn_add) FloatingActionButton addButton;

    @Override
    protected void initUi() {
        ButterKnife.bind(this,getActivity());
    }

    @Override
    protected void setUi(final Bundle savedInstanceState) {
        logEvent("home-activity");

        final ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(ContactsListFragment.newInstance(Account.OwnerType.CONTACT), "Contacts");
        adapter.addFragment(ContactsListFragment.newInstance(Account.OwnerType.ACCOUNT), "My Accounts");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        addButton.setOnClickListener(v -> {
            int currentSelectedTab = viewPager.getCurrentItem();
            Account.OwnerType ownerType = currentSelectedTab == 0 ? Account.OwnerType.CONTACT : Account.OwnerType.ACCOUNT;
            EventBus.getDefault().post(new OnAccountAddClickEvent(ownerType));
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                EventBus.getDefault().post(new OnAccountPageScrolled(position));

                int color = getResources().getColor(position == 0 ? R.color.colorPrimary : R.color.keppel);
                tabLayout.setSelectedTabIndicatorColor(color);
                tabLayout.setTabTextColors(getResources().getColor(R.color.fiord), color);
                addButton.setBackgroundTintList(ColorStateList.valueOf(color));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_accounts;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> titles = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }
    }
}
