package com.caseynbrown.moneymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MoneyManagerActivity extends Activity{
	/** Called when the activity is first created. */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /* View Balances */
        ((Button) findViewById(R.id.balanceView)).setOnClickListener(
        		new Button.OnClickListener() {
        			@Override public void onClick(View view){
        				Intent balIntent = new Intent(view.getContext(), ViewBalanceActivity.class);
        				balIntent.putExtra("view", false);
        				startActivity(balIntent);
        			}
        		}
    		);

        /* Add New Person */
        ((Button) findViewById(R.id.addNewPerson)).setOnClickListener(
        		new Button.OnClickListener() {
        			@Override public void onClick(View view){
        				Intent addUserIntent = new Intent (view.getContext(), PeopleAddActivity.class);
        				startActivity(addUserIntent);
    				}
        		}
    		);

        /* Add New Entry */
        ((Button) findViewById(R.id.addNewEntry)).setOnClickListener(
        		new Button.OnClickListener() {
        			@Override public void onClick(View view){
        				Intent entryIntent = new Intent(view.getContext(), NewEntryActivity.class);
        				startActivity(entryIntent);
        			}
        		}
    		);

        /* Help section */
        ((Button) findViewById(R.id.help)).setOnClickListener(
        		new Button.OnClickListener() {
        			@Override public void onClick(View view){
        				Intent helpIntent = new Intent(view.getContext(), HelpActivity.class);
        				startActivity(helpIntent);
        			}
        		}
    		);
    }
}