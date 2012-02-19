package com.caseynbrown.moneymanager;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TextCheckboxAdapter extends BaseAdapter{
	private Context mContext;

	private ArrayList<TextCheckbox> mItems = new ArrayList<TextCheckbox>();

	public TextCheckboxAdapter(Context c){
		this.mContext = c;
	}

	public void addItem(TextCheckbox t){
		this.mItems.add(t);
	}

	public void setListItems(ArrayList<TextCheckbox> list){
		this.mItems = list;
	}

	public int getCount(){
		return this.mItems.size();
	}

	public ArrayList<TextCheckbox> getItems(){
		return this.mItems;
	}

	public TextCheckbox getItem(int index){
		return this.mItems.get(index);
	}

	public long getItemId(int position){
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent){
		TextCheckboxView tcv;
		if (convertView == null){
			tcv = new TextCheckboxView(mContext, mItems.get(position));
		} else {
			tcv = (TextCheckboxView) convertView;
			tcv.setText(mItems.get(position).getText());
			tcv.setIcon(mItems.get(position).getIcon());
		}

		return tcv;
	}
}
