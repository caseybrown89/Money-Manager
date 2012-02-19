package com.caseynbrown.moneymanager;
import android.provider.BaseColumns;


public interface ConstantsDB extends BaseColumns {
	/* Table names */
	public static final String TABLE_NAME_PEOPLE = "people";
	public static final String TABLE_NAME_ENTRY = "entries";

	/* Columns in the People table */
	public static final String NAME_PEOPLE = "name";
	public static final String AMOUNT_PEOPLE = "amount";

	/* Columns in the Entry table */
	public static final String USER_ENTRY = "user";
	public static final String DATE_ENTRY = "date";
	public static final String TITLE_ENTRY = "title";
	public static final String AMOUNT_ENTRY = "amount";
	public static final String NOTES_ENTRY = "notes";
}
