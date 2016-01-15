package com.rameses.clfc.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rameses.db.android.AbstractDB;

public class RemarksDB extends AbstractDB 
{
	public final static Object LOCK = new Object();
	public RemarksDB(Context ctx, String dbname, int dbversion) {
		super(ctx, dbname, dbversion); 
		setDebug(true);
	}

	protected void onCreateProcess(SQLiteDatabase sqldb) { 
		System.out.println("clfcremarksdb oncreateprocess");
		try { 
			loadDBResource(sqldb, "clfcremarksdb_create");
			System.out.println("clfcremarksdb created");
		} catch(RuntimeException re) {
			throw re; 
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e); 
		}
	}

	protected void onUpgradeProcess(SQLiteDatabase sqldb, int arg1, int arg2) {
		System.out.println("clfcremarksdb onUpgradeProcess");
		try { 
			loadDBResource(sqldb, "clfcremarksdb_upgrade");
			onCreate(sqldb); 
		} catch(RuntimeException re) {
			throw re; 
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e); 
		}		
	}
}
