package com.yhsh.xiayiyeim.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import com.yhsh.xiayiyeim.R
import com.yhsh.xiayiyeim.adapter.ConversationListAdapter
import com.yhsh.xiayiyeim.adapter.EMMessageListenerAdapter
import kotlinx.android.synthetic.main.fragment_conversation.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/*
 * Copyright (c) 2020, smuyyh@gmail.com All Rights Reserved.
 * #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG            #
 * #                                                   #
 */

/**
 * @author 下一页5（轻飞扬）
 * 创建时间：2019/12/24 16:50
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim.ui.fragment
 * 文件说明：聊天的页面
 */
class ConversationFragment : BaseFragment() {
    private var conversations = mutableListOf<EMConversation>()
    private val messageListener = object : EMMessageListenerAdapter() {
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            super.onMessageReceived(p0)
            //重新加载消息
            loadConversations()
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_conversation
    override fun init() {
        super.init()
        initHeader()
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ConversationListAdapter(context, conversations)
        }
        EMClient.getInstance().chatManager().addMessageListener(messageListener)
        loadConversations()
    }

    private fun loadConversations() {
        doAsync {
            val allConversations = EMClient.getInstance().chatManager().allConversations
            //添加之前要清空之前的消息
            conversations.clear()
            conversations.addAll(allConversations.values)
            uiThread { recyclerView.adapter?.notifyDataSetChanged() }
        }
    }

    private fun initHeader() {
        headerTitle.text = getString(R.string.message)
    }

    override fun onResume() {
        super.onResume()
        //获取焦点的时候再次加载数据
        loadConversations()
    }

    override fun onDestroy() {
        super.onDestroy()
        //解绑消息监听
        EMClient.getInstance().chatManager().removeMessageListener(messageListener)
    }
}