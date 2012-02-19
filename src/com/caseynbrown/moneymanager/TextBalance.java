package com.caseynbrown.moneymanager;

public class TextBalance {
	private int mId;
	private String mDate = "";
	private String mName = "";
	private int mAmount;

	public TextBalance(int id, String name, int amount){
		this.mId = id;
		this.mName = name;
		this.mAmount = amount;
	}

	public TextBalance(int id, String date, String name, int amount){
		this.mId = id;
		this.mDate = date;
		this.mName = name;
		this.mAmount = amount;
	}

	public int getId(){
		return this.mId;
	}

	public void setId(int id){
		this.mId = id;
	}

	public String getName(){
		return this.mName;
	}

	public void setName(String text){
		this.mName = text;
	}

	public String getDate(){
		return this.mDate;
	}

	public void setDate(String date){
		this.mDate = date;
	}

	public int getAmount(){
		return this.mAmount;
	}

	public void setAmount(int amt){
		this.mAmount = amt;
	}
}
