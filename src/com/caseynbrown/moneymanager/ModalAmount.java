package com.caseynbrown.moneymanager;

import static com.caseynbrown.moneymanager.ModalAmountConstants.NEW_ENTRY;
import static com.caseynbrown.moneymanager.ModalAmountConstants.NEW_USER;
import static com.caseynbrown.moneymanager.ModalAmountConstants.UPDATE_BALANCE_NEGATIVE;
import static com.caseynbrown.moneymanager.ModalAmountConstants.UPDATE_BALANCE_POSITIVE;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/* This class represents a modal pop-up where the user can enter amounts for entries.  It is
 * called from the NewEntryActivity, the PeopleAddActivity, and the EditBalance Activity.
 */
public class ModalAmount extends Dialog {
	public interface ReadyListener{
		public void ready(boolean negative, String string);
	}

	/* These variables contain the amount entered and whether the entry will be a credit or debit
	 * If the amount String is != "" on instantiation, the form fields are automatically filled
	 * with these values.
	 */
	private boolean negative;
	private String amount;

	/* Form inputs */
	private TextView neg, pos;
	private CheckBox iOweCheck, theyOweCheck;
	private EditText amountEdit;

	/* Returns the negative and amount variables to the Activity from which this was called */
	private ReadyListener readyListener;

	/* Determines which set of strings should be displayed next to the check boxes */
	int displayStrings;


	public ModalAmount(Context c, ReadyListener readyListener, String amount, boolean negative, int displayStrings){
		super(c);
		this.readyListener = readyListener;
		this.amount = amount;
		this.negative = negative;
		this.displayStrings = displayStrings;
	}

	/* Determines if the amount form input is empty */
	public boolean amountEmpty(){
		if (this.amountEdit.getText().toString().equals("")){
			return true;
		} else {
			return false;
		}
	}

	/* Gets the amount from the amound form input */
	public String getAmount(){
		return this.amountEdit.getText().toString();
	}

	/* Returns the negative variable */
	public boolean getNegative(){
		return this.negative;
	}

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.amountmodal);
		setTitle(R.string.amountmodal_Title);

		this.neg = ((TextView) findViewById(R.id.amountmodal_negative));
		this.pos = ((TextView) findViewById(R.id.amountmodal_positive));
		this.iOweCheck = ((CheckBox) findViewById(R.id.amountmodal_negativeCheck));
		this.theyOweCheck = ((CheckBox) findViewById(R.id.amountmodal_positiveCheck));
		this.amountEdit = ((EditText) findViewById(R.id.amountmodal_amount));

		/* Set the text to be displayed next to the check boxes */
		switch (this.displayStrings) {
			case (NEW_ENTRY): {
				System.out.println("in new entry");
				this.neg.setText(R.string.amountmodal_newUserNeg);
				this.pos.setText(R.string.amountmodal_newUserPos);
				break;
			} case (UPDATE_BALANCE_NEGATIVE): {
				this.neg.setText(R.string.amountmodal_updateNegBalNeg);
				this.pos.setText(R.string.amountmodal_updateNegBalPos);
				break;
			} case (UPDATE_BALANCE_POSITIVE): {
				this.neg.setText(R.string.amountmodal_updatePosBalNeg);
				this.pos.setText(R.string.amountmodal_updatePosBalPos);
				break;
			} case (NEW_USER): {
				System.out.println("in new user");
				this.neg.setText(R.string.amountmodal_newUserNeg);
				this.pos.setText(R.string.amountmodal_newUserPos);
				break;
			}
		}

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

	    /* Set restriction on the amount of decimal digits on the amount text box */
	    this.amountEdit.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2, 6)});

		/* Set listeners */
		this.neg.setOnClickListener(
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

		this.pos.setOnClickListener(
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

		Button doneButton = ((Button) findViewById(R.id.amountmodal_done));
		doneButton.setOnClickListener(new ModalListener());
	}

	/* Updates UI and variables to reflect a negative amount for entry */
	protected void checkNegative(){
		this.theyOweCheck.setChecked(false);
		this.iOweCheck.setChecked(true);
		this.negative = true;
		this.amountEdit.setEnabled(true);
	}

	/* Updates UI and variable to reflect a credit amount for entry */
	protected void checkCredit(){
		this.theyOweCheck.setChecked(true);
		this.iOweCheck.setChecked(false);
		this.negative = false;
		this.amountEdit.setEnabled(true);
	}

	/* Nested class that is listener for Done button */
	protected class ModalListener implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			System.out.println(amountEmpty());
			if (amountEmpty()){
				displayErrorToast("an amount");
			} else if (! HelperMethods.validAmount(getAmount())) {
				displayErrorToast("a valid amount");
			} else {
				readyListener.ready(getNegative(), getAmount());
				ModalAmount.this.dismiss();
			}
		}
	}

	/* Create and display Toast error message */
	public void displayErrorToast(String msg){

		Toast t = new Toast(this.getContext());
    	String errorMessage = "Please provide "+msg+"";
    	TextView error = new TextView(this.getContext());
    	error.setText(errorMessage);
    	error.setTextColor(Color.WHITE);
    	error.setBackgroundColor(Color.BLACK);

    	t.setView(error);
    	t.setDuration(2);
    	t.show();

	}
}
