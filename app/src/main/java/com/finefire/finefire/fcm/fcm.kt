package com.finefire.finefire.fcm

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage



class fcm : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //화면이 보여지는 상태(포그라운드)에서 푸시가 올경우 이함수가 출력된다.
        Log.d("", "===============================")
        val acpt_no = remoteMessage.getData().get("acpt_no")
        val bz_procs_no = remoteMessage.getData().get("bz_procs_no")
        val title = remoteMessage.getData().get("title")
        val body = remoteMessage.getData().get("body")

        val nextIntent = Intent("push")
        nextIntent.putExtra("acpt_no", acpt_no)
        nextIntent.putExtra("bz_procs_no", bz_procs_no)
        nextIntent.putExtra("title", title)
        nextIntent.putExtra("body", body)
        //액션이 push인 액티비티(mainActivity)를 호출한다.
        //이후 mainActivity에서는 다이얼로그 팝업을 표시해줌
//        LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent)

    }
}