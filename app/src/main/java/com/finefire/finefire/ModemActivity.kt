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

            var lists = JSONArray()
            var danger_all = 0
            var sensors = data.getJSONArray("sensors")
            for( i in 0 until sensors.length()){
                var danger = 0
                var sensor = sensors.getJSONObject(i)
                var temperature_type = sensor.getString("temperature_type")
                if(temperature_type.equals("W", ignoreCase = true) && danger < 1){
                 danger = 1
                }else if(temperature_type.equals("D", ignoreCase = true) && danger < 2){
                 danger = 2
                }
                var gas_type = sensor.getString("gas_type")
                if(gas_type.equals("W", ignoreCase = true) && danger < 1){
                 danger = 1
                }else if(gas_type.equals("D", ignoreCase = true) && danger < 2){
                 danger = 2
                }
                var smoke_type = sensor.getString("smoke_type")
                if(smoke_type.equals("W", ignoreCase = true) && danger < 1){
                 danger = 1
                }else if(smoke_type.equals("D", ignoreCase = true) && danger < 2){
                 danger = 2
                }
                var humidity_type = sensor.getString("humidity_type")
                if(humidity_type.equals("W", ignoreCase = true) && danger < 1){
                 danger = 1
                }else if(humidity_type.equals("D", ignoreCase = true) && danger < 2){
                 danger = 2
                }
                var flame_type = sensor.getString("flame_type")
                if(flame_type.equals("W", ignoreCase = true) && danger < 1){
                 danger = 1
                }else if(flame_type.equals("D", ignoreCase = true) && danger < 2){
                 danger = 2
                }

                if(danger_all < danger){
                    danger_all = danger
                }
                sensor.put("danger", danger)
                lists.put(sensor)
            }

            if(danger_all == 0){
                 iv_image.background = getDrawable(R.drawable.ic_smile)
                 tv_status.text = "안정"
            }else if (danger_all == 1){
                 iv_image.background = getDrawable(R.drawable.ic_sad)
                 tv_status.text = "경고"
            }else if (danger_all == 2){
                 iv_image.background = getDrawable(R.drawable.ic_sceptic)
                 tv_status.text = "위험"
            }
            var adapter = Adapter(lists)
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

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onBindViewHolder(holer: ViewHolder, position: Int) {

            list[position].let { item ->
                with(holer) {
                    var i = item as? JSONObject
                    itemView.tv_ssName.text = i?.getString("ssName")
                    itemView.tv_ssDisplayName.text = i?.getString("ssDisplayName")
                    itemView.tv_ssSpec.text = i?.getString("ssSpec")

                    var danger= i?.getInt("danger")

                    if(danger == 0){
                        itemView.iv_image.background = itemView.context?.getDrawable(R.drawable.ic_smile)
                    }else if (danger == 1){
                        itemView.iv_image.background = itemView.context?.getDrawable(R.drawable.ic_sad)
                    }else if (danger == 2){
                        itemView.iv_image.background = itemView.context?.getDrawable(R.drawable.ic_sceptic)
                    }
                    var temperature_val = i?.getInt("temperature_val")
                    var gas_val = i?.getInt("gas_val")
                    var smoke_val = i?.getInt("smoke_val")
                    var humidity_val = i?.getInt("humidity_val")
                    var flame_val = i?.getInt("flame_val")

                    if(temperature_val!! > 0){
                        itemView.tv_temperature_val.visibility = View.VISIBLE
                        itemView.tv_temperature_val.text = "온도 : ${temperature_val}°C"
                        if(danger == 0){
                            itemView.tv_temperature_val.setTextColor(itemView.context.getColor(R.color.colorDarkGray))
                        }else if(danger == 1){
                            itemView.tv_temperature_val.setTextColor(itemView.context.getColor(R.color.colorWarning))
                        }else if(danger == 2){
                            itemView.tv_temperature_val.setTextColor(itemView.context.getColor(R.color.colorRed))
                        }
                    }else{
                        itemView.tv_temperature_val.visibility = View.GONE
                    }
                    if(gas_val!! > 0){
                        itemView.tv_gas_val.visibility = View.VISIBLE
                        itemView.tv_gas_val.text = "가스 : ${gas_val}"
                        if(danger == 0){
                            itemView.tv_gas_val.setTextColor(itemView.context.getColor(R.color.colorDarkGray))
                        }else if(danger == 1){
                            itemView.tv_gas_val.setTextColor(itemView.context.getColor(R.color.colorWarning))
                        }else if(danger == 2){
                            itemView.tv_gas_val.setTextColor(itemView.context.getColor(R.color.colorRed))
                        }
                    }else{
                        itemView.tv_gas_val.visibility = View.GONE
                    }
                    if(smoke_val!! > 0){
                        itemView.tv_smoke_val.visibility = View.VISIBLE
                        itemView.tv_smoke_val.text = "연기 : ${smoke_val}"
                        if(danger == 0){
                            itemView.tv_smoke_val.setTextColor(itemView.context.getColor(R.color.colorDarkGray))
                        }else if(danger == 1){
                            itemView.tv_smoke_val.setTextColor(itemView.context.getColor(R.color.colorWarning))
                        }else if(danger == 2){
                            itemView.tv_smoke_val.setTextColor(itemView.context.getColor(R.color.colorRed))
                        }
                    }else{
                        itemView.tv_smoke_val.visibility = View.GONE
                    }
                    if(humidity_val!! > 0){
                        itemView.tv_humidity_val.visibility = View.VISIBLE
                        itemView.tv_humidity_val.text = "습도 : ${humidity_val}%"
                        if(danger == 0){
                            itemView.tv_humidity_val.setTextColor(itemView.context.getColor(R.color.colorDarkGray))
                        }else if(danger == 1){
                            itemView.tv_humidity_val.setTextColor(itemView.context.getColor(R.color.colorWarning))
                        }else if(danger == 2){
                            itemView.tv_humidity_val.setTextColor(itemView.context.getColor(R.color.colorRed))
                        }
                    }else{
                        itemView.tv_humidity_val.visibility = View.GONE
                    }
                    if(flame_val!! > 0){
                        itemView.tv_flame_val.visibility = View.VISIBLE
                        itemView.tv_flame_val.text = "불꽃 : ${flame_val}"
                        if(danger == 0){
                            itemView.tv_flame_val.setTextColor(itemView.context.getColor(R.color.colorDarkGray))
                        }else if(danger == 1){
                            itemView.tv_flame_val.setTextColor(itemView.context.getColor(R.color.colorWarning))
                        }else if(danger == 2){
                            itemView.tv_flame_val.setTextColor(itemView.context.getColor(R.color.colorRed))
                        }
                    }else{
                        itemView.tv_flame_val.visibility = View.GONE
                    }
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