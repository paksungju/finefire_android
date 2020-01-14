package com.finefire.finefire.fragment

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.finefire.finefire.MainActivity
import com.finefire.finefire.R
import com.finefire.finefire.util.HttpManager
import com.google.android.gms.internal.measurement.zzwx.init
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import kotlinx.android.synthetic.main.fragment_alarm.*
import org.json.JSONArray

class AlarmFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alarm, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        gridview.numColumns = 2
        gridview.horizontalSpacing = 5
        gridview.verticalSpacing = 5
        gridview.stretchMode = GridView.STRETCH_COLUMN_WIDTH

        var list = JSONArray()
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")
//        list.put("")


        HttpManager(context!!).get("api/modem"){ obj ->
            val datas = obj.getJSONArray("data")
            for (i in 0 until datas.length()) {
                val data = datas.getJSONObject(i)
                val sensors = data.getJSONArray("sensors")
                val mdName = data.getString("mdName")
                val mdDisplayName = data.getString("mdDisplayName")
                for (j in 0 until sensors.length()) {
                    val sensor = sensors.getJSONObject(j)
                    sensor.put("mdName", mdName)
                    sensor.put("mdDisplayName", mdDisplayName)
                    list.put(sensor)
                }
            }
            val adapter = GridAdapter(list)
            gridview.adapter = adapter
        }
    }


    inner class GridAdapter() : BaseAdapter() {

        constructor(list:JSONArray) : this() {
            this.list = list
        }

        private var list:JSONArray = JSONArray()

        override fun getView(position:Int, convertView: View?, parent: ViewGroup?):View{
            // Inflate the custom view
            val inflater = parent?.context?.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.layout_alarm_item,null)

            val obj = this.list.getJSONObject(position)
            // Get the custom view widgets reference
            val ll_main = view.findViewById<LinearLayout>(R.id.ll_main)
            val tv_title = view.findViewById<TextView>(R.id.tv_title)
            val tv_temp = view.findViewById<TextView>(R.id.tv_temp)
            val tv_wetness = view.findViewById<TextView>(R.id.tv_wetness)

            tv_title.text = obj.getString("ssDisplayName")
            tv_temp.text = "${obj.getString("temperature_val")} ℃"
            tv_wetness.text = "${obj.getString("humidity_val")} %"
            ll_main.setOnClickListener{
                val activity  = parent.context as Activity

            }

            return view
        }

        override fun getItem(position: Int): Any? {
            return list[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }


        override fun getCount(): Int {
            return list.length()
        }

    }
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            (activity as MainActivity).toolbar.title = "센서 목록"
        }
    }
}