package com.example.user.fcmsender.legacy;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.user.fcmsender.legacy.Utils.inputStreamToString;

public class FCMSender {
    private static String url = "https://fcm.googleapis.com/fcm/send";
    private static final String KEY_STRING = "key=YOUR_KEY_HERE";

    public void send(Payload payload){
        new SendAsync().execute(payload);
    }

    static class SendAsync extends AsyncTask<Payload, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Payload... payloads) {
            Payload payload = payloads[0];
            try {
                URL url = new URL(FCMSender.url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestProperty("Authorization", KEY_STRING);
                httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");

                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod( "POST" );

                DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                outputStream.writeBytes(payload.message);
                outputStream.flush();
                outputStream.close();

                int responseCode = httpURLConnection.getResponseCode();
                String response;
                if (responseCode == 200) {
                    response = inputStreamToString(httpURLConnection.getInputStream());
                    payload.onMsgSentListener.onSuccess("SUCCESS: " + response);
                } else {
                    response = inputStreamToString(httpURLConnection.getErrorStream());
                    payload.onMsgSentListener.onFailure("ERROR: " + response);
                }
                return null;

            } catch (IOException e) {
                e.printStackTrace();
                payload.onMsgSentListener.onFailure("ERROR: " + e.toString());
                return null;
            }
        }
    }
}
