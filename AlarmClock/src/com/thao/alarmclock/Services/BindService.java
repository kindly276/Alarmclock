package com.thao.alarmclock.Services;

import com.thao.alarmclock.AlarmManagerHelper;
import com.thao.alarmclock.helper.AlarmDBHelper;
import com.thao.alarmclock.model.AlarmModel;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;

public class BindService extends Service {
	private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
	public long soMiliGiay;
	CountDownTimer demThoiGian;
	private AlarmModel alarm;
	private long id;
	private final IBinder mBinder = new ServiceBinder();

	public class ServiceBinder extends Binder {
		public BindService getService() {
			return BindService.this;
		}
	}

	public long getcount() {
		return soMiliGiay;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("Service is Created");
	}

	int hour, minute;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle bundle = intent.getBundleExtra("DATA");
		soMiliGiay = bundle.getLong("SNOOZETIME");
		thoigian();
		id = bundle.getLong("ID");
		alarm = dbHelper.getAlarm(id);
			AlarmManagerHelper.setSnooze(getApplicationContext(), alarm);
		
		return START_STICKY;
	}

	public void thoigian() {
		demThoiGian = new CountDownTimer(soMiliGiay, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				soMiliGiay = soMiliGiay - 1000;
				if(soMiliGiay==0){
					AlarmModel alarm = dbHelper.getAlarm(id);
					alarm.isnooze = false;
					dbHelper.updateAlarm(alarm);
				}
			}

			@Override
			public void onFinish() {
				demThoiGian.cancel();
			}
		};
		demThoiGian.start();
	}
}