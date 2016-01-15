package com.rameses.clfc.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rameses.db.android.AbstractDB;

public class MainDB extends AbstractDB 
{
	public final static Object LOCK = new Object();
	private static MainDB instance;	
	public static MainDB getInstance() {
		return instance; 
	}
	
	
	public MainDB(Context ctx, String dbname, int dbversion) {
		super(ctx, dbname, dbversion); 
		MainDB.instance = this;
		instance.setDebug(true);
		System.out.println("instantiate MainDB");
	}

	protected void onCreateProcess(SQLiteDatabase sqldb) { 
		System.out.println("clfcdb oncreateprocess");
		try { 
			loadDBResource(sqldb, "clfcdb_create"); 
			System.out.println("clfcdb created");
		} catch(RuntimeException re) {
			throw re; 
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e); 
		}
	}
	
	protected void loadDBResourcex(SQLiteDatabase sqldb, String name) {
		System.out.println("dbname -> "+name);
		super.loadDBResource(sqldb, name);
	}
	

	protected void onUpgradeProcess(SQLiteDatabase sqldb, int arg1, int arg2) {
		System.out.println("clfcdb onUpgradeProcess");
		try { 
			loadDBResource(sqldb, "clfcdb_upgrade");
			System.out.println("clfcdb upgraded");
			onCreate(sqldb); 
		} catch(RuntimeException re) {
			throw re; 
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e); 
		}		
	}
}
