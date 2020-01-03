package com.finefire.finefire.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finefire.finefire.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.fragment_bbs.*
import kotlinx.android.synthetic.main.layout_bbs_item.view.*
import org.json.JSONArray
import org.json.JSONObject

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


        var list = JSONArray()
        var obj = JSONObject()
        obj.put("title", "제목1")
        obj.put("date", "2020-01-01")
        list.put(obj)
        obj.put("title", "제목2")
        obj.put("date", "2020-01-03")
        list.put(obj)
        obj.put("title", "제목3")
        obj.put("date", "2020-01-04")
        list.put(obj)
        obj.put("title", "제목4")
        obj.put("date", "2020-01-05")
        list.put(obj)

        rv_notice.adapter = Adapter(list)
        rv_notice.layoutManager = LinearLayoutManager(this.context)
        rv_notice.setHasFixedSize(true)

        obj.put("title", "제목6")
        obj.put("date", "2020-01-07")
        list.put(obj)
        obj.put("title", "제목7")
        obj.put("date", "2020-01-07")
        list.put(obj)
        rv_qna.adapter = Adapter(list)
        rv_qna.layoutManager = LinearLayoutManager(this.context)
        rv_qna.setHasFixedSize(true)

    }

    class Adapter() : RecyclerView.Adapter<Adapter.MainViewHolder>() {
        constructor(list: JSONArray) : this() {
            this.list = list
        }

        private var list: JSONArray = JSONArray()

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = MainViewHolder(parent)


        override fun getItemCount(): Int {
            return list.length()
        }

        override fun onBindViewHolder(holer: MainViewHolder, position: Int) {

            list[position].let { item ->
                with(holer) {
                    var i = item as? JSONObject
                    tv_title.text = i?.getString("title")
                    tv_date.text = i?.getString("date")
                }
            }
        }

        inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_bbs_item, parent, false)) {
            val tv_title = itemView.tv_title
            val tv_date = itemView.tv_date
        }
    }
}
