package com.abooc.joker.notification;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.abooc.util.Debug;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by dayu on 2017/2/24.
 */

public class LeanCloud {


//    public static User getLCUser() {
//        AccountManager accountManager = AccountManager.getInstance();
//        UserBean userBean = accountManager.getLoginUser();
//        if (userBean == null) {
//            User user = new User();
//            user.uid = getGuestId();
//            return user;
//        } else {
//            return GsonUtils.modelA2B(userBean, User.class);
//        }
//    }

    /**
     * @return 返回用于LeanCloud平台的唯一身份，优先已登录账号的Uid。
     */
//    public static String getClientId() {
//        AccountManager accountManager = AccountManager.getInstance();
//        UserBean user = accountManager.getLoginUser();
//        if (user == null) {
//            return getGuestId();
//        } else {
//            return user.uid;
//        }
//    }

    /**
     * 访客分配ID
     *
     * @return
     */
    public static String getGuestId() {
        String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
        return "tour-android-" + installationId;
    }

    /**
     * 访客模式（未登录）
     * LeanCloud实时通信登录访客账号
     */
    public static void openGuest() {
        String clientId = getGuestId();
        AVIMClient avimClient = AVIMClient.getInstance(clientId);
        avimClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    String uid = AVInstallation.getCurrentInstallation().getString("uid");
                    Debug.anchor("【游客登录】成功, uid:" + uid);
                } else {
                    Debug.error("【游客登录】失败:" + e.getMessage());
                }
            }
        });
    }


    /**
     * For Push
     * 重置推送消息的LeanCloud账号（登录账号/访客）
     */
    public static void resetPushInstallation() {
        final String clientId = getGuestId();
        AVInstallation.getCurrentInstallation().put("uid", clientId);
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 保存成功
                    Debug.anchor("【关联推送账户】成功");
                    // 关联  installationId 到用户表等操作……
                } else {
                    // 保存失败，输出错误信息
                    Debug.error("【关联推送账户】失败:" + e.getMessage());
                }
            }
        });
    }

    public static void subscribePush(Context context) {
        // 显示的设备的 installationId，用于推送的设备标示
        context = context.getApplicationContext();
        String packName = context.getPackageName();

        AVInstallation avInstallation = AVInstallation.getCurrentInstallation();
        avInstallation.put("platform", "android");
        avInstallation.put("packageName", packName);

        JSONArray channels = avInstallation.getJSONArray("channels");
        if (channels != null && channels.length() > 0) {
            for (int i = 0; i < channels.length(); i++) {
                try {
                    String channel = channels.getString(i);
                    PushService.unsubscribe(context, channel);
                } catch (JSONException e) {
                    Debug.error(e);
                }
            }
        }

        String versionName = getVersionName(context);

        PushService.setDefaultPushCallback(context, NotifyActivity.class);
        PushService.subscribe(context, "version-" + versionName, NotifyActivity.class);
        PushService.subscribe(context, BuildConfig.DEBUG ? "dev" : "release", NotifyActivity.class);
        PushService.subscribe(context, "update", NotifyActivity.class);

        avInstallation.saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 保存成功
                    Debug.anchor("【订阅推送】成功");
                    // 关联  installationId 到用户表等操作……
                } else {
                    // 保存失败，输出错误信息
                    Debug.error("【订阅推送】失败:" + e.getMessage());
                }
            }
        });

    }


    public static String getVersionName(Context context) {
        PackageManager packagemanager = context.getPackageManager();
        String packName = context.getPackageName();
        try {
            PackageInfo packageinfo = packagemanager.getPackageInfo(packName, 0);
            return packageinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
