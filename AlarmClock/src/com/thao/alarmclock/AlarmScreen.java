package com.thao.alarmclock;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.thao.alarmclock.Services.MusicService;
import com.thao.alarmclock.Services.MusicService.ServiceBinder;
import com.thao.alarmclock.helper.AlarmDBHelper;
import com.thao.alarmclock.model.AlarmModel;
import com.trigg.alarmclock.R;

public class AlarmScreen extends Activity {

	public final String TAG = this.getClass().getSimpleName();
	private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
	private WakeLock mWakeLock;
	private static final int WAKELOCK_TIMEOUT = 60 * 1000;
	private final static int MAX_VOLUME = 100;
	AlarmModel alarmModel1;
	private TextView txtDismiss, tvName, txtSnooze;
	private long id;
	private MusicService mServ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup layout
		this.setContentView(R.layout.activity_alarm_screen);
		// nhan data tu id
		id = getIntent().getLongExtra(AlarmManagerHelper.ID, 0);
		alarmModel1 = dbHelper.getAlarm(id);
		Intent intent = new Intent(getApplicationContext(), MusicService.class);
		intent.putExtra("ID", id);
		startService(intent);
		doBindService();
		boolean vibrate = alarmModel1.vibrate;
		Vibrator mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		if (vibrate) {
			mVibrator.vibrate(Integer.MAX_VALUE);
		}

		// nhan da tu activity va thiet lap gia tri tam thoi
		final String name = alarmModel1.name;
		final int snooze = alarmModel1.snooze;
		final Uri uritone = alarmModel1.alarmTone;
		final int volume = getIntent().getIntExtra(AlarmManagerHelper.VOLUME,
				100);
		tvName = (TextView) findViewById(R.id.alarm_screen_name);
		tvName.setText(name);
		txtDismiss = (TextView) findViewById(R.id.txtDismiss);
		txtSnooze = (TextView) findViewById(R.id.textSnooze);
		settingdismiss();

		settingsnooze();

		Runnable releaseWakelock = new Runnable() {

			@Override
			public void run() {
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

				if (mWakeLock != null && mWakeLock.isHeld()) {
					mWakeLock.release();
				}
			}
		};

		new Handler().postDelayed(releaseWakelock, WAKELOCK_TIMEOUT);
	}

	private void settingsnooze() {
		if (alarmModel1.snozeemethod == 3) {
			txtSnooze.setVisibility(View.GONE);
		} else if (alarmModel1.snozeemethod == 1) {
			txtSnooze.setText(getResources().getString(
					R.string.pleasemakesnooze));
			txtSnooze.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(),
							Alarmmath.class);
					Bundle bundle = new Bundle();
					bundle.putLong("ID", id);
					bundle.putLong("KEY", 1);
					intent.putExtra("DATA", bundle);
					startActivity(intent);
					// mPlayer.stop();
				}
			});
		} else if (alarmModel1.snozeemethod == 2) {
			txtSnooze.setText(getResources().getString(
					R.string.pleaseentersnooze));
			txtSnooze.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(),
							AlarmCaptcha.class);
					Bundle bundle = new Bundle();
					bundle.putLong("ID", id);
					bundle.putLong("KEY", 1);
					intent.putExtra("DATA", bundle);
					startActivity(intent);
					// mPlayer.stop();
				}
			});
		} else {
			txtSnooze.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// mPlayer.stop();
					mServ.pauseMusic();
					alarmModel1.isnooze = true;
					dbHelper.updateAlarm(alarmModel1);
					Log.d("ISNOOZEEEEEEE", "" + alarmModel1.isnooze);
					AlarmManagerHelper.setSnooze(getApplicationContext(),
							alarmModel1);
					Intent i = new Intent(AlarmScreen.this,
							WaitingActivity.class);
					i.putExtra("ID", id);
					startActivity(i);
					finish();
				}
			});
		}
	}

	private void settingdismiss() {
		if (alarmModel1.dismissmethod == 0) {
			txtDismiss.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					mServ.pauseMusic();
					alarmModel1.isnooze = false;
					dbHelper.updateAlarm(alarmModel1);
					Intent intent = new Intent(getApplicationContext(),
							AlarmListActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}
			});
		} else if (alarmModel1.dismissmethod == 1) {
			txtDismiss.setText(getResources().getString(R.string.pleasemake));
			txtDismiss.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(),
							Alarmmath.class);
					Bundle bundle = new Bundle();
					bundle.putLong("ID", id);
					bundle.putLong("KEY", 0);
					intent.putExtra("DATA", bundle);
					startActivity(intent);
					// mPlayer.stop();
				}
			});
		} else {
			txtDismiss.setText(getResources().getString(R.string.pleaseenter));
			txtDismiss.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(getApplicationContext(),
							AlarmCaptcha.class);
					Bundle bundle = new Bundle();
					bundle.putLong("ID", id);
					bundle.putLong("KEY", 0);
					intent.putExtra("DATA", bundle);
					startActivity(intent);
					// mPlayer.stop();
				}
			});
		}
	}

	private ServiceConnection Scon = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder binder) {
			ServiceBinder services = (ServiceBinder) binder;
			mServ = services.getService();
		}

		public void onServiceDisconnected(ComponentName name) {
			mServ = null;
		}

	};

	void doBindService() {
		bindService(new Intent(this, MusicService.class), Scon,
				Context.BIND_AUTO_CREATE);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();

		// Set the window to keep screen on
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		// Acquire wakelock
		PowerManager pm = (PowerManager) getApplicationContext()
				.getSystemService(Context.POWER_SERVICE);
		if (mWakeLock == null) {
			mWakeLock = pm
					.newWakeLock(
							(PowerManager.FULL_WAKE_LOCK
									| PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP),
							TAG);
		}

		if (!mWakeLock.isHeld()) {
			mWakeLock.acquire();
			Log.i(TAG, "Wakelock aquired!!");
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mWakeLock != null && mWakeLock.isHeld()) {
			mWakeLock.release();
		}
	}

	@Override
	public void onBackPressed() {
	}
}
