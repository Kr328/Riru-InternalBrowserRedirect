package com.github.kr328.ibr

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.kr328.ibr.remote.RemoteConnection

class FirstInstallActivity : Activity() {
    companion object {
        private const val FIRST_INSTALL_NOTIFICATION_CHANNEL = "first_install_notification_channel"
        private const val FIRST_INSTALL_NOTIFICATION_ID = 142456
    }

    override fun onStart() {
        super.onStart()

        createNotificationChannel()

        if ( RemoteConnection.currentStatus() == RemoteConnection.RCStatus.RUNNING ) {
            NotificationCompat.Builder(this, FIRST_INSTALL_NOTIFICATION_CHANNEL)
                    .setColor(getColor(R.color.colorAccent))
                    .setSmallIcon(R.drawable.ic_installed)
                    .setContentTitle(getString(R.string.first_install_notification_success_installed_title))
                    .setContentText(getString(R.string.first_install_notification_success_installed_message))
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(this, 0,
                            Intent(this, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }, PendingIntent.FLAG_UPDATE_CURRENT))
                    .build()
                    .also { NotificationManagerCompat.from(this).notify(FIRST_INSTALL_NOTIFICATION_ID, it)}
        }
        else {
            NotificationCompat.Builder(this, FIRST_INSTALL_NOTIFICATION_CHANNEL)
                    .setColor(getColor(R.color.colorAccent))
                    .setSmallIcon(R.drawable.ic_error)
                    .setContentTitle(getString(R.string.first_install_notification_failure_installed_title))
                    .setContentText(getString(R.string.first_install_notification_failure_installed_message))
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(this, 0,
                            Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                            PendingIntent.FLAG_UPDATE_CURRENT))
                    .build()
                    .also { NotificationManagerCompat.from(this).notify(FIRST_INSTALL_NOTIFICATION_ID, it)}
        }

        finish()
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat.from(this)
                    .createNotificationChannel(NotificationChannel(FIRST_INSTALL_NOTIFICATION_CHANNEL,
                            getString(R.string.first_install_notification_channel), NotificationManager.IMPORTANCE_DEFAULT))
        }
    }
}