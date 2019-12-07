package kanielOutis.october.base

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.app.PendingIntent
import android.content.LocusId
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import kanielOutis.october.R
import kanielOutis.october.data.AppResponse
import kanielOutis.october.repository.MessagesRepo
import kanielOutis.october.ui.home.ChatActivity


class MyService : Service() {

    private val NOTIF_ID = 1
    private val NOTIF_CHANNEL_ID = "Channel_Id"

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        // do your jobs here

        startForeground()

        return super.onStartCommand(intent, flags, startId)
    }




    private fun startForeground() {
        val notificationIntent = Intent(this, ChatActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, 0
        )

        startForeground(
            NOTIF_ID, NotificationCompat.Builder(
                this,
                NOTIF_CHANNEL_ID
            ) // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_video)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build()
        )
    }
}