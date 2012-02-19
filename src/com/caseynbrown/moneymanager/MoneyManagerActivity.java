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

        // Add click listeners for the buttons
        // Quick Balance
        ((Button) findViewById(R.id.balanceView)).setOnClickListener(
        		new Button.OnClickListener() {
        			@Override public void onClick(View view){
        				System.out.println("clicked quick bal");
        				Intent balIntent = new Intent(view.getContext(), BalanceViewActivity.class);
        				balIntent.putExtra("view", true);
        				startActivity(balIntent);
        			}
        		}
    		);

        // Add New
        ((Button) findViewById(R.id.addNew)).setOnClickListener(
        		new Button.OnClickListener() {
        			@Override public void onClick(View view){
        				System.out.println("clicked add new");
        				Intent entryIntent = new Intent(view.getContext(), NewEntryActivity.class);
        				startActivity(entryIntent);
    				}
        		}
    		);

        // Edit Entry
        ((Button) findViewById(R.id.editBal)).setOnClickListener(
        		new Button.OnClickListener() {
        			@Override public void onClick(View view){
        				System.out.println("clicked editBal");
        				Intent balIntent = new Intent(view.getContext(), BalanceViewActivity.class);
        				balIntent.putExtra("view", false);
        				startActivity(balIntent);
        			}
        		}
    		);

        // About
        ((Button) findViewById(R.id.about)).setOnClickListener(
        		new Button.OnClickListener() {
        			@Override public void onClick(View view){
        				System.out.println("clicked about");
        			}
        		}
    		);
    }
}