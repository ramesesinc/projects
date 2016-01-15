package com.rameses.clfc.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.rameses.db.android.AbstractDB;

public class PaymentDB extends AbstractDB 
{
	public final static Object LOCK = new Object();	
	public PaymentDB(Context ctx, String dbname, int dbversion) {
		super(ctx, dbname, dbversion); 
		setDebug(true);
	}

	protected void onCreateProcess(SQLiteDatabase sqldb) { 
		System.out.println("clfcpaymentdb oncreateprocess");
		try { 
			loadDBResource(sqldb, "clfcpaymentdb_create");
			System.out.println("clfcpaymentdb created");
		} catch(RuntimeException re) {
			throw re; 
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e); 
		}
	}

	protected void onUpgradeProcess(SQLiteDatabase sqldb, int arg1, int arg2) {
		System.out.println("clfcpaymentdb onUpgradeProcess");
		try { 
			loadDBResource(sqldb, "clfcpaymentdb_upgrade");
			onCreate(sqldb); 
		} catch(RuntimeException re) {
			throw re; 
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e); 
		}		
	}
}
