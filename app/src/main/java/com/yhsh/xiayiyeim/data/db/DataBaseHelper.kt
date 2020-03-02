package com.yhsh.xiayiyeim.data.db

import android.database.sqlite.SQLiteDatabase
import com.yhsh.xiayiyeim.app.IMApplication
import org.jetbrains.anko.db.*

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
 * 创建时间：2020/3/2 18:45
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim.data
 * 文件说明：
 */
class DataBaseHelper() : ManagedSQLiteOpenHelper(IMApplication.instance,
    NAME, null,
    VERSION
) {
    companion object {
        val NAME = "im.db"
        val VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //创建数据库的操作
        db?.createTable(
            ContactTable.NAME,
            true,
            ContactTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            ContactTable.CONTACT to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //升级数据库，先删除数据再创建
        db?.dropTable(ContactTable.NAME)
        onCreate(db)
    }
}