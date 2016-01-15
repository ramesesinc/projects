package com.rameses.clfc.android.main;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.clfc.android.R;
import com.rameses.util.MapProxy;

public class PaymentsAdapter extends BaseAdapter {

	private Activity activity;
	private List list;
	public static int INDEX = "INDEX".hashCode();
	
	public PaymentsAdapter(Activity activity, List list) {
		this.activity = activity;
		this.list = list;
	}
	
	public int getCount() {
		return list.size();
	}

	public Object getItem(int index) {
		return list.get(index);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int index, View view, ViewGroup container) {
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		Map payment = (Map) list.get(index);

		String paymentid = MapProxy.getString(payment, "objid");
		
		View v = view;
		if (v == null) {
			v = inflater.inflate(R.layout.item_payment, null);
			v.setTag(INDEX, index);
			v.setTag(R.id.paymentid, paymentid);
			
			payment.put("view", v);
			payment.put("index", index);
		}
		
		//System.out.println("payment " + payment);
		
		((TextView) v.findViewById(R.id.tv_txndate)).setText(payment.get("refno").toString());
		((TextView) v.findViewById(R.id.tv_collector)).setText(payment.get("txndate").toString());
		((TextView) v.findViewById(R.id.tv_info_paidby)).setText(payment.get("paidby").toString());
		
//		String ptype = payment.get("paytype").toString();
		String ptype = MapProxy.getString(payment, "paytype");
//		System.out.println("payment type " + ptype);
//		String type = "";
//		if (payment.get("paytype").toString().equals("schedule")) {
//			type = "Schedule/Advance";
//		} else if (payment.get("paytype").toString().equals("over")) {
//			type = "Overpayment";
//		}
//		((TextView) v.findViewById(R.id.tv_info_paymenttype)).setText(payment.get("posttype").toString());
		((TextView) v.findViewById(R.id.tv_info_paymenttype)).setText(MapProxy.getString(payment, "posttype"));
		
		String option = payment.get("payoption").toString();
		RelativeLayout rl_check = (RelativeLayout) v.findViewById(R.id.rl_check);
		rl_check.setVisibility(View.GONE);
		if ("check".equals(option)) {
//			rl_check.setVisibility(View.VISIBLE);
//			((TextView) v.findViewById(R.id.tv_info_bank)).setText(payment.get("bank_name").toString());
//			((TextView) v.findViewById(R.id.tv_info_checkno)).setText(payment.get("check_no").toString());
//			((TextView) v.findViewById(R.id.tv_info_checkdate)).setText(payment.get("check_date").toString());

			((TextView) v.findViewById(R.id.tv_info_bank)).setText(MapProxy.getString(payment, "bank_name"));
			((TextView) v.findViewById(R.id.tv_info_checkno)).setText(MapProxy.getString(payment, "check_no"));
			((TextView) v.findViewById(R.id.tv_info_checkdate)).setText(MapProxy.getString(payment, "check_date"));
		}

//		BigDecimal amount = new BigDecimal(payment.get("amount").toString()).setScale(2);
		BigDecimal amount = new BigDecimal(MapProxy.getString(payment, "amount")).setScale(2);
		((TextView) v.findViewById(R.id.tv_info_paymentamount)).setText(formatValue(amount));
		
		
		boolean hasrequest = MapProxy.getBoolean(payment, "hasrequest");
//		voidRequest = voidSvc.findVoidRequestByPaymentid(payment.get("objid").toString());
//		if (hasrequest == false) {
//			addPaymentProperties(child);
//		} else 
		int idx = Integer.parseInt(v.getTag(INDEX).toString());
		if (hasrequest == true && idx == index) {
			v.setClickable(false);
			String voidType = MapProxy.getString(payment, "requeststate");;
//			System.out.println("void type " + voidType);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
			View overlay = inflater.inflate(R.layout.overlay_void_text, null);
			if ("PENDING".equals(voidType)) {
				((TextView) overlay).setTextColor(activity.getResources().getColor(R.color.red));
//				((TextView) overlay).setText(payment.get("refno").toString());
				((TextView) overlay).setText("VOID REQUEST PENDING");
				overlay.setLayoutParams(layoutParams);
				((RelativeLayout) v).addView(overlay); 
			} else if ("APPROVED".equals(voidType)) {
				((TextView) overlay).setTextColor(activity.getResources().getColor(R.color.green));
				((TextView) overlay).setText("VOID APPROVED");
//				((TextView) overlay).setText(payment.get("refno").toString());
				overlay.setLayoutParams(layoutParams);
				((RelativeLayout) v).addView(overlay);
				//addApprovedVoidPaymentProperies(child);
			} else {
				((TextView) overlay).setText(payment.get("refno").toString());
				overlay.setLayoutParams(layoutParams);
				((RelativeLayout) v).addView(overlay);
			}
		}
		
		return v;
	}
	
	private void println(String msg) {
		Log.i("[PaymentsAdapter]", msg);
	}
	

	private String formatValue(Object number) {
		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		StringBuffer sb = new StringBuffer();
		FieldPosition fp = new FieldPosition(0);
		df.format(number, sb, fp);
		return sb.toString();
	}

}
