package com.finefire.finefire

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_board.*

class BoardActivity : AppCompatActivity() {

    var type = 0
    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        type = intent.getIntExtra("type", 0)
        if(type == 0){
            tv_title.text = "공지사항"
        }else{
            tv_title.text = "Q&A"
        }
        bt_back.setOnClickListener{
            finish()
        }
    }
}