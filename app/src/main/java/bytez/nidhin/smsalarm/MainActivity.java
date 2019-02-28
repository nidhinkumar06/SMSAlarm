package bytez.nidhin.smsalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    final static int RQS_1 = 1;
    private static MainActivity inst;
    ArrayList<String> smsMessagesList = new ArrayList<String>();
    ListView smsListView;
    ArrayAdapter arrayAdapter;

    public static MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsListView = findViewById(R.id.SMSList);

        SMSReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.e("Message", messageText);
                Toast.makeText(MainActivity.this, "Message: " + messageText, Toast.LENGTH_LONG).show();
                refreshSmsInbox();
            }
        });

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        smsListView.setAdapter(arrayAdapter);

        smsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                try {
                    String[] smsMessages = smsMessagesList.get(pos).split("\n");
                    String address = smsMessages[0];
                    String smsMessage = "";
                    for (int i = 1; i < smsMessages.length; ++i) {
                        smsMessage += smsMessages[i];
                    }

                    String smsMessageStr = address + "\n";
                    smsMessageStr += smsMessage;
                    if (smsMessage.toLowerCase().matches("(.*)alarm(.*)")) {
                        Toast.makeText(MainActivity.this, "String matches", Toast.LENGTH_SHORT).show();
                        Calendar calNow = Calendar.getInstance();
                        Calendar calSet = (Calendar) calNow.clone();

                        int hour = calSet.get(Calendar.HOUR_OF_DAY);
                        int minute = calSet.get(Calendar.MINUTE);

                        calSet.set(Calendar.HOUR_OF_DAY, hour);
                        calSet.set(Calendar.MINUTE, minute + 1);
                        calSet.set(Calendar.SECOND, 0);
                        calSet.set(Calendar.MILLISECOND, 0);

                        if (calSet.compareTo(calNow) <= 0) {
                            // Today Set time passed, count to tomorrow
                            calSet.add(Calendar.DATE, 1);
                        }

                        setAlarm(calSet);
                    }
                    Toast.makeText(MainActivity.this, smsMessageStr, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Add SMS Read Permision At Runtime
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
            refreshSmsInbox();

        } else {

            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    private void setAlarm(Calendar targetCal) {
        String message = "\n\n***\n" + "Alarm is set "
                + targetCal.getTime() + "\n" + "***\n";

        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);
    }

    private void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            arrayAdapter.add(str);
        } while (smsInboxCursor.moveToNext());
    }

    public void updateList(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        SMSReceiver.unbindListener();
        super.onDestroy();
    }
}
