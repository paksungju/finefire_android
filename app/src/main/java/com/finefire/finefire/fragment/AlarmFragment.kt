package com.finefire.finefire.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.finefire.finefire.BoardQnAActivity
import com.finefire.finefire.MainActivity
import com.finefire.finefire.ModemActivity
import com.finefire.finefire.R
import com.finefire.finefire.util.HttpManager
import com.google.android.gms.internal.measurement.zzwx.init
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import kotlinx.android.synthetic.main.fragment_alarm.*
import org.json.JSONArray
import java.util.*
import kotlin.concurrent.schedule

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

        load()

    }
    fun load(){
        HttpManager(context!!).get("api/modem"){ obj ->
            var list = JSONArray()
            val datas = obj.getJSONArray("data")
            for (i in 0 until datas.length()) {
                val data = datas.getJSONObject(i)
                val sensors = data.getJSONArray("sensors")

                for (j in 0 until sensors.length()) {
                    val sensor = sensors.getJSONObject(j)
                    data.put("image", Random().nextInt(3))
                }
                list.put(data)
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

        @RequiresApi(Build.VERSION_CODES.M)
        override fun getView(position:Int, convertView: View?, parent: ViewGroup?):View{
            // Inflate the custom view
            val inflater = parent?.context?.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.layout_alarm_item,null)

            val obj = this.list.getJSONObject(position)
            // Get the custom view widgets reference
            val ll_main = view.findViewById<LinearLayout>(R.id.ll_main)
            val tv_mdDisplayName = view.findViewById<TextView>(R.id.tv_mdDisplayName)
            val tv_mdName = view.findViewById<TextView>(R.id.tv_mdName)
            val tv_mdSpec = view.findViewById<TextView>(R.id.tv_mdSpec)
            val iv_image = view.findViewById<ImageView>(R.id.iv_image)

            tv_mdDisplayName.text = obj.getString("mdDisplayName")
            tv_mdName.text = obj.getString("mdName")
            tv_mdSpec.text = obj.getString("mdSpec")
            var image = 0
            if(obj.has("image")) {
                image = obj.getInt("image")
            }
            if(image == 0){
                iv_image.background = context?.getDrawable(R.drawable.ic_smile)
            }else if (image == 1){
                iv_image.background = context?.getDrawable(R.drawable.ic_sad)
                ll_main.background = context?.getDrawable(R.drawable.view_rounded_corner_green)
            }else if (image == 2){
                iv_image.background = context?.getDrawable(R.drawable.ic_sceptic)
                setEmergency(view, true)
            }
            ll_main.setOnClickListener{
                val activity  = parent.context as Activity

                val mdId = obj.getInt("mdId")

                var intent = Intent(activity, ModemActivity::class.java)
                intent.putExtra("mdId", mdId)
                activity.startActivity(intent)
            }

            return view
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun setEmergency(view: View, f:Boolean){
            val ll_main = view.findViewById<LinearLayout>(R.id.ll_main)
            val tv_mdDisplayName = view.findViewById<TextView>(R.id.tv_mdDisplayName)
            val tv_mdName = view.findViewById<TextView>(R.id.tv_mdName)
            val tv_mdSpec = view.findViewById<TextView>(R.id.tv_mdSpec)
            if(ll_main == null)
                return
            var textColor = 0
            var backColor: Drawable? = null
            if (f){
                textColor  = context?.getColor(R.color.colorBlack)!!
                backColor  = context?.getDrawable(R.drawable.view_rounded_corner_gray)
            } else{
                textColor  = context?.getColor(R.color.colorDarkWhite)!!
                backColor  = context?.getDrawable(R.drawable.view_rounded_corner_red)
            }

            tv_mdDisplayName.setTextColor(textColor)
            tv_mdName.setTextColor(textColor)
            tv_mdSpec.setTextColor(textColor)
            ll_main.background = backColor


            Timer("getData", false).schedule(500) {
                (context as MainActivity).runOnUiThread {
                    setEmergency(view, !f)
                }
            }
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
            (activity as MainActivity).tv_title?.text = "센서 목록"
        }
    }
}