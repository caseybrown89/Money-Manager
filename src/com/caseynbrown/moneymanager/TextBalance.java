package com.caseynbrown.moneymanager;

public class TextBalance {
	private int mId;
	private String mDate = "";
	private String mName = "";
	private int mBalance;
	private String mLatestDate = "";
	private String mLatestWhere = "";
	private int mLatestAmount;

	public TextBalance(int id, String name, int balance, String latestDate, String latestWhere, int latestAmount){
		this.mId = id;
		this.mName = name;
		this.mBalance = balance;
		this.mLatestDate = latestDate;
		this.mLatestWhere = latestWhere;
		this.mLatestAmount = latestAmount;
	}

	public TextBalance(int id, String date, String name, int balance){
		this.mId = id;
		this.mDate = date;
		this.mName = name;
		this.mBalance = balance;
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

	public int getBalance(){
		return this.mBalance;
	}

	public void setBalance(int amt){
		this.mBalance = amt;
	}
	
	public String getLatestDate(){
		return this.mLatestDate;
	}

	public void setLatestDate(String date){
		this.mLatestDate = date;
	}
	
	public String getLatestWhere(){
		return this.mLatestWhere;
	}

	public void setLatestWhere(String where){
		this.mLatestWhere = where;
	}
	
	public int getLatestAmount(){
		return this.mLatestAmount;
	}

	public void setLatestAmount(int amt){
		this.mLatestAmount= amt;
	}
}
