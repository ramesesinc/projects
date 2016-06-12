package com.rameses.android.db;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.rameses.android.AbstractDBMapper;
import com.rameses.android.efaas.bean.ImageItem;
import com.rameses.db.android.DBContext;

public class ImageDB extends AbstractDBMapper {

	@Override
	public String getTableName() {
		return "images";
	}
	
}
