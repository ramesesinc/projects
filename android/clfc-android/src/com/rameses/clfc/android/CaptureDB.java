package com.rameses.clfc.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rameses.db.android.AbstractDB;

public class CaptureDB extends AbstractDB 
{
	public final static Object LOCK = new Object();
	private static CaptureDB instance;	
	public static CaptureDB getInstance() {
		return instance; 
	}
	
	
	public CaptureDB(Context ctx, String dbname, int dbversion) {
		super(ctx, dbname, dbversion); 
		CaptureDB.instance = this;
		instance.setDebug(true);
	}

	protected void onCreateProcess(SQLiteDatabase sqldb) { 
		try { 
			loadDBResource(sqldb, "clfccapturedb_create"); 
		} catch(RuntimeException re) {
			throw re; 
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e); 
		}
	}
	
	protected void loadDBResourcex(SQLiteDatabase sqldb, String name) {
		super.loadDBResource(sqldb, name);
	}
	

	protected void onUpgradeProcess(SQLiteDatabase sqldb, int arg1, int arg2) {
		try { 
			loadDBResource(sqldb, "clfccapturedb_upgrade");
			onCreate(sqldb); 
		} catch(RuntimeException re) {
			throw re; 
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e); 
		}		
	}
}
