package com.yhsh.xiayiyeim.ui.activity

import android.text.TextUtils
import android.view.KeyEvent
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.yhsh.xiayiyeim.R
import com.yhsh.xiayiyeim.adapter.AddFriendListAdapter
import com.yhsh.xiayiyeim.contract.AddFriendContract
import com.yhsh.xiayiyeim.presenter.AddFriendPresenter
import kotlinx.android.synthetic.main.activity_add_friend.*
import kotlinx.android.synthetic.main.header.*
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
 * 创建时间：2020/3/1 19:42
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim.ui.activity
 * 文件说明：添加好友的页面
 */
class AddFriendActivity : BaseActivity(), AddFriendContract.View {

    val addFriendPresenter by lazy { AddFriendPresenter(this) }
    override fun getLayoutResId(): Int = R.layout.activity_add_friend
    override fun init() {
        super.init()
        //设置标题
        headerTitle.text = getString(R.string.add_friend)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = AddFriendListAdapter(context,addFriendPresenter.addFriendItems)
        }
        //设置搜索按钮和键盘搜索按钮的点击事件
        search.setOnClickListener { search() }
        userName.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                search()
                return true
            }
        })
    }

    fun search() {
        val searchData = userName.text.toString().trim()
        if (TextUtils.isEmpty(searchData)) {
            return
        }
        //隐藏软键盘
        hideMethodKeyboard()
        showProgress(getString(R.string.searching))
        addFriendPresenter.search(searchData)
    }

    override fun onSearchSuccess() {
        dismissProgress()
        toast(R.string.search_success)
        //刷新页面
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onSearchFail() {
        dismissProgress()
        toast(R.string.search_failed)
    }
}
