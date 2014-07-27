package com.thao.alarmclock;

import android.provider.BaseColumns;

public final class AlarmContract {

	public AlarmContract() {
	}

	public static abstract class Alarm implements BaseColumns {
		public static final String TABLE_NAME = "alarm";
		public static final String COLUMN_NAME_ALARM_NAME = "name";
		public static final String COLUMN_NAME_ALARM_TIME_HOUR = "hour";
		public static final String COLUMN_NAME_ALARM_TIME_MINUTE = "minute";
		public static final String COLUMN_NAME_ALARM_REPEAT_DAYS = "days";
		public static final String COLUMN_NAME_ALARM_REPEAT_WEEKLY = "weekly";
		public static final String COLUMN_NAME_ALARM_TYPETONE = "typetone";
		public static final String COLUMN_NAME_ALARM_TONE = "tone";
		public static final String COLUMN_NAME_ALARM_SNOONE = "snooze";
		public static final String COLUMN_NAME_ALARM_SNOONEMETHOD = "snoozemethod";
		public static final String COLUMN_NAME_ALARM_DISMISSMETHOD = "dismissmethod";
		public static final String COLUMN_NAME_ALARM_MATHLEVEL = "mathlevel";
		public static final String COLUMN_NAME_ALARM_VIBRATE = "vibrate";
		public static final String COLUMN_NAME_ALARM_VOLUME = "volume";
		public static final String COLUMN_NAME_ALARM_ISSNOOE = "isnooze";
		public static final String COLUMN_NAME_ALARM_ENABLED = "enabled";
		
		public static final String TABLE_SETTING = "settings";
		public static final String COLUMN_NAME_LANGUAGE = "language";
		public static final String COLUMN_NAME_24H = "format24h";
	}

}
