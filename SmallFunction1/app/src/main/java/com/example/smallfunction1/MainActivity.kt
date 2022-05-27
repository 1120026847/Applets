package com.example.smallfunction1

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.ImageView
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.example.smallfunction1.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // declaring variables
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    private lateinit var imageView: ImageView
    private lateinit var cardView: CardView


    @SuppressLint("RemoteViewLayout")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageView = findViewById(R.id.imageView)
//https://img-blog.csdnimg.cn/1d81409db10a41ce8e033089bc4b93f5.png
      //  notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


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
                .setPositiveButton("神蛊温皇") { dialogInterface: DialogInterface, i: Int ->
                    Toast.makeText(this, "剑、蛊、毒三合一", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("秋水浮萍任缥缈") { dialogInterface: DialogInterface, i: Int ->
                    Toast.makeText(this, "我想陪伴你每一轮的365个日日夜夜", Toast.LENGTH_SHORT).show()

                }
                .setCancelable(false)
                .create()
                .show()

        }
        binding.notification.setOnClickListener {
            //https://www.geeksforgeeks.org/notifications-in-kotlin/
            createNotificationChannel()
            showNotification()

        }
        binding.zhendong.setOnClickListener {
            //v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS,HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            it.performHapticFeedback(
                HapticFeedbackConstants.LONG_PRESS,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
        }
        binding.jietu.setOnClickListener {
            cardView = findViewById<CardView>(R.id.card_View)
            // get the bitmap of the view using
            // getScreenShotFromView method it is
            // implemented below
            val bitmap = getScreenShotFromView(cardView)

            // if bitmap is not null then
            // save it to gallery
            if (bitmap != null) {
                saveMediaToStorage(bitmap)
            }


        }
    }

    private fun getScreenShotFromView(v: View): Bitmap? {
        // create a bitmap object
        var screenshot: Bitmap? = null
        try {
            // inflate screenshot object
            // with Bitmap.createBitmap it
            // requires three parameters
            // width and height of the view and
            // the background color
            screenshot =
                Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            // Now draw this bitmap on a canvas
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }
        // return the bitmap
        return screenshot
    }


    // this method saves the image to gallery
    private fun saveMediaToStorage(bitmap: Bitmap) {
        // Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        // Output stream
        var fos: OutputStream? = null

        // For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // getting the contentResolver
            this.contentResolver?.also { resolver ->

                // Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    // putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                // Inserting the contentValues to
                // contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                // Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            // These for devices running on android < Q
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            // Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this, "Captured View and saved to Gallery", Toast.LENGTH_SHORT).show()
        }
    }
    @SuppressLint("RemoteViewLayout")
    fun showNotification()
    {
       // val intent = Intent(this, afterNotification::class.java)

        // FLAG_UPDATE_CURRENT specifies that if a previous
        // PendingIntent already exists, then the current one
        // will update it with the latest intent
        // 0 is the request code, using it later with the
        // same method again will get back the same pending
        // intent for future reference
        // intent passed here is to our afterNotification class
    //    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // RemoteViews are used to use the content of
        // some different layout apart from the current activity layout
      //  val contentView = RemoteViews(packageName, R.layout.activity_after_notification)
        // CHANNEL_ID：通道ID，可在类 MainActivity 外自定义。如：val CHANNEL_ID = 'msg_1'
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("RNG赛程提醒")
            .setContentText("今天晚上19:00，RNG对阵IG")
//            .setContent(contentView)
//            .setContentIntent(pendingIntent)
            // 通知优先级，可以设置为int型，范围-2至2
            .setPriority(NotificationCompat.PRIORITY_MAX )
        // 显示通知
        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }
    private fun createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            // 提醒式通知(横幅显示)，不过大部分需要手动授权
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {description = descriptionText}
            // 注册通道(频道)
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}




