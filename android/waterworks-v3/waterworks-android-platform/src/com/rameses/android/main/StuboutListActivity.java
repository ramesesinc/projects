package com.rameses.android.main;

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
import com.rameses.android.database.StuboutDB;
import com.rameses.client.android.Task;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;

public class StuboutListActivity extends UserInfoMenuActivity {

	private ListView lv_list;
	private Context ctx;
	private String zoneid;
	private EditText et_search;
	private TextView tv_paging;
	private Handler handler = new Handler();

	private ImageButton ibtn_first, ibtn_prev, ibtn_next, ibtn_last;
	private int START = 0, LIMIT = 5, CURRENT_PAGE = 1;
	private Boolean allowNext = true, allowPrevious = true;
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		setContentView(R.layout.template_footer);
		setTitle("Sector");
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_account_stubout, rl_container, true);
		
		Bundle extras = getIntent().getExtras();
		zoneid = extras.get("zoneid").toString();
		setTitle(extras.get("zonedescription").toString());
				
		ctx = this;
		
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
				String stuboutid = view.getTag(R.string.id_tag).toString();
				String stuboutcode = view.getTag(R.string.code_tag).toString();
				
				Intent intent = new Intent(ctx, AccountListActivity.class);
				intent.putExtra("stuboutid", stuboutid);
				intent.putExtra("stuboutcode", stuboutcode);
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
		StuboutDB stuboutdb = new StuboutDB();
		stuboutdb.setDBContext(ctx);
		
		try {
			Map params = new HashMap();
			params.put("zoneid", zoneid);
			int noofrecords = stuboutdb.countStuboutByZoneid(params);

			idx = ((noofrecords == 0? noofrecords : noofrecords - 1) / LIMIT);
//			println("no of records " + (noofrecords/LIMIT));
			
		} catch (Throwable t) {
			UIDialog.showMessage(t, StuboutListActivity.this);
		}
		
		return idx;
	}
	
	int getLastPage() {
		int page = 1;
		
		DBContext ctx = new DBContext("main.db");
		StuboutDB stuboutdb = new StuboutDB();
		stuboutdb.setDBContext(ctx);
		
		try {
			Map params = new HashMap();
			params.put("zoneid", zoneid);
			int noofrecords = stuboutdb.countStuboutByZoneid(params);
			
			page = (noofrecords / LIMIT);
			if ((noofrecords % LIMIT) != 0) {
				page++;
			}
			
		} catch (Throwable t) {
			UIDialog.showMessage(t, StuboutListActivity.this);
		}
		
		if (page < 1) page = 1;
		
		return page;
	}
	
	protected void onStartProcess() {
		super.onStartProcess();
		reload();
//		list.clear();
//		list.add(createMenuItem("SO-C1-001", "BRGY. V.", R.drawable.change_password));
//		list.add(createMenuItem("SO-C1-002", "BRGY. V.", R.drawable.change_password));
//		list.add(createMenuItem("SO-C1-003", "BRGY. V.", R.drawable.change_password));
//		
//		lv_list.setAdapter(new StuboutListAdapter(this, list));
	}
	
	void reload() {
//		println("reload");
//		println("et search " + et_search);
		if (et_search == null) return;
		loadList(et_search.getText().toString());
	}
	
	void loadList(String searchtext) {
		handler.postAtTime(new LoadStuboutListTask(searchtext, handler), 300);
	}
	
	void loadListToView(List<Map> resultList) {
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

		if (resultList.size() > LIMIT) {
			resultList.remove(resultList.size() - 1);
		}

		tv_paging.setText("Page " + CURRENT_PAGE + " of " + getLastPage());
		lv_list.setAdapter(new StuboutListAdapter(this, resultList));
	}
	
	void println(String msg) {
		Log.i("StuboutListActivity", msg);
	}
	
	private class LoadStuboutListTask extends Task {
		
		private String searchtext;
		private Handler handler;
		private DBContext ctx = new DBContext("main.db");
		private StuboutDB stuboutdb = new StuboutDB();
		
		
		public LoadStuboutListTask(String searchtext, Handler handler) {
			this.searchtext = searchtext;
			this.handler = handler;
			stuboutdb.setDBContext(ctx);
		}
		
		public void run() {
			String st = et_search.getText().toString().trim();
			if (!searchtext.equals(st)) return;
			
			try {
				runImpl();
			} catch (Throwable t) {
				t.printStackTrace();
				UIDialog.showMessage(t, StuboutListActivity.this);
			}
		}
		
		private void runImpl() throws Exception {
			Map params = new HashMap();
			params.put("searchtext", "%" + searchtext + "%");
			params.put("zoneid", zoneid);
			params.put("start", START);
			params.put("limit", LIMIT + 1);
			
			final List<Map> resultList = stuboutdb.getStuboutByZoneid(params);

			handler.post(new Runnable() {
				public void run() {
					loadListToView(resultList); 
				}
			});
		}
	}
}
