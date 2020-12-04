package com.example.user.fcmsender.legacy;

import java.io.InputStream;
import java.util.Scanner;

public class Utils {

    public static String inputStreamToString(InputStream inputStream){
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }
}
