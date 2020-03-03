package com.yhsh.xiayiyeim.app

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import cn.bmob.v3.Bmob
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMOptions
import com.hyphenate.chat.EMTextMessageBody
import com.yhsh.xiayiyeim.BuildConfig
import com.yhsh.xiayiyeim.R
import com.yhsh.xiayiyeim.adapter.EMMessageListenerAdapter
import com.yhsh.xiayiyeim.ui.activity.ChatActivity

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
 * 创建时间：2019/12/23 17:18
 * 个人小站：http://yhsh.wap.ai(已挂)
 * 最新小站：http://www.iyhsh.icoc.in
 * 联系作者：企鹅 13343401268
 * 博客地址：http://blog.csdn.net/xiayiye5
 * 项目名称：XiaYiYeIM
 * 文件包名：com.yhsh.xiayiyeim.app
 * 文件说明：
 */
class IMApplication : Application() {
    companion object {
        lateinit var instance: IMApplication
    }

    private lateinit var contentText: String

    //通过使用SoundPool播放简短音频文件
    private val soundPool = SoundPool(2, AudioManager.STREAM_MUSIC, 0)
    val duan by lazy { soundPool.load(instance, R.raw.duan, 0) }
    val yulu by lazy { soundPool.load(instance, R.raw.yulu, 0) }
    private val messageListener = object : EMMessageListenerAdapter() {
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            super.onMessageReceived(p0)
            if (isForeground()) {
                //在前台播放短声音
                soundPool.play(duan, 1f, 1f, 0, 0, 1f)
            } else {
                //在后台播放长声音
                soundPool.play(yulu, 1f, 1f, 0, 0, 1f)
                //通知栏显示消息
                showMessage(p0)
            }
        }
    }

    private fun showMessage(p0: MutableList<EMMessage>?) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        p0?.forEach {
            contentText = getString(R.string.no_text_message)
            if (it.type == EMMessage.Type.TXT) {
                contentText = (it.body as EMTextMessageBody).message
            }
            //点击通知跳转聊天页面的方法
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("userName", it.conversationId())
            val pendingIntent: PendingIntent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                //此方法是点击通知栏后进入聊天页面，然后点击后退按钮会回退到主页activity的方法，
                // 记得在ChatActivity的xml清单配置文件中配置parentActivityName此属性为要回退到哪个activitiy的方法
                val addNextIntent =
                    TaskStackBuilder.create(this).addParentStack(ChatActivity::class.java)
                        .addNextIntent(intent)
                pendingIntent = addNextIntent.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            } else {
                pendingIntent =
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            val notification: Notification
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //兼容Android8.0及以上API26的消息通知栏
                val mChannel =
                    NotificationChannel(
                        getString(R.string.receive_new_message),
                        contentText,
                        NotificationManager.IMPORTANCE_LOW
                    );
                notificationManager.createNotificationChannel(mChannel);
                notification = Notification.Builder(this).setContentIntent(pendingIntent)
                    .setChannelId("通知栏监听音乐")
                    .setContentTitle(getString(R.string.receive_new_message)).setOngoing(true)
                    .setWhen(System.currentTimeMillis()).setContentText(contentText)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher).build();
            } else {
                notification =
                    Notification.Builder(this)
                        .setContentTitle(getString(R.string.receive_new_message))
                        .setContentText(contentText)
                        .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.avatar1))
                        //不让滑动移除通知
                        .setOngoing(false)
                        //点击后自动消失的方法
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_contact).notification
            }
            notificationManager.notify(1, notification)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Bmob.initialize(applicationContext, "c9034ef5d3355801a54b23d877977607")
        //初始化环信配置
        EMClient.getInstance().init(applicationContext, EMOptions())
        EMClient.getInstance().setDebugMode(BuildConfig.DEBUG)
        EMClient.getInstance().chatManager().addMessageListener(messageListener)
    }

    /**
     * 判断此应用是否在前台运行
     */
    private fun isForeground(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (runningAppProcess in activityManager.runningAppProcesses) {
            if (runningAppProcess.processName == packageName) {
                return runningAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return false
    }
}