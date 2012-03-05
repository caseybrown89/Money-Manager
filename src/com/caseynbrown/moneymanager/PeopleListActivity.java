package com.caseynbrown.moneymanager;

import static android.provider.BaseColumns._ID;
import static com.caseynbrown.moneymanager.DBConstants.AMOUNT_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.NAME_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.TABLE_NAME_PEOPLE;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class PeopleListActivity extends ListActivity{
	private DBData people;
	private TextCheckboxAdapter check;
	private static String[] FROM = {_ID, NAME_PEOPLE, AMOUNT_PEOPLE};
	private static String ORDER_BY = NAME_PEOPLE + " ASC";
	private ListView lv;
	Button addPeople;
	
	ArrayList<Integer> selectedIds;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peoplelist);
        people = new DBData(this);
        this.addPeople = ((Button) findViewById(R.id.peopleListAddNew));
        
        /* Persist selectedIds if user returns to this screen */
        Intent i = getIntent();
        this.selectedIds = i.getIntegerArrayListExtra("ids");

        // Add items from the database into a cursor
        populateList();

        lv = getListView();

        lv.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        		TextCheckbox current = (TextCheckbox) lv.getItemAtPosition(position);
        		current.setChecked(!current.getChecked());
        		setListAdapter(check);
        	}
        });

        ((Button) findViewById(R.id.backPeopleList)).setOnClickListener(
        		new Button.OnClickListener(){

					@Override
					public void onClick(View arg0) {
						Pair<ArrayList<Integer>, ArrayList<String>> clicked = findClickedUsers();
						// return that intent with the users added
						Intent returnIntent = new Intent();
						returnIntent.putIntegerArrayListExtra("ids", clicked.getFirst());
						returnIntent.putStringArrayListExtra("names", clicked.getSecond());
						setResult(Activity.RESULT_OK, returnIntent);
						finish();
					}
				});

        addPeople.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent entryIntent = new Intent(v.getContext(), PeopleAddActivity.class);
				startActivity(entryIntent);
			}
        });
    }

	public void populateList(){
		this.check = new TextCheckboxAdapter(this);
        try {
        	Cursor cursor = getUsers();
        	addUsersToList(cursor);
        } finally {
        	people.close();
        }

        setListAdapter(check);
	}

	@Override
	public void onResume(){
		super.onResume();

		populateList();
	}

    private void addUsersToList(Cursor cursor){
    	while (cursor.moveToNext()){
    		int id = cursor.getInt(0);
    		String name = cursor.getString(1);
    		Drawable icon = getResources().getDrawable(R.drawable.checkmarksmall);
    		TextCheckbox newCheck = new TextCheckbox(id, name, icon);
    		if (HelperMethods.intInArrayList(id, this.selectedIds)){
    			newCheck.setChecked(true);
    		}
    		this.check.addItem(newCheck);
    	}
	}

    private Pair<ArrayList<Integer>, ArrayList<String>> findClickedUsers(){
    	ArrayList<Integer> selectedInts = new ArrayList<Integer>();
    	ArrayList<String> selectedStrings = new ArrayList<String>();
    	ArrayList<TextCheckbox> allList = check.getItems();

    	// Add all selected items to the selected list.
    	for (TextCheckbox i : allList){
    		if (i.getChecked()){
    			selectedInts.add(i.getId());
    			selectedStrings.add(i.getText());
    		}
    	}

    	return new Pair(selectedInts, selectedStrings);
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

    private void addUser(String name, float amount){
    	SQLiteDatabase db = people.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(NAME_PEOPLE, name);
    	values.put(AMOUNT_PEOPLE, amount);
    	db.insertOrThrow(TABLE_NAME_PEOPLE, null, values);
    }
}
