package com.caseynbrown.moneymanager;

import static android.provider.BaseColumns._ID;
import static com.caseynbrown.moneymanager.DBConstants.AMOUNT_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.NAME_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.TABLE_NAME_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.TABLE_NAME_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.USER_ENTRY;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/* Activity which is called to view Balances.  Accessible from the 'Quick Balance View'
 * and 'Edit Balances' buttons from the main screen (MoneyManagerActivity) */
public class ViewBalanceActivity extends ListActivity {
	/* DB Constants */
	private static String[] FROM = {_ID, NAME_PEOPLE, AMOUNT_PEOPLE, };
	private static String ORDER_BY = NAME_PEOPLE + " DESC";

	private DBData people;
	private SQLiteDatabase db;
	private TextBalanceAdapter bal;

	boolean view; // If true, view only mode

	ListView lv;
	ArrayList<Integer> idList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balanceclick);

        /* Should list items be clickable? */
        Intent i = getIntent();
        this.view = i.getBooleanExtra("view", true);

        if (!this.view){
        	this.lv = getListView();

        	lv.setOnItemClickListener(new OnItemClickListener(){
            	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            		Intent startEdit = new Intent(view.getContext(), ViewIndividualBalanceActivity.class);
            		int userId = idList.get(position);
            		startEdit.putExtra("id", userId);
            		startActivity(startEdit);
            	}
            });
        }
    }

    @Override
    public void onResume(){
    	super.onResume();

        this.idList = new ArrayList<Integer>();
    	
    	this.people = new DBData(this);
    	this.db = people.getReadableDatabase();
    	populateList();
    }
    
    @Override 
    public void onPause(){
    	super.onPause();
    	
    	this.db.close();
    	this.people.close();
	}

	public void populateList(){
		this.bal = new TextBalanceAdapter(this);
        try {
        	Cursor cursor = getUsers();
        	addPeopleToList(cursor);
        } catch (Exception e){
        	System.out.println("Unable to get cursor of people in ViewBalanceActivity");
        	e.printStackTrace();
        	finish();
        }

        setListAdapter(bal);
	}

    private void addPeopleToList(Cursor peopleCursor){
    	while (peopleCursor.moveToNext()){
    		int id = peopleCursor.getInt(0);
    		idList.add(id);
    		String name = peopleCursor.getString(1);
    		int amount = peopleCursor.getInt(2);
    		
    		/* Get that person's latest entry */
    		String query = "SELECT * FROM "+TABLE_NAME_ENTRY+" WHERE "+USER_ENTRY+" = "+id+" ORDER BY "+_ID+" DESC LIMIT 1";
    		Cursor latestEntry = this.db.rawQuery(query, null);
    		
    		String latestDate = null;
    		String latestWhere = null;
    		int latestAmount = 0;
    		
    		if (latestEntry.moveToNext()){
    			latestDate = latestEntry.getString(2);
    			latestWhere = latestEntry.getString(3);
    			latestAmount = latestEntry.getInt(4);
    		}
    		
    		/* Add the entry to the list */
    		this.bal.addItem(new TextBalance(id, name, amount, latestDate, latestWhere, latestAmount));
    		latestEntry.close();
    	}
    	
    	peopleCursor.close();
	}

    private Cursor getUsers(){
        // Perform a managed query. The Activity will handle closing
        // and re-querying the cursor when needed.
        Cursor cursor = this.db.query(TABLE_NAME_PEOPLE, FROM, null, null, null,
              null, ORDER_BY);
        startManagingCursor(cursor);
        return cursor;
     }
}
