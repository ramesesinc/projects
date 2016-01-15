package com.rameses.clfc.android;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.rameses.clfc.android.db.DBCapturePayment;
import com.rameses.clfc.android.db.DBLocationTracker;
import com.rameses.clfc.android.db.DBPaymentService;
import com.rameses.clfc.android.db.DBRemarksService;
import com.rameses.clfc.android.db.DBSpecialCollectionPendingService;
import com.rameses.clfc.android.db.DBVoidService;
import com.rameses.client.android.AbstractActionBarActivity;
import com.rameses.client.android.AppContext;
import com.rameses.client.android.AppSettings;
import com.rameses.client.android.Logger;
import com.rameses.client.android.NetworkLocationProvider;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIApplication;
import com.rameses.client.services.SessionProviderImpl;
import com.rameses.db.android.DBContext;
import com.rameses.util.Base64Cipher;

public class ApplicationImpl extends UIApplication 
{
	private MainDB maindb;
	private TrackerDB trackerdb;
	private PaymentDB paymentdb;
	private VoidRequestDB requestdb;
	private RemarksDB remarksdb;
	private RemarksRemovedDB remarksremoveddb;
	private CaptureDB capturedb;
	private SpecialCollectionDB specialcollectiondb;
	private int networkStatus;
	private AppSettingsImpl appSettings; 

	public VoidRequestService voidRequestSvc;
	public PaymentService paymentSvc;
	public RemarksService remarksSvc;
	public RemarksRemovedService remarksRemovedSvc;
	public BroadcastLocationService broadcastLocationSvc;
	public LocationTrackerService locationTrackerSvc;
	public CaptureService captureSvc;
	public SpecialCollectionService specialColSvc;
	private NetworkCheckerService networkCheckerSvc;
	
	private DBLocationTracker tracker = new DBLocationTracker();
	private DBPaymentService payment = new DBPaymentService();
	private DBRemarksService remarks = new DBRemarksService();
	private DBCapturePayment capture = new DBCapturePayment(); 
	private DBVoidService voidsvc = new DBVoidService();
	private DBSpecialCollectionPendingService scPendingSvc = new DBSpecialCollectionPendingService();
		
	public File getLogFile() {
		// TODO Auto-generated method stub
		File dir = Environment.getExternalStorageDirectory();
		return new File(dir, "clfclog.txt");
	}
	 
	protected void init() {
		super.init();
//		System.out.println(getClass().getProtectionDomain());
	}

	protected void onCreateProcess() {
		super.onCreateProcess();
		println("is date synced " + getIsDateSync());
//		int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//		if (result == ConnectionResult.SUCCESS) {
//			System.out.println("Google Play services is available.");
//		} else {
//			System.out.println("Google Play services is unavailable.");
//		}
		//if(rc == MapModule.SUCCESS ) Ti.API.info(" TEST    >    Google Play services is installed.");
//		Platform.setDebug(true);
		NetworkLocationProvider.setEnabled(true);
		System.out.println("NetworkLocationProvider enabled");
//		NetworkLocationProvider.setDebug(true);
		try {
//			System.out.println("passing 1");
			maindb = new MainDB(this, "clfc.db", 1);
			maindb.load();
//			System.out.println("passing 2");
			trackerdb = new TrackerDB(this, "clfctracker.db", 1);
			trackerdb.load();
//			System.out.println("passing 3");
			paymentdb = new PaymentDB(this, "clfcpayment.db", 1);
			paymentdb.load();
//			System.out.println("passing 4");
			requestdb = new VoidRequestDB(this, "clfcrequest.db", 1);
			requestdb.load();
//			System.out.println("passing 5");
			remarksdb = new RemarksDB(this, "clfcremarks.db", 1);
			remarksdb.load();
//			System.out.println("passing 6");
			remarksremoveddb = new RemarksRemovedDB(this, "clfcremarksremoved.db", 1);
			remarksremoveddb.load();
			
			capturedb = new CaptureDB(this, "clfccapture.db", 1);
			capturedb.load();
			
			specialcollectiondb = new SpecialCollectionDB(this, "clfcspecialcollection.db", 1);
			specialcollectiondb.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		networkCheckerSvc = new NetworkCheckerService(this);
		locationTrackerSvc = new LocationTrackerService(this);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				networkCheckerSvc.start();
				locationTrackerSvc.start();
			}
		}, 1);
//		
		voidRequestSvc = new VoidRequestService(this);
		paymentSvc = new PaymentService(this);
		remarksSvc = new RemarksService(this);
		remarksRemovedSvc = new RemarksRemovedService(this);
		broadcastLocationSvc = new BroadcastLocationService(this);
		captureSvc = new CaptureService(this);
		specialColSvc = new SpecialCollectionService (this);
		
		DBContext ctx = new DBContext("clfctracker.db");
		tracker.setDBContext(ctx);
		try {
			boolean flag = tracker.hasLocationTrackers();
			if (flag) {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						broadcastLocationSvc.start();
					}
				}, 1);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		ctx = new DBContext("clfcpayment.db");
		payment.setDBContext(ctx);
		try {
			boolean flag = payment.hasUnpostedPayments();
			if (flag) {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						paymentSvc.start();
					}
				}, 1);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		ctx = new DBContext("clfcremarks.db");
		remarks.setDBContext(ctx);
		try {
			boolean flag = remarks.hasUnpostedRemarks();
			if (flag) {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						remarksSvc.start();
					}
				}, 1);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		ctx = new DBContext("clfcrequest.db");
		voidsvc.setDBContext(ctx);
		try {
			boolean flag = voidsvc.hasPendingVoidRequest();
			if (flag) {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						voidRequestSvc.start();
					}
				}, 1);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		/***************************
		 * 
		 *  add capture payment service
		 * 
		 */
		
		ctx = new DBContext("clfccapture.db");
		capture.setDBContext(ctx);
		try {
			boolean flag = capture.hasUnpostedPayments();
			if (flag) {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						captureSvc.start();
					}
				}, 1);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		ctx = new DBContext("clfcspecialcollection.db");
		scPendingSvc.setDBContext(ctx);
		try {
			boolean flag = scPendingSvc.hasUnpostedRequest();
			if (flag == true) {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						specialColSvc.start();
					}
				}, 1);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		
//		AppSettingsImpl sets = (AppSettingsImpl) Platform.getApplication().getAppSettings();
//		boolean flag = false;
//		if ("true".equals(sets.getDebugEnabled())) {
//			flag = true;
//		}
//		Platform.setDebug(flag);
	}
	
	protected AppSettings createAppSettings() {
		return new AppSettingsImpl(); 
	}
	
	protected boolean getIsConnected() {
		return isConnected();
	}
	
	protected boolean isConnected() {
		boolean isConnected = true;
		int networkStatus = ApplicationUtil.getNetworkStatus();
		if (networkStatus == 3) isConnected = false;
		return isConnected;
	}
	
	protected void dateChanged(Date date) {
//		println("date changed: " + date);
		if (date == null) return;
		
		println("is date synced " + getIsDateSync());
		
		boolean flag = true;
		AppSettingsImpl settings = (AppSettingsImpl) Platform.getApplication().getAppSettings();
		Map map = settings.getAll();
		if (map != null && map.containsKey("serverdate")) {
			long timemillis = java.sql.Timestamp.valueOf(map.get("serverdate").toString()).getTime();
			Date xdate = new java.sql.Timestamp(timemillis);
			if (date.compareTo(xdate) < 0) {
				flag = false;
			}
		}
		
//		println("date " + date);
		if (flag == true) {
			settings.put("serverdate", date.toString());
		}
	}
	
	protected long getServerTime() { 
		Calendar cal = Calendar.getInstance();
		Calendar xcal = cal;
		AppSettingsImpl settings = (AppSettingsImpl) Platform.getApplication().getAppSettings();
		long timemillis = 0L;
		Map map = settings.getAll();
//		println("settings " + map);
		if (map.containsKey("serverdate")) {
			String serverdate = map.get("serverdate").toString();
//			println("get server time " + serverdate);
			if (serverdate != null) {
				timemillis = java.sql.Timestamp.valueOf(serverdate).getTime();
				xcal.setTimeInMillis(timemillis);
			}
		}
		println("time: " + cal.getTime() + " server time: " + xcal.getTime());
		if (timemillis <= 0) timemillis = cal.getTimeInMillis();
//		println("get server time " + getServerTime());
		return timemillis;
	}

	public Logger createLogger() {
		Logger logger = Logger.create("clfclog.txt");
		//ApplicationUtil.showShortMsg("logger -> "+logger.toString());
		return logger;
	} 	 
 
	protected void beforeLoad(Properties appenv) {
		super.beforeLoad(appenv);
		appenv.put("app.context", "clfc");
		appenv.put("app.cluster", "osiris3");
		
		int ns = ApplicationUtil.getNetworkStatus();
		//if (ns == 0) ns = 1;
		appenv.put("app.host", ApplicationUtil.getAppHost(ns));
		println("before load");
		println("network status " + ns);
		
//		if (networkStatus == 3) {
//			AppSettingsImpl settings = (AppSettingsImpl) Platform.getApplication().getAppSettings();
//
//			String result = settings.getString("result", null);
//			Map mresult = null;
//			if (result != null) {
//				Gson gson = new Gson();
//				mresult = gson.fromJson(result, Map.class);
//			}
//			
//			if (mresult != null && !mresult.isEmpty()) {
//				appenv.put("result", mresult);
//			}
//			
//			String encpwd = settings.getString("encpwd", null);
//			
//			if (encpwd != null) {
//				appenv.put("encpwd", encpwd);
//			}
//			
//			
//			//println "session: " + session;
////			println("provider: " + provider);
////			if (provider != null) {
////				Gson gson = new Gson();
////				SessionProvider sessProvider = gson.fromJson(provider, SessionProvider.class); 
////				appenv.put("provider", sessProvider);	
////			}
//		}
	}
	
	protected void afterLoad() {
//		SessionContext sc = AppContext.getSession();
//		println("session context: " + sc);

		int networkStatus = ApplicationUtil.getNetworkStatus();
		if (networkStatus == 3) {
			AppSettingsImpl settings = (AppSettingsImpl) Platform.getApplication().getAppSettings();
			
			String result = settings.getString("result", null);
//			println("result: " + result);
			String encpwd = settings.getString("encpwd", null);
//			println("encpwd: " + encpwd);
			if (result != null && encpwd != null) {
				Map xresult = (Map) new Base64Cipher().decode(result);//gson.fromJson(result, Map.class);
//				println("xresult: " + xresult);
				
				SessionProviderImpl sessImpl = new SessionProviderImpl(xresult);
		        SessionContext sess = AppContext.getSession();
		        sess.setProvider(sessImpl); 
		        sess.set("encpwd", encpwd); 
		        
		        Map authOpts = (Map) xresult.remove("AUTH_OPTIONS");
		        //println("authopts " + authOpts);
		        if (authOpts != null) {
		            Iterator keys = authOpts.keySet().iterator(); 
		            while (keys.hasNext()) { 
		                String key = keys.next().toString(); 
		                //println("key: " + key);
		                sess.set(key, authOpts.get(key)); 
		            } 
		        }	
			}
		}
		
	} 
	
	private void println(String str) {
//		System.out.println("[ApplicationImpl] " + str);
		Log.i("ApplicationImpl", str);
	}
 
	protected void onTerminateProcess() { 
		super.onTerminateProcess(); 
		NetworkLocationProvider.setEnabled(false);
		AppSettingsImpl settings = (AppSettingsImpl) getAppSettings();
		Date date = getServerDate();
		if (date != null) {
			settings.put("serverdate", date);
		}
	} 	  
	 
	public int getNetworkStatus() { return networkStatus; }
	void setNetworkStatus(int networkStatus) { 
		this.networkStatus = networkStatus; 
		
		println("network status " + networkStatus);
		getAppEnv().put("app.host", ApplicationUtil.getAppHost(networkStatus)); 
	}

	public void suspend() {
		if (SuspendDialog.isVisible()) return;
		
		String sessionid = SessionContext.getSessionId();
		println("sessionid " + sessionid);

//		AbstractActivity aa = Platform.getCurrentActivity();
		AbstractActionBarActivity aa = Platform.getCurrentActionBarActivity();
		if (aa == null) aa = Platform.getActionBarMainActivity();//Platform.getMainActivity();
		
//		final AbstractActivity current = aa;
		final AbstractActionBarActivity current = aa;
		current.getHandler().postAtFrontOfQueue(new Runnable() {
			@Override
			public void run() {
				String content = "Collector: "+ SessionContext.getProfile().getFullName()+"\n\nTo resume your session, please enter your password";
				SuspendDialog.show(content);
			}
		});
	}
	
}
