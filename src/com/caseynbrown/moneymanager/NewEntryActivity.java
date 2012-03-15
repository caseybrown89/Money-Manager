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
import static com.caseynbrown.moneymanager.ModalAmountConstants.NEW_ENTRY;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/* Activity for entering new entries into DB.  Used on 'New Entry' and 'Update Balance
 * screens
 */
public class NewEntryActivity extends Activity {

	/* DB related */
	private DBData database;
	private static String[] FROM = { _ID, NAME_PEOPLE, AMOUNT_PEOPLE };
	private static String ORDER_BY = NAME_PEOPLE + " DESC";
	private static int[] TO = { 0, R.id.balancelist_rowName, 0 };

	/*
	 * Create boolean and string objects to persist amount from modal amount
	 * window If these are set, the modal amount window will automatically be
	 * filled with these values
	 */
	boolean negative = false;
	String amount = "";

	/* An entry can be created for multiple people */
	int numIds = 0;
	ArrayList<Integer> selectedIds;
	ArrayList<String> selectedNames;
	protected static final int PEOPLE_LIST_CREATE = 0;

	/* Form elements */
	TextView whoBox;
	EditText whereBox, amountBox, notesBox;
	CheckBox divideCheck, includeCheck;
	LinearLayout includeLay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newentry);
		database = new DBData(this);

		/* Do not highlight any form fields */
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		/* Get the interactive elements from screen. */
		this.whoBox = ((TextView) findViewById(R.id.entry_whoBox));
		this.whereBox = ((EditText) findViewById(R.id.entry_whereBox));
		this.amountBox = ((EditText) findViewById(R.id.entry_amountBox));
		this.notesBox = ((EditText) findViewById(R.id.entry_notesBox));
		this.divideCheck = ((CheckBox) findViewById(R.id.entry_divideCheckBox));
		this.includeCheck= ((CheckBox) findViewById(R.id.entry_includeCheckBox));
		this.includeLay = ((LinearLayout) findViewById(R.id.entry_includeLay));

		/* Initialize selectedIds ArrayList */
		selectedIds = null;

		/* Add listeners for fields/buttons */
		/* Bring up list to select people for the entry */
		this.whoBox.setOnClickListener(new EditText.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent listIntent = new Intent(view.getContext(),
						PeopleListActivity.class);
				listIntent.putExtra("ids", selectedIds);
				startActivityForResult(listIntent, PEOPLE_LIST_CREATE);
			}
		});

		/* Bring up a modal pop up to enter amount */
		this.amountBox.setOnClickListener(new EditText.OnClickListener() {
			@Override
			public void onClick(View view) {
				showModal();
			}
		});

		/* Submit the entry */
		((Button) findViewById(R.id.submitButton))
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View view) {
						/* Input validation */
						ArrayList<String> invalidFields = inputValidation();
						if (invalidFields.size() > 0) {
							/* Toast error */
							makeToastError(invalidFields);
						} else {
							try {
								updateSQL();
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								finish();
							}
						}
					}
				});

		/*
		 * If checked, divide the total amount entered among all people for
		 * entry. Otherwise, give the amount provided to each person in the
		 * entry.
		 */
		this.divideCheck.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				toggleIncludeView();
			}
		});

		((TextView) findViewById(R.id.entry_divideText))
				.setOnClickListener(new TextView.OnClickListener() {
					@Override
					public void onClick(View view) {

					}
				});
	}

	@Override
	public void onPause(){
		super.onPause();

		this.selectedIds = new ArrayList<Integer>();
		this.selectedNames = new ArrayList<String>();
	}

	/* Display the modal amount pop up */
	public void showModal() {
		ModalAmount d = new ModalAmount(this, new OnReadyListener(),
				this.amount, negative, NEW_ENTRY);
		d.show();
	}

	/* Get the amount back from the Modal Listener */
	private class OnReadyListener implements ModalAmount.ReadyListener {

		@Override
		public void ready(boolean negativeReturned, String amountReturned) {
			HelperMethods.updateAmountBox(amountBox, negativeReturned,
					amountReturned);
			amount = amountReturned;
			negative = negativeReturned;
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		CheckBox check = ((CheckBox) findViewById(R.id.entry_divideCheckBox));
		if (this.numIds > 0){

			/* Set the Who button to display all users */
			StringBuilder displayNames = new StringBuilder();
			for (int i = 0; i < this.selectedNames.size(); i++) {
				String s = this.selectedNames.get(i);
				if (i + 1 == this.selectedNames.size()) {
					displayNames.append(s);
				} else {
					displayNames = displayNames.append(s + "\n");
				}
			}
			this.whoBox.setText(displayNames.toString());

			/* Enable the 'Divide' checkbox */
			if (this.numIds > 1) {
				check.setEnabled(true);
			} else {
				/* Disable All Check boxes */
				check.setChecked(false);
				check.setEnabled(false);
				this.divideCheck.setChecked(false);
				this.divideCheck.setEnabled(false);
			}
		} else {
			this.whoBox.setText("");
			/* Disable All Check boxes */
			check.setChecked(false);
			check.setEnabled(false);
			this.divideCheck.setChecked(false);
			this.divideCheck.setEnabled(false);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		/*
		 * Set the id and name arrays to the people selected on the
		 * PeopleListActivity
		 */
		case (PEOPLE_LIST_CREATE): {
			if (resultCode == Activity.RESULT_OK) {
				// Update instance variables
				this.selectedIds = data.getIntegerArrayListExtra("ids");
				this.selectedNames = data.getStringArrayListExtra("names");
				numIds = selectedIds.size();
			}
		}
		}
	}

	private void updateSQL() {

		/* Get data from form fields */
		int amount = HelperMethods.dollarToInt(this.amountBox.getText()
				.toString());
		String title = this.whereBox.getText().toString();
		String notes = this.notesBox.getText().toString();

		try {
			/* SQL update for multiple people */
			int numSelectedIds = this.selectedIds.size();



			/* The remainder of the division of the amount, will be added to balances of others until 0 */
			int remainder = -1;
			if (numSelectedIds > 1) {

				/* Include yourself when dividing? */
				if (this.includeCheck.isChecked())
					numSelectedIds = numSelectedIds + 1;

				/* Determine the amount for each person in the entry */
				int newAmount = 0;
				if (this.divideCheck.isChecked()) {
					remainder = Math.abs(amount) % numSelectedIds;
					newAmount = amount / numSelectedIds;
				} else {
					newAmount = amount;
				}

				/* Add an entry in the DB for each user */
				SQLiteDatabase db = database.getWritableDatabase();
				for (int i = 0; i < this.selectedIds.size(); i++) {
					/* Divide remainder among people until 0 */
					int amountWithBal = newAmount;
					if (remainder > 0){
						if (amountWithBal > 0){
							amountWithBal = amountWithBal + 1;
							remainder--;
						} else {
							amountWithBal = amountWithBal - 1;
							remainder--;
						}

					}

					int id = this.selectedIds.get(i);
					addNewEntry(db, id, amountWithBal, title, notes);
				}

				/* Add an entry for one user */
			} else {
				SQLiteDatabase db = database.getWritableDatabase();
				addNewEntry(db, this.selectedIds.get(0), amount, title, notes);
			}
		} finally {
			database.close();
		}
	}

	/* Adds an entry in the entry table with the passed arguments */
	private void addNewEntry(SQLiteDatabase db, int id, int amount,
			String title, String notes) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
		Date dateType = new Date();
		String date = dateFormat.format(dateType);

		ContentValues values = new ContentValues();
		values.put(USER_ENTRY, id);
		values.put(DATE_ENTRY, date);
		values.put(TITLE_ENTRY, title);
		values.put(AMOUNT_ENTRY, amount);
		values.put(NOTES_ENTRY, notes);
		db.insertOrThrow(TABLE_NAME_ENTRY, null, values);
	}

	/* Check that all necessary fields have information */
	private ArrayList<String> inputValidation() {
		ArrayList<String> returnString = new ArrayList<String>();

		String whoString = this.whoBox.getText().toString();
		if (whoString.equals("") || whoString == null) {
			returnString.add("Who");
		}

		String whereString = this.whereBox.getText().toString();
		if (whereString.equals("") || whereString == null) {
			returnString.add("Where");
		}

		String amountString = this.amountBox.getText().toString();
		if (amountString.equals("") || amountString == null) {
			returnString.add("Amount");
		}

		return returnString;
	}

	/* Create and display Toast error message */
	private void makeToastError(ArrayList<String> input) {
		String errorMessage = "Please fill in the following fields: ";
		for (String s : input) {
			errorMessage = errorMessage + s + " ";
		}
		Toast t = new Toast(this);
		TextView error = new TextView(this);
		error.setText(errorMessage);
		error.setTextColor(Color.WHITE);
		error.setBackgroundColor(Color.BLACK);
		t.setView(error);
		t.show();
	}

	/* Toggle the 'Divide' checkbox */
	private void toggleDivideCheckbox() {
		if (this.divideCheck.isEnabled()) {
			this.divideCheck.toggle();
		}
	}

	/* Toggle whether the includeCheckbox is visible */
	private void toggleIncludeView(){
		if (this.includeLay.getVisibility() == View.GONE){
			this.includeLay.setVisibility(View.VISIBLE);
		} else {
			this.includeLay.setVisibility(View.GONE);
		}
	}
}
