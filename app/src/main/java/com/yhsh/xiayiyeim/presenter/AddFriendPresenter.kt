package com.yhsh.xiayiyeim.presenter

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.hyphenate.chat.EMClient
import com.yhsh.xiayiyeim.contract.AddFriendContract
import com.yhsh.xiayiyeim.data.AddFriendItem
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
 * 创建时间：2020/3/2 17:33
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim.presenter
 * 文件说明：
 */
class AddFriendPresenter(val view: AddFriendContract.View) : AddFriendContract.Presenter {
    val addFriendItems = mutableListOf<AddFriendItem>()
    override fun search(key: String) {
        val query = BmobQuery<BmobUser>()
        //查找是否有此key的用户户名数据
        query.addWhereContains("username", key)
            //结果排除当前用户
            .addWhereNotEqualTo("username", EMClient.getInstance().currentUser)
        query.findObjects(object : FindListener<BmobUser>() {
            override fun done(p0: MutableList<BmobUser>?, p1: BmobException?) {
                for (index in 0 until p0!!.size) {
                    println("打印集合1=${p0[index].username}")
                }
                if (p1 == null) {
                    val allContact = IMDataBase.instance.getAllContact()

                    doAsync {
                        p0.forEach {
                            println("打印集合2=${it.username}")
                            var isAdded = false
                            //比对是否添加好友
                            for (contact in allContact) {
                                if (contact.name == it.username) {
                                    isAdded = true
                                }
                            }
                            val addFriendItem = AddFriendItem(it.username, it.createdAt, isAdded)
                            addFriendItems.add(addFriendItem)
                        }
                    }
                    uiThread {
                        view.onSearchSuccess()
                    }
                } else view.onSearchFail()
            }
        })
    }
}
