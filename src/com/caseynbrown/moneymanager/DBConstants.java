package com.caseynbrown.moneymanager;
import android.provider.BaseColumns;


public interface DBConstants extends BaseColumns {
	/* Table names */
	public static final String TABLE_NAME_PEOPLE = "people";
	public static final String TABLE_NAME_ENTRY = "entries";

	/* Columns in the People table */
	public static final String NAME_PEOPLE = "name";
	public static final String AMOUNT_PEOPLE = "amount_people";

	/* Columns in the Entry table */
	public static final String USER_ENTRY = "user";
	public static final String DATE_ENTRY = "date";
	public static final String TITLE_ENTRY = "title";
	public static final String AMOUNT_ENTRY = "amount_entry";
	public static final String NOTES_ENTRY = "notes";
}
