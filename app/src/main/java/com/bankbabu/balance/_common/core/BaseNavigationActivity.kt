package com.bankbabu.balance._common.core

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bankbabu.balance._common.navigator.core.FragmentCallback
import com.bankbabu.balance._common.navigator.core.IFragmentData
import com.bankbabu.balance._common.navigator.core.ToolbarId
import com.bankbabu.balance._common.navigator.manager.core.ManagerUI


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/2/19.
 */
abstract class BaseNavigationActivity : AppCompatActivity(), FragmentCallback {
    var managerUI: ManagerUI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        managerUI = initManagerUI()
        setUI()
    }

    open fun setUI() {

    }

    abstract fun initManagerUI(): ManagerUI?

    @LayoutRes
    protected abstract fun getContentView(): Int

    override fun changeFragmentTo(fragmentData: IFragmentData) {
        managerUI?.changeFragmentTo(fragmentData)
    }

    override fun initToolbar(baseFragment: BaseFragment, toolbarId: ToolbarId, title: Int) {
        managerUI?.initToolbar(baseFragment, toolbarId, title)
    }

    override fun initToolbar(baseFragment: BaseFragment, toolbarId: ToolbarId, title: String) {
        managerUI?.initToolbar(baseFragment, toolbarId, title)
    }

    override fun popBackStack() {
        managerUI?.popBackStack()
    }

    open fun onToolbarItemClicked(it: View?) {

    }
}