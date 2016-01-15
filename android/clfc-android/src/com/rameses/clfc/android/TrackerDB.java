package com.rameses.clfc.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rameses.db.android.AbstractDB;

public class TrackerDB extends AbstractDB
{	
	public final static Object LOCK = new Object();
	public TrackerDB(Context ctx, String dbname, int dbversion) {
		super(ctx, dbname, dbversion); 
		setDebug(true);
		System.out.println("instantiate TrackerDB");
	}

	protected void onCreateProcess(SQLiteDatabase sqldb) { 
		System.out.println("clfctracker oncreateprocess");
		try { 
			loadDBResource(sqldb, "clfctrackerdb_create"); 
			System.out.println("clfctracker created");
		} catch(RuntimeException re) {
			throw re; 
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e); 
		}
	}

	protected void onUpgradeProcess(SQLiteDatabase sqldb, int arg1, int arg2) {
		System.out.println("clfctracker onUpgradeProcess");
		try { 
			loadDBResource(sqldb, "clfctrackerdb_upgrade");
			onCreate(sqldb); 
		} catch(RuntimeException re) {
			throw re; 
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e); 
		}		
	}
}
