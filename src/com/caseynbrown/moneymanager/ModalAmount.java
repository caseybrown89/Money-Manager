package com.caseynbrown.moneymanager;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ModalAmount extends Dialog {
	public interface ReadyListener{
		public void ready(boolean negative, String string);
	}

	private boolean negative;
	private TextView iOwe, theyOwe;
	private CheckBox iOweCheck, theyOweCheck;
	private EditText amount;
	private ReadyListener readyListener;

	public ModalAmount(Context c, ReadyListener readyListener){
		super(c);
		this.readyListener = readyListener;
	}

	public boolean amountEmpty(){
		if (this.amount.getText().toString().equals("")){
			return true;
		} else {
			return false;
		}
	}

	public String getAmount(){
		return this.amount.getText().toString();
	}

	public boolean getNegative(){
		return this.negative;
	}

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.oweview);
		setTitle(R.string.owe_Title);

		this.iOwe = ((TextView) findViewById(R.id.owe_i));
		this.theyOwe = ((TextView) findViewById(R.id.owe_they));
		this.iOweCheck = ((CheckBox) findViewById(R.id.owe_iCheck));
		this.theyOweCheck = ((CheckBox) findViewById(R.id.owe_theyCheck));
		this.amount = ((EditText) findViewById(R.id.owe_amount));

		this.amount.setEnabled(false);
		this.amount.clearFocus();

	    // Set restriction on the amount of decimal digits on the amount text box
	    this.amount.setFilters(new InputFilter[]{new MoneyValueFilter()});

		// Click listeners Owe options
		this.iOwe.setOnClickListener(
				new TextView.OnClickListener(){
					@Override
					public void onClick(View v) {
						checkIOwe();
					}
				});

		this.iOweCheck.setOnClickListener(
				new CheckBox.OnClickListener(){
					@Override
					public void onClick(View v) {
						checkIOwe();
					}
				});

		this.theyOwe.setOnClickListener(
				new TextView.OnClickListener(){
					@Override
					public void onClick(View v) {
						checkTheyOwe();
					}
				});

		this.theyOweCheck.setOnClickListener(
				new CheckBox.OnClickListener(){
					@Override
					public void onClick(View v) {
						checkTheyOwe();
					}
				});

		Button doneButton = ((Button) findViewById(R.id.owe_done));
		doneButton.setOnClickListener(new ModalListener());

	}

	protected void checkIOwe(){
		this.theyOweCheck.setChecked(false);
		this.iOweCheck.setChecked(true);
		this.negative = true;
		this.amount.setEnabled(true);
	}

	protected void checkTheyOwe(){
		this.theyOweCheck.setChecked(true);
		this.iOweCheck.setChecked(false);
		this.negative = false;
		this.amount.setEnabled(true);
	}

	// Nested Class for dialog
	protected class ModalListener implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			System.out.println(amountEmpty());
			if (amountEmpty()){
				// display toast
				System.out.println("Must provide amount");
			} else if (! HelperMethods.validAmount(getAmount())) {
				System.out.println("Must provide valid amount");
			} else {
				readyListener.ready(getNegative(), getAmount());
				ModalAmount.this.dismiss();
			}
		}
	}
}
