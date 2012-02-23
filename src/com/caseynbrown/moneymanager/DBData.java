package com.caseynbrown.moneymanager;
import static android.provider.BaseColumns._ID;
import static com.caseynbrown.moneymanager.DBConstants.AMOUNT_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.AMOUNT_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.DATE_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.NAME_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.NOTES_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.TABLE_NAME_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.TABLE_NAME_PEOPLE;
import static com.caseynbrown.moneymanager.DBConstants.TITLE_ENTRY;
import static com.caseynbrown.moneymanager.DBConstants.USER_ENTRY;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBData extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "main.db";
	private static final int DATABASE_VERSION = 1;

	public DBData(Context ctx){
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
	    super.onOpen(db);
	    if (!db.isReadOnly()) {
	        // Enable foreign key constraints
	        db.execSQL("PRAGMA foreign_keys=ON;");
	    }
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL("CREATE TABLE " + TABLE_NAME_PEOPLE + "("
				+_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ NAME_PEOPLE + " STRING NOT NULL, "
				+ AMOUNT_PEOPLE + " INTEGER);");
		db.execSQL("CREATE TABLE " + TABLE_NAME_ENTRY + " ("
				+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ USER_ENTRY + " INTEGER NOT NULL, "
				+ DATE_ENTRY + " STRING NOT NULL, "
				+ TITLE_ENTRY + " STRING NOT NULL, "
				+ AMOUNT_ENTRY + " INTEGER NOT NULL, "
				+ NOTES_ENTRY + " STRING, " +
				"FOREIGN KEY ("+ USER_ENTRY +") REFERENCES "+TABLE_NAME_PEOPLE+" ("+_ID+") ON DELETE CASCADE);");
		db.execSQL("CREATE TRIGGER update_total_balance AFTER INSERT ON "+TABLE_NAME_ENTRY+" BEGIN " +
				"UPDATE "+TABLE_NAME_PEOPLE+" SET "+AMOUNT_PEOPLE+" = "+AMOUNT_PEOPLE+" + NEW."+AMOUNT_ENTRY+" WHERE "+_ID+" = NEW."+USER_ENTRY+";" +
						"END;");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PEOPLE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ENTRY);
		onCreate(db);
	}


}
