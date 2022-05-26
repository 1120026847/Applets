package com.example.test1

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.ImageView
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.example.test1.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    // declaring variables
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    private lateinit var imageView: ImageView
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageView= findViewById(R.id.imageView)
//https://img-blog.csdnimg.cn/1d81409db10a41ce8e033089bc4b93f5.png
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        binding.imageload.setOnClickListener {
            val url = "https://img-blog.csdnimg.cn/abebfc1ef2884cd9b1230594646b87fa.png"
            Glide.with(imageView)
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.huaji)
                .error(R.drawable.huaji_cos)
                .into(imageView)
        }
        binding.snackbar.setOnClickListener {
/*
Snackbar.make(coordinatorLayout,"这是massage", Snackbar.LENGTH_LONG).setAction("这是action", new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(MainActivity.this,"你点击了action",Toast.LENGTH_SHORT).show();
     }
 }).show();

 */
            val snackBar = Snackbar.make(
                it, "test",
                Snackbar.LENGTH_LONG
            ).setAction("Action", View.OnClickListener {
                Toast.makeText(this, "点击snackbar后的提示", Toast.LENGTH_SHORT).show()
            })
            snackBar.show()
        }
        binding.dialog.setOnClickListener {
            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("还珠楼主")
                .setMessage("飘渺峰还珠楼")
                .setPositiveButton("神蛊温皇"){ dialogInterface: DialogInterface, i: Int ->
                    Toast.makeText(this, "剑、蛊、毒三合一", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("秋水浮萍任缥缈"){ dialogInterface: DialogInterface, i: Int ->
                    Toast.makeText(this, "我想陪伴你每一轮的365个日日夜夜", Toast.LENGTH_SHORT).show()

                }
                .setCancelable(false)
                .create()
                .show()

        }
        binding.notification.setOnClickListener {
            //https://www.geeksforgeeks.org/notifications-in-kotlin/
//            showNotification()
//            createNotificationChannel()
            // it is a class to notify the user of events that happen.
            // This is how you tell the user that something has happened in the
            // background.


                // pendingIntent is an intent for future use i.e after
                // the notification is clicked, this intent will come into action
                val intent = Intent(this, afterNotification::class.java)

                // FLAG_UPDATE_CURRENT specifies that if a previous
                // PendingIntent already exists, then the current one
                // will update it with the latest intent
                // 0 is the request code, using it later with the
                // same method again will get back the same pending
                // intent for future reference
                // intent passed here is to our afterNotification class
                val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

                // RemoteViews are used to use the content of
                // some different layout apart from the current activity layout
                val contentView = RemoteViews(packageName, R.layout.activity_after_notification)

                // checking if android version is greater than oreo(API 26) or not
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.GREEN
                    notificationChannel.enableVibration(false)
                    notificationManager.createNotificationChannel(notificationChannel)

                    builder = Notification.Builder(this, channelId)
                        .setContent(contentView)
                        .setSmallIcon(R.drawable.huaji)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.huaji_cos))
                        .setContentIntent(pendingIntent)
                } else {

                    builder = Notification.Builder(this)
                        .setContent(contentView)
                        .setSmallIcon(R.drawable.huaji)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.huaji_cos))
                        .setContentIntent(pendingIntent)
                }
                notificationManager.notify(1234, builder.build())
        }
        binding.zhendong.setOnClickListener {
            //v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS,HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            it.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS,HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
        }
        binding.jietu.setOnClickListener {
// 待做


        }
    }
//    fun showNotification()
//    {
//        // CHANNEL_ID：通道ID，可在类 MainActivity 外自定义。如：val CHANNEL_ID = 'msg_1'
//        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContentTitle("RNG赛程提醒")
//            .setContentText("今天晚上19:00，RNG对阵IG")
//            // 通知优先级，可以设置为int型，范围-2至2
//            .setPriority(NotificationCompat.PRIORITY_MAX )
//        // 显示通知
//        with(NotificationManagerCompat.from(this)) {
//            notify(1, builder.build())
//        }
//    }
//    private fun createNotificationChannel()
//    {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = getString(R.string.channel_name)
//            val descriptionText = getString(R.string.channel_description)
//            // 提醒式通知(横幅显示)，不过大部分需要手动授权
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {description = descriptionText}
//            // 注册通道(频道)
//            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
}