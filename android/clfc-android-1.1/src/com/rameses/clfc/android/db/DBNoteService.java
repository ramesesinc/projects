package com.rameses.clfc.android.db;

import java.util.List;
import java.util.Map;

import com.rameses.db.android.DBContext;

public class DBNoteService extends AbstractDBMapper
{

	public String getTableName() {
		return "notes";
	}

	public List<Map> getNotes(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE parentid=? ORDER BY dtcreated DESC";
			return ctx.getList(sql, new Object[]{id});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public List<Map> getNotes() throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " ORDER BY dtcreated DESC";
			return ctx.getList(sql, new Object[]{});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public boolean hasNotes(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() + " WHERE parentid=? LIMIT 1";
			return (ctx.getCount(sql, new Object[]{id}) > 0);
		} catch (Exception e) {
 			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
}
