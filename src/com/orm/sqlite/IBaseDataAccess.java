package com.orm.sqlite;

import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.SQLException;

/**
 * Every data access interface should extend this interface and this library 
 * use should be through the interfaces only
 * @author Noam
 * 
 */
public interface IBaseDataAccess<T> {

	/**
	 * Insert or update an entity in the database
	 * @param something The entity to update
	 * @return The id, whether it new or exists 
	 */
	long save(T something);

	/**
	 * Returning all the entities in the database
	 */
	List<T> findAll();

	/**
	 * Returning all the entities ordered by the parameter
	 */
	List<T> findAll(String orderBy);

	/**
	 * Finding one entity by its database Id
	 */
	T findById(Long id);

	/**
	 * Finding all the entities by one property of them
	 * @param key The column as it represented in the database
	 * @param value The value to search for
	 * @return List of all the entities
	 */
	List<T> findByProperty(String key, Object value);

	/**
	 * The same as {@link IBaseDataAccess#findByProperty(String, Object)} but with sorting
	 * @see IBaseDataAccess#findByProperty(String, Object)
	 * @param orderBy The column to order by the results
	 */
	List<T> findByProperty(String key, Object value, String orderBy);

	/**
	 * Finding all the entities by many properties of them
	 * @param properties The column as it represented in the database to the value to search for
	 * @return List of all the entities
	 */
	List<T> findByProperties(Map<String, Object> properties);

	/**
	 * The same as {@link IBaseDataAccess#findByProperties(Map)} but with sorting
	 * @see IBaseDataAccess#findByProperties(Map)
	 * @param orderBy The column to order by the results
	 */
	List<T> findByProperties(Map<String, Object> properties, String orderBy);

	/**
	 * A convenient method to get the cursor in order to lazy fetch many results
	 * @param orderBy The column to order by the results
	 */
	Cursor cursorFindAll(String orderBy);

	/**
	 * A convenient method to get the cursor in order to lazy fetch many results
	 * @see IBaseDataAccess#findByProperty(String, Object, String)
	 * @param orderBy The column to order by the results
	 */
	Cursor cursorFindByProperty(String key, Object value, String orderBy);

	/**
	 * A convenient method to get the cursor in order to lazy fetch many results
	 * @see IBaseDataAccess#findByProperties(Map, String)
	 * @param orderBy The column to order by the results
	 */
	Cursor cursorFindByProperties(Map<String, Object> properties, String orderBy);

	/**
	 * Updating the selected rows in the database with the new data in the second paramater
	 * @param query The rows to select (column name -> value to select)
	 * @param update The data to update to (column name -> value to update)
	 */
	void updateByProperties(Map<String, Object> query, Map<String, Object> update);

	/**
	 * Delete one entity by its database Id
	 */
	void deleteById(String id);

	/**
	 * Delete all the entities that selected by the column name and row value
	 * @param key The column name
	 * @param value The value to select by
	 */
	void deleteByProperty(String key, Object value);

	/**
	 * Delete all the entities that selected by the column names and row values
	 * @param map The data to update to (column name -> value to select by)
	 */
	void deleteByProperties(Map<String, Object> properties);

	/**
	 * Is the database open?
	 * @return true or false
	 */
	boolean isOpen();

	/**
	 * Open this database
	 * @throws SQLException
	 */
	void open() throws SQLException;

	/**
	 * Close this database
	 */
	void close();
}