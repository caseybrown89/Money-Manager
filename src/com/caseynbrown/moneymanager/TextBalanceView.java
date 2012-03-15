package com.caseynbrown.moneymanager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TextBalanceView extends LinearLayout {
	private TextView mDate;
	private TextView mName;
	private TextView mAmount;
	private TextView mLatest;
	private TextView mLatestAmount;

	public TextBalanceView(Context c, TextBalance tb){
		super(c);

		LayoutInflater i = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if (! tb.getDate().equals("")){
			/* We are displaying a balance view of entries for a specific person */
			
			this.addView(i.inflate(R.layout.individualbalancelist, null));
			
			this.mDate = (TextView) findViewById(R.id.indivList_date);
			this.mName = (TextView) findViewById(R.id.indivList_title);
			this.mAmount = (TextView) findViewById(R.id.indivList_amount);
			
			this.mDate.setText(tb.getDate());
			this.mName.setText(tb.getName());
			
			/* Set amount */
			int amount = tb.getBalance();
			String formatted = HelperMethods.intToDollar(amount);
			this.mAmount.setText(formatted);
			
			/* Change color of balance accordingly */
			if (amount < 0){
				this.mAmount.setTextColor(Color.RED);
			} else {
				this.mAmount.setTextColor(Color.parseColor(getResources().getString(R.string.defaultGreenColor)));
			}
		} else {
			/* We are displaying the balance view of all people */
			
			this.addView(i.inflate(R.layout.balanceviewlist, null));
			
			this.mName = (TextView) findViewById(R.id.balancelist_rowName);
			this.mAmount = (TextView) findViewById(R.id.balancelist_rowAmount);
			this.mLatest = (TextView) findViewById(R.id.balancelist_entryDateWhere);
			
			/* Set name */
			mName.setText(tb.getName());
			
			/* Set amount */
			int amount = tb.getBalance();
			String formatted = HelperMethods.intToDollar(amount);
			mAmount.setText(formatted);
			
			/* Change color of balance accordingly */
			if (amount < 0){
				mAmount.setTextColor(Color.RED);
			} else {
				mAmount.setTextColor(Color.parseColor(getResources().getString(R.string.defaultGreenColor)));
			}
			
			/* Set Latest entry information */
			if (tb.getLatestDate() != null){
				String latest = tb.getLatestDate()+" "+tb.getLatestWhere();
				mLatest.setText(latest);
				
				this.mLatestAmount = (TextView) findViewById(R.id.balancelist_entryAmount);
				int amountLatestEntry = tb.getLatestAmount();
				mLatestAmount.setText(HelperMethods.intToDollar(amountLatestEntry));
				if (amountLatestEntry < 0){
					mLatestAmount.setTextColor(Color.RED);
				} else {
					mLatestAmount.setTextColor(Color.parseColor(getResources().getString(R.string.defaultGreenColor)));
				}
			} else {
				/* Sets the 'No Entries' to italics */
				this.mLatest.setTypeface(null, Typeface.ITALIC);
			}
		}
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
	
	public void setLatest(String date, String where){
		mLatest.setText(date+" "+where);
	}
	
	public void setLatestAmount(int amount){
		mLatestAmount.setText(HelperMethods.intToDollar(amount));
	}

}
