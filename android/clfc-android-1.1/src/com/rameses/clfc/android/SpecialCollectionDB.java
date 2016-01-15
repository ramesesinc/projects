package com.rameses.clfc.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rameses.db.android.AbstractDB;

public class SpecialCollectionDB extends AbstractDB 
{
	public final static Object LOCK = new Object();
	private static SpecialCollectionDB instance;	
	public static SpecialCollectionDB getInstance() {
		return instance; 
	}
	
	
	public SpecialCollectionDB(Context ctx, String dbname, int dbversion) {
		super(ctx, dbname, dbversion); 
		SpecialCollectionDB.instance = this;
		instance.setDebug(true);
	}

	protected void onCreateProcess(SQLiteDatabase sqldb) { 
		try { 
			loadDBResource(sqldb, "clfcspecialcollectiondb_create"); 
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
			loadDBResource(sqldb, "clfcspecialcollectiondb_upgrade");
			onCreate(sqldb); 
		} catch(RuntimeException re) {
			throw re; 
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e); 
		}		
	}
}
