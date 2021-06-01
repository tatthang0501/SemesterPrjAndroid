package com.example.semesterprojectnguyentatthangb17dccn563.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.semesterprojectnguyentatthangb17dccn563.R;

public class AddRecevier extends BroadcastReceiver {
    final String CHANNEL_ID = "101";
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is channel 1");
            manager.createNotificationChannel(channel1);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.cashinhan)
                .setContentTitle("Thêm thành công").
                setContentText("Tài khoản " + intent.getStringExtra("email") +", số tiền: " + intent.getStringExtra("money") +", loại: "+ intent.getStringExtra("type")
                +", ngày: " + intent.getStringExtra("date") + ", mô tả: " + intent.getStringExtra("description"))
                .setColor(Color.RED).setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        manager.notify(123, builder.build());
    }
}
