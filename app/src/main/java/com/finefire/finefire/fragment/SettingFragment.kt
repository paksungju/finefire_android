package com.finefire.finefire.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import com.finefire.finefire.util.StorageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.finefire.finefire.LoginActivity
import com.finefire.finefire.MainActivity
import com.finefire.finefire.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_setting.*


class SettingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ll_logout.setOnClickListener {

            StorageManager().setLoginToken(this.context!!, "")
            StorageManager().setID(this.context!!, "")
            StorageManager().setPW(this.context!!, "")
            val intent = Intent(this.context, LoginActivity::class.java)
            startActivity(intent)
            this.activity?.finish()
        }
        ll_version.setOnClickListener {
            var cm = this.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(ClipData.newPlainText("text", StorageManager().getToken(this.context!!)))
            Toast.makeText(context,"토큰이 복사되었습니다.",Toast.LENGTH_SHORT).show()
        }

        tv_version.text = getVersionInfo(this.context)
    }
    fun getVersionInfo(context: Context?): String? {
        var version = "Unknown"
        val packageInfo: PackageInfo
        if (context == null) {
            return version
        }
        try {
            packageInfo = context.applicationContext
                .packageManager
                .getPackageInfo(context.applicationContext.packageName, 0)
            version = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return version
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            (activity as MainActivity).tv_title?.text = "설정"
        }
    }
}