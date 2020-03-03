package com.yhsh.xiayiyeim.ui.activity

import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.yhsh.xiayiyeim.R
import com.yhsh.xiayiyeim.adapter.EMMessageListenerAdapter
import com.yhsh.xiayiyeim.factory.FragmentFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

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
        //设置多设备登录冲突的监听
        EMClient.getInstance().addConnectionListener(object : EMConnectionListener {
            override fun onConnected() {

            }

            //设备断开了
            override fun onDisconnected(p0: Int) {
                if (p0 == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    //跳转登录页面重新登录
                    startActivity<LoginActivity>()
                    finish()
                }
            }
        })
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
