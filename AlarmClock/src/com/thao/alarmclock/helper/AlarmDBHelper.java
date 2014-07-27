package com.thao.alarmclock.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.thao.alarmclock.AlarmContract.Alarm;
import com.thao.alarmclock.AlarmSetting;
import com.thao.alarmclock.model.AlarmModel;
import com.thao.alarmclock.model.SettingModel;

public class AlarmDBHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "alarmclock.db";

	private static final String SQL_CREATE_ALARM = "CREATE TABLE "
			+ Alarm.TABLE_NAME + " (" + Alarm._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ Alarm.COLUMN_NAME_ALARM_NAME + " TEXT,"
			+ Alarm.COLUMN_NAME_ALARM_TIME_HOUR + " INTEGER,"
			+ Alarm.COLUMN_NAME_ALARM_TIME_MINUTE + " INTEGER,"
			+ Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS + " TEXT,"
			+ Alarm.COLUMN_NAME_ALARM_REPEAT_WEEKLY + " BOOLEAN,"
			+ Alarm.COLUMN_NAME_ALARM_TONE + " TEXT,"
			+ Alarm.COLUMN_NAME_ALARM_SNOONE + " INTEGER,"
			+ Alarm.COLUMN_NAME_ALARM_SNOONEMETHOD + " INTEGER,"
			+ Alarm.COLUMN_NAME_ALARM_DISMISSMETHOD + " INTEGER,"
			+ Alarm.COLUMN_NAME_ALARM_MATHLEVEL + " INTEGER,"
			+ Alarm.COLUMN_NAME_ALARM_ISSNOOE + " BOOLEAN,"
			+ Alarm.COLUMN_NAME_ALARM_VOLUME + " INTEGER,"
			+ Alarm.COLUMN_NAME_ALARM_TYPETONE + " TEXT,"
			+ Alarm.COLUMN_NAME_ALARM_VIBRATE + " BOOLEAN,"
			+ Alarm.COLUMN_NAME_ALARM_ENABLED + " BOOLEAN" + " )";
	private static final String SQL_CREATE_SETTINGS = "CREATE TABLE "
			+ Alarm.TABLE_SETTING + " (" + Alarm._ID + " INTEGER PRIMARY KEY,"
			+ Alarm.COLUMN_NAME_LANGUAGE + " INTEGER," + Alarm.COLUMN_NAME_24H

			+ " BOOLEAN" + " )";

	private static final String SQL_DELETE_ALARM = "DROP TABLE IF EXISTS "
			+ Alarm.TABLE_NAME;
	private static final String SQL_DELETE_SETTING = "DROP TABLE IF EXISTS "
			+ Alarm.TABLE_SETTING;

	public AlarmDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ALARM);
		db.execSQL(SQL_CREATE_SETTINGS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ALARM);
		db.execSQL(SQL_DELETE_SETTING);
		onCreate(db);
	}

	private SettingModel populateSetting(Cursor c) {
		SettingModel model = new SettingModel();
		model.setId(c.getInt(c.getColumnIndex(Alarm._ID)));
		model.setLanguage(c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_LANGUAGE)));
		model.setFormat(c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_24H)) == 0 ? false
				: true);
		return model;
	}

	private AlarmModel populateModel(Cursor c) {
		AlarmModel model = new AlarmModel();
		model.id = c.getLong(c.getColumnIndex(Alarm._ID));
		model.name = c
				.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_NAME));
		model.timeHour = c.getInt(c
				.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TIME_HOUR));
		model.timeMinute = c.getInt(c
				.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TIME_MINUTE));
		model.repeatWeekly = c.getInt(c
				.getColumnIndex(Alarm.COLUMN_NAME_ALARM_REPEAT_WEEKLY)) == 0 ? false
				: true;
		model.alarmTone = c.getString(c
				.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TONE)) != "" ? Uri
				.parse(c.getString(c
						.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TONE))) : null;
		model.snooze = c.getInt(c
				.getColumnIndex(Alarm.COLUMN_NAME_ALARM_SNOONE));
		model.snozeemethod = c.getInt(c
				.getColumnIndex(Alarm.COLUMN_NAME_ALARM_SNOONEMETHOD));
		model.dismissmethod = c.getInt(c
				.getColumnIndex(Alarm.COLUMN_NAME_ALARM_DISMISSMETHOD));
		model.mathlevel = c.getInt(c
				.getColumnIndex(Alarm.COLUMN_NAME_ALARM_MATHLEVEL));
		model.isnooze = c.getInt(c
				.getColumnIndex(Alarm.COLUMN_NAME_ALARM_ISSNOOE)) == 0 ? false
				: true;
		model.vibrate = c.getInt(c
				.getColumnIndex(Alarm.COLUMN_NAME_ALARM_VIBRATE)) == 0 ? false
				: true;
		model.volume = c.getInt(c
				.getColumnIndex(Alarm.COLUMN_NAME_ALARM_VOLUME));
		model.typetone = c.getString(c
				.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TYPETONE));

		model.isEnabled = c.getInt(c
				.getColumnIndex(Alarm.COLUMN_NAME_ALARM_ENABLED)) == 0 ? false
				: true;

		String[] repeatingDays = c.getString(
				c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS)).split(
				",");
		for (int i = 0; i < repeatingDays.length; ++i) {
			model.setRepeatingDay(i, repeatingDays[i].equals("false") ? false
					: true);
		}

		return model;
	}

	private ContentValues populateContent(AlarmModel model) {
		ContentValues values = new ContentValues();
		values.put(Alarm.COLUMN_NAME_ALARM_NAME, model.name);
		values.put(Alarm.COLUMN_NAME_ALARM_TIME_HOUR, model.timeHour);
		values.put(Alarm.COLUMN_NAME_ALARM_TIME_MINUTE, model.timeMinute);
		values.put(Alarm.COLUMN_NAME_ALARM_REPEAT_WEEKLY, model.repeatWeekly);
		values.put(Alarm.COLUMN_NAME_ALARM_TONE,
				model.alarmTone != null ? model.alarmTone.toString() : "");
		values.put(Alarm.COLUMN_NAME_ALARM_SNOONE, model.snooze);
		values.put(Alarm.COLUMN_NAME_ALARM_ISSNOOE, model.isnooze);
		values.put(Alarm.COLUMN_NAME_ALARM_SNOONEMETHOD, model.snozeemethod);
		values.put(Alarm.COLUMN_NAME_ALARM_DISMISSMETHOD, model.dismissmethod);
		values.put(Alarm.COLUMN_NAME_ALARM_MATHLEVEL, model.mathlevel);

		values.put(Alarm.COLUMN_NAME_ALARM_TYPETONE, model.typetone);
		values.put(Alarm.COLUMN_NAME_ALARM_VOLUME, model.volume);
		values.put(Alarm.COLUMN_NAME_ALARM_VIBRATE, model.vibrate);
		values.put(Alarm.COLUMN_NAME_ALARM_ENABLED, model.isEnabled);

		String repeatingDays = "";
		for (int i = 0; i < 7; ++i) {
			repeatingDays += model.getRepeatingDay(i) + ",";
		}
		values.put(Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS, repeatingDays);

		return values;
	}

	private ContentValues populateSetting(SettingModel model) {
		ContentValues values = new ContentValues();
		values.put(Alarm._ID, model.getId());
		values.put(Alarm.COLUMN_NAME_LANGUAGE, model.getLanguage());
		values.put(Alarm.COLUMN_NAME_24H, model.isFormat());
		return values;
	}

	public long createsetting(SettingModel model) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = populateSetting(model);
		return db.insert(Alarm.TABLE_SETTING, null, values);
	}

	public int updateSetting(SettingModel model) {
		ContentValues values = populateSetting(model);
		return getWritableDatabase().update(Alarm.TABLE_SETTING, values,
				Alarm._ID + " = ?",
				new String[] { String.valueOf(model.getId()) });
	}

	public long createAlarm(AlarmModel model) {
		ContentValues values = populateContent(model);
		return getWritableDatabase().insert(Alarm.TABLE_NAME, null, values);
	}

	public long updateAlarm(AlarmModel model) {
		ContentValues values = populateContent(model);
		return getWritableDatabase().update(Alarm.TABLE_NAME, values,
				Alarm._ID + " = ?", new String[] { String.valueOf(model.id) });
	}

	public AlarmModel getAlarm(long id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String select = "SELECT * FROM " + Alarm.TABLE_NAME + " WHERE "
				+ Alarm._ID + " = " + id;

		Cursor c = db.rawQuery(select, null);

		if (c.moveToNext()) {
			return populateModel(c);
		}

		return null;
	}

	public SettingModel getSetting(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String select = "SELECT * FROM " + Alarm.TABLE_SETTING + " WHERE "
				+ Alarm._ID + " = " + id;

		Cursor c = db.rawQuery(select, null);

		if (c.moveToNext()) {
			return populateSetting(c);
		}

		return null;
	}

	public boolean kiemtra() {
		SQLiteDatabase db = this.getReadableDatabase();
		String select = "SELECT * FROM " + Alarm.TABLE_SETTING;
		Cursor c = db.rawQuery(select, null);
		if (c.getCount() == 0) {
			return true;
		}
		return false;

	}

	public List<AlarmModel> getAlarms() {
		SQLiteDatabase db = this.getReadableDatabase();

		String select = "SELECT * FROM " + Alarm.TABLE_NAME + " ORDER BY "
				+ Alarm.COLUMN_NAME_ALARM_TIME_HOUR + ","
				+ Alarm.COLUMN_NAME_ALARM_TIME_MINUTE + " ASC";

		Cursor c = db.rawQuery(select, null);

		List<AlarmModel> alarmList = new ArrayList<AlarmModel>();

		while (c.moveToNext()) {
			alarmList.add(populateModel(c));
		}

		if (!alarmList.isEmpty()) {
			return alarmList;
		}
		
		return null;
	}

	public int deleteAlarm(long id) {
		return getWritableDatabase().delete(Alarm.TABLE_NAME,
				Alarm._ID + " = ?", new String[] { String.valueOf(id) });
	}
}
