package com.rameses.android.main;

import com.rameses.android.efaas.HomeActivity;
import com.rameses.android.system.AbstractHomeLoader;

public class DefaultHomeLoader extends AbstractHomeLoader 
{

	@Override
	protected Class<?> getActivityClass() {
		return HomeActivity.class;
	}

}
