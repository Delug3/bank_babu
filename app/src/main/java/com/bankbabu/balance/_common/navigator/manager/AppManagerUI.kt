package com.bankbabu.balance._common.navigator.manager

import android.annotation.SuppressLint
import android.support.v4.app.FragmentManager
import android.support.v7.widget.AppCompatImageView
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bankbabu.balance.R
import com.bankbabu.balance._common.core.BaseFragment
import com.bankbabu.balance._common.core.BaseNavigationActivity
import com.bankbabu.balance._common.navigator.core.FragmentData
import com.bankbabu.balance._common.navigator.core.FragmentId
import com.bankbabu.balance._common.navigator.core.IFragmentData
import com.bankbabu.balance._common.navigator.core.ToolbarId
import com.bankbabu.balance._common.navigator.manager.core.BaseManagerUI
import com.bankbabu.balance.fragments.*


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/2/19.
 */
class AppManagerUI(var activity: BaseNavigationActivity,
                   initialFragment: FragmentData = FragmentData(FragmentId.BANK_BALANCE_CHECK)) : BaseManagerUI(activity,
        initialFragment = initialFragment) {

    override fun getIdFragmentsContainer(): Int = R.id.fragment_container

    init {
        initUI()
    }

    override fun initUI() {
        changeFragmentTo(initialFragment)
    }

    override fun getFirstInStackFragmentClass(): Class<*> = BankBalanceCheckFragment::class.java

    override fun changeFragmentTo(fragmentData: IFragmentData) {
        val fragmentId = fragmentData.fragmentId

        when (fragmentId) {
            FragmentId.BANK_BALANCE_CHECK -> addFragmentToContainer(BankBalanceCheckFragment(), false, fragmentData.data)
            FragmentId.TOOLS -> addFragmentToContainer(ToolsFragment(), false, fragmentData.data)
            FragmentId.ATM -> addFragmentToContainer(NearByFragment(), true, fragmentData.data)
            FragmentId.REMINDER -> addFragmentToContainer(PaymentReminderFragment(), true, fragmentData.data)
            FragmentId.CALCULATORS -> addFragmentToContainer(CalculatorFragment(), true, fragmentData.data)
            FragmentId.PF_STATUS -> addFragmentToContainer(PfStatusFragment(), true, fragmentData.data)
            FragmentId.SEARCH -> addFragmentToContainer(SearchFragment(), true, fragmentData.data)
            FragmentId.PAYMENT_REMINDER -> addFragmentToContainer(PaymentReminderFragment(), true, fragmentData.data)
        }
    }

    override fun initToolbar(baseFragment: BaseFragment, toolbarId: ToolbarId, title: Int) {
        updateToolbar(baseFragment, initToolBarUI(baseFragment, toolbarId, activity.getString(title)))
    }

    override fun initToolbar(baseFragment: BaseFragment, toolbarId: ToolbarId, title: String) {
        updateToolbar(baseFragment, initToolBarUI(baseFragment, toolbarId, title))
    }

    override fun popBackStack() {
        activity.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    @SuppressLint("InflateParams")
    private fun initToolBarUI(fragment: BaseFragment, toolbarId: ToolbarId, title: String): View {
        return when (toolbarId) {
            ToolbarId.TOOLBAR_RATE -> {
                val view = activity.layoutInflater.inflate(R.layout.toolbar_tool_activity, null)
                view.findViewById<TextView>(R.id.toolbarTitle).text = title
                view.findViewById<ImageView>(R.id.image_view_menu).setOnClickListener {
                    activity.onToolbarItemClicked(it)
                }

                view.findViewById<AppCompatImageView>(R.id.toolbarRate).setOnClickListener {
                    activity.onToolbarItemClicked(it)
                }
                view
            }

            ToolbarId.TOOLBAR_HOME -> {
                val view = activity.layoutInflater.inflate(R.layout.toolbar_left_menu, null)
                view.findViewById<TextView>(R.id.toolbarTitle).text = title
                view.findViewById<ImageView>(R.id.image_view_menu).setOnClickListener {
                    activity.onToolbarItemClicked(it)
                }
                view
            }
            ToolbarId.TOOLBAR_BOOKMARK -> {
                val view = activity.layoutInflater.inflate(R.layout.toolbar_bookmark, null)
                view.findViewById<TextView>(R.id.toolbarTitle).text = title
                view.findViewById<ImageView>(R.id.image_view_menu).setOnClickListener {
                    activity.onToolbarItemClicked(it)
                }
                view
            }
        }
    }

    private fun updateToolbar(baseFragment: BaseFragment, inflate: View) {
        baseFragment.toolbarContainer.removeAllViews()
        baseFragment.toolbarContainer.addView(inflate, LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
    }
}