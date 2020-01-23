package com.finefire.finefire

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnPreDrawListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.finefire.finefire.util.HttpManager
import com.finefire.finefire.util.StorageManager
import kotlinx.android.synthetic.main.activity_board_qna.*
import kotlinx.android.synthetic.main.activity_board_qna.bt_back
import kotlinx.android.synthetic.main.activity_board_write.*
import java.util.*


class BoardQnAActivity : AppCompatActivity() {

    var type = 0
    var wrId  = 0
    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_qna)

        wrId = intent.getIntExtra("wrId", 0)

        bt_comment.setOnClickListener {

            if (et_comment.text.toString().isEmpty()){
                val builder= AlertDialog.Builder(this)
                builder.setMessage("코멘트를 입력해주세요.")
                builder.setPositiveButton("확인") { dialog, id -> }
                builder.create().show()
            }else {
                var param = mapOf(
                    "co_wrid" to "${wrId}",
                    "co_content" to et_comment.text.toString(),
                    "comment_id" to ""
                )
                HttpManager(this).post(param, "api/board/comment") { obj ->
                    load()
                    et_comment.setText("")
                }
            }
        }
        load()

    }

    fun load(){
        val us_id = StorageManager().getUserId(this)
        HttpManager(this).get("api/board/cs/detail/${wrId}"){ obj ->
            val board = obj.getJSONObject("board")
            val wrContent = board.getString("wrContent")
            val wrName = board.getString("wrName")
            val wrSubject = board.getString("wrSubject")
            val wrDatetime = board.getString("wrDatetime")
//            val usId = board.getInt("usId")
            tv_board_content.text = wrContent
            tv_board_title.text = wrSubject
            tv_name.text = wrName
            tv_board_date.text = wrDatetime

//            if ( usId == us_id){
//                bt_delete.visibility = View.VISIBLE
//            }else{
//                bt_delete.visibility = View.GONE
//            }
            val commentList = obj.getJSONArray("commentList")

            ll_comment.removeAllViews()
            for (i in 0 until commentList.length()) {
                val comment= commentList.getJSONObject(i)
                val coDatetime = comment.getString("coDatetime")
                val wrName = comment.getString("wrName")
                val coContent = comment.getString("coContent")
                val usId = comment.getInt("usId")

                val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(R.layout.layout_qna_comment,null)
                val tv_content = view.findViewById<TextView>(R.id.tv_content)
                val tv_name = view.findViewById<TextView>(R.id.tv_name)
                val tv_date = view.findViewById<TextView>(R.id.tv_date)
                val bt_delete= view.findViewById<Button>(R.id.bt_delete)

                tv_content.text = coContent
                tv_name.text = wrName
                tv_date.text = coDatetime
                if ( usId == us_id){
                    bt_delete.visibility = View.VISIBLE
                }else{
                    bt_delete.visibility = View.GONE
                }
                ll_comment.addView(view)
            }
        }

        bt_back.setOnClickListener{
            finish()
        }
    }
}