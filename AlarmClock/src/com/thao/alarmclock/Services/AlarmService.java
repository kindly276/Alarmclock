package com.thao.alarmclock.Services;

import com.thao.alarmclock.AlarmManagerHelper;
import com.thao.alarmclock.AlarmScreen;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlarmService extends Service {

	public static String TAG = AlarmService.class.getSimpleName();
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Intent alarmIntent = new Intent(getBaseContext(), AlarmScreen.class);
		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		alarmIntent.putExtras(intent);
		getApplication().startActivity(alarmIntent);
		
		AlarmManagerHelper.setAlarms(this);
		return super.onStartCommand(intent, flags, startId);
	}
	
}