package com.yhsh.xiayiyeim.data.db

import com.yhsh.xiayiyeim.extentions.toVarargArray
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

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
 * 创建时间：2020/3/2 18:59
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim.data.db
 * 文件说明：
 */
class IMDataBase {
    companion object {
        val dataBaseHelper = DataBaseHelper()
        val instance = IMDataBase()
    }

    /**
     * 保存用户信息
     */
    fun saveContact(contact: Contact) {
        dataBaseHelper.use {
            insert(ContactTable.NAME, *contact.map.toVarargArray())
        }
    }

    /**
     * 获取所有用户信息
     */
    fun getAllContact(): List<Contact> = dataBaseHelper.use {
        select(ContactTable.NAME).parseList(object : MapRowParser<Contact> {
            override fun parseRow(columns: Map<String, Any?>): Contact =
                Contact(columns.toMutableMap())
        })
    }

    /**
     * 删除联系人的方法
     */
    fun deleteAllContact() {
        dataBaseHelper.use { delete(ContactTable.NAME, null, null) }
    }
}