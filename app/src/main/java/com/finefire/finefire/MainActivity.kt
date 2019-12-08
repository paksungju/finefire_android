package com.finefire.finefire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.finefire.finefire.util.StorageManager
import com.google.firebase.iid.FirebaseInstanceId

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("","")

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val deviceToken = instanceIdResult.token
            Log.d("a", "token = " + deviceToken!!)
            StorageManager().setToken(this, deviceToken)
        }
    }
}
