package com.rameses.clfc.android.main;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.PaymentDB;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.VoidRequestDB;
import com.rameses.clfc.android.db.DBCollectionSheet;
import com.rameses.clfc.android.db.DBPaymentService;
import com.rameses.clfc.android.db.DBVoidService;
import com.rameses.db.android.DBContext;
import com.rameses.util.MapProxy;

public class CollectionSheetAdapter extends BaseAdapter 
{
	private Activity activity;
	private List<Map> list;
	private boolean showAll = true;
	private DBPaymentService paymentSvc = new DBPaymentService();
	private DBVoidService voidSvc = new DBVoidService();
	private DBCollectionSheet collectionSheet = new DBCollectionSheet();
	
	public CollectionSheetAdapter(Activity activity, List<Map> list) {
		this.activity = activity;
		this.list = list;
	}
	
	public CollectionSheetAdapter(Activity activity, List<Map> list, boolean showAll) {
		this.activity = activity;
		this.list = list;
		this.showAll = showAll;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		MapProxy item = new MapProxy(list.get(position));	
//		int total = 0;
//		synchronized (MainDB.LOCK) {
//			DBContext clfcdb = new DBContext("clfc.db");
//			collectionSheet.setDBContext(clfcdb);
//			try {
//				total = collectionSheet.getCountByRoutecode(item.getString("routecode"));
//			} catch (Throwable t) {
//				t.printStackTrace();
//			}
//		}

		View v = view;
//		item.put("hasnext", false);
//		System.out.println("total-> "+total+" list size-> "+list.size()+" position-> "+position);
//		if (list.size() < total && position == list.size()-1) {
//			v = inflater.inflate(R.layout.item_string, null);
//			item.put("hasnext", true);
//			((TextView) v.findViewById(R.id.tv_item_str)).setText("View More..");
//			return v;
//		}
		
		if(v == null) {
			v = inflater.inflate(R.layout.item_collectionsheet, null);
		}
		//CheckedTextView ctv_name = (CheckedTextView) v.findViewById(R.id.ctv_info_name);
		TextView tv_info_name = (TextView) v.findViewById(R.id.tv_item_collectionsheet);
		ImageView iv_info_paid = (ImageView) v.findViewById(R.id.iv_item_collectionsheet);
			
		tv_info_name.setText(item.get("borrower_name").toString());
//		
//		String loanappid = item.get("loanapp_objid").toString();
//		
//		int noOfPayments = 0;
//		synchronized (PaymentDB.LOCK) {
//			DBContext paymentdb = new DBContext("clfcpayment.db");
//			paymentSvc.setDBContext(paymentdb);
//			try {
//				noOfPayments = paymentSvc.noOfPaymentsByLoanappid(loanappid);
//			} catch (Exception e) {
//				e.printStackTrace();
//				noOfPayments = 0;
//			}
//		}
//
//		int noOfVoids = 0;
//		synchronized (VoidRequestDB.LOCK) {
//			DBContext requestdb = new DBContext("clfcrequest.db");
//			voidSvc.setDBContext(requestdb);
//			try { 
//				noOfVoids = voidSvc.noOfVoidPaymentsByLoanappid(loanappid);
//			} catch (Exception e) {
//				e.printStackTrace();
//				noOfVoids = 0;
//			}
//		}
//
		iv_info_paid.setVisibility(View.GONE);
////		if (item.get("acctname").toString().equals("ARNADO, MARICRIS")) {
////			System.out.println("acctname-> "+item.get("acctname").toString());
////			System.out.println("no of voids -> "+noOfVoids+" no of payments -> "+noOfPayments);
////		}
//		if (noOfPayments > 0 && noOfPayments > noOfVoids) {
//			if (MapProxy.getInteger(item, "isfirstbill") == 1) {
//				item.put("isfirstbill", 0);
//			}
//			iv_info_paid.setVisibility(View.VISIBLE);
//		}
		return v;
	}

}
