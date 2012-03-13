package com.caseynbrown.moneymanager;

import static android.provider.BaseColumns._ID;
import static com.caseynbrown.moneymanager.DBConstants.AMOUNT_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.AMOUNT_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.DATE_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.NAME_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.TABLE_NAME_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.TABLE_NAME_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.TITLE_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.USER_ENTRY;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/* An activity that presents a list of all entries, the option to edit the balance,
 * and an option to delete the user.  Accessed via the ViewBalanceActivity.
 */
public class ViewIndividualBalanceActivity extends ListActivity {
	private DBData database;
	SQLiteDatabase db;
	private TextBalanceAdapter tba;
	private String name;
	private int amount;
	private int personId;
	private static String[] FROM = {_ID, DATE_ENTRY, TITLE_ENTRY, AMOUNT_ENTRY};
	private static String ORDER_BY = DATE_ENTRY + " DESC";
	private static int[] TO = {0 ,R.id.indiv_date, R.id.indiv_title, R.id.indiv_amount};
	private ListView lv;

	/* entryIds contains the entry ids of each entry listed on the ListView, entryIds is
	 * used when calling the ViewEntryActivity
	 */
	private ArrayList<Integer> entryIds;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individualbalance);

		Intent i = getIntent();
        this.personId = i.getIntExtra("id", 0);

        Button delButton = ((Button) findViewById(R.id.indiv_delUser));
		delButton.setOnClickListener(
				new Button.OnClickListener(){
					@Override
					public void onClick(View v) {
						showModal();
					}
				});

		Button editButton = ((Button) findViewById(R.id.indiv_editBal));
		editButton.setOnClickListener(
				new Button.OnClickListener(){
					@Override
					public void onClick(View v){
						Intent editBal = new Intent(v.getContext(), EditBalanceActivity.class);
						editBal.putExtra("id", personId);
						editBal.putExtra("amount", amount);
						startActivity(editBal);
					}
				});

		ListView lv = this.getListView();
		lv.setOnItemClickListener(new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent i = new Intent(view.getContext(), ViewEntryActivity.class);
				i.putExtra("entryId", entryIds.get(position));
				i.putExtra("name", name);
				startActivity(i);
			}

		});
	}

	@Override
	public void onResume(){
		super.onResume();

		this.entryIds = new ArrayList<Integer>();

		database = new DBData(this);
		this.db = database.getReadableDatabase();

		Cursor c = getPersonInfo();
		updatePersonInfo(c);
		c.close();

		c = getEntries();
		showEntries(c);
		c.close();
	}

	@Override
	public void onPause(){
		super.onPause();

		this.db.close();
		this.database.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu m){
		MenuInflater i = getMenuInflater();
		i.inflate(R.layout.individualbalancemenu, m);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem i){
		switch (i.getItemId()){
			case R.id.indivMenu_email:
				sendMail();
			default:
				return super.onOptionsItemSelected(i);
		}
	}

	/* A method which starts the Mail Activity with a list of balances as the message */
	private void sendMail(){
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plan/text");
		StringBuilder body = new StringBuilder();

		body.append("Hi "+this.name+",\n\n");

		if (this.amount != 0){
			if (this.amount < 0){
				body.append(getResources().getString(R.string.mail_iOwe)+HelperMethods.intToDollar(this.amount)+".");
			} else {
				body.append(getResources().getString(R.string.mail_youOwe)+HelperMethods.intToDollar(this.amount)+".");
			}

			body.append("  "+getResources().getString(R.string.mail_summary));

			/* Add the individual entries */
			Cursor c = getEntries();
			while (c.moveToNext()){
				String date = c.getString(1);
				String title = c.getString(2);
				int amount = c.getInt(3);

				body.append(date+" "+title+" $"+HelperMethods.intToDollar(amount)+"\n");
			}
			c.close();
		} else {
			body.append(getResources().getString(R.string.mail_even));
		}

		body.append(getResources().getString(R.string.mail_promo));

		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body.toString());
		startActivity(Intent.createChooser(emailIntent, "Send email..."));
	}

	/* Displays the Modal Delete pop up */
	public void showModal(){
		ModalDelete d = new ModalDelete(this, this.name ,new OnReadyListener());
		d.show();
	}

	/* Get the decision to delete back from the Modal Listener */
	private class OnReadyListener implements ModalDelete.ReadyListener{

		/* Delete the person from the database */
		@Override
		public void ready(boolean delete) {
			if (delete){
				String query = "DELETE FROM "+TABLE_NAME_PEOPLE+" WHERE "+_ID+"="+personId;
				db.execSQL(query);

				finish();
			}
		}
	}

	/* Retrieve the entries corresponding to the current person */
    private Cursor getEntries(){
        String query = "SELECT "+ _ID +", "+ DATE_ENTRY +", "+TITLE_ENTRY+", "+AMOUNT_ENTRY+" FROM "+TABLE_NAME_ENTRY+
        	" WHERE "+USER_ENTRY+"="+this.personId+" ORDER BY "+_ID+" DESC";
        Cursor cursor = this.db.rawQuery(query, null);
        startManagingCursor(cursor);

        return cursor;
    }

    /* Returns the information of the person whose balance is being viewed */
    private Cursor getPersonInfo(){
    	String query = "SELECT "+ NAME_PEOPLE +", "+AMOUNT_PEOPLE+" FROM "+TABLE_NAME_PEOPLE+
    	" WHERE "+_ID+"="+this.personId;
    	Cursor cursor = this.db.rawQuery(query, null);
    	startManagingCursor(cursor);

    	return cursor;
    }

    /* Updates the screen to reflect the current person being viewed */
    private void updatePersonInfo(Cursor c){
    	TextView title = ((TextView) findViewById(R.id.indiv_Name));
    	TextView balanceAmt = ((TextView) findViewById(R.id.indiv_balanceAmt));

    	while (c.moveToNext()){
    		this.name = c.getString(0);
    		title.setText(c.getString(0));

    		int amt = c.getInt(1);
    		this.amount = amt;
    		balanceAmt.setText(HelperMethods.intToDollar(amt));

    		if (amt < 0){
    			balanceAmt.setTextColor(Color.RED);
    		} else {
    			balanceAmt.setTextColor(Color.parseColor(getResources().getString(R.string.defaultGreenColor)));
    		}
    	}
    }

    /* Takes the information from the entry cursor and populates the list view */
    private void showEntries(Cursor cursor){
    	this.tba = new TextBalanceAdapter(this);

    	while (cursor.moveToNext()){
    		int id = cursor.getInt(0);
    		String date = cursor.getString(1);
    		String name = cursor.getString(2);
    		int amount = cursor.getInt(3);
    		this.entryIds.add(id);
    		this.tba.addItem(new TextBalance(id, date, name, amount));
    	}

    	this.setListAdapter(this.tba);
	}
}