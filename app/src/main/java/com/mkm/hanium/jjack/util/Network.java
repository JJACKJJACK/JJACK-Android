package com.mkm.hanium.jjack.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by 김민선 on 2017-06-02.
 *
 */

public class Network {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null;
    }
}