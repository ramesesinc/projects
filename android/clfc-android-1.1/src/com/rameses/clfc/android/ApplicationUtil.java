package com.rameses.clfc.android;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.rameses.clfc.android.db.DBCapturePayment;
import com.rameses.clfc.android.db.DBCollectionGroup;
import com.rameses.clfc.android.db.DBCollectionSheet;
import com.rameses.clfc.android.db.DBLocationTracker;
import com.rameses.clfc.android.db.DBPaymentService;
import com.rameses.clfc.android.db.DBRemarksService;
import com.rameses.clfc.android.db.DBSystemService;
import com.rameses.clfc.android.db.DBVoidService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIApplication;
import com.rameses.client.interfaces.UserProfile;
import com.rameses.db.android.DBContext;


public final class ApplicationUtil 
{
	private static boolean isDeviceRegistered = false;
		
	public static boolean getIsDeviceRegistered() { 
		return isDeviceRegistered; 
	}
	
	public static void deviceResgistered() {
		isDeviceRegistered = true;
	}
	
	public static int getNetworkStatus() {
		return ((ApplicationImpl) Platform.getApplication()).getNetworkStatus();
	}
	
	public static boolean hasCapturedPayments() throws Exception {
		UserProfile prof = SessionContext.getProfile();
		String collectorid = (prof != null? prof.getUserId() : "");
		
		String date = "";
		Date dt = Platform.getApplication().getServerDate();
		if (dt != null) {
			date = formatDate(dt, "yyyy-MM-dd");
		}
		
		return hasCapturedPaymentsImpl(collectorid, date);
		
	}
	
	public static boolean hasCapturedPayments(String collectorid, String date) throws Exception {
		return hasCapturedPaymentsImpl(collectorid, date);
	}
	
	private static boolean hasCapturedPaymentsImpl(String collectorid, String date) throws Exception {
		DBContext ctx = new DBContext("clfccapture.db");
		DBCapturePayment dbcp = new DBCapturePayment();
		dbcp.setDBContext(ctx);
		dbcp.setCloseable(false);
		try {
			return dbcp.hasPaymentS(collectorid, date);
		} catch (Exception e) {
			throw e;
		} finally {
			ctx.close();
		}
	}
	
	public static String getPrefix() {
		Date dt = Platform.getApplication().getServerDate();
		String date = formatDate(dt, "yyyy-MM-dd");
		
		UserProfile prof = SessionContext.getProfile();
		String userid = (prof != null? prof.getUserId() : "");
		
		return date + userid;
	}
	
	public static boolean isCollectionCreated(String collectionid) throws Exception {
		boolean flag = false;
		
		DBContext ctx = new DBContext("clfc.db");
		try {
			flag = isCollectionCreatedImpl(ctx, collectionid);
		} catch (Exception e) {
			throw e;
		} finally {
			ctx.close();
		}
		
		return flag;
	}
	
	private static boolean isCollectionCreatedImpl(DBContext clfcdb, String collectionid) throws Exception {
		DBCollectionGroup dbcg = new DBCollectionGroup();
		dbcg.setDBContext(clfcdb);
		dbcg.setCloseable(false);
		
		return dbcg.isCollectionCreatedByCollectionid(collectionid);
	}
	
	public static String getBillingid() throws Exception {
		Date dt = Platform.getApplication().getServerDate();
		String date = "";
		if (dt != null) {
			date = formatDate(dt, "yyyy-MM-dd");
		}
		
		UserProfile prof = SessionContext.getProfile();
		String collectorid = (prof != null? prof.getUserId() : "");
		return getBillingid(collectorid, date);
	}
	
	public static String getBillingid(String collectorid, String date) throws Exception {
		String billingid = "";
		DBContext ctx = new DBContext("clfc.db");
		DBSystemService sysSvc = new DBSystemService();
		sysSvc.setDBContext(ctx);
		sysSvc.setCloseable(false);
		
		try {
			billingid = sysSvc.getBillingid(collectorid, date);
		} catch (Exception e) {
			throw e;
		} finally {
			ctx.close();
		}
		
		return billingid;
	}
	
	public static Map renewTracker() {
		String prefix = getPrefix();
		return renewTracker(prefix);
	}
	
	public static Map renewTracker(String prefix) {
//		AppSettingsImpl settings = (AppSettingsImpl) Platform.getApplication().getAppSettings();
		Date dt = Platform.getApplication().getServerDate();
		if (dt == null) return new HashMap();
		
		String date = formatDate(dt, "yyMMdd");
		String trackerid = "TRCK" + date + UUID.randomUUID().toString();

		String key = prefix + "trackerid";
		Map map = new HashMap();
		map.put(key, trackerid);
		
		return map;
//		settings.put(key, trackerid);
		
//		settings.put("tracker_owner", userid);
//		settings.put("tracker_date", ApplicationUtil.formatDate(dt, "yyyy-MM-dd"));
	}
	
	public static Map renewCapture() {
		String prefix = getPrefix();
		return renewCapture(prefix);
	}
	
	public static Map renewCapture(String prefix) {
//		AppSettingsImpl settings = (AppSettingsImpl) Platform.getApplication().getAppSettings();
		Date dt = Platform.getApplication().getServerDate();
		if (dt == null) return new HashMap();
		
		String date = formatDate(dt, "yyMMdd");
		String captureid = "CP" + date + UUID.randomUUID().toString();
		
		String key = prefix + "captureid";
		Map map = new HashMap();
		map.put(key, captureid);
		
		return map;
//		settings.put(key, captureid);
	}

	public static void showLongMsg(String msg) {
		showLongMsg(msg, Platform.getActionBarMainActivity());
	}
	
	public static void showLongMsg(String msg, Activity activity) {
		if (activity == null) return;
		
		Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
	}

	public static void showShortMsg(String msg) {
		showShortMsg(msg, Platform.getActionBarMainActivity());
	}
	
	public static void showShortMsg(String msg, Activity activity) {
		if (activity == null) return;
		
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}
	
	private static void println(String msg) {
		Log.i("[ApplicationUtil]", msg);
	}

	public static Map<String, Object> createMenuItem(String id, String text, String subtext, int iconid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("iconid", iconid);
		
		String t = "";
		if (text != null) t = text;
		map.put("text", t);
		
		String s = "";
		if (subtext != null) s = subtext;
		map.put("subtext", s);
		
		return map;
	}
	
	public static String getAppHost() {
		ApplicationImpl app = (ApplicationImpl) Platform.getApplication();
		return getAppHost(app.getNetworkStatus());
	}
	
	public static String getAppHost(int networkStatus) {		
		UIApplication app = Platform.getApplication();
		return ((AppSettingsImpl) app.getAppSettings()).getAppHost(networkStatus);
	}	
	
	public static String formatDate(Date date, String format) {
//		System.out.println("date -> "+date+" format -> "+format);
		if (date == null) return null;
		
		return new java.text.SimpleDateFormat(format).format(date);
	}
	
	public static boolean hasBilling() throws Exception {
		UserProfile prof = SessionContext.getProfile();
		String collectorid = (prof != null? prof.getUserId() : "");
		
		return hasBilling(collectorid);
	}
	
	public static boolean hasBilling(String collectorid) throws Exception {
		DBContext ctx = new DBContext("clfc.db");
		DBCollectionGroup dbcg = new DBCollectionGroup();
		dbcg.setDBContext(ctx);
		
		boolean flag = false;
		try {
			flag = dbcg.hasCollectionGroupByCollector(collectorid);
		} catch (Exception e) {
			throw e;
		} finally {
			ctx.close();
		}
		
		return flag;
	}
	
	public static boolean checkUnpostedCapture() throws Exception {
		UserProfile prof = SessionContext.getProfile();
		String collectorid = (prof != null? prof.getUserId() : "");
		
		return checkUnpostedCapture(collectorid);
	}
	
	public static boolean checkUnpostedCapture(String collectorid) throws Exception {
		DBContext ctx = new DBContext("clfccapture.db");
		DBCapturePayment dbcp = new DBCapturePayment();
		dbcp.setDBContext(ctx);
		dbcp.setCloseable(false);
		
		boolean flag = true;
		
		try {
			flag = dbcp.hasPendingPayments(collectorid);
		} catch (Exception e) {
			throw e;
		} finally {
			ctx.close();
		}
		
		return flag;
	}
	
	public static boolean checkUnposted() throws Exception {
		return checkUnposted(null);
	}
	
	public static boolean checkPendingVoidRequests(String itemid) throws Exception {
		boolean flag = false;
		
		DBContext clfcdb = new DBContext("clfc.db");
		
		try {
			flag = checkPendingVoidRequestsImpl(clfcdb, itemid);
			return flag;
		} catch (RuntimeException re) {
			throw re; 
		} catch (Exception e) {
			throw e; 
		} catch (Throwable t) {
			throw new Exception(t.getMessage(), t); 
		} finally {
			clfcdb.close();
		}
	}
	
	private static boolean checkPendingVoidRequestsImpl(DBContext clfcdb, String itemid) throws Exception {
		DBVoidService voidSvc = new DBVoidService();
		voidSvc.setDBContext(clfcdb);
		voidSvc.setCloseable(false);
		
		boolean flag = false;
		Map map = voidSvc.findVoidRequestByItemidAndState(itemid, "PENDING");
		if (map != null && !map.isEmpty()) {
			flag = true;
		}
		
		return flag;
	}
	
	public static boolean checkUnpostedTracker() throws Exception  {
		UserProfile prof = SessionContext.getProfile();
		String collectorid = (prof == null)? "" : prof.getUserId();
		return checkUnpostedTracker(collectorid);
	}
	
	public static boolean checkUnpostedTracker(String collectorid) throws Exception {
		boolean flag = false;

		DBContext ctx = new DBContext("clfctracker.db");
		DBLocationTracker trackerSvc = new DBLocationTracker();
		trackerSvc.setDBContext(ctx);
		trackerSvc.setCloseable(false);
		
		String date = Platform.getApplication().getServerDate().toString();
		
		try {
			flag = trackerSvc.hasLocationTrackerByCollectoridLessThanDate(collectorid, date);
			
			return flag;
		} catch (RuntimeException re) {
			throw re; 
		} catch (Exception e) {
			throw e; 
		} catch (Throwable t) {
			throw new Exception(t.getMessage(), t); 
		} finally {
			ctx.close();
		}
	}
	
	public static boolean checkUnposted(String itemid) throws Exception {
		boolean flag = false;
		
		DBContext paymentdb = new DBContext("clfcpayment.db");
		DBContext remarksdb = new DBContext("clfcremarks.db");
		DBContext clfcdb = new DBContext("clfc.db");
		try {
			flag = checkUnpostedImpl(paymentdb, remarksdb, clfcdb, itemid);
			return flag;
		} catch (RuntimeException re) {
			throw re; 
		} catch (Exception e) {
			throw e; 
		} catch (Throwable t) {
			throw new Exception(t.getMessage(), t); 
		} finally {
			paymentdb.close();
			remarksdb.close();
			clfcdb.close();
		}
	}
	
	private static boolean checkUnpostedImpl(DBContext paymentdb, DBContext remarksdb, DBContext clfcdb, String itemid) 
			throws Exception {
		boolean flag = false;

		DBPaymentService paymentSvc = new DBPaymentService();
		paymentSvc.setDBContext(paymentdb);
		paymentSvc.setCloseable(false);
		
		String collectorid = SessionContext.getProfile().getUserId();
		if (itemid.equals("") || itemid == null) {
			flag = paymentSvc.hasUnpostedPaymentsByCollectorAndItemid(collectorid, itemid);
		} else {
			flag = paymentSvc.hasUnpostedPaymentsByCollector(collectorid);
		}
		
		if (flag == true) return flag;
		
		DBRemarksService remarksSvc = new DBRemarksService();
		remarksSvc.setDBContext(remarksdb);
		remarksSvc.setCloseable(false);
		
		if (itemid.equals("") || itemid == null) {
			flag = remarksSvc.hasUnpostedRemarksByCollector(collectorid);
		} else {
			flag = remarksSvc.hasUnpostedRemarksByCollectorAndItemid(collectorid, itemid);
		}
		
		if (flag == true) return flag;
		

		DBCollectionSheet collectionSheet = new DBCollectionSheet();
		collectionSheet.setDBContext(clfcdb);
		collectionSheet.setCloseable(false);
		
		if (itemid.equals("") || itemid == null) {
			List<Map> list = collectionSheet.getUnremittedCollectionSheetsByCollector(collectorid);
			if (!list.isEmpty()) {
				String sql = "";
				String objid = "";
				Map map;
				for (int i=0; i<list.size(); i++) {
					map = (Map) list.get(i);
					
					objid = map.get("objid").toString();
					sql = "SELECT objid FROM payment WHERE parentid=? LIMIT 1";
					if (!paymentdb.getList(sql, new Object[]{objid}).isEmpty()) {
						flag = true;
						break;
					}
					
					sql = "SELECT objid FROM remarks WHERE objid=? LIMIT 1";
					if (!remarksdb.getList(sql, new Object[]{objid}).isEmpty()) {
						flag = true;
						break;
					}
				}
			}
		}
		
		return flag;
	}
	
	private ApplicationUtil() {
	}
}
