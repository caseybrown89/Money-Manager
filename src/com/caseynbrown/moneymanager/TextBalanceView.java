package com.caseynbrown.moneymanager;


import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextBalanceView extends LinearLayout {
	private TextView mDate;
	private TextView mName;
	private TextView mAmount;
	private TextBalance mBox;

	public TextBalanceView(Context c, TextBalance tb){
		super(c);

		// Set orientation
		this.setOrientation(HORIZONTAL);
		this.setGravity(Gravity.CENTER);

		if (! tb.getDate().equals("")){
			mDate = new TextView(c);
			mDate.setText(tb.getDate());
			mDate.setGravity(Gravity.CENTER_HORIZONTAL);
			mDate.setTextColor(Color.BLACK);
			mDate.setFocusable(false);
			mDate.setPadding(0, 5, 10, 5);
			this.addView(mDate, new ViewGroup.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
		}

		// Set name
		mName = new TextView(c);

		mName.setText(tb.getName());
		mName.setGravity(Gravity.CENTER_HORIZONTAL);
		mName.setTextColor(Color.BLACK);
		mName.setFocusable(false);
		mName.setPadding(0, 5, 0, 5);
		addView(mName, new ViewGroup.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		// Set amount
		mAmount = new TextView(c);
		int amount = tb.getAmount();
		String formatted = HelperMethods.intToDollar(amount);
		mAmount.setText(formatted);
		mAmount.setGravity(Gravity.CENTER_HORIZONTAL);
		mAmount.setTextColor(Color.BLACK);
		mAmount.setFocusable(false);
		if (amount < 0){
			mAmount.setTextColor(Color.RED);
		} else {
			mAmount.setTextColor(Color.parseColor("#00A300"));
		}
		mAmount.setPadding(10, 0, 0, 0);
		addView(mAmount, new ViewGroup.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
	}

	public void setName(String name){
		mName.setText(name);
	}

	public void setDate(String date){
		this.mDate.setText(date);
	}

	public void setAmount(int amt){
		mAmount.setText(HelperMethods.intToDollar(amt));
	}

}
