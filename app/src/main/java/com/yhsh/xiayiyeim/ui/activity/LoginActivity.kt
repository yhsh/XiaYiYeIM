package com.yhsh.xiayiyeim.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.yhsh.xiayiyeim.R
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
        //注册按钮的监听
        newUser.setOnClickListener { startActivity<RegisterActivity>() }
        login.setOnClickListener { login() }
        password.setOnEditorActionListener { v, actionId, event ->
            login()
            true
        }
    }

    //点击登录按钮登录的方法
    private fun login() {
        //隐藏键盘
        hideMethodKeyboard()
        //检查是否又写入SD卡权限的方法
        if (hasWriteExternalStoragePermission()) {
            loginPresenter.login(userName.text.trim().toString(), password.text.trim().toString())
        } else {
            //请求权限
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //申请同意了
            login()
        } else {
            //申请拒绝了
            toast(R.string.permission_denied)
        }
    }

    private fun hasWriteExternalStoragePermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
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