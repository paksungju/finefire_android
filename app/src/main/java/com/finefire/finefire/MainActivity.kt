package com.finefire.finefire

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
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

    }
}