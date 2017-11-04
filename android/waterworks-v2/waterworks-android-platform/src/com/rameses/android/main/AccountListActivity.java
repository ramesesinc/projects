package com.rameses.android.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.android.R;
import com.rameses.android.UserInfoMenuActivity;
import com.rameses.android.database.AccountDB;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.Task;
import com.rameses.client.android.UIDialog;
import com.rameses.client.interfaces.UserProfile;
import com.rameses.db.android.DBContext;

public class AccountListActivity extends UserInfoMenuActivity {
	
	private ListView lv_list;
	private String stuboutid;
	private String stuboutcode;
	private EditText et_search;
	private TextView tv_paging;
	private Handler handler = new Handler();
	private Context ctx;
	private List<Map> list = new ArrayList<Map>();

	private ImageButton ibtn_first, ibtn_prev, ibtn_next, ibtn_last;
	private int START = 0, LIMIT = 5, CURRENT_PAGE = 1;
	private Boolean allowNext = true;
	private Boolean allowPrevious = true;
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		setContentView(R.layout.template_footer);
		setTitle("Stubout");
		
		ctx = this;
		
		Bundle extras = getIntent().getExtras();
		stuboutid = extras.get("stuboutid").toString();
		stuboutcode = extras.get("stuboutcode").toString(); 
		setTitle(stuboutcode);
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_account, rl_container, true);
		
		tv_paging = (TextView) findViewById(R.id.tv_paging);
		
		et_search = (EditText) findViewById(R.id.et_search);
		et_search.addTextChangedListener(new TextWatcher(){

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				START = 0;
				CURRENT_PAGE = 1;
				reload();
			}

			public void afterTextChanged(Editable e) {}
		});
		
		lv_list = (ListView) findViewById(R.id.lv_list);
		lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String acctid = view.getTag(R.string.id_tag).toString();
				int index = Integer.parseInt(view.getTag(R.string.index_tag).toString());
				
				Intent intent = new Intent(ctx, AccountInfoActivity.class);
				intent.putExtra("acctid", acctid);
				intent.putExtra("stuboutcode", stuboutcode);
				intent.putExtra("index", index);
				
//				String[] strlist = new String[list.size()];
//				
//				Map data = new HashMap();
////				List<String> strlist = new ArrayList<String>();
//				for (int i = 0; i < list.size(); i++) {
//					data = (Map) list.get(i);
//					strlist[i] = data.get("objid").toString();
//				}
//				
//				intent.putExtra("list", strlist);
				
				startActivity(intent);
			}
		});
		
		ibtn_first = (ImageButton) findViewById(R.id.ibtn_first);
		ibtn_first.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				println("first");
				START = 0;
				CURRENT_PAGE = 1;
				reload();
			}
		});
		
		ibtn_prev = (ImageButton) findViewById(R.id.ibtn_prev);
		ibtn_prev.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				println("previous");
				if (allowPrevious == true) {
					START -= LIMIT;
					CURRENT_PAGE--;
					reload();
				}
			}
		});
		
		ibtn_next = (ImageButton) findViewById(R.id.ibtn_next);
		ibtn_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				println("next");
				if (allowNext == true) {
					START += LIMIT;
					CURRENT_PAGE++;
					reload();
				}
			}
		});
		
		ibtn_last = (ImageButton) findViewById(R.id.ibtn_last);
		ibtn_last.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				println("last");
				int idx = getLastPageIndex();
				START = (idx * LIMIT);
				CURRENT_PAGE = getLastPage();
				reload();
			}
		});
	}

	int getLastPageIndex() {
		int idx = 0;
		
		DBContext ctx = new DBContext("main.db");
		AccountDB accountdb = new AccountDB();
		accountdb.setDBContext(ctx);
		
		try {
			UserProfile prof = SessionContext.getProfile();
			String userid = (prof != null? prof.getUserId() : "");
			
			Map params = new HashMap();
			params.put("stuboutid", stuboutid);
			int noofrecords = accountdb.countAccountByStuboutid(params);
						
			idx = ((noofrecords == 0? noofrecords : noofrecords - 1) / LIMIT);
//			println("no of records " + (noofrecords/LIMIT));
			
		} catch (Throwable t) {
			UIDialog.showMessage(t, AccountListActivity.this);
		}
		
		return idx;
	}
	
	int getLastPage() {
		int page = 1;

		DBContext ctx = new DBContext("main.db");
		AccountDB accountdb = new AccountDB();
		accountdb.setDBContext(ctx);
		
		try {
			UserProfile prof = SessionContext.getProfile();
			String userid = (prof != null? prof.getUserId() : "");
			
			Map params = new HashMap();
			params.put("stuboutid", stuboutid);
			int noofrecords = accountdb.countAccountByStuboutid(params);
						
			page = (noofrecords / LIMIT);
			if ((noofrecords % LIMIT) != 0) {
				page++;
			}
//			println("no of records " + (noofrecords/LIMIT));
			
		} catch (Throwable t) {
			UIDialog.showMessage(t, AccountListActivity.this);
		}
		
		if (page < 1) page = 1;
		
		return page;
	}
	
	protected void onStartProcess() {
		super.onStartProcess();
		reload();
	}
	
	void reload() {
		if (et_search == null) return;
		loadList(et_search.getText().toString());
	}
	
	void loadList(String searchtext) {
		handler.postAtTime(new LoadAccountListTask(searchtext, handler), 300);
	}
	
	void loadListToView(List<Map> resultList) {
		this.list = resultList;

		if (resultList.size() > LIMIT) {
			allowNext = true;
		} else {
			allowNext = false;
		}
		
		if (START > 0) {
			allowPrevious = true;
		} else {
			allowPrevious = false;
		}

		if (resultList.size() > LIMIT) {
			resultList.remove(resultList.size() - 1);
		}

		tv_paging.setText("Page " + CURRENT_PAGE + " of " + getLastPage());
		lv_list.setAdapter(new AccountListAdapter(this, resultList));
	}

	void println(String msg) {
		Log.i("AccoutListActivity", msg);
	}
	
	private class LoadAccountListTask extends Task {
		
		private String searchtext;
		private Handler handler;
		private DBContext ctx = new DBContext("main.db");
		private AccountDB accountdb = new AccountDB();
		
		public LoadAccountListTask(String searchtext, Handler handler) {
			this.searchtext = searchtext;
			this.handler = handler;
			accountdb.setDBContext(ctx);
		}
		 
		public void run() {
			String st = et_search.getText().toString();
			if (!searchtext.equals(st)) return;
			
			try {
				runImpl();
			} catch (Throwable t) {
				UIDialog.showMessage(t, AccountListActivity.this);
			}
		}
		
		private void runImpl() throws Exception {
			
			Map params = new HashMap();
			params.put("searchtext", "%" + searchtext + "%");
			params.put("stuboutid", stuboutid);
			params.put("start", START);
			params.put("limit", LIMIT + 1);
			
			final List<Map> resultList = accountdb.getAccountByStuboutid(params);
			
			handler.post(new Runnable() {
				public void run() {
					loadListToView(resultList);
				}
			});
		}
	}
	
}
