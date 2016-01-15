package com.rameses.clfc.android.main;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;



public class CollectionSheetInfoTabPagerAdapter 
	extends FragmentStatePagerAdapter {

	private Map<String, Object> params = new HashMap<String, Object>();
	private Fragment currentFragment = null;
	 
	public CollectionSheetInfoTabPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	
	@Override
	public Fragment getItem(int idx) {
		Bundle args = new Bundle();
		if (params.containsKey("objid")) {
			args.putString("objid", params.get("objid").toString());
		}
				
		Fragment fragment = null;
		switch (idx) {
			case 0:
				fragment = new GeneralInfoFragment();
				currentFragment = fragment;
				break;
			case 1:
				fragment = new PaymentsFragment();
				currentFragment = fragment;
				break;
			case 2:
				fragment = new CollectorRemarksFragment();
				currentFragment = fragment;
				break;
			case 3:
				fragment = new NotesFragment();
				currentFragment = fragment;
				break;
			case 4:
				fragment = new FollowupRemarksFragment();
				currentFragment = fragment;
				break;
		}
		
		if (fragment != null) {
			fragment.setArguments(args);
		}
		
		return fragment;
	}
	
	public Fragment getCurrentFragment() { return currentFragment; }

	@Override
	public int getCount() {
		return 5;
	}

	
}
