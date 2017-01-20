package com.example.myhistorycleaner.Receiver;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.myhistorycleaner.Activity.DisplaySmsActivity;
import com.example.myhistorycleaner.Model.SmsModel;

import java.util.Date;

import static com.example.myhistorycleaner.Utils.Constants.SMS_BUNDLE;

/**
 * Created by Mishtiii on 13-01-2017.
 */
public class SmsBroadcastReceiver extends BroadcastReceiver
{
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null)
        {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i], "3gpp");

                String id = 100 + "";
                String smsBody = smsMessage.getMessageBody().toString();
                String phnNumber = smsMessage.getOriginatingAddress();
                Long dt = smsMessage.getTimestampMillis();
                Date smsDay = new Date(dt);

                smsMessageStr += "SMS From: " + phnNumber + "\n";
                smsMessageStr += smsBody + "\n";
                smsMessageStr += "on: " + dt + "\n";
                Log.i("msg",smsMessageStr);

                SmsModel smsModel = new SmsModel(id,phnNumber,smsBody,"inbox",smsDay+"");
                //DisplaySmsActivity inst = DisplaySmsActivity.instance();
               // inst.updateList(smsModel);
            }
        }
    }
}
