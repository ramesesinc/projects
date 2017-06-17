package com.rameses.android.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.rameses.android.AbstractDBMapper;
import com.rameses.db.android.DBContext;

public class BldgKindDB extends AbstractDBMapper {

	@Override
	public String getTableName() {
		return "bldgkind";
	}
	
	public List<Map> getBuildingKinds(String bldgtypeid){
		List<Map> list = new ArrayList<Map>();
		DBContext ctx = getDBContext();
		try{
			list = ctx.getList("SELECT b.*, bc.basevalue FROM bldgkind b " +
					"INNER JOIN bldgkindbucc bc ON b.objid = bc.bldgkind_objid " +
					"WHERE bc.bldgtypeid = ?",
					new String[]{bldgtypeid});
		}catch(Throwable t){
			t.printStackTrace();
		}finally {
			if (isAutoCloseConnection()) ctx.close();
		}
		return list;
	}
	
}
