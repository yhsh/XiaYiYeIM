package com.yhsh.xiayiyeim.ui.fragment

import com.hyphenate.chat.EMClient
import com.yhsh.xiayiyeim.R
import com.yhsh.xiayiyeim.adapter.EMCallBackAdapter
import com.yhsh.xiayiyeim.ui.activity.LoginActivity
import kotlinx.android.synthetic.main.fragment_dynamic.*
import kotlinx.android.synthetic.main.header.*
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
 * 创建时间：2019/12/24 16:50
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim.ui.fragment
 * 文件说明：我的的页面
 */
class DynamicFragment : BaseFragment() {
    override fun getLayoutResId(): Int = R.layout.fragment_dynamic
    override fun init() {
        super.init()
        headerTitle.text = getString(R.string.dynamic)
        logout.text = String.format(getString(R.string.logout, EMClient.getInstance().currentUser))
        logout.setOnClickListener { logoutStart() }
    }

    /**
     * 推出登录的方法
     */
    private fun logoutStart() {
        EMClient.getInstance().logout(true, object : EMCallBackAdapter() {
            override fun onSuccess() {
                activity?.runOnUiThread {
                    activity?.toast(R.string.logout_success)
                    activity?.startActivity<LoginActivity>()
                    activity?.finish()
                }
            }

            override fun onError(p0: Int, p1: String?) {
                activity?.runOnUiThread { context?.toast(R.string.logout_failed) }
            }
        })
    }
}