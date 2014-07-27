package com.thao.alarmclock.model;

import android.net.Uri;

public class AlarmModel {

	public static final int SUNDAY = 0;
	public static final int MONDAY = 1;
	public static final int TUESDAY = 2;
	public static final int WEDNESDAY = 3;
	public static final int THURSDAY = 4;
	public static final int FRDIAY = 5;
	public static final int SATURDAY = 6;

	public long id = -1;
	public int timeHour;
	public int timeMinute;
	private boolean repeatingDays[];
	public boolean repeatWeekly;
	public Uri alarmTone;
	public String name;
	public boolean isEnabled;
	public String typetone;
	public int snozeemethod;
	public int dismissmethod;
	public int mathlevel;
	public int snooze;
	public boolean vibrate;
	public int volume;
	public boolean isnooze;
	public AlarmModel(long id1, int timeHour, int timeMinute, Uri alarmTone, String name,
			int snooze) {
		super();
		this.id=id1;
		this.timeHour = timeHour;
		this.timeMinute = timeMinute;
		this.alarmTone = alarmTone;
		this.name = name;
		this.snooze = snooze;
	}

	public AlarmModel() {
		repeatingDays = new boolean[7];
	}

	public void setRepeatingDay(int dayOfWeek, boolean value) {
		repeatingDays[dayOfWeek] = value;
	}

	public boolean getRepeatingDay(int dayOfWeek) {
		return repeatingDays[dayOfWeek];
	}

}
