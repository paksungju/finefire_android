package com.finefire.finefire

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finefire.finefire.util.HttpManager
import kotlinx.android.synthetic.main.activity_board_notice.bt_back
import kotlinx.android.synthetic.main.activity_modem.*
import kotlinx.android.synthetic.main.layout_modem_item.view.*
import kotlinx.android.synthetic.main.layout_qna_comment.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class ModemActivity : AppCompatActivity() {

    var mdId = 0
    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modem)

        mdId  = intent.getIntExtra("mdId", -1)
        bt_back.setOnClickListener{
            finish()
        }
        load()
    }
     fun load(){

         HttpManager(this).get("api/modem/${mdId}"){ obj ->
             var data = obj.getJSONObject("data")
             tv_mdDisplayName.text = data.getString("mdDisplayName")
             tv_mdName.text = data.getString("mdName")
             tv_mdSpec.text = data.getString("mdSpec")

             var image = Random().nextInt(3)
             if(image == 0){
                 iv_image.background = getDrawable(R.drawable.ic_smile)
             }else if (image == 1){
                 iv_image.background = getDrawable(R.drawable.ic_sad)
             }else if (image == 2){
                 iv_image.background = getDrawable(R.drawable.ic_sceptic)
             }
             var sensors = data.getJSONArray("sensors")
             var adapter = Adapter(sensors)
             rv.layoutManager = LinearLayoutManager(this)
             rv.adapter = adapter
         }

     }

    class Adapter(var list: JSONArray) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val convertView: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_modem_item, parent, false)
            return ViewHolder(convertView)
        }

        override fun getItemCount(): Int {
            return list.length()
        }

        override fun onBindViewHolder(holer: ViewHolder, position: Int) {

            list[position].let { item ->
                with(holer) {
                    var i = item as? JSONObject
                    itemView.tv_ssName.text = i?.getString("ssName")
                    itemView.tv_ssDisplayName.text = i?.getString("ssDisplayName")
                    itemView.tv_ssSpec.text = i?.getString("ssSpec")
                }
            }

        }
        inner class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
            init {
                convertView.setOnClickListener{
                }
            }
        }
    }
}