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

public class AlarmListTypeTone extends Activity {
	private String[] type = new String[] { "Silent", "Ringtone", "Music" };
	public static final int RESULTCODE = 3;
	private TextView textCancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dimension = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dimension);
		setContentView(R.layout.acitivy_tonetype);
		textCancel=(TextView)findViewById(R.id.txtcancel);
		textCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ListView lv = (ListView) findViewById(R.id.lvtypetone);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(),
				android.R.layout.simple_list_item_single_choice, type);
		lv.setAdapter(adapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		String typetone = getIntent().getStringExtra("TYPETONE");
		if (typetone.equals("Silent")) {
			lv.setItemChecked(0, true);
		} else if (typetone.equals("Ringtone")) {
			lv.setItemChecked(1, true);
		} else if (typetone.equals("Music")) {
			lv.setItemChecked(2, true);
		}
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent();
				i.putExtra("TONETYPE", type[position].toString());
				setResult(RESULTCODE, i);
				finish();
			}

		});
	}
}
