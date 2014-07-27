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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.trigg.alarmclock.R;

public class SnoozeAtivity extends Activity {
	private TextView txttime;
	private SeekBar seekTime;
	public static final int RESULT_CODE = 4;
	private TextView textsave, textcancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snooze_settings);
		txttime = (TextView) findViewById(R.id.txtsnooze);
		seekTime = (SeekBar) findViewById(R.id.seek1);
		textsave = (TextView) findViewById(R.id.txtsave);
		textsave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("TIMESOONE", seekTime.getProgress());
				setResult(RESULT_CODE, intent);
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
		int snooze = b.getInt("SNOOZE");
		if (id >= 0) {
			seekTime.setProgress(snooze);
		} else {
			seekTime.setProgress(10);
			snooze = 10;
		}
		txttime.setText(snooze + " "+getResources().getString(R.string.minutes));
		seekTime.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				txttime.setText(seekTime.getProgress() + " "+getResources().getString(R.string.minutes));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				txttime.setText(seekTime.getProgress() + " "+getResources().getString(R.string.minutes));
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				txttime.setText(seekTime.getProgress() + " "+getResources().getString(R.string.minutes));
			}
		});
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
