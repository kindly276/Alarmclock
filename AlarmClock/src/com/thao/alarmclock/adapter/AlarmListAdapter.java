package com.thao.alarmclock.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.thao.alarmclock.AlarmListActivity;
import com.thao.alarmclock.helper.AlarmDBHelper;
import com.thao.alarmclock.model.AlarmModel;
import com.thao.alarmclock.model.SettingModel;
import com.trigg.alarmclock.R;

public class AlarmListAdapter extends BaseAdapter {
	AlarmDBHelper dbHelper;
	public Context mContext;
	private List<AlarmModel> mAlarms;

	public AlarmListAdapter(Context context, List<AlarmModel> alarms) {
		mContext = context;
		mAlarms = alarms;
	}

	public void setAlarms(List<AlarmModel> alarms) {
		mAlarms = alarms;
	}

	@Override
	public int getCount() {
		if (mAlarms != null) {
			return mAlarms.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mAlarms != null) {
			return mAlarms.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (mAlarms != null) {
			return mAlarms.get(position).id;
		}
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.alarm_list_item, parent, false);
		}
		dbHelper = new AlarmDBHelper(mContext);
		SettingModel setting = dbHelper.getSetting(1);
		final AlarmModel model = (AlarmModel) getItem(position);

		TextView txtTime = (TextView) view.findViewById(R.id.alarm_item_time);
		TextView txtap = (TextView) view.findViewById(R.id.alarm_item_ap);
		if (setting.isFormat() == false) {
			if (model.timeHour >= 12) {
				if (model.timeHour >= 13) {
					model.timeHour = model.timeHour - 12;
				}
				txtTime.setText(String.format("%02d:%02d", model.timeHour,
						model.timeMinute));
				txtap.setText("pm");
			} else {
				txtTime.setText(String.format("%02d:%02d", model.timeHour,
						model.timeMinute));
				txtap.setText("am");
			}
		} else {
			txtTime.setText(String.format("%02d:%02d", model.timeHour,
					model.timeMinute));
		}
		TextView txtName = (TextView) view.findViewById(R.id.alarm_item_name);
		txtName.setText(model.name);
		updateTextColor((TextView) view.findViewById(R.id.alarm_item_sunday),
				model.getRepeatingDay(AlarmModel.SUNDAY));
		updateTextColor((TextView) view.findViewById(R.id.alarm_item_monday),
				model.getRepeatingDay(AlarmModel.MONDAY));
		updateTextColor((TextView) view.findViewById(R.id.alarm_item_tuesday),
				model.getRepeatingDay(AlarmModel.TUESDAY));
		updateTextColor(
				(TextView) view.findViewById(R.id.alarm_item_wednesday),
				model.getRepeatingDay(AlarmModel.WEDNESDAY));
		updateTextColor((TextView) view.findViewById(R.id.alarm_item_thursday),
				model.getRepeatingDay(AlarmModel.THURSDAY));
		updateTextColor((TextView) view.findViewById(R.id.alarm_item_friday),
				model.getRepeatingDay(AlarmModel.FRDIAY));
		updateTextColor((TextView) view.findViewById(R.id.alarm_item_saturday),
				model.getRepeatingDay(AlarmModel.SATURDAY));

		ToggleButton btnToggle = (ToggleButton) view
				.findViewById(R.id.alarm_item_toggle);
		btnToggle.setChecked(model.isEnabled);
		btnToggle.setTag(Long.valueOf(model.id));
		final TextView txtsnooze = (TextView) view
				.findViewById(R.id.alarm_item_snooze);
		btnToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				txtsnooze.setText("");
				if (model.isEnabled) {
					model.isEnabled = false;
				} else {
					model.isEnabled = true;
				}
				((AlarmListActivity) mContext).setAlarmEnabled(model.id,
						model.isEnabled);
			}
		});
		final ImageView imagedelete = (ImageView) view
				.findViewById(R.id.imgdelete);
		imagedelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((AlarmListActivity) mContext).deleteAlarm(model.id);
			}
		});
		Log.d("AMMMMMMMMMMM", "" + model.isnooze);
		if (model.isnooze) {
			Log.d("SNNOOOOOOOOOO", model.isnooze + "");
			txtsnooze.setText("Snoozed!");
		}
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (model.isnooze) {
					((AlarmListActivity) mContext)
							.startAlarmWaitingActivity(model.id);

				} else {
					((AlarmListActivity) mContext)
							.startAlarmDetailsActivity(model.id);
				}
			}
		});

		return view;
	}

	private void updateTextColor(TextView view, boolean isOn) {
		if (isOn) {
			view.setTextColor(Color.GREEN);
		} else {
			view.setTextColor(Color.BLACK);
		}
	}

}
