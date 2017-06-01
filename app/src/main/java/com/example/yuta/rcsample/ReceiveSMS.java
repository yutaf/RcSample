package com.example.yuta.rcsample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class ReceiveSMS extends BroadcastReceiver {
    private static final String TAG = "ReceiveSMS";

    // This method is called when the device receives SMS
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "handling message");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            final SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            if (messages.length > -1) {
                String info = messages[0].getMessageBody();

                if( info != null) {
                    String code = null;
                    try {
                        code = info.substring(13, 18).trim();
                        Log.i(TAG, "pin: " + code);
                    } catch (Exception e) {
                        Log.e(TAG, "Error getting SMS code.");
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
