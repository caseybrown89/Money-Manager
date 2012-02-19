package com.caseynbrown.moneymanager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextCheckboxView extends LinearLayout {
	private TextView mText;
	private ImageView mCheck;
	private TextCheckbox mBox;

	public TextCheckboxView(Context c, TextCheckbox text){
		super(c);
		this.mBox = text;

		// Set orientation
		this.setOrientation(HORIZONTAL);
		this.setGravity(Gravity.CENTER);

		// Set text
		mText = new TextView(c);

		mText.setText(text.getText());
		mText.setGravity(Gravity.CENTER_HORIZONTAL);
		mText.setTextColor(Color.BLACK);
		mText.setFocusable(false);
		mText.setPadding(0, 5, 0, 5);
		addView(mText, new ViewGroup.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		// If selected, add a check mark
		mCheck = new ImageView(c);
		if (text.getChecked()){
			mCheck.setImageDrawable(text.getIcon());
			mCheck.setPadding(10, 0, 0, 0);
			addView(mCheck, new ViewGroup.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
		}
	}

	public void setText(String text){
		mText.setText(text);
	}

	public void setIcon(Drawable check){
		mCheck.setImageDrawable(check);
	}
}
