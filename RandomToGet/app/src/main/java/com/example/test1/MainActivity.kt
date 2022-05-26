package com.example.test1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import retrofit2.Response
import java.util.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private lateinit var retService: WordService
    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView=findViewById(R.id.textview)
        retService = RetrofitInstance
            .getRetrofitInstance()
            .create(WordService::class.java)
        getRequest()
//        val button =findViewById<ImageView>(R.id.button).setOnClickListener {
//            getRequest()
//        }
//        val button =findViewById<ImageView>(R.id.button).setOnClickListener {
//            getRequest()
//        }
        button_change()
        //getRequestWithPathParameters()
        //uploadAlbum()
    }
    private fun getRequest() {
        val responseLiveData: LiveData<Response<Words>> = liveData {
            val response = retService.getWords()
            emit(response)
        }
        responseLiveData.observe(this, Observer {
            val words = it.body()
            val random= Random()
            val RandomIndex=random.nextInt(6)
            /**
            for (int i = 0; i < surveyListVO.getResult().size(); i++) {
            System.out.print(surveyListVO.getResult().get(i)
            .getSurveyId());  print: 12
            System.out.print(surveyListVO.getResult().get(i)
            .getSurveyName());   print: B///C
            System.out.print(surveyListVO.getMessage());   print: success
            }
             */
//            Log.e("moli", ""+words )
//            Log.e("moli", ""+words?.data?.get(0) )
//            Log.e("moli", ""+words?.data?.get(0)?.id )
//            Log.e("moli", ""+words?.data?.get(0)?.newsId)
//            Log.e("moli", ""+words?.msg)
            Log.e("moli", ""+words?.data?.get(RandomIndex)?.word )
            textView.setText(""+words?.data?.get(RandomIndex)?.word)
//            if (words != null) {
//
//            }
        })
    }
    private fun button_change(){
        button.setOnClickListener {
            textView.setText("")
            getRequest()
        }

    }
}