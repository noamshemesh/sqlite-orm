package com.orm.sqlite.example;

import com.orm.sqlite.BaseDataAccess;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class ExampleDataAccess extends BaseDataAccess<Example> {

	public ExampleDataAccess(SQLiteOpenHelper dbHelper) {
		super(dbHelper);
	}
	
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_STR = "str";		
	public static final String COLUMN_BOOL = "bool";		
	public static final String COLUMN_LNG = "lng";		
	public static final String COLUMN_INTEGER = "integer";		
	public static final String COLUMN_DATE = "date";		
	public static final String COLUMN_DBL = "dbl";		
	public static final String COLUMN_FLT = "flt";		
	public static final String COLUMN_BYTE_ARRAY = "byte_array";		

	public static final String TABLE_NAME = "Example";

	public static final String DATABASE_CREATE = "create table " + TABLE_NAME + 
		"(" + COLUMN_ID + " integer primary key autoincrement," +
			COLUMN_STR + " text, " +
			COLUMN_BOOL + " number, " +
			COLUMN_LNG + " number, " +
			COLUMN_INTEGER + " number, " +
			COLUMN_DATE + " number, " +
			COLUMN_DBL + " number, " +
			COLUMN_FLT + " number, " +
			COLUMN_BYTE_ARRAY + " blob " +
			");";
	@Override
	public long save(Example something) {
		return save(
				something.getId(),
				new String[] { 
					COLUMN_STR, 
					COLUMN_BOOL, 
					COLUMN_LNG, 
					COLUMN_INTEGER, 
					COLUMN_DATE, 
					COLUMN_DBL, 
					COLUMN_FLT, 
					COLUMN_BYTE_ARRAY, 
 },
				new Object[] { 
					something.getStr(), 
					something.getBool(), 
					something.getLng(), 
					something.getInteger(), 
					something.getDate(), 
					something.getDbl(), 
					something.getFlt(), 
					something.getByteArray(), 
 });
	}

	@Override
	protected String getColumnId() {
		return COLUMN_ID;
	}

	@Override
	protected Example cursorToT(Cursor cursor) {
		return new Example(getLong(cursor, COLUMN_ID), 
				getString(cursor, COLUMN_STR) , 
				getBoolean(cursor, COLUMN_BOOL) , 
				getLong(cursor, COLUMN_LNG) , 
				getInteger(cursor, COLUMN_INTEGER) , 
				getDate(cursor, COLUMN_DATE) , 
				getDouble(cursor, COLUMN_DBL) , 
				getFloat(cursor, COLUMN_FLT) , 
				getByteArray(cursor, COLUMN_BYTE_ARRAY) 
		);
	}

	@Override
	protected String getTable() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getAllColumns() {
		return new String[] { COLUMN_ID, 
				COLUMN_STR, 
				COLUMN_BOOL, 
				COLUMN_LNG, 
				COLUMN_INTEGER, 
				COLUMN_DATE, 
				COLUMN_DBL, 
				COLUMN_FLT, 
				COLUMN_BYTE_ARRAY, 
 		}; 
	}
}