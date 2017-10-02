package com.rameses.android.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.rameses.android.R;
import com.rameses.android.UserInfoMenuActivity;

public class AccountsActivity extends UserInfoMenuActivity {
	private final int SIZE = 2;
	private AccountsTabPagerAdapter adapter;
	private ViewPager pager;
	private ActionBar actionBar;

	 protected void onCreateProcess(Bundle savedInstanceState) {
		 
		 super.onCreateProcess(savedInstanceState);
		 setTitle("Accounts");
		 setContentView(R.layout.template_footer);
		 RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		 LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 inflater.inflate(R.layout.activity_menu_accounts, rl_container, true);
		  
		 adapter = new AccountsTabPagerAdapter(getSupportFragmentManager(), SIZE);
		 
		 pager = (ViewPager) findViewById(R.id.accounts_pager);
		 pager.setAdapter(adapter);

		 pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			 
			public void onPageSelected(int pos) {
				println("on page selected");
//				actionBar = getActionBar();
				actionBar = getSupportActionBar();
				actionBar.setSelectedNavigationItem(pos);
			}

			public void onPageScrollStateChanged(int arg0) {
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
		 });
		 
		 actionBar = getSupportActionBar();
//		 actionBar = getActionBar();
		 actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		 ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabReselected(Tab t, FragmentTransaction ft) {
			}

			public void onTabSelected(Tab t, FragmentTransaction ft) {
				int pos = t.getPosition();
				pager.setCurrentItem(pos);
				reloadList(t.getTag().toString(), pos);
			}

			public void onTabUnselected(Tab t, FragmentTransaction ft) {
			}

		 };
		 actionBar.addTab(actionBar.newTab().setTag("ZONE").setText("Zones").setTabListener(tabListener));
		 actionBar.addTab(actionBar.newTab().setTag("ACCOUNT").setText("Accounts").setTabListener(tabListener));

	 }

	 protected void onStartProcess() {
		 super.onStartProcess();
	 }
	 
	 private void reloadList(String tag, int pos) {
		 Fragment fragment = adapter.getItem(pos);
		 if (fragment != null) {
			 if ("ZONE".equals(tag)) {
				 ((ZoneFragment) fragment).reload();
			 } else if ("ACCOUNT".equals(tag)) {
				 ((AccountFragment) fragment).reload();
			 }
		 }

	 }
	 
	 void println(String msg) {
		 Log.i("AccountsActivity", msg);
	 }
}


class AccountsTabPagerAdapter extends FragmentStatePagerAdapter {
	
	private int size = 2;
	
	public AccountsTabPagerAdapter(FragmentManager fragmentManager, int size) {
		super(fragmentManager);
		this.size = size;
	}

	public Fragment getItem(int pos) {
		Fragment f = new Fragment();
		switch (pos) {
			case 0	: f = new ZoneFragment(); break;
			case 1	: f = new AccountFragment(); break;
		}
		return f;
	}
	
	public int getCount() {
		return size;
	}
	
	void println(String msg) {
		Log.i("AccountsActivity", msg);
	}
}