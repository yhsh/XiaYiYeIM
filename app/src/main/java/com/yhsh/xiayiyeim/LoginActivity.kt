package com.yhsh.xiayiyeim

import com.yhsh.xiayiyeim.contract.LoginContract
import com.yhsh.xiayiyeim.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
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
 * 创建时间：2019/12/23 16:38
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim
 * 文件说明：登录页面
 */
class LoginActivity : BaseActivity(), LoginContract.View {
    private val loginPresenter by lazy { LoginPresenter(this) }

    override fun getLayoutResId(): Int = R.layout.activity_login
    override fun init() {
        super.init()
        login.setOnClickListener { login() }
        password.setOnEditorActionListener { v, actionId, event ->
            login()
            true
        }
    }

    //点击登录按钮登录的方法
    private fun login() {
        loginPresenter.login(userName.text.trim().toString(), password.text.trim().toString())
    }

    override fun onUserNameError() {
        userName.error = getString(R.string.user_name_error)
    }

    override fun onPasswordError() {
        password.error = getString(R.string.password_error)
    }

    override fun onStartLogin() {
        //隐藏进度条
        showProgress(getString(R.string.logging))

    }

    override fun onLoggedSuccess() {
        //隐藏进度条
        dismissProgress()
        //进入主页面
        startActivity<MainActivity>()
        //关闭登录页面
        finish()
    }

    override fun onLoggedFail() {
        //隐藏进度条
        dismissProgress()
        //弹出失败信息
        toast(getString(R.string.login_failed))
    }

}