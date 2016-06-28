package com.example.yaofa.client.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lemon_bar on 15/6/12.
 */
public class FormatCheckUtils {
    private static String PHONE_REGEX = "^1\\d{10}$";
    public static boolean isPhone(String phone){
        Pattern p = Pattern.compile(PHONE_REGEX);
        Matcher m = p.matcher(phone);
        return m.matches();
    }
}