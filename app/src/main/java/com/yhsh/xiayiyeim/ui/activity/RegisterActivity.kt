package com.yhsh.xiayiyeim.ui.activity

import com.yhsh.xiayiyeim.R
import com.yhsh.xiayiyeim.contract.RegisterContract
import com.yhsh.xiayiyeim.presenter.RegisterPresenter
import kotlinx.android.synthetic.main.activity_register.*
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
 * 创建时间：2019/12/24 14:11
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim
 * 文件说明：注册页面
 */
class RegisterActivity : BaseActivity(), RegisterContract.View {

    private val registerPresenter by lazy { RegisterPresenter(this) }
    override fun getLayoutResId(): Int = R.layout.activity_register
    override fun init() {
        super.init()
        register.setOnClickListener {
            register()
        }
        confirmPassword.setOnEditorActionListener { v, actionId, event ->
            //隐藏软键盘
            hideMethodKeyboard()
            register()
            true
        }
    }

    private fun register() {
        registerPresenter.register(
            userName.text.trim().toString(),
            password.text.trim().toString(),
            confirmPassword.text.trim().toString()
        )
    }

    override fun userNameError() {
        userName.error = getString(R.string.user_name_error)
    }

    override fun passwordError() {
        password.error = getString(R.string.password_error)
    }

    override fun confirmPasswordError() {
        confirmPassword.error = getString(R.string.password_error)
    }

    override fun startRegister() {
        //显示进度条
        showProgress(getString(R.string.registering))
    }

    override fun onRegisterSuccess() {
        //隐藏进度条
        dismissProgress()
        toast(R.string.register_success)
        finish()
    }

    override fun onRegisterFail() {
        //隐藏进度条
        dismissProgress()
        toast(R.string.register_failed)
    }

    override fun userNameExist() {
        //隐藏进度条
        dismissProgress()
        //用户已存在
        toast(R.string.user_already_exist)
    }
}