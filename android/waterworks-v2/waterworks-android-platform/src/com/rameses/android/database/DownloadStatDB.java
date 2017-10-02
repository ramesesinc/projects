package com.rameses.android.database;

import java.util.Map;

import com.rameses.android.AbstractDBMapper;
import com.rameses.android.bean.DownloadStat;
import com.rameses.db.android.DBContext;
import com.rameses.util.MapProxy;

public class DownloadStatDB extends AbstractDBMapper {

	public String getTableName() {
		return "download_stat";
	}
	
	public DownloadStat findByUserid(String userid) throws Exception {
		DBContext ctx = getDBContext();
		try {
			DownloadStat ds = new DownloadStat();
			
			String sql = "SELECT  * FROM " + getTableName() + " WHERE assigneeid=? LIMIT 1";
			Map result = ctx.find(sql, new Object[]{userid});
			
			if (result != null && !result.isEmpty()) {
				ds.setBatchid(MapProxy.getString(result, "batchid"));
				ds.setAssigneeid(MapProxy.getString(result, "assigneeid"));
				ds.setRecordcount(MapProxy.getInteger(result, "recordcount"));
				ds.setIndexno(MapProxy.getInteger(result, "indexno"));
			}
			
			return ds;
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
	
	public DownloadStat findByBatchid(String batchid) throws Exception {
		DBContext ctx = getDBContext();
		try {
			DownloadStat ds = new DownloadStat();
			
			String sql = "SELECT  * FROM " + getTableName() + " WHERE batchid=? LIMIT 1";
			Map result = ctx.find(sql, new Object[]{batchid});
			
			if (result != null && !result.isEmpty()) {
				ds.setBatchid(MapProxy.getString(result, "batchid"));
				ds.setAssigneeid(MapProxy.getString(result, "assigneeid"));
				ds.setRecordcount(MapProxy.getInteger(result, "recordcount"));
				ds.setIndexno(MapProxy.getInteger(result, "indexno"));
			}
			
			return ds;
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
	
}
