package com.thao.alarmclock;

import java.util.Random;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thao.alarmclock.Services.MusicService;
import com.thao.alarmclock.Services.MusicService.ServiceBinder;
import com.thao.alarmclock.helper.AlarmDBHelper;
import com.thao.alarmclock.model.AlarmModel;
import com.trigg.alarmclock.R;

public class AlarmCaptcha extends Activity implements OnClickListener {
	private TextView txtCapcha, txtsubmit, txtback;
	private EditText editnhap;
	private LinearLayout layoutpas, layouttatam, layoutxoa;
	private Random ran = new Random();
	private String captcha, traloi;
	private long id, key;
	private AlarmModel alarm;
	private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
	private MusicService mServ;
	String[] s = { " a", " b", " c", " d", " e", " f", " g", " h", " i", " j",
			" k", " m", " n", " o", " p", " q", " r", " s", " t", " u", " v",
			" w", " x", " y", " z", " 1", " 2", " 3", " 4", " 5", " 6", " 7",
			" 8", " 9", " 0" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.captchalayout);
		Bundle bundle = getIntent().getBundleExtra("DATA");
		id = bundle.getLong("ID");
		key = bundle.getLong("KEY");
		alarm = dbHelper.getAlarm(id);
		doBindService();
		captcha = s[ran.nextInt(s.length)] + s[ran.nextInt(s.length)]
				+ s[ran.nextInt(s.length)] + "        "
				+ s[ran.nextInt(s.length)] + s[ran.nextInt(s.length)]
				+ s[ran.nextInt(s.length)] + s[ran.nextInt(s.length)]
				+ s[ran.nextInt(s.length)];
		System.out.println(captcha);
		setting();
		txtback.setOnClickListener(this);

		layoutpas.setOnClickListener(this);

		layouttatam.setOnClickListener(this);

		layoutxoa.setOnClickListener(this);
		txtsubmit.setOnClickListener(this);
		txtCapcha.setText(captcha);
	}

	private void setting() {
		txtCapcha = (TextView) findViewById(R.id.txtcaptcha);
		txtsubmit = (TextView) findViewById(R.id.txtsubmit);
		txtback = (TextView) findViewById(R.id.txtback);
		editnhap = (EditText) findViewById(R.id.editnhap);
		layoutpas = (LinearLayout) findViewById(R.id.layoutpass);
		layouttatam = (LinearLayout) findViewById(R.id.layouttatam);
		layoutxoa = (LinearLayout) findViewById(R.id.layoutxoa);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtback:
			finish();
			break;
		case R.id.layoutpass:
			captcha = s[ran.nextInt(s.length)] + s[ran.nextInt(s.length)]
					+ s[ran.nextInt(s.length)] + "        "
					+ s[ran.nextInt(s.length)] + s[ran.nextInt(s.length)]
					+ s[ran.nextInt(s.length)] + s[ran.nextInt(s.length)]
					+ s[ran.nextInt(s.length)];
			txtCapcha.setText(captcha);
			editnhap.setText("");
			break;
		case R.id.layouttatam:
			break;
		case R.id.layoutxoa:
			editnhap.setText("");
			break;
		case R.id.txtsubmit:
			traloi = editnhap.getText().toString();
			Log.d("", "traloi" + traloi + "captcha" + captcha);
			if (captcha.replace(" ", "").equalsIgnoreCase(
					traloi.replace(" ", ""))) {

				if (key == 1) {
					mServ.pauseMusic();
					alarm.isnooze = true;
					dbHelper.updateAlarm(alarm);
					Log.d("IDDDDDDDDDDDDDDDDDDdd", alarm.id + "");
					AlarmManagerHelper
							.setSnooze(getApplicationContext(), alarm);
					Intent i = new Intent(getApplicationContext(),
							WaitingActivity.class);
					i.putExtra("ID", id);
					startActivity(i);
					finish();
				} else {
					mServ.pauseMusic();
					alarm.isnooze = false;
					dbHelper.updateAlarm(alarm);
					Intent intent = new Intent(getApplicationContext(),
							AlarmListActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}
			} else {
				Toast.makeText(getBaseContext(),
						getResources().getString(R.string.wrong),
						Toast.LENGTH_SHORT).show();
				editnhap.setText(" ");
			}

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
}
