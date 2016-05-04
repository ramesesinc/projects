package com.rameses.android.db;

import java.util.Map;

import com.rameses.android.AbstractDBMapper;
import com.rameses.db.android.DBContext;

public class PropertyClassificationDB extends AbstractDBMapper {

	@Override
	public String getTableName() {
		return "propertyclassification";
	}	
}
