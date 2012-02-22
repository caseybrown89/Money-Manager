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
	private EditText amountEdit;
	private ReadyListener readyListener;
	private String amount;
	
	public ModalAmount(Context c, ReadyListener readyListener, String amount, boolean negative){
		super(c);
		this.readyListener = readyListener;
		this.amount = amount;
		this.negative = negative;
	}

	public boolean amountEmpty(){
		if (this.amountEdit.getText().toString().equals("")){
			return true;
		} else {
			return false;
		}
	}

	public String getAmount(){
		return this.amountEdit.getText().toString();
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
		this.amountEdit = ((EditText) findViewById(R.id.owe_amount));


		/* Persist previous selection if modal revisited */
		if (! this.amount.equals("")){
			this.amountEdit.setText(this.amount);
			if (this.negative){
				checkNegative();
			} else {
				checkCredit();
			}
		} else {
			this.amountEdit.setEnabled(false);
			this.amountEdit.clearFocus();
		}
		
	    // Set restriction on the amount of decimal digits on the amount text box
	    this.amountEdit.setFilters(new InputFilter[]{new MoneyValueFilter()});

		// Click listeners Owe options
		this.iOwe.setOnClickListener(
				new TextView.OnClickListener(){
					@Override
					public void onClick(View v) {
						checkNegative();
					}
				});

		this.iOweCheck.setOnClickListener(
				new CheckBox.OnClickListener(){
					@Override
					public void onClick(View v) {
						checkNegative();
					}
				});

		this.theyOwe.setOnClickListener(
				new TextView.OnClickListener(){
					@Override
					public void onClick(View v) {
						checkCredit();
					}
				});

		this.theyOweCheck.setOnClickListener(
				new CheckBox.OnClickListener(){
					@Override
					public void onClick(View v) {
						checkCredit();
					}
				});
		
		Button doneButton = ((Button) findViewById(R.id.owe_done));
		doneButton.setOnClickListener(new ModalListener());
	}

	protected void checkNegative(){
		this.theyOweCheck.setChecked(false);
		this.iOweCheck.setChecked(true);
		this.negative = true;
		this.amountEdit.setEnabled(true);
	}

	protected void checkCredit(){
		this.theyOweCheck.setChecked(true);
		this.iOweCheck.setChecked(false);
		this.negative = false;
		this.amountEdit.setEnabled(true);
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
