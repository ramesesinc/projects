package com.rameses.android.database;

import java.util.List;
import java.util.Map;

import com.rameses.android.AbstractDBMapper;
import com.rameses.db.android.DBContext;

public class SettingDB extends AbstractDBMapper {

	public String getTableName() {
		return "setting";
	}

	public Map findByName(String name) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE name=?";
			return ctx.find(sql, new Object[]{name});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
	
	public List<Map> getSettings() throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName();
			return ctx.getList(sql, new Object[]{});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
	
}
