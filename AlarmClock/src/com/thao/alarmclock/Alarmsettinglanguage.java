package com.thao.alarmclock;

import java.util.Locale;

import com.trigg.alarmclock.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Alarmsettinglanguage extends Activity {
	private String[] type;
	public static final int RESULTCODE = 0;
	private TextView textCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snoozemethod);
		type = new String[] { getResources().getString(R.string.defau),
				getResources().getString(R.string.English),
				getResources().getString(R.string.VietNam) };
		textCancel = (TextView) findViewById(R.id.txtcancel);
		textCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra("LANGUAGE", 0);
				setResult(RESULTCODE, i);
				finish();
			}
		});
		ListView lv = (ListView) findViewById(R.id.lvsnoozemethod);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(),
				android.R.layout.simple_list_item_single_choice, type);
		lv.setAdapter(adapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		int language = getIntent().getIntExtra("LANGUAGE", 0);
		if (language == 0) {
			lv.setItemChecked(0, true);
		} else if (language == 1) {
			lv.setItemChecked(1, true);
		} else if (language == 2) {
			lv.setItemChecked(2, true);
		}
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
				} else if (position == 1) {
					Locale locale = new Locale("en_US");
					Locale.setDefault(locale);
					Configuration config = new Configuration();
					config.locale = locale;
					getApplicationContext().getResources().updateConfiguration(
							config, null);
				} else {
					Locale locale = new Locale("vi");
					Locale.setDefault(locale);
					Configuration config = new Configuration();
					config.locale = locale;
					getApplicationContext().getResources().updateConfiguration(
							config, null);
				}
				Intent i = new Intent();
				i.putExtra("LANGUAGE", position);
				setResult(RESULTCODE, i);
				finish();
			}

		});
	}
}
