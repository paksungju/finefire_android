package com.finefire.finefire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
            Log.d("a", "token = " + deviceToken!!)
            StorageManager().setToken(this, deviceToken)
        }
        bt_login.setOnClickListener(){
            OnLogin()
        }
        bt_idpw.setOnClickListener(){
            OnIDPW()
        }
    }

    fun OnLogin(){

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun OnIDPW(){

    }
}
