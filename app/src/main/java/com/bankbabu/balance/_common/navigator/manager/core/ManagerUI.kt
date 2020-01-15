package com.bankbabu.balance._common.navigator.manager.core

import android.support.annotation.StringRes
import com.bankbabu.balance._common.core.BaseFragment
import com.bankbabu.balance._common.navigator.core.IFragmentData
import com.bankbabu.balance._common.navigator.core.ToolbarId


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/2/19.
 */
interface ManagerUI {
    fun changeFragmentTo(fragmentData: IFragmentData)

    fun initToolbar(baseFragment: BaseFragment, toolbarId: ToolbarId, @StringRes title: Int)

    fun initToolbar(baseFragment: BaseFragment, toolbarId: ToolbarId, title: String)

    fun popBackStack()
}