package com.app.kent.alarmtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = MainActivity.this.getClass().getSimpleName();
    private AlarmManager mAlarm;

    private MyReceiver mReceiver;
    private Calendar mCalendar = Calendar.getInstance();
    private Calendar cur_cal = new GregorianCalendar();
    private PendingIntent pIntent;
    private AlarmManager alarm;
    private int testCount = 0;

    private String alarmStart = "ACTION_ALARM_START";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        initView();

        start.setOnClickListener(this);
        stop.setOnClickListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(alarmStart);
        mReceiver = new MyReceiver();
        registerReceiver(mReceiver, filter);

    }

    private Button start, stop;
    private TextView value;
    private void initView() {
        start = (Button) findViewById(R.id.btn_start);
        stop = (Button) findViewById(R.id.btn_stop);
        value = (TextView) findViewById(R.id.tv_value);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        mCalendar.setTimeInMillis(System.currentTimeMillis());
//        mCalendar.add(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
//        mCalendar.set(Calendar.HOUR_OF_DAY, 17);
//        mCalendar.set(Calendar.MINUTE, 13);
//        mCalendar.set(Calendar.SECOND, cur_cal.get(Calendar.SECOND));
//        mCalendar.set(Calendar.MILLISECOND, cur_cal.get(Calendar.MILLISECOND));
//        mCalendar.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
//        mCalendar.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));
        //mAlarm.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), )
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_start:
                alarmStart();
                break;

            case R.id.btn_stop:
                alarmStop();
                break;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage: " + msg.arg1);
            switch(msg.arg1) {
                case 0:
                    value.setText(String.valueOf(msg.arg2));
                    break;
            }
        }
    };

    private void alarmStart() {


        Intent intent = new Intent(alarmStart);
        pIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60 * 1000, pIntent);




//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Message msg = mHandler.obtainMessage();
//                msg.arg1 = 1;
//                msg.sendToTarget();
//            }
//        });
    }

    private void alarmStop() {
        mAlarm.cancel(pIntent);

        Message msg = mHandler.obtainMessage();
        msg.arg1 = 0;
        msg.arg2 = testCount = 0;
        msg.sendToTarget();
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(alarmStart)) {
                Log.d(TAG, "Receiver intent: " + alarmStart);

                testCount++;
                Message msg = mHandler.obtainMessage();
                msg.arg1 = 0;
                msg.arg2 = testCount;
                msg.sendToTarget();
            }
        }

        // constructor
        public MyReceiver() {
        }
    }
}
