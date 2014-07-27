package com.thao.alarmclock;

import com.trigg.alarmclock.R;

import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class VolumeActivity extends Activity {
	private TextView txtvolume;
	private SeekBar seekTime;
	private TextView textsave, textcancel;
	public static final int RESULT_CODE = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.volume_settings);
		txtvolume = (TextView) findViewById(R.id.txtvolume);
		textsave = (TextView) findViewById(R.id.txtsave);
		textsave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("VOLUME", seekTime.getProgress());
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
		seekTime = (SeekBar) findViewById(R.id.seek2);
		Bundle b = getIntent().getBundleExtra("DATA");
		long id = b.getLong("ID");
		int volume = b.getInt("VOLUME");
		if (id >= 0) {
			seekTime.setProgress(volume);
		} else {
			seekTime.setProgress(100);
			volume=100;
		}

		txtvolume.setText(volume + "%");
		seekTime.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				txtvolume.setText(seekTime.getProgress() + "%");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				txtvolume.setText(seekTime.getProgress() + "%");
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				txtvolume.setText(seekTime.getProgress() + " %");
			}
		});
	}
	@Override
	public void onBackPressed() {
		finish();
	}
}
