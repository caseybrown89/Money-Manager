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
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IndividualBalanceViewActivity extends ListActivity {
	private DBData database;
	private TextBalanceAdapter tba;
	private String name;
	private int amount;
	private int userId;
	private static String[] FROM = {_ID, DATE_ENTRY, TITLE_ENTRY, AMOUNT_ENTRY};
	private static String ORDER_BY = DATE_ENTRY + " DESC";
	private static int[] TO = {0 ,R.id.indiv_date, R.id.indiv_title, R.id.indiv_amount};

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		database = new DBData(this);

		Intent i = getIntent();
        this.userId = i.getIntExtra("id", 0);

		setContentView(R.layout.individualbalance);

		Cursor c = getUserInfo();
		updateUserInfo(c);

		Cursor c2 = getEntries();
		showEntries(c2);

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
						editBal.putExtra("id", userId);
						editBal.putExtra("amount", amount);
						startActivity(editBal);
					}
				});
	}

	@Override
	public void onResume(){
		super.onResume();

		Cursor c = getUserInfo();
		updateUserInfo(c);

		Cursor c2 = getEntries();
		showEntries(c2);

	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		database.close();
	}

	public void showModal(){
		ModalDelete d = new ModalDelete(this, this.name ,new OnReadyListener());
		d.show();
	}

	// Get the amount back from the Modal Listener
	private class OnReadyListener implements ModalDelete.ReadyListener{

		@Override
		public void ready(boolean delete) {
			if (delete){
				System.out.println("Delete user and end activity");
				SQLiteDatabase db = database.getWritableDatabase();
				String query = "DELETE FROM "+TABLE_NAME_PEOPLE+" WHERE "+_ID+"="+userId;
				db.execSQL(query);

				finish();
			}
		}
	}

    private Cursor getEntries(){
        // Perform a managed query. The Activity will handle closing
        // and re-querying the cursor when needed.
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "SELECT "+ _ID +", "+ DATE_ENTRY +", "+TITLE_ENTRY+", "+AMOUNT_ENTRY+" FROM "+TABLE_NAME_ENTRY+
        	" WHERE "+USER_ENTRY+"="+this.userId+" ORDER BY "+_ID+" DESC";
        Cursor cursor = db.rawQuery(query, null);
        startManagingCursor(cursor);

        return cursor;

     }

    private Cursor getUserInfo(){
    	SQLiteDatabase db = database.getReadableDatabase();
    	System.out.println("UserID: "+this.userId);
    	String query = "SELECT "+ NAME_PEOPLE +", "+AMOUNT_PEOPLE+" FROM "+TABLE_NAME_PEOPLE+
    	" WHERE "+_ID+"="+this.userId;
    	Cursor cursor = db.rawQuery(query, null);
    	startManagingCursor(cursor);

    	return cursor;
    }

    private void updateUserInfo(Cursor c){
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
    			balanceAmt.setTextColor(Color.parseColor("#00A300"));
    		}
    	}

    	c.close();
    }

    private void showEntries(Cursor cursor){
    	this.tba = new TextBalanceAdapter(this);

    	while (cursor.moveToNext()){
    		int id = cursor.getInt(0);
    		String date = cursor.getString(1);
    		String name = cursor.getString(2);
    		int amount = cursor.getInt(3);
    		this.tba.addItem(new TextBalance(id, date, name, amount));
    	}

    	this.setListAdapter(this.tba);

    }
}