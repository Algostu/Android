package com.dum.dodam;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.dum.dodam.Alarm.Data.AlarmData;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyFireBaseMessagingService extends FirebaseMessagingService {
    String json;
    ArrayList<AlarmData> alarmDataArr;
    AlarmData alarmData;
    Random rnd = new Random();
    int notifyID = rnd.nextInt(100);
    String id = "my_channel_02";
    CharSequence name = "fcm_nt";
    String description = "push";
    int importance = NotificationManager.IMPORTANCE_LOW;
    MediaPlayer mediaPlayer;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("FCM Log", "token : " + s);
        SharedPreferences sharedPref = getSharedPreferences(
                "auto", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("fcmToken", s);
        editor.commit();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) { //포그라운드
            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
//            up_Nt(remoteMessage.getData().get("orderCnt1"), null, null);
        } else if (remoteMessage.getData().size() > 0) { //백그라운드
            sendNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"));
//            up_Nt(remoteMessage.getData().get("orderCnt1"), null, null);

        }
    }

    private void sendNotification(String messageBody, String messageTitle) {
        // Notification을 눌렀을때 실행될 Activity를 정한다.
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Gson gson = new Gson();
        SharedPreferences sharedPref = getSharedPreferences(
                "auto", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        // 저장되어있는 알람 불러오기
        json = sharedPref.getString("alarm", "");
        Type listType = new TypeToken<ArrayList<AlarmData>>(){}.getType();
        if (json.equals("")==true){
            alarmDataArr = new ArrayList<AlarmData>();
            Log.d("alarm debug", "no data stored");
        } else {
            Log.d("alarm debug", "data are stored");
            alarmDataArr = gson.fromJson(json, listType);
        }
        // 새로운 알람 추가 (맨 앞에)
        alarmData = gson.fromJson(messageBody, AlarmData.class);
        alarmData.title = messageTitle;
        alarmData.read = 1;
        alarmDataArr.add(0, alarmData);
        // 알람 저장 (나중에 동기화 문제 발생 가능)
        editor.putString("alarm", gson.toJson(alarmDataArr));
        editor.commit();
        intent.putExtra("notification", "1");
        intent.putExtra("notifyID", 2);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);

        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "my_channel_02";

        try {
            Notification notification = new Notification.Builder(MyFireBaseMessagingService.this)
                    .setContentTitle(URLDecoder.decode(messageTitle, "UTF-8"))
                    .setContentText(URLDecoder.decode(alarmData.content, "UTF-8"))
                    .setSmallIcon(R.drawable.ic_logo_white1)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .build();
            // 알람을 위한 것
//            mediaPlayer = MediaPlayer.create(this,R.raw.alarm);
//            mediaPlayer.start();

            mNotificationManager.notify(notifyID, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //////////////////////////// 포그라운드 및 백그라운드 푸시알림 처리 ////////////////////////////
    }
}