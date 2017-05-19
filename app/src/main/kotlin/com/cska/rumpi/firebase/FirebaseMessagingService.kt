package com.cska.rumpi.firebase

import android.app.PendingIntent
import android.support.v7.app.NotificationCompat
import android.util.Log
import com.cska.rumpi.R
import com.cska.rumpi.ui.news.NewsActivity
import com.cska.rumpi.ui.results.ResultActivity
import com.cska.rumpi.utils.getColorCompat
import com.cska.rumpi.utils.notificationManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.jetbrains.anko.intentFor

/**
 * Created by rumpi on 02.02.2017.
 */

class CskaFirebaseMessagingService : FirebaseMessagingService() {
    private val MESSAGE_NOTIFICATION_ID = 123

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("Dto", "From: " + remoteMessage?.data);

        // Check if message contains a data payload.
        if (remoteMessage?.data?.isNotEmpty() ?: false) {
            val notificationBuilder = NotificationCompat.Builder(baseContext)
                    .setSmallIcon(R.drawable.notification_logo)
                    .setContentTitle(remoteMessage?.data?.get("title"))
                    .setColor(baseContext.getColorCompat(R.color.colorPrimaryDark))
                    .setAutoCancel(true)
                    .setContentText(remoteMessage?.data?.get("message"))
            when (remoteMessage?.data?.get("category")) {
                "NEWS_CATEGORY" -> {
                    val id = remoteMessage?.data?.get("news_id")?.toLong()?: -1
                    val intent = this.intentFor<NewsActivity>(
                            NewsActivity.EXTRA_NEWS_ID to id)

                    val contentIntent = PendingIntent.getActivity(baseContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                    notificationBuilder.setContentIntent(contentIntent)
                    baseContext.notificationManager.notify(MESSAGE_NOTIFICATION_ID, notificationBuilder.build())
                }
                "RESULTS_CATEGORY" -> {
                    val id = remoteMessage?.data?.get("contest_id")?.toLong()?: -1
                    val event = remoteMessage?.data?.get("event_title") ?: ""
                    val contest = remoteMessage?.data?.get("contest_title") ?: ""

                    val intent = this.intentFor<ResultActivity>(
                            ResultActivity.EXTRA_CONTEST_ID to id,
                            ResultActivity.EXTRA_CONTEST_NAME to contest,
                            ResultActivity.EXTRA_EVENT_NAME to event)


                    val contentIntent = PendingIntent.getActivity(baseContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                    notificationBuilder.setContentIntent(contentIntent)
                    baseContext.notificationManager.notify(MESSAGE_NOTIFICATION_ID, notificationBuilder.build())
                }
            }
        }

    }
}
