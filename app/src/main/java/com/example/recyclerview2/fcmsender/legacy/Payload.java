package com.example.recyclerview2.fcmsender.legacy;

public class Payload {
    public OnMsgSentListner.OnMsgSentListener onMsgSentListener;
    public String message;

    public Payload(String message, OnMsgSentListner.OnMsgSentListener onMsgSentListener) {
        this.message = message;
        this.onMsgSentListener = onMsgSentListener;
    }
}
