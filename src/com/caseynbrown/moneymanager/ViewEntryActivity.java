package com.caseynbrown.moneymanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static com.caseynbrown.moneymanager.DBConstants.TABLE_NAME_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.DATE_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.TITLE_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.AMOUNT_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.NOTES_ENTRY;
import static android.provider.BaseColumns._ID;

/* An activity which displays existing entries and provides the option of deleting the entry */
public class ViewEntryActivity extends Activity {
	TextView who, date, where, amount, notes;
	Button deleteButton;
	String userName;
	int entryId;
	DBData dbdata;
	SQLiteDatabase db;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewentry);
		
		this.who = (TextView) findViewById(R.id.viewEntry_whoText);
		this.date = (TextView) findViewById(R.id.viewEntry_dateText);
		this.where = (TextView) findViewById(R.id.viewEntry_whereText);
		this.amount = (TextView) findViewById(R.id.viewEntry_amountText);
		this.notes = (TextView) findViewById(R.id.viewEntry_notesText);
		
		this.deleteButton = (Button) findViewById(R.id.viewEntry_deleteButton);
		this.deleteButton.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				deleteEntry(entryId);
			}
		});
	}
	
	@Override
	public void onResume(){
		super.onResume();
				
		/* Get entryId from Intent */
		Intent i = getIntent();
		this.entryId = i.getIntExtra("entryId", -1);
		this.userName = i.getStringExtra("name");
		
		/* Open connection to DB */
		this.dbdata = new DBData(this);
		this.db = this.dbdata.getReadableDatabase();
		
		/* Get cursor with entry information */
		Cursor c = getEntry(this.entryId);
	
		/* Set text of TextView components */
		int balance = 0;
		while (c.moveToNext()){
			this.who.setText(this.userName);
			this.date.setText(c.getString(0));
			this.where.setText(c.getString(1));
			balance = c.getInt(2);
			this.amount.setText(HelperMethods.intToDollar(c.getInt(2)));
			
			String notes = c.getString(3);
			if (notes.equals("")){
				this.notes.setText(getResources().getString(R.string.viewEntry_notes));
			} else {
				this.notes.setText(c.getString(3));
			}
		}
		
		/* Set color on amount */
		if (balance < 0){
			this.amount.setTextColor(Color.RED);
		} else {
			this.amount.setTextColor(Color.parseColor(getResources().getString(R.string.defaultGreenColor)));
		}
		
		/* Close the cursor */
		c.close();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		
		/* Close connection to DB */
		this.db.close();
		this.dbdata.close();
	 }
	
	/* Returns a cursor containing information from the given entryId */
	public Cursor getEntry(int entryId){
		String query = "SELECT "+DATE_ENTRY+", "+TITLE_ENTRY+", "+AMOUNT_ENTRY+", "+NOTES_ENTRY+""+
				" FROM "+TABLE_NAME_ENTRY+" WHERE "+_ID+" = "+entryId;
		
		Cursor c = null;
		try {
			c = this.db.rawQuery(query, null);
			startManagingCursor(c);
		} catch (Exception e){
			System.out.println("getEntry of entryId "+entryId+" in ViewEntryActivity failed");
			e.printStackTrace();
			finish();			
		} finally {
			return c;
		}
	}
	
	/* Delete the given entry from the entry table, finish the activity */
	public void deleteEntry(int entryId){
		String query = "DELETE FROM "+TABLE_NAME_ENTRY+" WHERE "+_ID+" = "+entryId+";";
		try {
			this.db.execSQL(query);
		} catch (Exception e) {
			System.out.println("deleteEntry of entryId "+entryId+" in ViewEntryActivity failed");
			e.printStackTrace();
		} finally {
			finish();
		}
	}
}