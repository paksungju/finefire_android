package com.finefire.finefire

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnPreDrawListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.finefire.finefire.util.HttpManager
import kotlinx.android.synthetic.main.activity_board.*


class BoardActivity : AppCompatActivity() {

    var type = 0
    var wrId  = 0
    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        var suburl = ""
        type = intent.getIntExtra("type", 0)
        if(type == 0){
            tv_title.text = "공지사항"
            suburl = "notice"
        }else{
            tv_title.text = "Q&A"
            suburl = "cs"
        }
        wrId = intent.getIntExtra("wrId", 0)


        HttpManager(this).get("api/board/${suburl}/detail/${wrId}"){ obj ->
            val board = obj.getJSONObject("board")
            val wrContent = board.getString("wrContent")
            wv_board_content.loadData(wrContent, "text/html", "UTF-8");
            val viewTreeObserver: ViewTreeObserver = wv_board_content.getViewTreeObserver()
            viewTreeObserver.addOnPreDrawListener(object : OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    val height: Int = wv_board_content.getMeasuredHeight()
                    if (height != 0) {
                        Toast.makeText(this@BoardActivity, "height:$height", Toast.LENGTH_SHORT).show()
                        wv_board_content.getViewTreeObserver().removeOnPreDrawListener(this)
                        wv_board_content.layoutParams.height = height*3
                    }
                    return false
                }
            })
            wv_board_content.loadData(wrContent, "text/html", "UTF-8");
        }

        bt_back.setOnClickListener{
            finish()
        }
    }
}