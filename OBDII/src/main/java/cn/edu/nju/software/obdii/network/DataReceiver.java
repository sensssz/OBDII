package cn.edu.nju.software.obdii.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.edu.nju.software.obdii.data.DataMap;
import cn.jpush.android.api.JPushInterface;

/**
 * Receive data from the server and pass them to UI
 */
public class DataReceiver extends BroadcastReceiver {
    private static final String TAG = "DataReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            if (bundle != null) {
                String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                if (message != null && message.contains(":")) {
                    Log.d(TAG, message);
                    String[] messageParts = message.split(":");
                    String dataType = messageParts[0];
                    String dataValue = messageParts[1];
                    DataMap.getInstance().onDataReceived(dataType, dataValue);
                }
            }
        }
    }
}
