package com.caseynbrown.moneymanager;

import static com.caseynbrown.moneymanager.ConstantsDB.AMOUNT_PEOPLE;
import static com.caseynbrown.moneymanager.ConstantsDB.NAME_PEOPLE;
import static com.caseynbrown.moneymanager.ConstantsDB.TABLE_NAME_PEOPLE;
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


public class PeopleAddActivity extends Activity {
	private DBData people;
	private EditText name, amount;
	private Button done;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.peopleadd);

		this.name = ((EditText) findViewById(R.id.peopleaddName));
		this.amount = ((EditText) findViewById(R.id.peopleaddBal));
		this.done = ((Button) findViewById(R.id.peopleAddDone));

		this.people = new DBData(this);

		this.done.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				if (emptyName()){
					makeToastAdd();
				} else {
					String amountVal =amount.getText().toString();
					float newAmount;
			    	if (amountVal.equals("")){
			    		newAmount = HelperMethods.roundFloat(0);
			    	} else {
			    		newAmount = HelperMethods.roundFloat(new Float(amountVal));
			    	}

					addUser(name.getText().toString(), newAmount);
					Intent returnIntent = new Intent();
					setResult(Activity.RESULT_OK, returnIntent);
					finish();
				}
			}
		});
	}

	@Override
	public void onDestroy(){
		super.onDestroy();

		// Close DB connection
		this.people.close();
	}

    private void addUser(String name, float amount){
    	SQLiteDatabase db = people.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(NAME_PEOPLE, name);
    	values.put(AMOUNT_PEOPLE, amount);
    	db.insertOrThrow(TABLE_NAME_PEOPLE, null, values);
    }

    private boolean emptyName(){
    	if (this.name.getText().toString().equals("")){
    		return true;
    	} else {
    		return false;
    	}
    }

    private void makeToastAdd(){
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
