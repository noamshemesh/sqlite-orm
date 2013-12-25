package com.orm.sqlite;

import java.util.List;

import android.database.SQLException;

/**
 * 
 * @author Noam
 *
 */
public interface IBaseDataAccess<T> {

	long save(T something);

	List<T> findAll();

	List<T> findAll(String orderBy);
	
	T findById(Long id);
	
	List<T> findByProperty(String key, String value);

	List<T> findByProperty(String key, String value, String orderBy);
	
	void deleteById(String id);
	
	boolean isOpen();
	
	void open() throws SQLException;

	void close();

}