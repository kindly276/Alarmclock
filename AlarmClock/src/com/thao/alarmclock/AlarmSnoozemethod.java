package com.thao.alarmclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.trigg.alarmclock.R;

public class AlarmSnoozemethod extends Activity {

	private String[] type;
	public static final int RESULTCODE = 6;
	private TextView textCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		type = new String[] { getResources().getString(R.string.pressbutton),
				getResources().getString(R.string.mathmaking),
				getResources().getString(R.string.entercatcha),
				getResources().getString(R.string.notsnooze) };
		setContentView(R.layout.snoozemethod);
		textCancel = (TextView) findViewById(R.id.txtcancel);
		textCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ListView lv = (ListView) findViewById(R.id.lvsnoozemethod);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(),
				android.R.layout.simple_list_item_single_choice, type);
		lv.setAdapter(adapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		int method = getIntent().getIntExtra("METHODSNOOZE", 0);
		if (method == 0) {
			lv.setItemChecked(0, true);
		} else if (method == 1) {
			lv.setItemChecked(1, true);
		} else if (method == 2) {
			lv.setItemChecked(2, true);
		}
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent();
				i.putExtra("METHODSNOOZE", position);
				setResult(RESULTCODE, i);
				finish();
			}

		});
	}
}