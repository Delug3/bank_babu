package com.bankbabu.balance._common.navigator.core

import android.support.annotation.StringRes
import com.bankbabu.balance._common.core.BaseFragment


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/2/19.
 */
interface FragmentCallback {
    fun changeFragmentTo(fragmentData: IFragmentData)

    fun onBackPressed()

    fun initToolbar(baseFragment: BaseFragment, toolbarId: ToolbarId, @StringRes title: Int)

    fun initToolbar(baseFragment: BaseFragment, toolbarId: ToolbarId, title: String)

    fun onLeftMenuClicked()

    fun popBackStack()
}