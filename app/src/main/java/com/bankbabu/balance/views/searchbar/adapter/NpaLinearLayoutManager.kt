package com.bankbabu.balance.views.searchbar.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 4/12/19.
 */
class NpaLinearLayoutManager : LinearLayoutManager {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}