package com.caseynbrown.moneymanager;

import static android.provider.BaseColumns._ID;
import static com.caseynbrown.moneymanager.DBConstants.AMOUNT_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.AMOUNT_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.DATE_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.NAME_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.NOTES_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.TABLE_NAME_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.TITLE_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.USER_ENTRY;
import static com.caseynbrown.moneymanager.ModalAmountConstants.UPDATE_BALANCE_NEGATIVE;
import static com.caseynbrown.moneymanager.ModalAmountConstants.UPDATE_BALANCE_POSITIVE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/* Edit a user's balance, very similar to NewEntryActivity */
public class EditBalanceActivity extends Activity{

	/* DB related */
	private DBData database;

	int selectedId;
	int amount;
	protected static final int PEOPLE_LIST_CREATE = 0;
	protected static final int OWE_CREATE = 1;
	EditText whyBox, notesBox, amountBox;
	String plusminus;

	/* Create boolean and string objects to persist amount from modal amount window If these
     * are set, the modal amount window will automatically be filled with these values
     */
    boolean negative = false;
    String amountString = "";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editbalance);

        /* Assign ID and amount from Intent */
        Intent i = getIntent();
        this.selectedId = i.getIntExtra("id", 0);
        this.amount = i.getIntExtra("amount", 0);

        /* Update balance text on screen */
        TextView balText = ((TextView) findViewById(R.id.editBal_amt));
        balText.setText(HelperMethods.intToDollar(this.amount));
        if (this.amount < 0){
        	this.negative = true;
        	balText.setTextColor(Color.RED);
        } else {
        	this.negative = false;
        	balText.setTextColor(Color.parseColor(getResources().getString(R.string.defaultGreenColor)));
        }

        /* Get the interactive elements from screen. */
        this.whyBox = ((EditText) findViewById(R.id.editBal_whereBox));
        this.amountBox = ((EditText) findViewById(R.id.editBal_amountBox));
        this.notesBox = ((EditText) findViewById(R.id.editBal_notesBox));

        /* Add click listeners for the buttons */
        this.amountBox.setOnClickListener(
        		new EditText.OnClickListener(){
        			@Override public void onClick(View view){
        				showModal();
        			}
        		});

        /* Quick Balance */
        ((Button) findViewById(R.id.editBal_submitButton)).setOnClickListener(
        		new Button.OnClickListener() {
        			@Override
        			public void onClick(View view){
        				
        				/* Check that the proper fields are filled. */
        				ArrayList<String> invalidFields = inputValidation();
        				if (invalidFields.size() > 0){
        					makeToastError(invalidFields);
        				} else {
        					updateSQL();
        					finish();
        				}
    				}
        		});
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
        this.database = new DBData(this);
	}
	
	public void onPause(){
		super.onPause();
		
		this.database.close();
	}

	/* Displays the modal amount pop up */
	public void showModal(){

		/* Pass different string code whether the current balance with the person is negative
		 * or positive.
		 */
		int modalStrings;
		if (this.negative){
			modalStrings = UPDATE_BALANCE_NEGATIVE;
		} else {
			modalStrings = UPDATE_BALANCE_POSITIVE;
		}

		ModalAmount d = new ModalAmount(this, new OnReadyListener(), this.amountString, this.negative, modalStrings);
		d.show();
	}

	/* Get the amount back from the Modal Listener */
	private class OnReadyListener implements ModalAmount.ReadyListener{

		@Override
		public void ready(boolean negativeReturned, String amountReturned) {
			updateAmount(negativeReturned, amountReturned);
			negative = negativeReturned;
			amountString = amountReturned;
		}
	}

	/* Sets the appropriate color for the givem amount */
	public void updateAmount(boolean neg, String amt){
		String sign;
		int color;

		if (neg){
			sign = "-";
			color = Color.RED;
		} else {
			sign = "+";
			color = Color.parseColor(getResources().getString(R.string.defaultGreenColor));
		}

		this.amountBox.setText(sign+amt);
		this.amountBox.setTextColor(color);
	}

	/* Get information from fields and begin process of writing to DB */
    private void updateSQL(){

    	/* Extract the data from the input fields. */
    	Integer amount = HelperMethods.dollarToInt(this.amountBox.getText().toString());
    	String title = this.whyBox.getText().toString();
    	String notes = this.notesBox.getText().toString();

    	SQLiteDatabase db = null;
    	try {
    		/* Add a new entry for that user */
    		db = database.getWritableDatabase();
    		addNewEntry(db, this.selectedId, amount, title, notes);
    	} finally {
    		db.close();
    	}
	}

    /* Add the new entry to the database */
    private void addNewEntry(SQLiteDatabase db, int id, double amount, String title, String notes){
    	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
    	Date dateType = new Date();
    	String date = dateFormat.format(dateType);

		ContentValues values = new ContentValues();
    	values.put(USER_ENTRY, id);
    	values.put(DATE_ENTRY, date);
    	values.put(TITLE_ENTRY, title);
    	values.put(AMOUNT_ENTRY, amount);
    	values.put(NOTES_ENTRY, notes);
    	try {
    		db.insertOrThrow(TABLE_NAME_ENTRY, null, values);
    	} catch (Exception e) {
    		System.out.println("Failed to add new entry in EditBalanceActivity");
    		e.printStackTrace();
    	}
    }

    /* Check that there all necessary fields are filled. */
    private ArrayList<String> inputValidation(){
    	ArrayList<String> returnString = new ArrayList<String>();

    	String amountString = this.amountBox.getText().toString();
    	if (amountString.equals("") || amountString == null){
    		returnString.add("Amount");
		}

    	return returnString;
   }

    /* Display an error message through a Toast */
    private void makeToastError(ArrayList<String> input){
    	StringBuilder errorMessage = new StringBuilder("Please fill in the following fields:");
    	
    	/* Append fields to the errorMessage */
    	for (String s: input){
    		errorMessage.append(" ");
    		errorMessage.append(s);
    	}
    	
    	Toast t = new Toast(this);
    	TextView error = new TextView(this);
    	error.setText(errorMessage);
    	error.setTextColor(Color.WHITE);
    	error.setBackgroundColor(Color.BLACK);
    	t.setView(error);
    	t.show();
    }
}
