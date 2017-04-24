package com.abooc.joker.notification;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.abooc.util.Debug;
import com.google.gson.Gson;

/**
 * 通知跳转分发器
 */
public class NotifyActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        String action = intent.getAction();
        String channel = intent.getExtras().getString("com.avos.avoscloud.Channel");
        //获取消息内容
        String dataJson = intent.getExtras().getString("com.avos.avoscloud.Data");

        if (!TextUtils.isEmpty(channel) && "update".equals(channel)) {
            open("http://fir.im/fmapp");
            onBackPressed();
            return;
        }

        //非空判断
        if (TextUtils.isEmpty(dataJson)) {
            onBackPressed();
            return;
        }

        NotifyMessage notifyMessage = null;
        try {
            Gson gson = new Gson();
            notifyMessage = gson.fromJson(dataJson, NotifyMessage.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (isInvalid(notifyMessage)) {
            onBackPressed();
            return;
        }

        String type = dispatchMessage(notifyMessage);
        StringBuffer buffer = new StringBuffer();
        buffer.append("ACTION:").append(action).append("\n")
                .append("CHANNEL:").append(channel).append("\n");
        Debug.anchor("type:" + type + buffer.toString() + ", " + dataJson);
        onBackPressed();
    }

    boolean isInvalid(NotifyMessage notifyMessage) {
        if (notifyMessage == null
                || notifyMessage.payload == null) {
            return true;
        }
        return false;
    }


    String dispatchMessage(NotifyMessage message) {
        String type = message.payload.type;
        switch (type) {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "30":
                break;
            case "10":
                break;
        }
        return type;
    }

    void start(Class<? extends Activity> cls) {
        Bundle extras = getIntent().getExtras();

        Intent intent = new Intent(this, cls);
        intent.putExtras(extras);
        startActivity(intent);

    }

    void open(String url) {
        if (URLUtil.isValidUrl(url)) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}