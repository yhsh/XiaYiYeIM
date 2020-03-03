package com.yhsh.xiayiyeim.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import com.hyphenate.util.DateUtils
import com.yhsh.xiayiyeim.R
import kotlinx.android.synthetic.main.view_receive_message_item.view.*
import java.util.*

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
 * 创建时间：2020/3/2 20:52
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim.widget
 * 文件说明：接收消息的布局
 */
class ReceiveMessageItemView(context: Context?, attrs: AttributeSet? = null) :
    RelativeLayout(context, attrs) {
    fun bindView(singleMessage: EMMessage, showStamp: Boolean) {
        updateStamp(singleMessage, showStamp)
        updateMessage(singleMessage)
    }

    private fun updateStamp(singleMessage: EMMessage, showStamp: Boolean) {
        if (showStamp) {
            timestamp.visibility = View.VISIBLE
            timestamp.text = DateUtils.getTimestampString(Date(singleMessage.msgTime))
        } else {
            timestamp.visibility = View.GONE
        }
    }

    private fun updateMessage(singleMessage: EMMessage) {
        if (singleMessage.type == EMMessage.Type.TXT) {
            receiveMessage.text = (singleMessage.body as EMTextMessageBody).message
        } else {
            receiveMessage.text = context.getString(R.string.no_text_message)
        }
    }

    init {
        View.inflate(context, R.layout.view_receive_message_item, this)
    }
}