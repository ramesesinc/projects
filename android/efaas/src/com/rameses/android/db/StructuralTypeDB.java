package com.rameses.android.db;

import com.rameses.android.AbstractDBMapper;
import com.rameses.db.android.DBContext;

public class StructuralTypeDB extends AbstractDBMapper {

	@Override
	public String getTableName() {
		return "bldgrpu_structuraltype";
	}	
	
}
