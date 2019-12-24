package com.yhsh.xiayiyeim.factory

import androidx.fragment.app.Fragment
import com.yhsh.xiayiyeim.R
import com.yhsh.xiayiyeim.ui.fragment.ContactFragment
import com.yhsh.xiayiyeim.ui.fragment.ConversationFragment
import com.yhsh.xiayiyeim.ui.fragment.DynamicFragment

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
 * 创建时间：2019/12/24 16:56
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim.factory
 * 文件说明：
 */
class FragmentFactory private constructor() {
    companion object {
        var instance = FragmentFactory()
    }

    private val conversationFragment = ConversationFragment()
    private val contactFragment = ContactFragment()
    private val dynamicFragment = DynamicFragment()

    fun getFragmentPage(tabId: Int): Fragment? {
        when (tabId) {
            R.id.tab_conversation -> return conversationFragment
            R.id.tab_contacts -> return contactFragment
            R.id.tab_dynamic -> return dynamicFragment
        }
        return null
    }
}