package com.orderdeliveryandroidapp.services;

/**
 * Created by abhay on 14/7/16.
 */
public class Queries {
    public static  final String host="http://192.168.0.104:3000/";
    public enum Url {registerUser, otpCheck}

    public static String QueriesUrl(String type, String[] params)
    {
        Url url = Url.valueOf(type);
        switch (url)
        {
            case registerUser:
                return host+"api/users";
            case otpCheck:
                return null;
            default:
                return null;

        }

    }

}
