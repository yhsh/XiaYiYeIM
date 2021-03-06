package com.yhsh.xiayiyeim.presenter

import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import com.yhsh.xiayiyeim.contract.ContactContract
import com.yhsh.xiayiyeim.data.ContactListItem
import com.yhsh.xiayiyeim.data.db.Contact
import com.yhsh.xiayiyeim.data.db.IMDataBase
import org.jetbrains.anko.doAsync

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
 * 创建时间：2019/12/25 10:17
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim.presenter
 * 文件说明：联系人P层的实现
 */
class ContractPresenter(private val contractView: ContactContract.View) :
    ContactContract.Presenter {
    val contactListItems = mutableListOf<ContactListItem>()
    override fun loadContacts() {
        doAsync {
            //先清空数据
            IMDataBase.instance.deleteAllContact()
            try {
                val userNameList = EMClient.getInstance().contactManager().allContactsFromServer
                //根据首字母排序
                userNameList.sortBy { it[0] }
                //添加数据之前清空
                contactListItems.clear()
                userNameList.forEachIndexed { index, s ->
                    val showFirstLetter = index == 0 || s[0] != userNameList[index - 1][0]
                    val contactListItem = ContactListItem(s, s[0].toUpperCase(), showFirstLetter)
                    contactListItems.add(contactListItem)
                    val contact = Contact(mutableMapOf("name " to s))
                    //保存联系人
                    IMDataBase.instance.saveContact(contact)
                }
                println(userNameList)
                uiThread { contractView.onLoadContactsSuccess() }
            } catch (e: HyphenateException) {
                uiThread { contractView.onLoadContactsFail() }
            }
        }
    }
}