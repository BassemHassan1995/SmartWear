package com.gp.smart.wear;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by basse on 18-Feb-17.
 */
public class Utilities {

    public static boolean checkInternetConnection(Context context) {

        ConnectivityManager con_manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    public static String getBrand(char first_char) {
        switch (first_char) {
            case 'a':
                return "asus";
            case 'h':
                return "huawei";
            case 'l':
                return "lg";
            case 's':
                return "samsung";
            case 'i':
                return "apple";
            case 'm':
                return "motorola";

            default:
                return "";
        }
    }
}

