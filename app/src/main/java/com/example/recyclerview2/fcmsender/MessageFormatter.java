package com.example.recyclerview2.fcmsender;

public class MessageFormatter {
    private static String sampleMsgFormat = "{" +
            "  \"to\": \"/topics/%s\"," +
            "  \"notification\": {" +
            "       \"title\":\"%s\"," +
            "       \"body\":\"%s\"" +
            "   }" + "," +
            " \"data\": {"+
            "       \"title\":\"apple\","+
            "       \"body\":\"apple apple\""+
            "   }"+
            "}";

    public static String getSampleMessage(String topic, String title, String body){
        return String.format(sampleMsgFormat, topic, title, body);
    }
}
