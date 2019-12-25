package com.yhsh.xiayiyeim.presenter

import com.hyphenate.chat.EMClient
import com.yhsh.xiayiyeim.adapter.EMCallBackAdapter
import com.yhsh.xiayiyeim.contract.LoginContract
import com.yhsh.xiayiyeim.extentions.isValidPassword
import com.yhsh.xiayiyeim.extentions.isValidUserName

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
 * 创建时间：2019/12/23 17:34
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim.presenter
 * 文件说明：
 */
class LoginPresenter(private val loginView: LoginContract.View) : LoginContract.Presenter {
    override fun login(userName: String, password: String) {
        if (userName.isValidUserName()) {
            //用户名合法
            if (password.isValidPassword()) {
                //密码合法，开启登录
                loginView.onStartLogin()
                //登录环信
                loginEaseMob(userName, password)
            } else loginView.onPasswordError()
        } else loginView.onUserNameError()
    }

    private fun loginEaseMob(userName: String, password: String) {
        EMClient.getInstance().login(userName, password, object : EMCallBackAdapter() {
            override fun onSuccess() {
                //登录成功，加载所有的联系人和群组
                EMClient.getInstance().groupManager().loadAllGroups()
                EMClient.getInstance().chatManager().loadAllConversations()
                //此处是子线程
                uiThread { loginView.onLoggedSuccess() }
            }

            override fun onError(erroeCode: Int, errorMessage: String?) {
                uiThread { loginView.onLoggedFail() }
            }
        })
    }
}