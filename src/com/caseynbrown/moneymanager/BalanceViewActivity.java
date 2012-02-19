package com.caseynbrown.moneymanager;

import static android.provider.BaseColumns._ID;
import static com.caseynbrown.moneymanager.ConstantsDB.AMOUNT_PEOPLE;
import static com.caseynbrown.moneymanager.ConstantsDB.NAME_PEOPLE;
import static com.caseynbrown.moneymanager.ConstantsDB.TABLE_NAME_PEOPLE;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/* Activity which is called to view Balances.  Accessible from the 'Quick Balance View'
 * and 'Edit Balances' buttons from the main screen (MoneyManagerActivity) */
public class BalanceViewActivity extends ListActivity {
	/* DB Constants */
	private static String[] FROM = {_ID, NAME_PEOPLE, AMOUNT_PEOPLE, };
	private static String ORDER_BY = NAME_PEOPLE + " DESC";
	private static int[] TO = {0, R.id.rowName, R.id.rowAmount };

	private DBData people;
	private TextBalanceAdapter bal;

	boolean view; // If true, view only mode

	ListView lv;
	ArrayList<Integer> idList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idList = new ArrayList<Integer>();

        /* Should list items be clickable? */
        Intent i = getIntent();
        this.view = i.getBooleanExtra("view", true);

        if (this.view){
        	setContentView(R.layout.balanceview);
        } else {
        	setContentView(R.layout.balanceclick);
        }

        people = new DBData(this);
        populateList();

        if (!this.view){
        	this.lv = getListView();

        	lv.setOnItemClickListener(new OnItemClickListener(){
            	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            		System.out.println("Clicked item # "+position);
            		Intent startEdit = new Intent(view.getContext(), IndividualBalanceViewActivity.class);
            		int userId = idList.get(position);
            		startEdit.putExtra("id", userId);
            		startActivity(startEdit);
            	}
            });
        }
    }

    public void onResume(){
    	super.onResume();

    	people = new DBData(this);
    	populateList();
    }

	public void populateList(){
		this.bal = new TextBalanceAdapter(this);
        try {
        	Cursor cursor = getUsers();
        	addUsersToList(cursor);
        } finally {
        	people.close();
        }

        setListAdapter(bal);
	}

    private void addUsersToList(Cursor cursor){
    	while (cursor.moveToNext()){
    		int id = cursor.getInt(0);
    		idList.add(id);
    		String name = cursor.getString(1);
    		int amount = cursor.getInt(2);
    		this.bal.addItem(new TextBalance(id, name, amount));
    	}
	}

    private void addPeople(String name, float amount){
    	SQLiteDatabase db = people.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(NAME_PEOPLE, name);
    	values.put(AMOUNT_PEOPLE, amount);
    	db.insertOrThrow(TABLE_NAME_PEOPLE, null, values);
    }

    private Cursor getUsers(){
        // Perform a managed query. The Activity will handle closing
        // and re-querying the cursor when needed.
        SQLiteDatabase db = people.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_PEOPLE, FROM, null, null, null,
              null, ORDER_BY);
        startManagingCursor(cursor);
        return cursor;
     }

    private void showUsers(Cursor cursor){
    	// Set up data binding
    	SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.balanceviewlist,
    			cursor, FROM, TO);
    	setListAdapter(adapter);
    }
}
