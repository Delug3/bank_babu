package com.bankbabu.balance.fragments

import android.content.Context
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bankbabu.balance.R

class OnBoardingFragment : Fragment() {
    private var drawableImageId: Int = 0

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        arguments?.getInt(ARG_DRAWABLE_ID)?.let {
            drawableImageId = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.image)
                .setImageResource(drawableImageId)
    }

    companion object {
        private const val ARG_DRAWABLE_ID = "drawable_id"

        @JvmStatic
        fun newInstance(@DrawableRes drawableImageId: Int): OnBoardingFragment {
            return OnBoardingFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_DRAWABLE_ID, drawableImageId)
                }
            }
        }
    }
}