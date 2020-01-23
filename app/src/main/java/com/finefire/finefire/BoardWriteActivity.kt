package com.finefire.finefire

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnPreDrawListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.finefire.finefire.util.HttpManager
import kotlinx.android.synthetic.main.activity_board_notice.*
import kotlinx.android.synthetic.main.activity_board_notice.bt_back
import kotlinx.android.synthetic.main.activity_board_notice.tv_board_date
import kotlinx.android.synthetic.main.activity_board_notice.tv_board_title
import kotlinx.android.synthetic.main.activity_board_notice.tv_name
import kotlinx.android.synthetic.main.activity_board_qna.*
import kotlinx.android.synthetic.main.activity_board_write.*
import kotlinx.android.synthetic.main.activity_login.*


class BoardWriteActivity : AppCompatActivity() {

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)
        bt_submit.setOnClickListener { submit() }
        bt_back.setOnClickListener { finish() }
    }
    fun submit(){


        if (et_title.text.toString().isEmpty() || et_content.text.toString().isEmpty() ) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("제목또는 내용을 모두 입력해주세요")
            builder.setPositiveButton("확인") { dialog, id -> }
            builder.create().show()
        }else {
            var param = mapOf(
                "wrSubject" to et_title.text.toString(),
                "wrContent" to et_content.text.toString(),
                "type" to ""
            )
            HttpManager(this).post(param, "api/board/cs/store") { obj ->
                finish()
            }
        }
    }
}