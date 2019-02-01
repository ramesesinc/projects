package com.rameses.android.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.rameses.android.R;
import com.rameses.android.database.AccountDB;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIDialog;
import com.rameses.client.interfaces.UserProfile;
import com.rameses.db.android.DBContext;

public class ReadingSheetSearchAdapter extends ArrayAdapter<Map> {

	private ReadingSheetSearchActivity activity;
	private AccountDB accountdb = new AccountDB();
//	private Context context;
	
    private Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

//        	println("perform filtering constraint " + constraint);
            if (constraint != null) {
            	DBContext ctx = new DBContext("main.db");
            	accountdb.setDBContext(ctx);
            	
            	List<Map> list = new ArrayList<Map>();
            	
            	try {
            		Map params = new HashMap();
            		params.put("searchtext", "%" + constraint + "%");
            		
            		UserProfile prof = SessionContext.getProfile();
            		String userid = (prof != null? prof.getUserId() : "");
            		
            		params.put("assigneeid", userid);
//            		println("params " + params);
            		
            		list = accountdb.getAccountByAssigneeid(params);
            	} catch (Exception e) {
            		UIDialog.showMessage(e, activity);
            	}
            	
//            	ArrayList<Map> list = activity.getList(String.valueOf(constraint));
//            	println("list " + list);
            	results.values = list;
            	results.count = list.size();
            	
           	
            }
        	
        			
        	
//            if (constraint != null) {
//                ArrayList<Customer> suggestions = new ArrayList<Customer>();
//                for (Customer customer : mCustomers) {
//                    // Note: change the "contains" to "startsWith" if you only want starting matches
//                    if (customer.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
//                        suggestions.add(customer);
//                    }
//                }
//
//                results.values = suggestions;
//                results.count = suggestions.size();
//            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        	if (results != null && results.count > 0) {
            	List<Map> list = (List<Map>) results.values;
                clear();
                addAll(list);
                notifyDataSetChanged();
        	}
//            addAll(results.values);
//            if (results != null && results.count > 0) {
//                // we have filtered results
//                addAll((ArrayList<Customer>) results.values);
//            } else {
//                // no filter, add entire original list back in
//                addAll(mCustomers);
//            }
        }
    };
    
	public ReadingSheetSearchAdapter(Context context, int resource) {
		super(context, resource);
		this.activity = (ReadingSheetSearchActivity) context;
		
	}

	@Override
	public View getView(int idx, View view, ViewGroup vg) {
		View v = view;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_account_search, null);
		}
		
		Map item = getItem(idx);
		
		if (item.containsKey("objid")) {
			v.setTag(R.string.id_tag, item.get("objid").toString());
		}
		
		TextView name = (TextView) v.findViewById(R.id.tv_name);
		String str = "";
		if (item.containsKey("acctno")) {
			str = item.get("acctno").toString();
		}
		
		if (item.containsKey("acctname")) {
			if (!str.equals("")) {
				str += " - ";
			}
			str += item.get("acctname").toString();
		}

		name.setText(str);
		
		return v;
	}

	@Override
	public Filter getFilter() {
//		println("return filter");
        return mFilter;
	}
	
	void println(String msg) {
		Log.i("ReadingSheetSearchAdapter", msg);
	}
}
