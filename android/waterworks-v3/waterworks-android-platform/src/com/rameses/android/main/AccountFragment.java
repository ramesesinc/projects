package com.rameses.android.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.rameses.android.ListFragment;
import com.rameses.android.R;
import com.rameses.android.database.AccountDB;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.Task;
import com.rameses.client.android.UIDialog;
import com.rameses.client.interfaces.UserProfile;
import com.rameses.db.android.DBContext;

public class AccountFragment extends ListFragment {

	private ListView lv_list;
	private EditText et_search;
	private TextView tv_paging;
	private ImageButton ibtn_first, ibtn_prev, ibtn_next, ibtn_last;
	private Handler handler = new Handler();
	private int START = 0, LIMIT = 5, CURRENT_PAGE = 1;
	private Boolean allowNext = true, allowPrevious = true;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_account, container, false);
		
		tv_paging = (TextView) view.findViewById(R.id.tv_paging);
		
		et_search = (EditText) view.findViewById(R.id.et_search);
		et_search.addTextChangedListener(new TextWatcher(){

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				START = 0;
				CURRENT_PAGE = 1;
				reload();
			}

			public void afterTextChanged(Editable e) {}
		});
 
		lv_list = (ListView) view.findViewById(R.id.lv_list);
		lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String acctid = view.getTag(R.string.id_tag).toString();
				
				Intent intent = new Intent(getActivity(), AccountInfoActivity.class);
//				intent.putExtra("acctid", acctid);
//				Intent intent = new Intent(ctx, AccountInfoActivity.class);
				intent.putExtra("acctid", acctid);
				intent.putExtra("index", 0);
				
				String[] strlist = new String[1];
				strlist[0] = acctid;
				intent.putExtra("list", strlist);
				
				startActivity(intent);
			}
		});
		
		ibtn_first = (ImageButton) view.findViewById(R.id.ibtn_first);
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
		
		ibtn_prev = (ImageButton) view.findViewById(R.id.ibtn_prev);
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
		
		ibtn_next = (ImageButton) view.findViewById(R.id.ibtn_next);
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
		
		ibtn_last = (ImageButton) view.findViewById(R.id.ibtn_last);
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
		
		return view;
	}
	
	int getLastPageIndex(){
		int idx = 0;
		
		DBContext ctx = new DBContext("main.db");
		AccountDB accountdb = new AccountDB();
		accountdb.setDBContext(ctx);
		
		try {
			UserProfile prof = SessionContext.getProfile();
			String userid = (prof != null? prof.getUserId() : "");
			
			Map params = new HashMap();
			params.put("assigneeid", userid);
			int noofrecords = accountdb.countAccountByAssigneeid(params);

			idx = ((noofrecords == 0? noofrecords : noofrecords - 1) / LIMIT);
//			println("no of records " + (noofrecords/LIMIT));
			
		} catch (Throwable t) {
			UIDialog.showMessage(t, getActivity());
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
			params.put("assigneeid", userid);
			int noofrecords = accountdb.countAccountByAssigneeid(params);
			
			page = (noofrecords / LIMIT);
			if ((noofrecords % LIMIT) != 0) {
				page++;
			}
			
		} catch (Throwable t) {
			UIDialog.showMessage(t, getActivity());
		}
		
		if (page < 1) page = 1;
		
		return page;
	}
	

	public void onStart() {
		super.onStart();
		reload();
	}

	public void reload() {
		if (et_search == null) return;
		loadList(et_search.getText().toString());
	}
	
	void loadList(String searchtext) {
		handler.postAtTime(new LoadAccountListTask(searchtext, handler), 300);
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
		
		tv_paging.setText("Page " + CURRENT_PAGE + " of " + getLastPage());
		
		lv_list.setAdapter(new AccountListAdapter(getActivity(), resultList));
	}
	
	void println(String msg) {
		Log.i("AccountFragment", msg);
	}
	
	private class LoadAccountListTask extends Task {
		
		private String searchtext;
		private DBContext ctx = new DBContext("main.db");
		private AccountDB accountdb = new AccountDB();
		private Handler handler;
		
		public LoadAccountListTask(String searchtext, Handler handler) {
			this.searchtext = searchtext;
			this.handler = handler;
			accountdb.setDBContext(ctx);
		}
		
		public void run() {
			String st = et_search.getText().toString().trim();
			if (!searchtext.equals(st)) return;

			try {
				runImpl();
			} catch (Throwable t) {
				t.printStackTrace();
				UIDialog.showMessage(t, getActivity()); 
			}
		}
		
		private void runImpl() throws Exception {
			UserProfile prof = SessionContext.getProfile();
			String userid = (prof != null? prof.getUserId() : "");
			
			Map params = new HashMap();
			params.put("searchtext", "%" + searchtext + "%");
			params.put("assigneeid", userid);
			params.put("start", START);
			params.put("limit", LIMIT + 1);
			
//			println("params " + params);
			
			final List<Map> resultList = accountdb.getAccountByAssigneeid(params);
//			println("loading list");
			handler.post(new Runnable() {
				public void run() {
					loadListToView(resultList); 
				}
			});
		}
	}
}
