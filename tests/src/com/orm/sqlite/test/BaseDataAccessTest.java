package com.orm.sqlite.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.test.ProviderTestCase2;

import com.orm.sqlite.example.Example;
import com.orm.sqlite.example.ExampleDataAccess;
import com.orm.sqlite.test.BaseDataAccessTest.DummyProvider;

public class BaseDataAccessTest extends ProviderTestCase2<DummyProvider> {
	public static class DummyProvider extends ContentProvider {

		@Override
		public int delete(Uri uri, String selection, String[] selectionArgs) {
			return 0;
		}

		@Override
		public String getType(Uri uri) {
			return "com";
		}

		@Override
		public Uri insert(Uri uri, ContentValues values) {
			return null;
		}

		@Override
		public boolean onCreate() {
			return false;
		}

		@Override
		public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
			return null;
		}

		@Override
		public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
			return 0;
		}
	}

	public BaseDataAccessTest() {
		super(DummyProvider.class, "com");
	}

	private ExampleDataAccess tested;

	protected void setUp() throws Exception {
		super.setUp();
		SQLiteOpenHelper openHelper = new SQLiteOpenHelper(mContext, "sqlite-orm-test", null, 1) {
			@Override
			public void onOpen(SQLiteDatabase db) {
				db.execSQL("DROP TABLE IF EXISTS " + ExampleDataAccess.TABLE_NAME);
				db.execSQL(ExampleDataAccess.DATABASE_CREATE);
			}
			
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			}

			@Override
			public void onCreate(SQLiteDatabase db) {
				onUpgrade(db, 0, 0);
			}
		};
		tested = new ExampleDataAccess(openHelper);
		tested.open();
		tested.clear();
	}

	public Long randomLong() {
		return new Random().nextLong();
	}

	public Boolean randomBoolean() {
		return new Random().nextBoolean();
	}

	public Date randomDate() {
		long curr = System.currentTimeMillis();
		return new Date(curr - 10000 + randomLong() * 10000);
	}

	public Double randomDouble() {
		return new Random().nextDouble() * 10000;
	}

	public byte[] randomByteArray() {
		byte[] arr = new byte[10];
		new Random().nextBytes(arr);
		return arr;
	}

	public Float randomFloat() {
		return new Random().nextFloat() * 10000;
	}

	public Integer randomInteger() {
		return new Random().nextInt();
	}

	public Example getExample() {
		return new Example(null, "abc", randomBoolean(), randomLong(), randomInteger(), randomDate(), randomDouble(),
				randomFloat(), randomByteArray());
	}

	public void testSaving() {
		Example example = getExample();
		Long id = tested.save(example);
		example.setId(id);
		Example dbExample = tested.findById(id);

		assertEquals(example, dbExample);
	}

	public void testUpdateOneEntity() {
		Example example = getExample();
		Long id = tested.save(example);
		example.setId(id);
		Example dbExample = tested.findById(id);
		assertEquals(example, dbExample);
		example.setLng(randomLong());
		tested.save(example);
		dbExample = tested.findById(id);
		assertEquals(example, dbExample);
	}

	public void testFindByProperty() {
		Example example = getExample();
		Long id = tested.save(example);
		example.setId(id);
		Example dbExample = tested.findByProperty(ExampleDataAccess.COLUMN_DATE, example.getDate()).get(0);
		assertEquals(example, dbExample);
		dbExample = tested.findByProperty(ExampleDataAccess.COLUMN_LNG, example.getLng()).get(0);
		assertEquals(example, dbExample);
		dbExample = tested.findByProperty(ExampleDataAccess.COLUMN_STR, example.getStr()).get(0);
		assertEquals(example, dbExample);
		dbExample = tested.findByProperty(ExampleDataAccess.COLUMN_INTEGER, example.getInteger()).get(0);
		assertEquals(example, dbExample);
		dbExample = tested.findByProperty(ExampleDataAccess.COLUMN_BOOL, example.getBool()).get(0);
		assertEquals(example, dbExample);
		dbExample = tested.findByProperty(ExampleDataAccess.COLUMN_FLT, example.getFlt()).get(0);
		assertEquals(example, dbExample);
		dbExample = tested.findByProperty(ExampleDataAccess.COLUMN_DBL, example.getDbl()).get(0);
		assertEquals(example, dbExample);
	}

	public void testFindByPropertyByteArray() {
		Example example = getExample();
		Long id = tested.save(example);
		example.setId(id);
		try {
			tested.findByProperty(ExampleDataAccess.COLUMN_BYTE_ARRAY, example.getByteArray()).get(0);
		} catch (IllegalArgumentException e) {
			return;
		}
		fail();
	}

	public void testFindByProperties() {
		Example example = getExample();
		Long id = tested.save(example);
		example.setId(id);
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(ExampleDataAccess.COLUMN_DATE, example.getDate());
		properties.put(ExampleDataAccess.COLUMN_LNG, example.getLng());
		properties.put(ExampleDataAccess.COLUMN_STR, example.getStr());
		properties.put(ExampleDataAccess.COLUMN_INTEGER, example.getInteger());
		properties.put(ExampleDataAccess.COLUMN_BOOL, example.getBool());
		properties.put(ExampleDataAccess.COLUMN_FLT, example.getFlt());
		properties.put(ExampleDataAccess.COLUMN_DBL, example.getDbl());

		Example dbExample = tested.findByProperties(properties).get(0);
		assertEquals(example, dbExample);
	}

	public void testUpdate() {
		Example example = getExample();
		Long id = tested.save(example);
		example.setId(id);
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(ExampleDataAccess.COLUMN_LNG, example.getLng());
		Map<String, Object> update = new HashMap<String, Object>();
		Long randomLong = randomLong();
		update.put(ExampleDataAccess.COLUMN_LNG, randomLong);
		example.setLng(randomLong);
		tested.updateByProperties(properties, update);
		Example dbExample = tested.findById(id);
		assertEquals(example, dbExample);
	}

	public void testDeleteByProperties() {
		Example example = getExample();
		Long id = tested.save(example);
		example.setId(id);
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(ExampleDataAccess.COLUMN_LNG, example.getLng());
		tested.deleteByProperties(properties);
		Example dbExample = tested.findById(id);
		assertEquals(null, dbExample);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		tested.close();
	}

}
