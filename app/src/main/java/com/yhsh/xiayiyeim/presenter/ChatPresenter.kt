package com.yhsh.xiayiyeim.presenter

import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.yhsh.xiayiyeim.adapter.EMCallBackAdapter
import com.yhsh.xiayiyeim.contract.ChatContract
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
 * 创建时间：2020/3/3 12:09
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim.presenter
 * 文件说明：聊天消息P层
 */
class ChatPresenter(val view: ChatContract.View) : ChatContract.Presenter {
    companion object {
        const val PAGE_SIZE = 10
    }

    //保存消息的集合
    var messages = mutableListOf<EMMessage>()

    override fun sendMessage(contact: String, message: String) {
        //发送消息
        val emMessage = EMMessage.createTxtSendMessage(message, contact)
        emMessage.setMessageStatusCallback(object : EMCallBackAdapter() {
            override fun onSuccess() {
                uiThread { view.onSendMessageSuccess() }
            }

            override fun onError(erroeCode: Int, errorMessage: String?) {
                uiThread { view.onSendMessageFail() }
            }
        })
        messages.add(emMessage)
        view.onStartSendMessage()
        EMClient.getInstance().chatManager().sendMessage(emMessage)
    }

    override fun adMessage(userName: String, p0: MutableList<EMMessage>?) {
        p0?.let { messages.addAll(it) }
        //更新消息为已读
        val conversation = EMClient.getInstance().chatManager().getConversation(userName)
        conversation.markAllMessagesAsRead()
    }

    override fun loadMessage(userName: String) {
        doAsync {
            val conversion = EMClient.getInstance().chatManager().getConversation(userName)
            //消息标记为已读
            conversion.markAllMessagesAsRead()
            messages.addAll(conversion.allMessages)
            uiThread { view.onMessageLoad() }
        }
    }

    override fun loadMoreMessage(userName: String) {
        doAsync {
            val conversation = EMClient.getInstance().chatManager().getConversation(userName)
            val startMsgId = messages[0].msgId
            val loadMoreMsgFromDB = conversation.loadMoreMsgFromDB(startMsgId, PAGE_SIZE)
            messages.addAll(0, loadMoreMsgFromDB)
            uiThread { view.onMoreMessageLoad(loadMoreMsgFromDB.size) }
        }
    }
}