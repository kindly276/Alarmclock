package com.thao.alarmclock;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thao.alarmclock.helper.AlarmDBHelper;
import com.thao.alarmclock.model.AlarmModel;
import com.trigg.alarmclock.R;

public class AlarmDetailsActivity extends Activity {

	private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
	private AlarmModel alarmDetails;

	private EditText edtName;
	private TextView txtToneSelection, txtrepeat, txtsnooze, txttypetone,
			txtvolume, txttime, txttimeselect, txtmethodsnooze,
			txtmethoddismiss, txtlevelmath;
	private CheckBox chkvibrate;
	private static final int REQUESTCODE = 1;
	private static final int REQUESTCODE2 = 2;
	private static final int REQUESTCODE3 = 3;
	private static final int REQUESTCODE4 = 4;
	private static final int REQUESTCODETYPE = 5;
	private static final int REQUESTCODETIME = 6;
	private static final int REQUESTCODESNOOZEMETHOD = 7;
	private static final int REQUESTCODEDISMISSMETHOD = 8;
	private static final int REQUESTCODELEVELMETHOD = 8;
	private LinearLayout ringToneContainer, mathlevelContainer,
			snoozeContainer, methodsnooze, methoddismiss, linearcancel,
			linearsave, timerContainer, typeToneContainer, repeatContainer,
			vibrate, volumeContainer;
	private View viewrow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		linearcancel = (LinearLayout) findViewById(R.id.linear1);
		linearsave = (LinearLayout) findViewById(R.id.linear2);
		viewrow = (View) findViewById(R.id.divider2);
		linearcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		linearsave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateModelFromLayout();
				Log.d("FFFFFFFFFFFFFFF", alarmDetails.typetone);
				AlarmManagerHelper.cancelAlarms(getApplicationContext());

				if (alarmDetails.id < 0) {
					dbHelper.createAlarm(alarmDetails);
				} else {
					dbHelper.updateAlarm(alarmDetails);
				}

				AlarmManagerHelper.setAlarms(getApplicationContext());

				setResult(RESULT_OK);
				finish();
			}
		});
		settingtextsection();
		settinglinerlayout();
		mathlevelContainer.setVisibility(View.GONE);
		long id = getIntent().getExtras().getLong("id");
		if (id == -1) {
			alarmDetails = new AlarmModel();
			newdays();
			newalarmandtime();
			updatemathleve();
		} else {
			alarmDetails = dbHelper.getAlarm(id);
			firstwithid();
			updatetext();
			updatemethodsnooze();
			updatemethoddismiss();
			updatemathleve();
		}
		ringToneContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = null;
				if (alarmDetails.typetone.equalsIgnoreCase("Ringtone")) {
					i = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
					i.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
							"Select Tone");
					i.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
							(Uri) alarmDetails.alarmTone);
				} else if (alarmDetails.typetone.equalsIgnoreCase("Music")) {
					i = new Intent(Intent.ACTION_GET_CONTENT);
					i.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
							(Uri) alarmDetails.alarmTone);
					i.setType("audio/*");
				}
				startActivityForResult(i, REQUESTCODE);
			}
		});
		methoddismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						Alarmdismissmethod.class);
				intent.putExtra("METHODDISMISS", alarmDetails.dismissmethod);
				startActivityForResult(intent, REQUESTCODEDISMISSMETHOD);
			}
		});
		timerContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), TimePick.class);
				Bundle bundle = new Bundle();
				bundle.putLong("ID", alarmDetails.id);
				bundle.putInt("HOUR", alarmDetails.timeHour);
				bundle.putInt("MINUTE", alarmDetails.timeMinute);
				Log.d("fffffffffff", "" + alarmDetails.id
						+ alarmDetails.timeHour + alarmDetails.timeMinute);
				intent.putExtra("DATA", bundle);
				startActivityForResult(intent, REQUESTCODETIME);
			}
		});
		typeToneContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						AlarmListTypeTone.class);
				intent.putExtra("TYPETONE", alarmDetails.typetone);
				Log.d("TTTTTTTTTTTTTT", alarmDetails.typetone);
				startActivityForResult(intent, REQUESTCODETYPE);
			}
		});
		methodsnooze.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						AlarmSnoozemethod.class);
				intent.putExtra("METHODSNOOZE", alarmDetails.snozeemethod);
				startActivityForResult(intent, REQUESTCODESNOOZEMETHOD);
			}
		});
		repeatContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						RepeatListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putBoolean("WEEKLY", alarmDetails.repeatWeekly);
				bundle.putBoolean("SUNDAY",
						alarmDetails.getRepeatingDay(AlarmModel.SUNDAY));
				bundle.putBoolean("MONDAY",
						alarmDetails.getRepeatingDay(AlarmModel.MONDAY));
				bundle.putBoolean("TUESDAY",
						alarmDetails.getRepeatingDay(AlarmModel.TUESDAY));
				bundle.putBoolean("WEDNESDAY",
						alarmDetails.getRepeatingDay(AlarmModel.WEDNESDAY));
				bundle.putBoolean("THURSDAY",
						alarmDetails.getRepeatingDay(AlarmModel.THURSDAY));
				bundle.putBoolean("FRIDAY",
						alarmDetails.getRepeatingDay(AlarmModel.FRDIAY));
				bundle.putBoolean("SATURDAY",
						alarmDetails.getRepeatingDay(AlarmModel.SATURDAY));
				intent.putExtra("DATAFROMLIST", bundle);
				startActivityForResult(intent, REQUESTCODE2);
			}
		});
		snoozeContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						SnoozeAtivity.class);
				Bundle bundle = new Bundle();
				bundle.putLong("ID", alarmDetails.id);
				bundle.putInt("SNOOZE", alarmDetails.snooze);
				intent.putExtra("DATA", bundle);
				startActivityForResult(intent, REQUESTCODE3);
			}
		});
		vibrate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (chkvibrate.isChecked()) {
					chkvibrate.setChecked(false);
				} else {
					chkvibrate.setChecked(true);
				}

			}
		});
		volumeContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						VolumeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putLong("ID", alarmDetails.id);
				bundle.putInt("VOLUME", alarmDetails.volume);
				intent.putExtra("DATA", bundle);
				startActivityForResult(intent, REQUESTCODE4);
			}

		});
		mathlevelContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						AlarmLevelMath.class);
				intent.putExtra("LEVEL", alarmDetails.mathlevel);
				startActivityForResult(intent, REQUESTCODELEVELMETHOD);
			}

		});
	}

	private void settingtextsection() {
		chkvibrate = (CheckBox) findViewById(R.id.check_vibrate);
		edtName = (EditText) findViewById(R.id.alarm_details_name);
		txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);
		txtrepeat = (TextView) findViewById(R.id.alarm_label_repeat_selection);
		txtsnooze = (TextView) findViewById(R.id.alarm_label_snooze_selection);
		txttypetone = (TextView) findViewById(R.id.alarm_label_typetone_selection);
		txtvolume = (TextView) findViewById(R.id.alarm_label_volume_selection);
		txttime = (TextView) findViewById(R.id.alarm_label_time);
		txttimeselect = (TextView) findViewById(R.id.alarm_label_time_selection);
		txtmethodsnooze = (TextView) findViewById(R.id.alarm_snoozemethod_selection);
		txtmethoddismiss = (TextView) findViewById(R.id.alarm_dismissmethod_selection);
		txtlevelmath = (TextView) findViewById(R.id.alarm_level_selection);
	}

	private void settinglinerlayout() {
		ringToneContainer = (LinearLayout) findViewById(R.id.alarm_ringtone_container);
		mathlevelContainer = (LinearLayout) findViewById(R.id.alarm_levelmath_container);
		snoozeContainer = (LinearLayout) findViewById(R.id.alarm_snooze_container);
		methodsnooze = (LinearLayout) findViewById(R.id.alarm_snoozemethod_container);
		methoddismiss = (LinearLayout) findViewById(R.id.alarm_dismissmethod_container);
		timerContainer = (LinearLayout) findViewById(R.id.alarm_time_container);
		repeatContainer = (LinearLayout) findViewById(R.id.alarm_repeat_container);
		typeToneContainer = (LinearLayout) findViewById(R.id.alarm_typetone_container);
		volumeContainer = (LinearLayout) findViewById(R.id.alarm_volume_container);
		vibrate = (LinearLayout) findViewById(R.id.alarm_vibrate_container);
	}

	private void updatemathleve() {
		if (alarmDetails.snozeemethod == 1 && alarmDetails.dismissmethod == 1) {
			mathlevelContainer.setVisibility(View.VISIBLE);
		} else if (alarmDetails.snozeemethod != 1
				&& alarmDetails.dismissmethod != 1) {
			mathlevelContainer.setVisibility(View.GONE);
		} else if (alarmDetails.snozeemethod == 1
				|| alarmDetails.dismissmethod == 1) {
			mathlevelContainer.setVisibility(View.VISIBLE);
		} else {
			mathlevelContainer.setVisibility(View.VISIBLE);
		}
		if (alarmDetails.mathlevel == 0) {
			txtlevelmath.setText(getResources().getString(R.string.easy));
		} else if (alarmDetails.mathlevel == 1) {
			txtlevelmath.setText(getResources().getString(R.string.Average));
		} else if (alarmDetails.mathlevel == 2) {
			txtlevelmath.setText(getResources().getString(R.string.Difficult));
		} else {
			txtlevelmath.setText(getResources().getString(R.string.Difficultest));
		}
	}

	private void updatemethoddismiss() {
		if (alarmDetails.dismissmethod == 0) {
			txtmethoddismiss.setText(getResources().getString(R.string.pressbutton));
		} else if (alarmDetails.dismissmethod == 1) {
			txtmethoddismiss.setText(getResources().getString(R.string.mathmaking));
		} else if (alarmDetails.dismissmethod == 2) {
			txtmethoddismiss.setText(getResources().getString(R.string.entercatcha));
		}
	}

	private void updatemethodsnooze() {
		if (alarmDetails.snozeemethod == 0) {
			txtmethodsnooze.setText(R.string.pressbutton);
		} else if (alarmDetails.snozeemethod == 1) {
			txtmethodsnooze.setText(R.string.mathmaking);
		} else if (alarmDetails.snozeemethod == 2) {
			txtmethodsnooze.setText(R.string.entercatcha);
		} else if (alarmDetails.snozeemethod == 3) {
			snoozeContainer.setVisibility(View.GONE);
			txtmethodsnooze.setText(R.string.notsnooze);
		}
	}

	private void firstwithid() {
		edtName.setText(alarmDetails.name);
		txtToneSelection.setText(RingtoneManager.getRingtone(this,
				alarmDetails.alarmTone).getTitle(this));
		// snoozetime = alarmDetails.snooze;
		txtsnooze.setText(alarmDetails.snooze +" " +getResources().getString(R.string.minutes));
		chkvibrate.setChecked(alarmDetails.vibrate);
		txtvolume.setText(alarmDetails.volume + "%");
		txttypetone.setText(alarmDetails.typetone);
		int hour = alarmDetails.timeHour;
		int minute = alarmDetails.timeMinute;
		if (!AlarmListActivity.check()) {
			if (hour >= 12) {
				if (hour >= 13) {
					hour = hour - 12;
				}
				txttimeselect.setText(String.format("%02d:%02d pm", hour,
						minute));
			} else {
				txttimeselect.setText(String.format("%02d:%02d am", hour,
						minute));
			}
		} else {
			txttimeselect.setText(String.format("%02d:%02d", hour, minute));
		}
		if (alarmDetails.typetone.equalsIgnoreCase("Silent")) {
			ringToneContainer.setVisibility(View.GONE);
			viewrow.setVisibility(View.GONE);
			alarmDetails.alarmTone = null;
		} else {
			ringToneContainer.setVisibility(View.VISIBLE);
		}
		if (alarmDetails.typetone == null) {
			txttypetone.setText(getResources().getString(R.string.selectsoud));
		}

	}

	private void newalarmandtime() {
		alarmDetails.volume = 100;
		alarmDetails.snooze = 10;
		alarmDetails.snozeemethod = 0;
		alarmDetails.dismissmethod = 0;
		txtmethoddismiss.setText(getResources().getString(R.string.pressbutton));
		txtmethodsnooze.setText(getResources().getString(R.string.pressbutton));
		mathlevelContainer.setVisibility(View.GONE);
		txtsnooze.setText(alarmDetails.snooze +" "+getResources().getString(R.string.minutes));
		alarmDetails.typetone = "Ringtone";
		txttypetone.setText(alarmDetails.typetone);
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		alarmDetails.timeHour = hour;
		alarmDetails.timeMinute = minute;
		if (!DateFormat.is24HourFormat(getBaseContext())) {
			if (hour >= 12) {
				if (hour >= 13) {
					hour = hour - 12;
				}
				txttimeselect.setText(String.format("%02d:%02d pm", hour,
						minute));
			} else {
				txttimeselect.setText(String.format("%02d:%02d am", hour,
						minute));
			}
		} else {
			txttimeselect.setText(String.format("%02d:%02d", hour, minute));
		}
	}

	private void newdays() {
		alarmDetails.setRepeatingDay(alarmDetails.MONDAY, true);
		alarmDetails.setRepeatingDay(alarmDetails.TUESDAY, true);
		alarmDetails.setRepeatingDay(alarmDetails.WEDNESDAY, true);
		alarmDetails.setRepeatingDay(alarmDetails.THURSDAY, true);
		alarmDetails.setRepeatingDay(alarmDetails.FRDIAY, true);
		alarmDetails.setRepeatingDay(alarmDetails.SATURDAY, true);
		alarmDetails.setRepeatingDay(alarmDetails.SUNDAY, true);
		txtrepeat.setText(getResources().getString(R.string.everydays));
	}

	private void updatetext() {
		boolean chkWeekly = alarmDetails.repeatWeekly;
		boolean chkMonday = alarmDetails.getRepeatingDay(AlarmModel.MONDAY);
		boolean chkTuesday = alarmDetails.getRepeatingDay(AlarmModel.TUESDAY);
		boolean chkWednesday = alarmDetails
				.getRepeatingDay(AlarmModel.WEDNESDAY);

		boolean chkThursday = alarmDetails.getRepeatingDay(AlarmModel.THURSDAY);
		boolean chkFriday = alarmDetails.getRepeatingDay(AlarmModel.FRDIAY);
		boolean chkSaturday = alarmDetails.getRepeatingDay(AlarmModel.SATURDAY);
		boolean chkSunday = alarmDetails.getRepeatingDay(AlarmModel.SUNDAY);
		String repeat = "";
		if(chkMonday && chkTuesday && chkWednesday && chkThursday && chkFriday
				&& chkSaturday && chkSunday){
			repeat = getResources().getString(R.string.everydays);
		}
		else if (chkMonday && chkTuesday && chkWednesday && chkThursday && chkFriday
				&& chkSaturday && chkSunday && chkWeekly) {
			repeat = getResources().getString(R.string.everyweekly);
		} else if (!chkMonday && !chkTuesday && !chkWednesday && !chkThursday
				&& !chkFriday && !chkSaturday && !chkSunday && !chkWeekly) {
			repeat = getResources().getString(R.string.choosetype);
		} else {
			if (chkMonday) {
				repeat = getResources().getString(R.string.monday)+" ";
			}
			if (chkTuesday) {
				repeat = repeat + getResources().getString(R.string.tuesday)+" ";
			}
			if (chkWednesday) {
				repeat = repeat + getResources().getString(R.string.Wednesday)+" ";
			}
			if (chkThursday)
				repeat = repeat + getResources().getString(R.string.Thursday)+" ";
			if (chkFriday)
				repeat = repeat + getResources().getString(R.string.Friday)+" ";
			if (chkSaturday)
				repeat = repeat + getResources().getString(R.string.Satudays)+" ";
			if (chkSunday)
				repeat = repeat + getResources().getString(R.string.sunday)+" ";
			if (chkWeekly)
				repeat = repeat + getResources().getString(R.string.weekly);
		}
		txtrepeat.setText(repeat);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUESTCODE: {
				if (alarmDetails.typetone.equalsIgnoreCase("Ringtone")) {
					alarmDetails.alarmTone = data
							.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
					txtToneSelection.setText(RingtoneManager.getRingtone(this,
							alarmDetails.alarmTone).getTitle(this));
				} else if (alarmDetails.typetone.equalsIgnoreCase("Music")) {
					alarmDetails.alarmTone = data.getData();
					txtToneSelection.setText(getRealPathFromURI(
							getBaseContext(), alarmDetails.alarmTone));
				}
				break;
			}
			default: {
				break;
			}
			}
		}
		if (resultCode == SnoozeAtivity.RESULT_CODE) {
			alarmDetails.snooze = data.getIntExtra("TIMESOONE", 10);
			txtsnooze.setText(alarmDetails.snooze + " "+ getResources().getString(R.string.minutes));
		}
		if (resultCode == RepeatListActivity.RESULT_CODE) {
			Bundle b = data.getBundleExtra("DATA");
			alarmDetails.repeatWeekly = b.getBoolean("WEEKLY");
			alarmDetails.setRepeatingDay(alarmDetails.SUNDAY,
					b.getBoolean("SUNDAY"));
			alarmDetails.setRepeatingDay(alarmDetails.MONDAY,
					b.getBoolean("MONDAY"));
			alarmDetails.setRepeatingDay(alarmDetails.TUESDAY,
					b.getBoolean("TUESDAY"));
			alarmDetails.setRepeatingDay(alarmDetails.WEDNESDAY,
					b.getBoolean("WEDNESDAY"));
			alarmDetails.setRepeatingDay(alarmDetails.THURSDAY,
					b.getBoolean("THURSDAY"));
			alarmDetails.setRepeatingDay(alarmDetails.FRDIAY,
					b.getBoolean("FRIDAY"));
			alarmDetails.setRepeatingDay(alarmDetails.SATURDAY,
					b.getBoolean("SATURDAY"));
			boolean chkWeekly = alarmDetails.repeatWeekly;
			boolean chkMonday = alarmDetails.getRepeatingDay(AlarmModel.MONDAY);
			boolean chkTuesday = alarmDetails
					.getRepeatingDay(AlarmModel.TUESDAY);
			boolean chkWednesday = alarmDetails
					.getRepeatingDay(AlarmModel.WEDNESDAY);

			boolean chkThursday = alarmDetails
					.getRepeatingDay(AlarmModel.THURSDAY);
			boolean chkFriday = alarmDetails.getRepeatingDay(AlarmModel.FRDIAY);
			boolean chkSaturday = alarmDetails
					.getRepeatingDay(AlarmModel.SATURDAY);
			boolean chkSunday = alarmDetails.getRepeatingDay(AlarmModel.SUNDAY);
			String repeat = "";
			if (chkMonday && chkTuesday && chkWednesday && chkThursday
					&& chkFriday && chkSaturday && chkSunday && chkWeekly) {
				repeat = "EVERY DAYS (WEEKLY)";
			} else if (!chkMonday && !chkTuesday && !chkWednesday
					&& !chkThursday && !chkFriday && !chkSaturday && !chkSunday
					&& !chkWeekly) {
				repeat = "Choose repeat type";
			} else {
				if (chkMonday) {
					repeat = "MON ";
				}
				if (chkTuesday) {
					repeat = repeat + "TUE ";
				}
				if (chkWednesday) {
					repeat = repeat + "WED ";
				}
				if (chkThursday)
					repeat = repeat + "THU ";
				if (chkFriday)
					repeat = repeat + "FRI ";
				if (chkSaturday)
					repeat = repeat + "SAT ";
				if (chkSunday)
					repeat = repeat + "SUN ";
				if (chkWeekly)
					repeat = repeat + "(WEEKLY)";
			}
			txtrepeat.setText(repeat);
		}
		if (resultCode == VolumeActivity.RESULT_CODE) {
			int volume = data.getIntExtra("VOLUME", 0);
			txtvolume.setText(volume + "%");
			alarmDetails.volume = volume;
		}
		if (resultCode == AlarmListTypeTone.RESULTCODE) {
			String typetone = data.getStringExtra("TONETYPE");
			txttypetone.setText(typetone);
			alarmDetails.typetone = typetone;
			Log.d("FFFFFFFFFFFFFFF", typetone);
			if (alarmDetails.typetone.equalsIgnoreCase("Silent")) {
				ringToneContainer.setVisibility(View.GONE);
				alarmDetails.alarmTone = null;
				viewrow.setVisibility(View.GONE);
				if (alarmDetails.typetone.equalsIgnoreCase("Silent")) {
					ringToneContainer.setVisibility(View.GONE);
					viewrow.setVisibility(View.GONE);
					alarmDetails.alarmTone = null;
				}
			} else {
				ringToneContainer.setVisibility(View.VISIBLE);
			}
		}
		if (resultCode == TimePick.RESULTCODE) {
			Bundle bundle = data.getBundleExtra("DATATIME");
			alarmDetails.timeHour = bundle.getInt("HOUR");
			alarmDetails.timeMinute = bundle.getInt("MINUTE");
			int hour = alarmDetails.timeHour;
			int minute = alarmDetails.timeMinute;
			if (!AlarmListActivity.check()) {
				if (hour >= 12) {
					if (hour >= 13) {
						hour = hour - 12;
					}
					txttimeselect.setText(String.format("%02d:%02d pm", hour,
							minute));
				} else {
					txttimeselect.setText(String.format("%02d:%02d am", hour,
							minute));
				}
			} else {
				txttimeselect.setText(String.format("%02d:%02d", hour, minute));
			}

		}
		if (resultCode == AlarmSnoozemethod.RESULTCODE) {
			int methodsnooze = data.getIntExtra("METHODSNOOZE", 0);
			alarmDetails.snozeemethod = methodsnooze;
			if (methodsnooze == 3) {
				alarmDetails.snooze = 0;
				snoozeContainer.setVisibility(View.GONE);
				txtmethodsnooze.setText(getResources().getString(R.string.notsnooze));
			}
			if (methodsnooze == 0) {
				txtmethodsnooze.setText(getResources().getString(R.string.pressbutton));
				alarmDetails.snooze = 10;
				snoozeContainer.setVisibility(View.VISIBLE);
			}
			if (methodsnooze == 1) {
				txtmethodsnooze.setText(getResources().getString(R.string.mathmaking));
				alarmDetails.snooze = 10;
				snoozeContainer.setVisibility(View.VISIBLE);
			}
			if (methodsnooze == 2) {
				txtmethodsnooze.setText(getResources().getString(R.string.entercatcha));
				alarmDetails.snooze = 10;
				snoozeContainer.setVisibility(View.VISIBLE);
			}
			updatemathleve();
		}
		if (resultCode == Alarmdismissmethod.RESULTCODE) {
			int methoddismiss = data.getIntExtra("METHODDISMISS", 0);
			alarmDetails.dismissmethod = methoddismiss;
			if (methoddismiss == 0) {
				txtmethoddismiss.setText(getResources().getString(R.string.pressbutton));
			}
			if (methoddismiss == 1) {
				txtmethoddismiss.setText(getResources().getString(R.string.mathmaking));
			}
			if (methoddismiss == 2) {
				txtmethoddismiss.setText(getResources().getString(R.string.entercatcha));
			}
			updatemathleve();
		}
		if (resultCode == AlarmLevelMath.RESULTCODE) {
			int level = data.getIntExtra("LEVEL", 0);
			alarmDetails.mathlevel = level;
			if (alarmDetails.mathlevel == 0) {
				txtlevelmath.setText(getResources().getString(R.string.easy));
			} else if (alarmDetails.mathlevel == 1) {
				txtlevelmath.setText(getResources().getString(R.string.Average));
			} else if (alarmDetails.mathlevel == 2) {
				txtlevelmath.setText(getResources().getString(R.string.Difficult));
			} else {
				txtlevelmath.setText(getResources().getString(R.string.Difficultest));
			}
		}
	}

	private void updateModelFromLayout() {
		alarmDetails.name = edtName.getText().toString();
		alarmDetails.vibrate = chkvibrate.isChecked();
		alarmDetails.isEnabled = true;
	}

	private String getRealPathFromURI(Context context, Uri uri) {
		String fileName = "unknown";// default fileName
		Uri filePathUri = uri;
		if (uri.getScheme().toString().compareTo("content") == 0) {
			Cursor cursor = context.getContentResolver().query(uri, null, null,
					null, null);
			if (cursor.moveToFirst()) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				filePathUri = Uri.parse(cursor.getString(column_index));
				fileName = filePathUri.getLastPathSegment().toString();
			}
		} else if (uri.getScheme().compareTo("file") == 0) {
			fileName = filePathUri.getLastPathSegment().toString();
		} else {
			fileName = fileName + "_" + filePathUri.getLastPathSegment();
		}
		return fileName;
	}

	@Override
	public void onBackPressed() {
		this.finish();
	}
}
