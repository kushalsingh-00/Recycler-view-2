package com.example.user.fcmsender.legacy;

public class OnMsgSentListner {
    public interface OnMsgSentListener {
        void onSuccess(String response);

        void onFailure(String error);
    }
}
