package com.yhsh.xiayiyeim.ui.activity

import com.yhsh.xiayiyeim.R
import com.yhsh.xiayiyeim.factory.FragmentFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_main
    override fun init() {
        super.init()
        bottomBar.setOnTabSelectListener { tabId ->
            FragmentFactory.instance.getFragmentPage(tabId)?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_frame, it)
                    .commit()
            }
        }
    }
}
