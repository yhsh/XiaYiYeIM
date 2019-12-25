package com.yhsh.xiayiyeim.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hyphenate.chat.EMClient
import com.yhsh.xiayiyeim.R
import com.yhsh.xiayiyeim.data.ContactListItem
import com.yhsh.xiayiyeim.ui.activity.ChatActivity
import com.yhsh.xiayiyeim.widget.ContactListItemView
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.startActivity
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
 * 创建时间：2019/12/25 10:04
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim.adapter
 * 文件说明：每个联系人的item的adapter
 */
class ContactListAdapter(
    private val context: Context,
    private val contactListItems: MutableList<ContactListItem>
) :
    RecyclerView.Adapter<ContactListAdapter.ContractViewHolder>() {
    var isShow: Boolean = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractViewHolder {
        return ContractViewHolder(ContactListItemView(context))
    }

    override fun getItemCount(): Int = contactListItems.size

    override fun onBindViewHolder(holder: ContractViewHolder, position: Int) {
        val contactListItemView = holder.itemView as ContactListItemView
        val userName = contactListItems[position].userName
        println("打印用户：$userName")
        isShow =
            position == 0 || contactListItems[position].firstLetter.toUpperCase() != contactListItems[position - 1].firstLetter.toUpperCase()
        contactListItemView.bindView(contactListItems[position], isShow)
        //设置联系人的点击事件
        contactListItemView.setOnClickListener {
            context.startActivity<ChatActivity>("userName" to userName)
        }
        //长按删除好友
        contactListItemView.setOnLongClickListener {
            AlertDialog.Builder(context).setMessage(
                String.format(
                    context.getString(R.string.delete_friend_message),
                    userName
                )
            ).setTitle(context.getString(R.string.delete_friend_title))
                .setNegativeButton(context.getString(R.string.cancel), null)
                .setPositiveButton(
                    context.getString(R.string.confirm)
                ) { dialog, which ->
                    deleteFriend(userName)
                }.show()
            true
        }
    }

    //删除好友的方法
    private fun deleteFriend(userName: String) {
        EMClient.getInstance().contactManager()
            .aysncDeleteContact(userName, object : EMCallBackAdapter() {
                override fun onSuccess() {
                    context.runOnUiThread { context.toast(R.string.delete_friend_success) }
                }

                override fun onError(erroeCode: Int, errorMessage: String?) {
                    context.runOnUiThread { context.toast(R.string.delete_friend_failed) }
                }
            })
    }

    class ContractViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}