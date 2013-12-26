package com.orm.sqlite;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orm.sqlite.util.Tuple;

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

	@Override
	public boolean isOpen() {
		return database.isOpen();
	}

	@Override
	public void open() throws SQLException {
		if (database == null || !database.isOpen())
			database = dbHelper.getWritableDatabase();
	}

	@Override
	public void close() {
		if (database != null && database.isOpen())
			database.close();
		if (dbHelper != null)
			dbHelper.close();
	}

	@Override
	public List<T> findAll() {
		return findAll(null);
	}

	@Override
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

	@Override
	public synchronized Cursor cursorFindAll(String orderBy) {
		Cursor cursor = database.query(getTable(), getAllColumns(), null, null, null, null, orderBy);
		cursor.moveToFirst();
		return cursor;
	}

	@Override
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
	
	@Override
	public List<T> findByProperties(Map<String, Object> properties) {
		return findByProperties(properties, null);
	}
	
	@Override
	public List<T> findByProperties(Map<String, Object> properties, String orderBy) {
		if (properties == null || properties.size() == 0) {
			return null;
		}
		Cursor cursor = null;
		try {
			List<T> toReturn = new LinkedList<T>();
			cursor = cursorFindByProperties(properties, orderBy);
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

	@Override
	public List<T> findByProperty(String key, Object value) {
		return findByProperty(key, value, null);
	}

	@Override
	public List<T> findByProperty(final String key, final Object value, final String orderBy) {
		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put(key, value);
		return findByProperties(properties, orderBy);
	}

	@Override
	public Cursor cursorFindByProperty(String key, Object value, String orderBy) {
		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put(key, value);
		return cursorFindByProperties(properties, orderBy);
	}

	private Tuple<String, String[]> getWhereClause(Map<String, Object> properties) {
		String whereString = "";
		List<String> whereProperties = new ArrayList<String>();
		boolean first = true;
		for (String key : properties.keySet()) {
			if (!first) {
				whereString += " AND ";
			} else {
				first = false;
			}
			whereString += key + " = ?";
			Object representation = convertToDatabaseRepresentationForWhereClause(properties.get(key));
			whereProperties.add(representation == null ? null : representation.toString());
		}
		return new Tuple<String, String[]>(whereString, whereProperties.toArray(new String[0]));
	}

	@Override
	public synchronized Cursor cursorFindByProperties(Map<String, Object> properties, String orderBy) {
		Tuple<String, String[]> whereClause = getWhereClause(properties);
		Cursor cursor = database.query(getTable(), getAllColumns(), whereClause.getFirst(), whereClause.getSecond(), null, null,
				orderBy);
		cursor.moveToFirst();
		return cursor;
	}

	@Override
	public synchronized void updateByProperties(Map<String, Object> query, Map<String, Object> update) {
		Tuple<String, String[]> whereClause = getWhereClause(query);

		database.update(getTable(), convertToContentValues(update), whereClause.getFirst(), whereClause.getSecond());
	}
	
	@Override
	public void clear() {
		deleteByProperty(null, null);
	}

	@Override
	public void deleteByProperty(String key, Object value) {
		Map<String, Object> properties = new HashMap<String, Object>();

		if (key != null || value != null) {
			properties.put(key, value);
		}
		deleteByProperties(properties);
	}
	
	public synchronized void deleteByProperties(Map<String, Object> properties) {
		Tuple<String, String[]> whereClause = getWhereClause(properties);
		database.delete(getTable(), whereClause.getFirst(), whereClause.getSecond());
	}


	@Override
	public void deleteById(String id) {
		deleteByProperty(getColumnId(), id);
	}

	protected synchronized long save(Long id, String[] keys, Object[] values) {
		ContentValues cv = new ContentValues();
		if (keys.length != values.length) {
			throw new IllegalArgumentException("Keys size != values size");
		}

		for (int i = 0; i < keys.length; i++) {
			Object value = values[i];
			String key = keys[i];
			addToContentValues(cv, key, value);
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

	private Object convertToDatabaseRepresentationForWhereClause(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			return (String) value;
		} else if (value instanceof Long) {
			return (Long) value;
		} else if (value instanceof Double) {
			return (Double) value;
		} else if (value instanceof Integer) {
			return (Integer) value;
		} else if (value instanceof Boolean) {
			return ((Boolean) value ? 1 : 0);
		} else if (value instanceof Date) {
			return ((Date) value).getTime();
		} else if (value instanceof Float) {
			return ((Float) value).doubleValue();
		} else if (value instanceof byte[]) {
			throw new IllegalArgumentException("byte[] is not supported.");
		} else {
			throw new IllegalArgumentException("Value " + value + " type is not supported.");
		}
	}

	private void addToContentValues(ContentValues cv, String key, Object value) {
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
			cv.put(key, ((Float) value).doubleValue());
		} else if (value instanceof byte[]) {
			cv.put(key, (byte[]) value);
		} else {
			throw new IllegalArgumentException("Value of " + key + "=" + value + " type is not supported.");
		}
	}

	private ContentValues convertToContentValues(Map<String, Object> update) {
		return null;
	}

	protected String getString(Cursor cursor, String columnName) {
		return cursor.getString(cursor.getColumnIndex(columnName));
	}

	protected Double getDouble(Cursor cursor, String columnName) {
		return cursor.getDouble(cursor.getColumnIndex(columnName));
	}
	
	protected Float getFloat(Cursor cursor, String columnName) {
		return getDouble(cursor, columnName).floatValue();
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

}
