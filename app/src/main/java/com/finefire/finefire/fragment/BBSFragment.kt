package com.finefire.finefire.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finefire.finefire.BoardActivity
import com.finefire.finefire.MainActivity
import com.finefire.finefire.R
import com.finefire.finefire.util.HttpManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_alarm.*
import kotlinx.android.synthetic.main.fragment_bbs.*
import kotlinx.android.synthetic.main.layout_bbs_item.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class BBSFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bbs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ll_notice.visibility = View.VISIBLE
        ll_qna.visibility = View.GONE

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab) {
                if(tab.position == 0){
                    ll_notice.visibility = View.VISIBLE
                    ll_qna.visibility = View.GONE
                }else if (tab.position == 1){
                    ll_notice.visibility = View.GONE
                    ll_qna.visibility = View.VISIBLE
                }
            }
        })


        HttpManager(context!!).get("api/board/notice"){ obj ->
            var list = JSONArray()
            val datas = obj.getJSONArray("boardList")
            for (i in 0 until datas.length()) {
                val data = datas.getJSONObject(i)

                list.put(data)
            }
            rv_notice.adapter = Adapter(list, 0)
            rv_notice.layoutManager = LinearLayoutManager(this.context)
            rv_notice.setHasFixedSize(true)
        }

        HttpManager(context!!).get("api/board/cs"){ obj ->
            var list = JSONArray()
            val datas = obj.getJSONArray("boardList")
            for (i in 0 until datas.length()) {
                val data = datas.getJSONObject(i)

                list.put(data)
            }
            rv_qna.adapter = Adapter(list, 1)
            rv_qna.layoutManager = LinearLayoutManager(this.context)
            rv_qna.setHasFixedSize(true)
        }

    }

    class Adapter(var list: JSONArray, var type: Int) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val convertView: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_bbs_item, parent, false)
            return ViewHolder(convertView)
        }

        override fun getItemCount(): Int {
            return list.length()
        }

        override fun onBindViewHolder(holer: ViewHolder, position: Int) {

            list[position].let { item ->
                with(holer) {
                    var i = item as? JSONObject
                    itemView.tv_title.text = i?.getString("wrSubject")
                    itemView.tv_date.text = i?.getString("wrDatetime")
                    itemView.tv_name.text = i?.getString("wrName")
                }
            }

        }
        inner class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
            init {
                convertView.setOnClickListener{
                    val intent = Intent(convertView.context, BoardActivity::class.java)
                    var i = list[layoutPosition] as? JSONObject
                    var wrId = i?.getInt("wrId")

                    intent.putExtra("type", type)
                    intent.putExtra("wrId", wrId)
                    convertView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            (activity as MainActivity).tv_title?.text = "게시판"
        }
    }
}
