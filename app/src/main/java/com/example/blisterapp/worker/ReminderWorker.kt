package com.example.blisterapp.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.blisterapp.R

class ReminderWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val CHANNEL_ID = "blister_reminder_channel"
        const val NOTIF_ID = 1001
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        // Crea el canal (seguro llamar repetidamente)
        createChannel()

        // Asegúrate de tener un drawable ic_notification en res/drawable o usa un icono del sistema
        val smallIcon = try {
            R.drawable.ic_notification
        } catch (e: Exception) {
            // Si no tienes ic_notification en tu recurso, usa el icono por defecto de la app
            // o un icono del sistema (android.R.drawable.ic_dialog_info)
            android.R.drawable.ic_dialog_info
        }

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(smallIcon)
            .setContentTitle("Recordatorio Blister")
            .setContentText("Es hora de tomar tu pastilla")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        NotificationManagerCompat.from(applicationContext).notify(NOTIF_ID, builder.build())

        return Result.success()
    }

    private fun createChannel() {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Recordatorios Blister",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Canal para recordatorios de pastillas"
        }
        manager.createNotificationChannel(channel)
    }
}