package com.thao.alarmclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.trigg.alarmclock.R;

public class RepeatListActivity extends Activity {
	private CustomSwitch chkWeekly;
	private CustomSwitch chkSunday;
	private CustomSwitch chkMonday;
	private CustomSwitch chkTuesday;
	private CustomSwitch chkWednesday;
	private CustomSwitch chkThursday;
	private CustomSwitch chkFriday;
	private CustomSwitch chkSaturday;
	private TextView textsave, textcancel;
	public static final int RESULT_CODE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.repeat_list);
		chkWeekly = (CustomSwitch) findViewById(R.id.alarm_details_repeat_weekly);
		chkSunday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_sunday);
		chkMonday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_monday);
		chkTuesday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_tuesday);
		chkWednesday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_wednesday);
		chkThursday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_thursday);
		chkFriday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_friday);
		chkSaturday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_saturday);
		textsave = (TextView) findViewById(R.id.txtsave);
		textsave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				 Intent returnIntent = new Intent();
				 Bundle bundle = new Bundle();
				 bundle.putBoolean("WEEKLY", chkWeekly.isChecked());
				 bundle.putBoolean("SUNDAY", chkSunday.isChecked());
				 bundle.putBoolean("MONDAY", chkMonday.isChecked());
				 bundle.putBoolean("TUESDAY", chkTuesday.isChecked());
				 bundle.putBoolean("WEDNESDAY", chkWednesday.isChecked());
				 bundle.putBoolean("THURSDAY", chkThursday.isChecked());
				 bundle.putBoolean("FRIDAY", chkFriday.isChecked());
				 bundle.putBoolean("SATURDAY", chkSaturday.isChecked());
				 returnIntent.putExtra("DATA", bundle);
				 setResult(RESULT_CODE, returnIntent);
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
		Bundle b = getIntent().getBundleExtra("DATAFROMLIST");
		boolean Weekly = b.getBoolean("WEEKLY");
		boolean Sunday = b.getBoolean("SUNDAY");
		boolean Monday = b.getBoolean("MONDAY");
		boolean Tuesday = b.getBoolean("TUESDAY");
		boolean Wednesday = b.getBoolean("WEDNESDAY");
		boolean Thursday = b.getBoolean("THURSDAY");
		boolean Friday = b.getBoolean("FRIDAY");
		boolean Saturday = b.getBoolean("SATURDAY");
		chkWeekly.setChecked(Weekly);
		chkSunday.setChecked(Sunday);
		chkMonday.setChecked(Monday);
		chkTuesday.setChecked(Tuesday);
		chkWednesday.setChecked(Wednesday);
		chkThursday.setChecked(Thursday);
		chkFriday.setChecked(Friday);
		chkSaturday.setChecked(Saturday);

	}
	@Override
	public void onBackPressed() {
		finish();
	}
}
