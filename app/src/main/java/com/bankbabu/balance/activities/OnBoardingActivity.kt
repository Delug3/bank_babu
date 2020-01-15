package com.bankbabu.balance.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.Button
import com.bankbabu.balance.R
import com.bankbabu.balance.fragments.OnBoardingFragment
import com.rd.PageIndicatorView


private const val NUMBER_OF_ONBOARDING_SCREENS = 4

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var pageIndicator: PageIndicatorView
    private lateinit var actionButton: Button
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        removeStatusBar()
        initViews()

        viewPager.adapter = getOnBoardingAdapter()
        viewPager.addOnPageChangeListener(getOnPageChangeListener())

        pageIndicator.count = NUMBER_OF_ONBOARDING_SCREENS
        actionButton.setOnClickListener { onActionButtonClicked() }
    }

    private fun onActionButtonClicked() {
        if (viewPager.currentItem == NUMBER_OF_ONBOARDING_SCREENS - 1) {
            val intent = Intent(this@OnBoardingActivity, WelcomeActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        } else {
            viewPager.currentItem = viewPager.currentItem + 1
        }
    }

    private fun getOnPageChangeListener(): ViewPager.OnPageChangeListener {
        return object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                pageIndicator.setSelected(position)
                if (isLastScreen(position)) {
                    actionButton.text = getString(R.string.finish)
                } else {
                    actionButton.text = getString(R.string.next)
                }
            }

            private fun isLastScreen(position: Int) = position == NUMBER_OF_ONBOARDING_SCREENS - 1

            override fun onPageScrollStateChanged(state: Int) {
            }
        }
    }

    private fun getOnBoardingAdapter(): FragmentStatePagerAdapter {
        return object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment? {
                return when (position) {
                    0 -> OnBoardingFragment.newInstance(R.drawable.onboarding_1)
                    1 -> OnBoardingFragment.newInstance(R.drawable.onboarding_2)
                    2 -> OnBoardingFragment.newInstance(R.drawable.onboarding_3)
                    3 -> OnBoardingFragment.newInstance(R.drawable.onboarding_4)
                    else -> null
                }
            }

            override fun getCount(): Int {
                return NUMBER_OF_ONBOARDING_SCREENS
            }
        }
    }

    private fun initViews() {
        pageIndicator = findViewById(R.id.pageIndicatorView)
        actionButton = findViewById(R.id.actionButton)
        viewPager = findViewById(R.id.view_pager)
    }

    private fun removeStatusBar() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}