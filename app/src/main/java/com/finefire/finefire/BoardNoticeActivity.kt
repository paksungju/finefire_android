package com.finefire.finefire

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnPreDrawListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.finefire.finefire.util.HttpManager
import kotlinx.android.synthetic.main.activity_board_notice.*
import kotlinx.android.synthetic.main.activity_board_notice.bt_back
import kotlinx.android.synthetic.main.activity_board_notice.tv_board_date
import kotlinx.android.synthetic.main.activity_board_notice.tv_board_title
import kotlinx.android.synthetic.main.activity_board_notice.tv_name
import kotlinx.android.synthetic.main.activity_board_qna.*


class BoardNoticeActivity : AppCompatActivity() {

    var type = 0
    var wrId  = 0
    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_notice)

        wrId = intent.getIntExtra("wrId", 0)


        HttpManager(this).get("api/board/notice/detail/${wrId}"){ obj ->

            val board = obj.getJSONObject("board")
            val wrContent = board.getString("wrContent")
            val wrName = board.getString("wrName")
            val wrSubject = board.getString("wrSubject")
            val wrDatetime = board.getString("wrDatetime")
            wv_board_content.loadData(wrContent, "text/html", "UTF-8")
            tv_board_title.text = wrSubject
            tv_name.text = wrName
            tv_board_date.text = wrDatetime


            wv_board_content.setBackgroundColor(getColor(R.color.colorAlpha))
        }

        bt_back.setOnClickListener{
            finish()
        }
    }
}