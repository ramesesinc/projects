package com.rameses.android.main;

import com.rameses.android.system.AbstractHomeLoader;

public class DefaultHomeLoader extends AbstractHomeLoader 
{

	@Override
	protected Class<?> getActivityClass() {
		return DefaultHomeActivity.class;
	}

}
