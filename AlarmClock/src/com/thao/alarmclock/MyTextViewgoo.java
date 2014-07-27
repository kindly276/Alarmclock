package com.thao.alarmclock;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextViewgoo extends TextView {
	public MyTextViewgoo(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MyTextViewgoo(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyTextViewgoo(Context context) {
		super(context);
		init();
	}

	private void init() {
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/spooky night.ttf");
		setTypeface(tf);
	}
}
