package com.caseynbrown.moneymanager;

import android.graphics.drawable.Drawable;

public class TextCheckbox {
	private String mText = "";
	private Drawable mIcon;
	private int mId;
	private boolean mSelectable;
	private boolean mChecked;

	public TextCheckbox(int id, String text, Drawable check){
		this.mId = id;
		this.mText = text;
		this.mIcon = check;
		this.mSelectable = true;
		this.mChecked = false;
	}

	public boolean isSelectable(){
		return mSelectable;
	}

	public void setSelectable(boolean bool){
		this.mSelectable = bool;
	}

	public Drawable getIcon(){
		return this.mIcon;
	}

	public void setIcon(Drawable icon){
		this.mIcon = icon;
	}

	public String getText(){
		return this.mText;
	}

	public void setText(String text){
		this.mText = text;
	}

	public int getId(){
		return this.mId;
	}

	public void setId(int id){
		this.mId = id;
	}

	public boolean getChecked(){
		return this.mChecked;
	}

	public void setChecked(boolean bool){
		this.mChecked = bool;
	}
}
