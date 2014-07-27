package com.thao.alarmclock;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.thao.alarmclock.Services.AlarmService;
import com.thao.alarmclock.Services.BindService;
import com.thao.alarmclock.Services.BindService.ServiceBinder;
import com.thao.alarmclock.helper.AlarmDBHelper;
import com.thao.alarmclock.model.AlarmModel;
import com.trigg.alarmclock.R;

public class WaitingActivity extends Activity {
	private long soMiliGiay;
	CountDownTimer demThoiGian;
	private TextView txttime, textCancel, txtalarmname;
	private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
	private AlarmModel alarm;
	private long id;
	private Context mcontext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mcontext = this;
		setContentView(R.layout.waiting_activity);
		txttime = (TextView) findViewById(R.id.txttime);
		id = getIntent().getLongExtra("ID", 0);
		alarm = dbHelper.getAlarm(id);
		soMiliGiay = alarm.snooze * DateUtils.MINUTE_IN_MILLIS;
		textCancel = (TextView) findViewById(R.id.txtcancel);
		txtalarmname = (TextView) findViewById(R.id.txtalarmname);
		txtalarmname.setText(alarm.name);
		textCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlarmManagerHelper.cancelAlarms(getApplicationContext(), alarm);
				alarm.isnooze = false;
				dbHelper.updateAlarm(alarm);
				Intent intent = new Intent(getApplicationContext(),
						AlarmListActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		});
		thoigian();
	}

	public void thoigian() {
		demThoiGian = new CountDownTimer(soMiliGiay, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				long phut = (millisUntilFinished / 1000) / 60;
				long giay = (millisUntilFinished / 1000) % 60;
				if ((phut >= 10) && (giay >= 10)) {
					txttime.setText("00:" + phut + ":" + giay + " ");
				} else if ((phut < 10) && (giay >= 10)) {
					txttime.setText("00:0" + phut + ":" + giay + " ");
				} else if ((phut < 10) && (giay < 10)) {
					txttime.setText("00:0" + phut + ":0" + giay + " ");
				}
			}

			@Override
			public void onFinish() {
				demThoiGian.cancel();
				finish();
			}
		};
		demThoiGian.start();
	}

	@Override
	public void onBackPressed() {
	}

}
