package com.rameses.clfc.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rameses.db.android.AbstractDB;

public class RemarksRemovedDB extends AbstractDB 
{
	public final static Object LOCK = new Object();
	public RemarksRemovedDB(Context ctx, String dbname, int dbversion) {
		super(ctx, dbname, dbversion); 
		setDebug(true);
	}

	protected void onCreateProcess(SQLiteDatabase sqldb) { 
		System.out.println("clfcremarksremoveddb oncreateprocess");
		try { 
			loadDBResource(sqldb, "clfcremarksremoveddb_create");
			System.out.println("clfcremarksremoveddb created");
		} catch(RuntimeException re) {
			throw re; 
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e); 
		}
	}

	protected void onUpgradeProcess(SQLiteDatabase sqldb, int arg1, int arg2) {
		System.out.println("clfcremarksremoveddb onUpgradeProcess");
		try { 
			loadDBResource(sqldb, "clfcremarksremoveddb_upgrade");
			onCreate(sqldb); 
		} catch(RuntimeException re) {
			throw re; 
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e); 
		}		
	}
}
