package com.yhsh.xiayiyeim.ui.activity

import android.os.Handler
import android.os.Looper
import com.yhsh.xiayiyeim.R
import com.yhsh.xiayiyeim.contract.SplashContract
import com.yhsh.xiayiyeim.presenter.SplashPresenter
import org.jetbrains.anko.startActivity

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
 * 创建时间：2019/12/23 16:12
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim
 * 文件说明：打开APP的第一个页面(闪屏页)
 */
class SplashActivity : BaseActivity(), SplashContract.View {
    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private val splashPresenter = SplashPresenter(this)

    companion object {
        const val DELAYED = 2000L
    }

    override fun getLayoutResId(): Int = R.layout.activity_splash

    override fun init() {
        //检查登录状态
        splashPresenter.checkLoginStatus()
    }

    override fun onNotLoggedIn() {
        //未登录，延迟2秒跳转到登录页面
        handler.postDelayed({
            startActivity<LoginActivity>()
            finish()
        }, DELAYED)
    }

    override fun onLoggedIn() {
        //跳转到主页面
        startActivity<MainActivity>()
        finish()
    }
}