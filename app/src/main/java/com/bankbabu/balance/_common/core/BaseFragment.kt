package com.bankbabu.balance._common.core

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import butterknife.ButterKnife
import com.bankbabu.balance.R
import com.bankbabu.balance._common.navigator.core.FragmentCallback
import com.bankbabu.balance._common.navigator.core.IFragmentData
import com.bankbabu.balance._common.navigator.core.ToolbarId


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 2/16/19.
 */
abstract class BaseFragment : Fragment() {

    lateinit var rootView: View
    private var outStateData: Bundle? = null
    lateinit var toolbarContainer: LinearLayout
    private lateinit var fragmentCallback: FragmentCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getContentView(), container, false)
        if (withToolbar())
            toolbarContainer = rootView.findViewById(R.id.toolbar_container)
        return rootView
    }

    protected open fun withToolbar(): Boolean {
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCallback = activity as FragmentCallback
        this.handleData()
        initUi()
        setUi(savedInstanceState)
    }


    protected open fun initUi() {

    }

    protected open fun setUi(savedInstanceState: Bundle?) {

    }

    @LayoutRes
    protected abstract fun getContentView(): Int

    fun <T : View> findViewById(@IdRes id: Int): T = rootView.findViewById(id)

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBundle("outStateData", this.outStateData)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            this.outStateData = savedInstanceState.getBundle("outStateData")
        }
        retainInstance = true
    }


    override fun setArguments(args: Bundle?) {
        this.outStateData = args
        super.setArguments(args)
    }

    fun changeFragmentTo(baseFragmentData: IFragmentData) {
        fragmentCallback.changeFragmentTo(baseFragmentData)
    }

    protected open fun handleData() {

    }

    protected fun getFragmentData(): Bundle? {
        return this.outStateData
    }

    protected fun getContentActivity(): BaseNavigationActivity {
        return activity as BaseNavigationActivity
    }

    open fun onToolbarItemClicked(view: View) {

    }

    fun onBackPressed() {
        fragmentCallback.onBackPressed()
    }

    fun initToolbar(toolBarId: ToolbarId, @StringRes label: Int) {
        fragmentCallback.initToolbar(this, toolBarId, label)
    }

    fun initToolbar(toolBarId: ToolbarId, label: String) {
        fragmentCallback.initToolbar(this, toolBarId, label)
    }

    fun popBackStack() {
        fragmentCallback.popBackStack()
    }
}