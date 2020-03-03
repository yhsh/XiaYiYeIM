package com.yhsh.xiayiyeim.ui.activity

import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.yhsh.xiayiyeim.R
import com.yhsh.xiayiyeim.adapter.EMMessageListenerAdapter
import com.yhsh.xiayiyeim.factory.FragmentFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private val messageListener = object : EMMessageListenerAdapter() {
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            updateBottomBarUnReadCount()
        }
    }

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
        //初始化接收消息的监听器
        EMClient.getInstance().chatManager().addMessageListener(messageListener)
    }

    override fun onResume() {
        super.onResume()
        updateBottomBarUnReadCount()
    }

    private fun updateBottomBarUnReadCount() {
        //初始化底部bottom未读消息条数
        val tab = bottomBar.getTabWithId(R.id.tab_conversation)
        tab.setBadgeCount(EMClient.getInstance().chatManager().unreadMessageCount)
    }

    override fun onDestroy() {
        super.onDestroy()
        //移除消息监听
        EMClient.getInstance().chatManager().removeMessageListener(messageListener)
    }
}
