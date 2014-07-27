package com.thao.alarmclock;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thao.alarmclock.helper.AlarmDBHelper;
import com.thao.alarmclock.model.SettingModel;
import com.trigg.alarmclock.R;

public class AlarmSetting extends Activity {
	private LinearLayout layoutngonngu, layoutformat;
	private TextView textngonngu;
	private ImageView imageback;
	private CheckBox checkformat24h;
	AlarmDBHelper dbHelper;
	private SettingModel model;
	private static final int REQUESTCODE = 1;
	boolean check;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		dbHelper = new AlarmDBHelper(getApplicationContext());
		model = dbHelper.getSetting(1);
		textngonngu = (TextView) findViewById(R.id.label_language);
		imageback = (ImageView) findViewById(R.id.imageback);
		checkformat24h = (CheckBox) findViewById(R.id.check_format24h);
		checkformat24h.setChecked(model.isFormat());
		imageback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateform();
				dbHelper.updateSetting(model);
				Log.d("TRUEEEEEEEEEEE", "" + model.isFormat());
				Intent i = new Intent(getApplicationContext(),
						AlarmListActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			}

		});
		layoutformat = (LinearLayout) findViewById(R.id.setting_format24h_container);
		layoutformat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkformat24h.isChecked())
					checkformat24h.setChecked(false);
				else
					checkformat24h.setChecked(true);
			}
		});
		layoutngonngu = (LinearLayout) findViewById(R.id.setting_language_container);
		layoutngonngu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						Alarmsettinglanguage.class);
				i.putExtra("LANGUAGE", model.getLanguage());
				startActivityForResult(i, REQUESTCODE);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		setting();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Alarmsettinglanguage.RESULTCODE) {
			int language = data.getIntExtra("LANGUAGE", 0);
			model.setLanguage(language);
			setting();
			refresh();
		}
	}

	private void setting() {
		if (model.getLanguage() == 0) {
			textngonngu.setText(getResources().getString(R.string.Language)
					+ "    " + getResources().getString(R.string.defau));
			Locale locale = new Locale("en_US");
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getApplicationContext().getResources().updateConfiguration(config,
					null);
		} else if (model.getLanguage() == 1) {
			textngonngu.setText(getResources().getString(R.string.Language)
					+ "    " + getResources().getString(R.string.English));
			Locale locale = new Locale("en_US");
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getApplicationContext().getResources().updateConfiguration(config,
					null);
		} else {
			textngonngu.setText(getResources().getString(R.string.Language)
					+ "    " + getResources().getString(R.string.VietNam));
			Locale locale = new Locale("vi");
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getApplicationContext().getResources().updateConfiguration(config,
					null);
		}
		dbHelper.updateSetting(model);
	}

	private void updateform() {
		if (checkformat24h.isChecked()) {
			model.setFormat(true);
		} else {
			model.setFormat(false);
		}
	}

	@Override
	public void onBackPressed() {
		updateform();
		dbHelper.updateSetting(model);
		Intent i = new Intent(getApplicationContext(), AlarmListActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}

	private void refresh() {
		finish();
		Intent myIntent = new Intent(AlarmSetting.this, AlarmSetting.class);
		startActivity(myIntent);
	}
}
