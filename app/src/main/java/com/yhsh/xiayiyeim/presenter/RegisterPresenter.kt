package com.yhsh.xiayiyeim.presenter

import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import com.yhsh.xiayiyeim.contract.RegisterContract
import com.yhsh.xiayiyeim.extentions.isValidPassword
import com.yhsh.xiayiyeim.extentions.isValidUserName
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
 * 创建时间：2019/12/24 14:30
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim.presenter
 * 文件说明：注册页面的presenter层
 */
class RegisterPresenter(private val registerView: RegisterContract.View) :
    RegisterContract.Presenter {
    override fun register(username: String, password: String, confirmPassword: String) {
        if (username.isValidUserName()) {
            if (password.isValidPassword()) {
                //两个密码一致才可以登录
                if (password == confirmPassword) {
                    registerView.startRegister()
                    //注册Bmob
                    registerBmob(username, password)
                } else registerView.confirmPasswordError()
            } else registerView.passwordError()
        } else registerView.userNameError()
    }

    private fun registerBmob(userName: String, password: String) {
        val userBmob = BmobUser()
        userBmob.username = userName
        userBmob.setPassword(password)
        userBmob.signUp<BmobUser>(object : SaveListener<BmobUser>() {
            override fun done(p0: BmobUser?, p1: BmobException?) {
                if (null == p1) {
                    //注册成功后再注册到环信后台服务器
                    registerEMClient(userName, password)
                } else {
                    if (p1.errorCode == 202) {
                        //账号已存在
                        registerView.userNameExist()
                    } else registerView.onRegisterFail()
                }
            }

            private fun registerEMClient(userName: String, password: String) {
                //同步方法需要放到子线程
                doAsync {
                    try {
                        EMClient.getInstance().createAccount(userName, password)
                        uiThread {
                            //主线程回调成功结果
                            registerView.onRegisterSuccess()
                        }
                    } catch (e: HyphenateException) {
                        uiThread {
                            //主线程回调失败的方法
                            registerView.onRegisterFail()
                        }
                    }
                }
            }

        })
    }
}