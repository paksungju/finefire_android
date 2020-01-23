package com.finefire.finefire

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.finefire.finefire.util.HttpManager
import com.finefire.finefire.util.StorageManager
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d("","")

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val deviceToken = instanceIdResult.token
            Log.d("a", "token = " + deviceToken)
            StorageManager().setToken(this, deviceToken)
        }
        bt_login.setOnClickListener {

            val id = et_id.text.toString()
            val pw = et_pw.text.toString()
            OnLogin(id, pw)
        }
        bt_idpw.setOnClickListener {
            OnIDPW()
        }
        val id = StorageManager().getID(this)
        val pw = StorageManager().getPW(this)
        if( !(id?.isEmpty())!! && !(pw?.isEmpty())!!){
            OnLogin(id, pw)
        }
    }

    fun OnLogin(id:String, pw:String){

        HttpManager(this@LoginActivity).login(id,pw) {

            val access_token = StorageManager().getLoginToken(this@LoginActivity)
            if(!access_token?.isEmpty()!!) {
                StorageManager().setID(this, id)
                StorageManager().setPW(this, pw)
                goToMain()
            }else{
                val oDialog = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog)
                oDialog.setMessage("로그인정보가 잘못되었습니다.")
                    .setTitle("아이디 및 비밀번호를 확인해주십시오.")
                    .setPositiveButton("확인") { dialog, which -> }
                    .setCancelable(false).show()
            }
        }

    }
    fun OnIDPW(){

    }

    fun goToMain(){
        val access_token = StorageManager().getLoginToken(this@LoginActivity)
        if(!access_token?.isEmpty()!!){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
