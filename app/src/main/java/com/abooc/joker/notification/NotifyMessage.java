package com.abooc.joker.notification;

import android.support.annotation.Keep;

import com.google.gson.JsonObject;

@Keep
public class NotifyMessage {
    @Keep
    public String alert;
    @Keep
    public String title;
    @Keep
    public boolean silent;
    @Keep
    public Payload payload;

    @Keep
    class Payload {
        @Keep
        public String type;
        @Keep
        public JsonObject model;
    }
}
