package com.rameses.android.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rameses.android.R;
import com.rameses.android.system.AbstractHomeActivity;

public class DefaultHomeActivity extends AbstractHomeActivity 
{

	@Override
	protected void loadMenuItems(List<Map<String, Object>> items) {
		
	}

	@Override
	protected void onItemClick(String name, Map<String, Object> item) {
		if(name.equals("Master Files")) System.err.println("You clicked the master files buttons!");
	}

}
