package com.rameses.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rameses.db.android.AbstractDB;

public class MainDB extends AbstractDB 
{
	public final static Object LOCK = new Object();
	private static MainDB instance;	
	
	public static MainDB getInstance() { return instance; }
	
	
	public MainDB(Context ctx, String dbname, int dbversion) {
		super(ctx, dbname, dbversion); 
		
		MainDB.instance = this;
		instance.setDebug(true);
		System.out.println("instantiate MainDB");
	}

	protected void onCreateProcess(SQLiteDatabase sqldb) { 
		try { 
			loadDBResource(sqldb, "maindb_create"); 
			System.out.println("maindb created");
		} catch(RuntimeException re) {
			throw re; 
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e); 
		}
	}
	
	protected void onUpgradeProcess(SQLiteDatabase sqldb, int arg1, int arg2) {
		try { 
			loadDBResource(sqldb, "maindb_upgrade");
			System.out.println("maindb upgraded");
			onCreate(sqldb); 
		} catch(RuntimeException re) {
			throw re; 
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e); 
		}		
	}
}
