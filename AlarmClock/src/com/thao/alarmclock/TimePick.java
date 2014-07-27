package com.thao.alarmclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.thao.alarmclock.helper.AlarmDBHelper;
import com.trigg.alarmclock.R;

public class TimePick extends Activity {
	private TimePicker timePicker;
	private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
	public static final int RESULTCODE = 7;
	private TextView textsave, textcancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timepicker);
		timePicker = (TimePicker) findViewById(R.id.alarm_details_time_picker);
		if (AlarmListActivity.check()) {
			timePicker.setIs24HourView(true);
			Log.d("HELLLLLO", "true");
		}
		textsave = (TextView) findViewById(R.id.txtsave);
		textsave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putInt("HOUR", timePicker.getCurrentHour().intValue());
				bundle.putInt("MINUTE", timePicker.getCurrentMinute()
						.intValue());
				Intent intent = new Intent();
				intent.putExtra("DATATIME", bundle);
				setResult(RESULTCODE, intent);
				finish();
			}
		});
		textcancel = (TextView) findViewById(R.id.txtCancel);
		textcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Bundle b = getIntent().getBundleExtra("DATA");
		long id = b.getLong("ID");
		int hour = b.getInt("HOUR");
		Log.d("HOURRRRRRRRR",""+ hour);
		int minute = b.getInt("MINUTE");
		timePicker.setCurrentMinute(minute);
		timePicker.setCurrentHour(hour);

	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
