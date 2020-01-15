package com.bankbabu.balance._common.navigator.manager.core

import android.os.Bundle
import android.support.annotation.AnimRes
import com.bankbabu.balance.R
import com.bankbabu.balance._common.core.BaseFragment
import com.bankbabu.balance._common.core.BaseNavigationActivity
import com.bankbabu.balance._common.navigator.core.FragmentData
import com.bankbabu.balance._common.navigator.core.FragmentId


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/2/19.
 */
abstract class BaseManagerUI(private val activity: BaseNavigationActivity,
                             private val useFragmentAnim: Boolean = true,
                             var initialFragment: FragmentData = FragmentData(FragmentId.BANK_BALANCE_CHECK)) : ManagerUI {
    init {
        this.initUI()
    }

    protected abstract fun getIdFragmentsContainer(): Int

    protected abstract fun initUI()

    protected fun addFragmentToContainer(fragment: BaseFragment, toBackStack: Boolean, bundle: Bundle?) {
        fragment.arguments = bundle
        val fm = activity.supportFragmentManager
        val dashboardFragment: BaseFragment?
        if (fragment::class === getFirstInStackFragmentClass()) {
            for (i in 0 until fm.backStackEntryCount) {
                fm.popBackStack()
            }
            return
        }

        dashboardFragment = fm.findFragmentByTag(fragment.javaClass.simpleName) as BaseFragment?
        if (dashboardFragment != null && dashboardFragment.isAdded) {
        } else {
            val transaction = fm.beginTransaction()
            if (useFragmentAnim) {
                transaction.setCustomAnimations(this.getFragmentAnimationEnter(), this.getFragmentAnimationExit(), this.getFragmentAnimationPopEnter(), this.getFragmentAnimationPopExit())
            }

            if (toBackStack) {
                transaction.addToBackStack(fragment.javaClass.simpleName).replace(this.getIdFragmentsContainer(), fragment, fragment.javaClass.simpleName)
            } else {
                transaction.replace(this.getIdFragmentsContainer(), fragment, fragment.javaClass.simpleName)
            }
            transaction.commitAllowingStateLoss()

        }
    }

    protected abstract fun getFirstInStackFragmentClass(): Class<*>

    @AnimRes
    protected open fun getFragmentAnimationEnter(): Int = R.anim.fragment_animation_enter

    @AnimRes
    protected open fun getFragmentAnimationExit(): Int = R.anim.fragment_animation_exit

    @AnimRes
    protected open fun getFragmentAnimationPopEnter(): Int = R.anim.fragment_animation_pop_enter

    @AnimRes
    protected open fun getFragmentAnimationPopExit(): Int = R.anim.fragment_animation_pop_exit
}