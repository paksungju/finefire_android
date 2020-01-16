package com.finefire.finefire

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.finefire.finefire.fragment.AlarmFragment
import com.finefire.finefire.fragment.BBSFragment
import com.finefire.finefire.fragment.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var baseFragment: Fragment? = null
    var alramFragment = AlarmFragment()
    var bbsFragment = BBSFragment()
    var settingFragment = SettingFragment()
    var toolbar:Toolbar? = null
    var tv_title:TextView? = null
    val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

                transaction.hide(alramFragment)
                transaction.hide(bbsFragment)
                transaction.hide(settingFragment)
                when (item.itemId) {
                    R.id.alram -> baseFragment = alramFragment
                    R.id.bbs -> baseFragment = bbsFragment
                    R.id.settting -> baseFragment = settingFragment
                }
                transaction.show(baseFragment!!)
                transaction.commit()
                return true
            }
        }


    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, alramFragment)
        transaction.add(R.id.fragment_container, bbsFragment)
        transaction.add(R.id.fragment_container, settingFragment)
        transaction.hide(bbsFragment)
        transaction.hide(settingFragment)
        baseFragment = alramFragment
        transaction.commit()

        toolbar = findViewById<Toolbar>(R.id.toolbar)
        tv_title = toolbar?.findViewById<TextView>(R.id.tv_title)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.app_name, R.string.app_name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.drawerArrowDrawable.color = getColor(R.color.colorWhite)
        } else {
            toggle.drawerArrowDrawable.color = resources.getColor(R.color.colorWhite)
        }
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        toolbar?.setTitleTextColor(resources.getColor(R.color.colorWhite))
        toolbar?.title = "센서 목록"

        val nav_header= nav_view.getHeaderView(0)

        val ll_my = nav_header.findViewById<FrameLayout>(R.id.ll_my)
        val ll_terms = nav_header.findViewById<FrameLayout>(R.id.ll_terms)
        val ll_company = nav_header.findViewById<FrameLayout>(R.id.ll_company)
        val ll_bbs = nav_header.findViewById<FrameLayout>(R.id.ll_bbs)
        ll_my.setOnClickListener {

        }
        ll_terms.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.finefire.co.kr/bbs/content.php?co_id=e_provision")
            startActivity(intent)
        }
        ll_company.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.finefire.co.kr/bbs/content.php?co_id=c_about")
            startActivity(intent)
        }
        ll_bbs.setOnClickListener {
            navigation.selectedItemId = R.id.bbs
            drawer_layout.closeDrawers()
        }
    }
}