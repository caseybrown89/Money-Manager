package com.caseynbrown.moneymanager;

import static com.caseynbrown.moneymanager.DBConstants.AMOUNT_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.NAME_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.TABLE_NAME_PEOPLE;
import static com.caseynbrown.moneymanager.ModalAmountConstants.NEW_USER;
import static com.caseynbrown.moneymanager.DBConstants.AMOUNT_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.AMOUNT_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.NAME_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.TABLE_NAME_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.DATE_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.NAME_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.NOTES_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.TABLE_NAME_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.TITLE_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.USER_ENTRY;
import static android.provider.BaseColumns._ID;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PeopleAddActivity extends Activity {
	private DBData db;
	private EditText nameEdit, amountEdit;
	private Button doneButton;

	/* Create boolean and string objects to persist amount from modal amount window If these
     * are set, the modal amount window will automatically be filled with these values
     */
    boolean negative = false;
    String amount = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.peopleadd);

		this.nameEdit = ((EditText) findViewById(R.id.peopleaddName));
		this.amountEdit = ((EditText) findViewById(R.id.peopleaddBal));
		this.doneButton = ((Button) findViewById(R.id.peopleAddDone));

		this.doneButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			/*
			 * If user clicks on Done, check for input. If no input exists, show
			 * error toast. Otherwise, add a new user to the DB and return to the
			 * previous activity
			 */
			public void onClick(View v) {
				if (emptyName()) {
					makeToastAdd();
				} else {
					String amountVal = amountEdit.getText().toString();
					int newAmount;
					if (amountVal.equals("")) {
						newAmount = HelperMethods.dollarToInt("+0");

					} else {
						newAmount = HelperMethods.dollarToInt(amountVal);
					}

					addUserEntry(nameEdit.getText().toString(), newAmount);
					Intent returnIntent = new Intent();
					setResult(Activity.RESULT_OK, returnIntent);
					finish();
				}
			}
		});

		/* Display modal amount pop up when user clicks the amount box */
		this.amountEdit.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				showModal();
			}
		});
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		this.db = new DBData(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		/* Close DB connection */
		this.db.close();
	}

	/* Displays a Modal amount box */
	public void showModal() {
		ModalAmount d = new ModalAmount(this, new OnReadyListener(), this.amount, this.negative, NEW_USER);
		d.show();
	}

	/* Get the amount back from the Modal Listener */
	private class OnReadyListener implements ModalAmount.ReadyListener {

		@Override
		public void ready(boolean negativeReturned, String amountReturned) {
			HelperMethods.updateAmountBox(amountEdit, negativeReturned, amountReturned);
			negative = negativeReturned;
			amount = amountReturned;
		}
	}

	/* Add the new person to the DB and add an entry of the initial amount if provided*/
	private void addUserEntry(String name, int amount) {
		SQLiteDatabase writable = db.getWritableDatabase();
		ContentValues personEntry = new ContentValues();
		personEntry.put(NAME_PEOPLE, name);
		personEntry.put(AMOUNT_PEOPLE, 0); // Set amount to 0 here, will add below in entry
		writable.insertOrThrow(TABLE_NAME_PEOPLE, null, personEntry);
		
		if (amount != 0){
			/* Get the ID assigned to the new person just added */
			String query = "SELECT "+_ID+" FROM "+TABLE_NAME_PEOPLE+" WHERE" +
					" "+NAME_PEOPLE+" = '"+name+"';";
			Cursor c = writable.rawQuery(query, null);
			int id = 0;
			while (c.moveToNext()){
				id = c.getInt(0);
			}
			c.close();
						
			/* Add a new entry for the initial balance from the person just added to the DB */ 
			personEntry.clear();
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
			Date dateType = new Date();
			String date = dateFormat.format(dateType);			

			/* Will need to get the ID of the user just entered */
			personEntry.put(USER_ENTRY, id);
			personEntry.put(DATE_ENTRY, date);
			personEntry.put(TITLE_ENTRY, getString(R.string.peopleAdd_initial));
			personEntry.put(AMOUNT_ENTRY, amount);
			personEntry.put(NOTES_ENTRY, "");
			
			try {
				writable.insertOrThrow(TABLE_NAME_ENTRY, null, personEntry);
			} catch (Exception e){
				System.out.println("Unable to create entry in PeopleAddActivity");
				e.printStackTrace();
			}
		}
		writable.close();
	}

	/* Check if the name input box is empty */
	private boolean emptyName() {
		if (this.nameEdit.getText().toString().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/* Display error toast */
	private void makeToastAdd() {
		String errorMessage = "Please provide a Name";
		Toast t = new Toast(this);
		TextView error = new TextView(this);
		error.setText(errorMessage);
		error.setTextColor(Color.WHITE);
		error.setBackgroundColor(Color.BLACK);
		t.setView(error);
		t.show();
	}
}
