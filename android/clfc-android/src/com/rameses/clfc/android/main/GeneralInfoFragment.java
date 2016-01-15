package com.rameses.clfc.android.main;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.db.DBCSAmnesty;
import com.rameses.clfc.android.db.DBCollectionSheet;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;
import com.rameses.util.MapProxy;

public class GeneralInfoFragment extends Fragment {
	
	private int VISIBLE = "VISIBLE".hashCode();
	private Handler handler = new Handler();
	private MapProxy collectionsheet = null;
	private MapProxy amnesty = null;
	
	private TextView tv_acctname, tv_appno, tv_loanamount, tv_balance, tv_dailydue;
	private TextView tv_amountdue, tv_overpayment, tv_duedate, tv_homeaddress, tv_collectionaddress;
	private TextView tv_interest, tv_penalty, tv_others, tv_term, tv_collapsible, tv_releasedate;
	private TextView tv_amnesty_refno, tv_amnesty_dtstarted, tv_amnesty_offer;
	private LinearLayout ll_otherinfo, ll_amnesty;
	private RelativeLayout rl_otherinfo;
	private String acctname = "", appno = "", homeaddress = "", collectionaddress = "", duedate = "", releasedate = "";
	private String amnesty_refno = "", amnesty_dtstarted = "", amnesty_dtended = "", amnesty_offer = "";
	private BigDecimal amountdue = new BigDecimal("0"), loanamount = new BigDecimal("0"), balance = new BigDecimal("0");
	private BigDecimal dailydue = new BigDecimal("0"), overpayment = new BigDecimal("0"), interest = new BigDecimal("0");
	private BigDecimal penalty = new BigDecimal("0"), others = new BigDecimal("0");
	private int term = 0;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_generalinfo, container, false);
		
		tv_acctname = (TextView) view.findViewById(R.id.tv_info_acctname);
		tv_appno = (TextView) view.findViewById(R.id.tv_info_appno);
		tv_loanamount = (TextView) view.findViewById(R.id.tv_info_loanamount);
		tv_balance = (TextView) view.findViewById(R.id.tv_info_balance);
		tv_dailydue = (TextView) view.findViewById(R.id.tv_info_dailydue);
		tv_amountdue = (TextView) view.findViewById(R.id.tv_info_amountdue);
		tv_overpayment = (TextView) view.findViewById(R.id.tv_info_overpayment);
		tv_duedate = (TextView) view.findViewById(R.id.tv_info_duedate);
		tv_releasedate = (TextView) view.findViewById(R.id.tv_info_releasedate);
		tv_homeaddress = (TextView) view.findViewById(R.id.tv_info_homeaddress);
		tv_collectionaddress = (TextView) view.findViewById(R.id.tv_info_collectionaddress);
		tv_interest = (TextView) view.findViewById(R.id.tv_info_interest);
		tv_penalty = (TextView) view.findViewById(R.id.tv_info_penalty);
		tv_others = (TextView) view.findViewById(R.id.tv_info_others);
		tv_term = (TextView) view.findViewById(R.id.tv_info_term);
		
		tv_collapsible = (TextView) view.findViewById(R.id.tv_collapsible);
		
		ll_otherinfo = (LinearLayout) view.findViewById(R.id.ll_otherinfo);
		ll_otherinfo.setVisibility(View.GONE);
		
		rl_otherinfo = (RelativeLayout) view.findViewById(R.id.rl_otherinfo);
		rl_otherinfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isShown = ll_otherinfo.isShown();
				if (isShown == false) {
					ll_otherinfo.setVisibility(View.VISIBLE);
					tv_collapsible.setText(R.string.collapse_true);
				} else if (isShown == true) {
					ll_otherinfo.setVisibility(View.GONE);
					tv_collapsible.setText(R.string.collapse_false);
				}
			}
		});
		
		ll_amnesty = (LinearLayout) view.findViewById(R.id.ll_amnesty);
		ll_amnesty.setVisibility(View.GONE);
		
		tv_amnesty_refno = (TextView) view.findViewById(R.id.tv_amnesty_refno);
		tv_amnesty_dtstarted = (TextView) view.findViewById(R.id.tv_amnesty_dtstarted);
//		tv_amnesty_dtended = (TextView) view.findViewById(R.id.tv_amnesty_dtended);
		tv_amnesty_offer = (TextView) view.findViewById(R.id.tv_amnesty_offer);
		
		/*tv_otherinfo  = (TextView) view.findViewById(R.id.tv_otherinfo);
		tv_otherinfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				boolean isShown = ll_otherinfo.isShown();
				if (isShown == false) {
					ll_otherinfo.setVisibility(View.VISIBLE);
				} else if (isShown == true) {
					ll_otherinfo.setVisibility(View.GONE);
				}
			}
		});*/
		
		return view;		
	}
	
	public void onStart() {
		super.onStart();
		Bundle args = getArguments();
		final String objid = args.getString("objid");

		handler.post(new Runnable() {
			public void run() {

				SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
				SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
				
				DBContext ctx = new DBContext("clfc.db");
				DBCollectionSheet collectionsheetdb = new DBCollectionSheet();
				collectionsheetdb.setDBContext(ctx);
				collectionsheetdb.setCloseable(false);
				
				DBCSAmnesty amnestydb = new DBCSAmnesty();
				amnestydb.setDBContext(ctx);
				amnestydb.setCloseable(false);
				synchronized (MainDB.LOCK) {
					try {
						collectionsheet = new MapProxy(collectionsheetdb.findCollectionSheet(objid));
						String id = "";
						if (collectionsheet != null && !collectionsheet.isEmpty()) id = collectionsheet.getString("objid");
						amnesty = new MapProxy(amnestydb.findByParentid(id));
//						runImpl(ctx);
					} catch (Throwable t) {
						t.printStackTrace();
						UIDialog.showMessage(t, ((CollectionSheetInfoMainActivity) getActivity())); 
					} finally {
						ctx.close();
					}
				}
				
				if (collectionsheet != null && !collectionsheet.isEmpty()) {
					acctname = collectionsheet.getString("borrower_name");
					appno = collectionsheet.getString("loanapp_appno");
					amountdue = new BigDecimal(collectionsheet.getDouble("amountdue")+"");
					loanamount = new BigDecimal(collectionsheet.getDouble("loanapp_loanamount")+"");
					balance = new BigDecimal(collectionsheet.getDouble("balance")+"");
					dailydue = new BigDecimal(collectionsheet.getDouble("dailydue")+"");
					overpayment = new BigDecimal(collectionsheet.getDouble("overpaymentamount")+"");
					interest = new BigDecimal(collectionsheet.getDouble("interest")+"");
					penalty = new BigDecimal(collectionsheet.getDouble("penalty")+"");
					others = new BigDecimal(collectionsheet.getDouble("others")+"");
					term = collectionsheet.getInteger("term");
					homeaddress = collectionsheet.getString("homeaddress");
					collectionaddress = collectionsheet.getString("collectionaddress");
					try {
						duedate = df.format(df2.parse(collectionsheet.getString("maturitydate")));
						releasedate = df.format(df2.parse(collectionsheet.getString("releasedate")));
					} catch (Exception e) { e.printStackTrace();}
					
				}
						
				tv_acctname.setText(acctname);
				tv_appno.setText(appno);
				tv_loanamount.setText(formatValue(loanamount));
				tv_balance.setText(formatValue(balance));
				tv_dailydue.setText(formatValue(dailydue));
				tv_amountdue.setText(formatValue(amountdue));
				tv_overpayment.setText(formatValue(overpayment));
				tv_duedate.setText(duedate);
				tv_releasedate.setText(releasedate);
				tv_homeaddress.setText(homeaddress);
				tv_collectionaddress.setText(collectionaddress);
				tv_interest.setText(formatValue(interest));
				tv_penalty.setText(formatValue(penalty));
				tv_others.setText(formatValue(others));
				tv_term.setText(term+" days");
				
				if (amnesty != null && !amnesty.isEmpty()) {
					ll_amnesty.setVisibility(View.VISIBLE);
					amnesty_refno = amnesty.getString("refno");
					try {
						amnesty_dtstarted = df.format(df2.parse(amnesty.getString("dtstarted")));
					} catch (Exception e) { e.printStackTrace();}
					String option = amnesty.getString("amnestyoption");
					if ("WAIVER".equals(option)) {
						int iswaivepenalty = amnesty.getInteger("iswaivepenalty");
						int iswaiveinterest = amnesty.getInteger("iswaiveinterest");
						if (iswaivepenalty == 1 || iswaiveinterest == 1) {
							amnesty_offer = "WAIVED ";
							if (iswaivepenalty == 1) amnesty_offer += "PENALTY ";
							if (amnesty_offer.contains("PENALTY")) amnesty_offer += "AND ";
							if (iswaiveinterest == 1) amnesty_offer += "INTEREST";
						}
					} else if ("FIX".equals(option)) {
						amnesty_offer = formatValue(amnesty.getDouble("grantedoffer_amount"));
						int isspotcash = amnesty.getInteger("grantedoffer_isspotcash");
						if (isspotcash == 0) {
							int days = amnesty.getInteger("grantedoffer_days");
							int months = amnesty.getInteger("grantedoffer_months");
							if (days > 0) amnesty_offer += "\n" + days + " Day(s)";
							if (months > 0) amnesty_offer += "\n" + months + " Month(s)";
						} else if (isspotcash == 1) {
							try {
								amnesty_offer += "\nspot cash " + df.format(df2.parse(amnesty.getString("grantedoffer_date")));								
							} catch (Exception e) { e.printStackTrace();}
						}
					}
				}
				
				tv_amnesty_refno.setText(amnesty_refno);
				tv_amnesty_dtstarted.setText(amnesty_dtstarted);
				tv_amnesty_offer.setText(amnesty_offer);
			}
		});	
		
	}
	

	private String formatValue(Object number) {
		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		StringBuffer sb = new StringBuffer();
		FieldPosition fp = new FieldPosition(0);
		df.format(number, sb, fp);
		return sb.toString();
	}
 	
}
