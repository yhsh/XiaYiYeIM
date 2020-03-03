package com.yhsh.xiayiyeim.ui.activity

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.yhsh.xiayiyeim.R
import com.yhsh.xiayiyeim.adapter.EMMessageListenerAdapter
import com.yhsh.xiayiyeim.adapter.MessageListAdapter
import com.yhsh.xiayiyeim.contract.ChatContract
import com.yhsh.xiayiyeim.presenter.ChatPresenter
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.toast

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
 * 创建时间：2019/12/25 13:31
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim.ui.activity
 * 文件说明：聊天的页面
 */
class ChatActivity : BaseActivity(), ChatContract.View {
    private val chatPresenter by lazy { ChatPresenter(this) }
    lateinit var userName: String
    private val messageListener = object : EMMessageListenerAdapter() {
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            super.onMessageReceived(p0)
            chatPresenter.adMessage(userName, p0)
            //刷新消息
            runOnUiThread {
                recyclerView.adapter?.notifyDataSetChanged()
                scrollToBottom()
            }
        }
    }

    override fun getLayoutResId(): Int = R.layout.activity_chat
    override fun init() {
        super.init()
        initHeader()
        initEditText()
        initRecyclerView()
        EMClient.getInstance().chatManager().addMessageListener(messageListener)
        send.setOnClickListener { send() }
        chatPresenter.loadMessage(userName)
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MessageListAdapter(this@ChatActivity, chatPresenter.messages)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val linearLayoutManager = layoutManager as LinearLayoutManager
                        if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                            //证明滑动到顶部了,再去加载更多数据
                            chatPresenter.loadMoreMessage(userName)
                        }
                    }
                }
            })
        }
    }

    private fun send() {
        val sendData = edit.text.toString().trim()
        if (sendData.isBlank()) {
            return
        }
        hideMethodKeyboard()
        chatPresenter.sendMessage(userName, sendData)
    }

    private fun initHeader() {
        back.visibility = View.VISIBLE
        userName = intent.getStringExtra("userName")
        headerTitle.text = String.format(getString(R.string.chat_title), userName)
        back.setOnClickListener { finish() }
    }

    private fun initEditText() {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                send.isEnabled = !s.isNullOrEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        edit.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                send()
                return true
            }
        })
    }

    override fun onStartSendMessage() {
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onSendMessageSuccess() {
        recyclerView.adapter?.notifyDataSetChanged()
        toast(R.string.send_message_success)
        edit.text.clear()
        scrollToBottom()
    }

    override fun onSendMessageFail() {
        recyclerView.adapter?.notifyDataSetChanged()
        toast(R.string.send_message_failed)
    }

    /**
     * 自动滑动到底部的方法
     */
    private fun scrollToBottom() {
        recyclerView.scrollToPosition(chatPresenter.messages.size - 1)
    }

    /**
     * 打开聊天页面自动加载之前的消息后自动刷新并且滑动到最下面
     */
    override fun onMessageLoad() {
        recyclerView.adapter?.notifyDataSetChanged()
        scrollToBottom()
    }

    override fun onMoreMessageLoad(size: Int) {
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scrollToPosition(size)
    }

    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().chatManager().removeMessageListener(messageListener)
    }
}