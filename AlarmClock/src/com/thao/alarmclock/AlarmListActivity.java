package com.thao.alarmclock;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thao.alarmclock.adapter.AlarmListAdapter;
import com.thao.alarmclock.helper.AlarmDBHelper;
import com.thao.alarmclock.model.AlarmModel;
import com.thao.alarmclock.model.SettingModel;
import com.trigg.alarmclock.R;

public class AlarmListActivity extends Activity {

	private AlarmListAdapter mAdapter;
	private static Context mContext;
	private static AlarmDBHelper dbHelper;
	private ListView lvalarm;
	private static SettingModel model;
	private long soMiliGiay = 0;
	CountDownTimer demThoiGian;
	private TextView nextalarm, txtap, thoigianconlai;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		dbHelper = new AlarmDBHelper(getApplicationContext());
		setContentView(R.layout.activity_alarm_list);
		AlarmManagerHelper.cancelAlarms(this);
		if (dbHelper.getAlarms() == null) {
			dbHelper.createAlarm(setting());
		}
		if (dbHelper.kiemtra()) {
			dbHelper.createsetting(settingmodel());
		}
		AlarmManagerHelper.setAlarms(this);
		mAdapter = new AlarmListAdapter(this, dbHelper.getAlarms());
		lvalarm = (ListView) findViewById(R.id.list);
		lvalarm.setAdapter(mAdapter);
		thoigianconlai = (TextView) findViewById(R.id.thoigianconlai);
		nextalarm = (TextView) findViewById(R.id.nextalarm);
		txtap = (TextView) findViewById(R.id.textap);
		settingnextam();

	}

	public void settingnextam() {
		AlarmModel settime = settingstimeconlai();
		if (settime != null) {
			thoigianconlai(settime);
			if (check()) {
				nextalarm.setText(String.format("Next Alarm %02d:%02d",
						settime.timeHour, settime.timeMinute));
			} else {
				if (settime.timeHour >= 12) {
					if (settime.timeHour >= 13) {
						settime.timeHour = settime.timeHour - 12;
					}
					nextalarm.setText(String.format("Next Alarm %02d:%02d",
							settime.timeHour, settime.timeMinute));
					txtap.setText("pm");
				} else {
					nextalarm.setText(String.format("Next Alarm %02d:%02d",
							settime.timeHour, settime.timeMinute));
					txtap.setText("am");
				}
			}
		} else {
			nextalarm.setText("You not sets Alarm Time");
			txtap.setText("");
			thoigianconlai.setText("");
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		demThoiGian.cancel();
	}

	private void thoigianconlai(AlarmModel settime) {
		Calendar calendar = Calendar.getInstance();
		long time = calendar.getTimeInMillis();
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(Calendar.HOUR_OF_DAY, settime.timeHour);
		calendar2.set(Calendar.MINUTE, settime.timeMinute);
		calendar2.set(Calendar.SECOND, 0);
		long timealarm = calendar2.getTimeInMillis();
		if (timealarm <= time) {
			timealarm = timealarm + 24 * DateUtils.HOUR_IN_MILLIS;
			soMiliGiay = timealarm - time;
		} else {
			soMiliGiay = timealarm - time;
		}
		demThoiGian = new CountDownTimer(soMiliGiay+60*1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				int gio = (int) (millisUntilFinished / DateUtils.HOUR_IN_MILLIS);
				int phut = (int) (millisUntilFinished
						% (DateUtils.HOUR_IN_MILLIS) / DateUtils.MINUTE_IN_MILLIS);
				String nextalarm = thoigianconlai.getText().toString();
				if (gio == 0 && phut == 1) {
					thoigianconlai
							.setText("Time remaining: Less than 1 minute");
				} else {
					if (gio == 1) {
						thoigianconlai.setText("Time remaining: " + gio
								+ " hour " + phut+ " minutes");
					}
					thoigianconlai.setText("Time remaining: " + gio + " hours "
							+ phut + " minutes");
				}
			}

			@Override
			public void onFinish() {
				demThoiGian.cancel();
			}
		};
		demThoiGian.start();
	}

	private AlarmModel settingstimeconlai() {
		AlarmModel alarmchon = null;
		int minminute = 60, minhour = 24, maxhour = 0, maxminute = 0;
		boolean kiemtra = true;
		final int nowhour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		final int nowminute = Calendar.getInstance().get(Calendar.MINUTE);
		List<AlarmModel> alarms = dbHelper.getAlarms();
		if (alarms != null) {
			for (AlarmModel alarm : alarms) {
				if ((alarm.getRepeatingDay(Calendar.DAY_OF_WEEK - 1)
						&& (alarm.isEnabled) && ((alarm.timeHour > nowhour) || ((alarm.timeHour == nowhour) && (alarm.timeMinute > nowminute))))) {

					if (((alarm.timeHour < minhour) && (alarm.timeMinute < minminute))
							|| ((alarm.timeHour == minhour) && (alarm.timeMinute < minminute))) {
						minhour = alarm.timeHour;
						minminute = alarm.timeMinute;
						alarmchon = alarm;
					}
					kiemtra = false;
				}
			}
			if (kiemtra) {
				for (AlarmModel alarm : alarms) {
					if ((alarm.getRepeatingDay(Calendar.DAY_OF_WEEK - 1) && alarm.isEnabled)
							&& ((alarm.timeHour < nowhour) || ((alarm.timeHour == nowhour) && (alarm.timeMinute <= nowminute)))) {
						if (((alarm.timeHour > maxhour) && (alarm.timeMinute > maxminute))
								|| ((alarm.timeHour == maxhour) && (alarm.timeMinute > maxhour))) {
							maxhour = alarm.timeHour;
							maxhour = alarm.timeMinute;
							alarmchon = alarm;
						}
					}
				}
			}
		}
		return alarmchon;
	}

	// Kiem tra format 24h cho time chinh
	public static boolean check() {
		if (dbHelper.kiemtra()) {
			dbHelper.createsetting(settingmodel());
		}
		model = dbHelper.getSetting(1);
		settingngonngu();
		if (model.isFormat()) {
			return true;
		} else {
			return false;
		}
	}

	private static void settingngonngu() {
		if (model.getLanguage() == 0) {
			Locale locale = new Locale("en_US");
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			mContext.getResources().updateConfiguration(config, null);
		} else if (model.getLanguage() == 1) {
			Locale locale = new Locale("en_US");
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			mContext.getResources().updateConfiguration(config, null);
		} else {
			Locale locale = new Locale("vi");
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			mContext.getResources().updateConfiguration(config, null);
		}
	}

	// cap nhat time va adapter
	@Override
	protected void onResume() {
		super.onResume();
		mAdapter = new AlarmListAdapter(this, dbHelper.getAlarms());
		lvalarm.setAdapter(mAdapter);
		check();
	}

	private AlarmModel setting() {
		AlarmModel alarmModel = new AlarmModel();
		alarmModel.timeHour = 6;
		alarmModel.timeMinute = 30;
		alarmModel.repeatWeekly = true;
		alarmModel.alarmTone = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		alarmModel.name = mContext.getResources().getString(R.string.study);
		alarmModel.isEnabled = true;
		alarmModel.typetone = "Ringtone";
		alarmModel.snooze = 10;
		alarmModel.vibrate = true;
		alarmModel.volume = 100;
		alarmModel.setRepeatingDay(alarmModel.MONDAY, true);
		alarmModel.setRepeatingDay(alarmModel.TUESDAY, true);
		alarmModel.setRepeatingDay(alarmModel.WEDNESDAY, true);
		alarmModel.setRepeatingDay(alarmModel.THURSDAY, true);
		alarmModel.setRepeatingDay(alarmModel.FRDIAY, true);
		alarmModel.setRepeatingDay(alarmModel.SATURDAY, true);
		alarmModel.setRepeatingDay(alarmModel.SUNDAY, true);
		return alarmModel;
	}

	private static SettingModel settingmodel() {
		SettingModel model = new SettingModel();

		model.setId(1);
		model.setLanguage(0);
		model.setFormat(true);
		return model;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.alarm_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_add_new_alarm: {
			startAlarmDetailsActivity(-1);
			break;
		}
		case R.id.action_settings: {
			Intent i = new Intent(getApplicationContext(), AlarmSetting.class);
			startActivity(i);
		}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			mAdapter.setAlarms(dbHelper.getAlarms());
			mAdapter.notifyDataSetChanged();
			demThoiGian.cancel();
			settingnextam();
		}
	}

	public void setAlarmEnabled(long id, boolean isEnabled) {

		AlarmManagerHelper.cancelAlarms(this);
		AlarmModel model = dbHelper.getAlarm(id);
		model.isEnabled = isEnabled;
		model.isnooze = false;
		dbHelper.updateAlarm(model);
		Log.d("ENABLE", "" + model.isEnabled);
		mAdapter = new AlarmListAdapter(this, dbHelper.getAlarms());
		lvalarm.setAdapter(mAdapter);
		demThoiGian.cancel();
		settingnextam();
		AlarmManagerHelper.cancelAlarms(getApplicationContext(), model);
		AlarmManagerHelper.setAlarms(this);

	}

	public void startAlarmDetailsActivity(long id) {
		Intent intent = new Intent(this, AlarmDetailsActivity.class);
		intent.putExtra("id", id);
		startActivityForResult(intent, 0);
	}

	public void startAlarmWaitingActivity(long id) {
		Toast.makeText(getBaseContext(),
				mContext.getResources().getString(R.string.pleasetake),
				Toast.LENGTH_SHORT).show();
	}

	public void deleteAlarm(long id) {
		final long alarmId = id;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(mContext.getResources().getString(R.string.please))
				.setTitle(mContext.getResources().getString(R.string.delete))
				.setCancelable(true)
				.setNegativeButton(
						mContext.getResources().getString(R.string.CANCEL),
						null)
				.setPositiveButton(
						mContext.getResources().getString(R.string.OK),
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								AlarmModel alarm = dbHelper.getAlarm(alarmId);
								AlarmManagerHelper.cancelAlarms(
										getApplicationContext(), alarm);
								// Cancel Alarms
								AlarmManagerHelper.cancelAlarms(mContext);
								// Delete alarm from DB by id
								dbHelper.deleteAlarm(alarmId);
								// Refresh the list of the alarms in the adaptor
								mAdapter = new AlarmListAdapter(mContext,
										dbHelper.getAlarms());
								mAdapter.setAlarms(dbHelper.getAlarms());
								lvalarm.setAdapter(mAdapter);
								demThoiGian.cancel();
								settingnextam();
								// Set the alarms
								if (dbHelper.getAlarms() != null) {
									AlarmManagerHelper.setAlarms(mContext);
								}

							}
						}).show();
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
