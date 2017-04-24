package com.abooc.joker.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.abooc.util.Debug;
import com.google.gson.Gson;

@Deprecated
public class LCPushReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.baofeng.fengmi.NOTIFICATIONS";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION)) {

            String channel = intent.getExtras().getString("com.avos.avoscloud.Channel");
            String string = intent.getExtras().getString("com.avos.avoscloud.Data");
            if (TextUtils.isEmpty(string)) return;
            Debug.anchor("got action " + action + " on channel " + channel + "\ndata:" + string);

            Gson gson = new Gson();
            LCNotify lcNotify = gson.fromJson(string, LCNotify.class);


            showNotify(context, intent, lcNotify.title, lcNotify.alert);

        }
    }

    class LCNotify {
        String alert;
        String title;
        Payload payload;

        class Payload {
            String type;
            Model model;

            class Model {
                String id;
                String carname;
            }
        }
    }

    public static void showNotify(Context context, Intent intent, String title, String content) {
//        AVPushServiceAppManager manager = new AVPushServiceAppManager(context);
//        manager.addDefaultPushCallback("notify", NotifyActivity.class.getName());
//        manager.sendNotification("notify", content, intent);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("风迷频道")
                        .setContentText(content)
                        .setAutoCancel(true);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, NotifyActivity.class);
        resultIntent.putExtras(intent);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(JokerNotificationActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    void samples(Context context) {
        Intent intent = new Intent();
        intent.putExtra("com.avos.avoscloud.Data", "{\"_expiration_time\":\"2017-04-19T02:57:01.744Z\",\"_channel\":true,\"alert\":\"\\u7b80\\u7b80\\u5355\\u5355\\u6b63\\u5728\\u76f4\\u64ad\\u54e6\\uff0c\\u5feb\\u53bb\\u64a9\\u4e00\\u4e0b\",\"action\":\"com.baofeng.fengmi.NOTIFICATIONS\",\"title\":\"\",\"silent\":false,\"payload\":{\"type\":\"2\",\"model\":{\"id\":\"182\",\"carname\":\"\\u7b80\\u7b80\\u5355\\u5355\"}}}\n");
        LCPushReceiver.showNotify(context, intent, "测试", "测试");
    }
}