package com.orm.sqlite;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author Noam
 * 
 */
public abstract class BaseDataAccess<T> implements IBaseDataAccess<T> {
	// Database fields
	protected SQLiteDatabase database;
	protected SQLiteOpenHelper dbHelper;

	public BaseDataAccess(SQLiteOpenHelper dbHelper) {
		this.dbHelper = dbHelper;
	}
	
	public boolean isOpen() {
		return database.isOpen();
	}

	public void open() throws SQLException {
		if (database == null || !database.isOpen())
			database = dbHelper.getWritableDatabase();
	}

	public void close() {
		if (database != null && database.isOpen())
			database.close();
		if (dbHelper != null)
			dbHelper.close();
	}

	public List<T> findAll() {
		return findAll(null);
	}

	public List<T> findAll(String orderBy) {
		List<T> tis = new LinkedList<T>();

		Cursor cursor = cursorFindAll(orderBy);
		while (!cursor.isAfterLast()) {
			tis.add(cursorToT(cursor));
			cursor.moveToNext();
		}

		cursor.close();
		return tis;
	}

	public synchronized Cursor cursorFindAll(String orderBy) {
		Cursor cursor = database.query(getTable(), getAllColumns(), null, null, null, null, orderBy);
		cursor.moveToFirst();
		return cursor;
	}

	public T findById(Long id) {
		if (id == null) {
			return null;
		}

		List<T> list = findByProperty(getColumnId(), id.toString());
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Cursor cursorFindById(String id) {
		return cursorFindByProperty(getColumnId(), id, null);
	}

	public List<T> findByProperty(String key, String value) {
		return findByProperty(key, value, null);
	}

	public List<T> findByProperty(String key, String value, String orderBy) {
		if (key == null || value == null) {
			return null;
		}
		Cursor cursor = null;
		try {
			List<T> toReturn = new LinkedList<T>();
			cursor = cursorFindByProperty(key, value, orderBy);
			while (cursor.getCount() > 0 && !cursor.isAfterLast()) {
				toReturn.add(cursorToT(cursor));
				cursor.moveToNext();
			}
			return toReturn;
		} finally {
			if (cursor != null) 
				cursor.close();
		}
	}

	public synchronized Cursor cursorFindByProperty(String key, String value, String orderBy) {
		Cursor cursor = database.query(getTable(), getAllColumns(), key + " = ?", new String[] { value }, null, null, orderBy);
		cursor.moveToFirst();
		return cursor;
	}

	protected synchronized long save(Long id, String[] keys, Object[] values) {
		ContentValues cv = new ContentValues();
		if (keys.length != values.length) {
			throw new IllegalArgumentException("Keys size != values size");
		}

		for (int i = 0; i < keys.length; i++) {
			Object value = values[i];
			String key = keys[i];
			if (value == null) {
				cv.put(key, (String) null);
			} else if (value instanceof String) {
				cv.put(key, (String) value);
			} else if (value instanceof Long) {
				cv.put(key, (Long) value);
			} else if (value instanceof Double) {
				cv.put(key, (Double) value);
			} else if (value instanceof Integer) {
				cv.put(key, (Integer) value);
			} else if (value instanceof Boolean) {
				cv.put(key, ((Boolean) value ? 1 : 0));
			} else if (value instanceof Date) {
				cv.put(key, ((Date) value).getTime());
			} else if (value instanceof Float) {
				cv.put(key, (Float) value);
			} else if (value instanceof byte[]) {
				cv.put(key, (byte[]) value);
			} else {
				throw new IllegalArgumentException("Value of " + key + "=" + value + " type is not supported.");
			}
		}

		if (id == null) {
			return database.insertOrThrow(getTable(), null, cv);
		} else {
			if (findById(id) != null) {
				database.update(getTable(), cv, getColumnId() + " = ?", new String[] { id.toString() });
			} else {
				cv.put(getColumnId(), id);
				database.insertOrThrow(getTable(), null, cv);
			}
			return id;
		}
	}

	public synchronized void deleteByProperty(String key, String value) {
		database.delete(getTable(), key + " = ?", new String[] { value });
	}

	public void deleteById(String id) {
		deleteByProperty(getColumnId(), id);
	}
	
	protected String getString(Cursor cursor, String columnName) {
		return cursor.getString(cursor.getColumnIndex(columnName));
	}
	
	protected Double getDouble(Cursor cursor, String columnName) {
		return cursor.getDouble(cursor.getColumnIndex(columnName));
	}

	protected Boolean getBoolean(Cursor cursor, String columnName) {
		Integer value = getInteger(cursor, columnName);
		if (value == null) {
			return null;
		} else {
			return value == 1 ? true : false;
		}
	}

	protected Long getLong(Cursor cursor, String columnName) {
		int columnIndex = cursor.getColumnIndex(columnName);
		if (cursor.isNull(columnIndex)) {
			return null;
		} else {
			return cursor.getLong(columnIndex);
		}
	}

	protected Integer getInteger(Cursor cursor, String columnName) {
		int columnIndex = cursor.getColumnIndex(columnName);
		if (cursor.isNull(columnIndex)) {
			return null;
		} else {
			return cursor.getInt(columnIndex);
		}
	}

	protected Date getDate(Cursor cursor, String columnName) {
		return new Date(cursor.getLong(cursor.getColumnIndex(columnName)));
	}

	protected byte[] getByteArray(Cursor cursor, String columnName) {
		return cursor.getBlob(cursor.getColumnIndex(columnName));
	}

	protected abstract String getColumnId();

	protected abstract T cursorToT(Cursor cursor);

	protected abstract String getTable();

	protected abstract String[] getAllColumns();

	protected abstract String getDatabaseCreate();

}
