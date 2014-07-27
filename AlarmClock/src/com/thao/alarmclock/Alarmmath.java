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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thao.alarmclock.Services.MusicService;
import com.thao.alarmclock.Services.MusicService.ServiceBinder;
import com.thao.alarmclock.helper.AlarmDBHelper;
import com.thao.alarmclock.model.AlarmModel;
import com.trigg.alarmclock.R;

public class Alarmmath extends Activity implements OnClickListener {
	private TextView txtbaitoan, txt0, txt1, txt2, txt3, txt4, txt5, txt6,
			txt7, txt8, txt9, txtsubmit, txtback, btnsubmit, txttru;
	private EditText editketqua;
	private Random r = new Random();
	int a, b, cong = 0, tru = 1, nhan = 2, pheptinh, dapan;
	private String ketqua = "";
	private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
	private AlarmModel alarm;
	private long id, key;
	private LinearLayout layoutxoa, layouttatam, layoutpass;
	private MusicService mServ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mathlayout);
		Bundle bundle = getIntent().getBundleExtra("DATA");
		id = bundle.getLong("ID");
		key = bundle.getLong("KEY");
		alarm = dbHelper.getAlarm(id);
		goirandom();
		doBindService();
		layoutxoa = (LinearLayout) findViewById(R.id.layoutxoa);
		layoutxoa.setOnClickListener(this);
		layouttatam = (LinearLayout) findViewById(R.id.layouttatam);
		layouttatam.setOnClickListener(this);
		layoutpass = (LinearLayout) findViewById(R.id.layoutpass);
		layoutpass.setOnClickListener(this);
		txt0 = (TextView) findViewById(R.id.txt0);
		txt0.setOnClickListener(this);
		txt1 = (TextView) findViewById(R.id.txt1);
		txt1.setOnClickListener(this);
		txt2 = (TextView) findViewById(R.id.txt2);
		txt2.setOnClickListener(this);
		txt3 = (TextView) findViewById(R.id.txt3);
		txt3.setOnClickListener(this);
		txt4 = (TextView) findViewById(R.id.txt4);
		txt4.setOnClickListener(this);
		txt5 = (TextView) findViewById(R.id.txt5);
		txt5.setOnClickListener(this);
		txt6 = (TextView) findViewById(R.id.txt6);
		txt6.setOnClickListener(this);
		txt7 = (TextView) findViewById(R.id.txt7);
		txt7.setOnClickListener(this);
		txt8 = (TextView) findViewById(R.id.txt8);
		txt8.setOnClickListener(this);
		txt9 = (TextView) findViewById(R.id.txt9);
		txt9.setOnClickListener(this);
		txtsubmit = (TextView) findViewById(R.id.txtsubmit);
		txtsubmit.setOnClickListener(this);
		txtbaitoan = (TextView) findViewById(R.id.txtbaitoan);
		txtback = (TextView) findViewById(R.id.txtback);
		txtback.setOnClickListener(this);
		txttru = (TextView) findViewById(R.id.txttru);
		txttru.setOnClickListener(this);
		editketqua = (EditText) findViewById(R.id.editinput);
		btnsubmit = (TextView) findViewById(R.id.txtsubmit);
		txtbaitoan.setText(a + " + " + b + " = ");
		pheptinh = cong;
		dapan = a + b;
	}

	private void goirandom() {
		if (alarm.mathlevel == 0) {
			a = r.nextInt(10);
			b = r.nextInt(10);
		} else if (alarm.mathlevel == 1) {
			a = r.nextInt(11) + 10;
			b = r.nextInt(11) + 10;
		} else if (alarm.mathlevel == 2) {
			a = r.nextInt(21) + 20;
			b = r.nextInt(21) + 20;
		} else {
			a = r.nextInt(31) + 30;
			b = r.nextInt(31) + 30;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt0:
			ketqua = ketqua + "0";
			break;
		case R.id.txt1:
			ketqua = ketqua + "1";
			break;
		case R.id.txt2:
			ketqua = ketqua + "2";
			break;
		case R.id.txt3:
			ketqua = ketqua + "3";
			break;
		case R.id.txt4:
			ketqua = ketqua + "4";
			break;
		case R.id.txt5:
			ketqua = ketqua + "5";
			break;
		case R.id.txt6:
			ketqua = ketqua + "6";
			break;
		case R.id.txt7:
			ketqua = ketqua + "7";
			break;
		case R.id.txt8:
			ketqua = ketqua + "8";
			break;
		case R.id.txt9:
			ketqua = ketqua + "9";
			break;
		case R.id.layoutxoa:
			ketqua = "";
			break;
		case R.id.layouttatam:
			mServ.pauseMusic();
			break;
		case R.id.txtback:
			finish();
			break;
		case R.id.txttru:
			ketqua = ketqua + "-";
			break;
		case R.id.layoutpass:
			goirandom();
			if (pheptinh == cong) {
				txtbaitoan.setText(a + " * " + b + " = ");
				pheptinh = nhan;
				dapan = a * b;
				break;
			} else if (pheptinh == nhan) {
				txtbaitoan.setText(a + " - " + b + " = ");
				pheptinh = tru;
				dapan = a - b;
				break;
			} else if (pheptinh == tru) {
				txtbaitoan.setText(a + " + " + b + " = ");
				pheptinh = cong;
				dapan = a + b;
				break;
			}
			editketqua.setText(" ");
			break;
		case R.id.txtsubmit:
			if (ketqua.equalsIgnoreCase(String.valueOf((dapan)))) {
				if (key == 1) {
					mServ.pauseMusic();
					alarm.isnooze = true;
					dbHelper.updateAlarm(alarm);
					Log.d("IDDDDDDDDDDDDDDDDDDdd", alarm.id + "");
					AlarmManagerHelper
							.setSnooze(getApplicationContext(), alarm);
					Intent i = new Intent(getApplicationContext(),
							WaitingActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
				editketqua.setText(" ");
			}
			break;
		default:
			break;
		}
		editketqua.setText(ketqua);
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
