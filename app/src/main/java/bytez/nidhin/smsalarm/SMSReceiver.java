package bytez.nidhin.smsalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";
    //interface
    private static SmsListener mListener;
    Boolean b;

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }

    public static void unbindListener() {
        mListener = null;
    }

    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.toString(), Toast.LENGTH_SHORT).show();
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody();
                String address = smsMessage.getOriginatingAddress();

                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";

                String sender = smsMessage.getDisplayOriginatingAddress();
                Toast.makeText(context, sender, Toast.LENGTH_SHORT).show();
                if (b == true) {
                    //Pass the message text to interface
                    mListener.messageReceived(smsMessageStr);
                }
            }
            Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();


            //this will update the UI with message
            MainActivity inst = MainActivity.instance();
            inst.updateList(smsMessageStr);
        }
    }


}
