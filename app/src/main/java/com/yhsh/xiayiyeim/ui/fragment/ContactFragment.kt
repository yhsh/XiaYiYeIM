package com.yhsh.xiayiyeim.ui.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyphenate.chat.EMClient
import com.yhsh.xiayiyeim.R
import com.yhsh.xiayiyeim.adapter.ContactListAdapter
import com.yhsh.xiayiyeim.adapter.EMContactListenerAdapter
import com.yhsh.xiayiyeim.contract.ContactContract
import com.yhsh.xiayiyeim.presenter.ContractPresenter
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.header.add
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
 * 文件说明：联系人的页面
 */
class ContactFragment : BaseFragment(), ContactContract.View {

    private val contractPresenter by lazy { ContractPresenter(this) }
    override fun getLayoutResId(): Int = R.layout.fragment_contacts
    override fun init() {
        super.init()
        headerTitle.text = getString(R.string.contact)
        add.visibility = View.VISIBLE
        swipeRefreshLayout.apply {
            setColorSchemeResources(
                R.color.qqBlueColor,
                R.color.colorAccent,
                R.color.qqRedDarkColor
            )
            isRefreshing = true
            //设置下拉刷新的监听
            setOnRefreshListener { contractPresenter.loadContacts() }
        }
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ContactListAdapter(context, contractPresenter.contactListItems)
        }
        //加载联系人
        contractPresenter.loadContacts()
        //监听联系人变化
        EMClient.getInstance().contactManager()
            .setContactListener(object : EMContactListenerAdapter() {
                override fun onContactDeleted(userName: String?) {
                    //联系人被删除重新加载联系人数据
                    contractPresenter.loadContacts()
                }
            })
    }

    override fun onLoadContactsSuccess() {
        //隐藏刷新
        swipeRefreshLayout.isRefreshing = false
        //刷新数据
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onLoadContactsFail() {
        //隐藏刷新
        swipeRefreshLayout.isRefreshing = false
        //提示失败
        context?.toast(R.string.load_contacts_failed)
    }
}