package com.caseynbrown.moneymanager;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TextBalanceAdapter extends BaseAdapter{
	private Context mContext;

	private ArrayList<TextBalance> mItems = new ArrayList<TextBalance>();

	public TextBalanceAdapter(Context c){
		this.mContext = c;
	}

	public void addItem(TextBalance t){
		this.mItems.add(t);
	}

	public void setListItems(ArrayList<TextBalance> list){
		this.mItems = list;
	}

	public int getCount(){
		return this.mItems.size();
	}

	public ArrayList<TextBalance> getItems(){
		return this.mItems;
	}

	public TextBalance getItem(int index){
		return this.mItems.get(index);
	}

	public long getItemId(int position){
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent){
		TextBalanceView tcv;
		if (convertView == null){
			tcv = new TextBalanceView(mContext, mItems.get(position));
		} else {
			tcv = (TextBalanceView) convertView;
			tcv.setName(mItems.get(position).getName());
			tcv.setAmount(mItems.get(position).getBalance());
			tcv.setLatest(mItems.get(position).getDate(), mItems.get(position).getLatestWhere());
			tcv.setLatestAmount(mItems.get(position).getLatestAmount());

			// Check if we're including date
			if (! mItems.get(position).getDate().equals(""))
				tcv.setDate(mItems.get(position).getDate());
		}

		return tcv;
	}
}