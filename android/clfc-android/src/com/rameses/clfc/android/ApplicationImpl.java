package com.rameses.clfc.android;

import java.io.File;
import java.util.Properties;

import android.os.Environment;
import android.os.Handler;

import com.rameses.clfc.android.db.DBCapturePayment;
import com.rameses.clfc.android.db.DBLocationTracker;
import com.rameses.clfc.android.db.DBPaymentService;
import com.rameses.clfc.android.db.DBRemarksService;
import com.rameses.clfc.android.db.DBVoidService;
import com.rameses.client.android.AbstractActionBarActivity;
import com.rameses.client.android.AppSettings;
import com.rameses.client.android.Logger;
import com.rameses.client.android.NetworkLocationProvider;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIApplication;
import com.rameses.db.android.DBContext;

public class ApplicationImpl extends UIApplication 
{
	private MainDB maindb;
	private TrackerDB trackerdb;
	private PaymentDB paymentdb;
	private VoidRequestDB requestdb;
	private RemarksDB remarksdb;
	private RemarksRemovedDB remarksremoveddb;
	private CaptureDB capturedb;
	private int networkStatus;
	private AppSettingsImpl appSettings;

	public VoidRequestService voidRequestSvc;
	public PaymentService paymentSvc;
	public RemarksService remarksSvc;
	public RemarksRemovedService remarksRemovedSvc;
	public BroadcastLocationService broadcastLocationSvc;
	public LocationTrackerService locationTrackerSvc;
	public CaptureService captureSvc;
	private NetworkCheckerService networkCheckerSvc;
	
	private DBLocationTracker tracker = new DBLocationTracker();
	private DBPaymentService payment = new DBPaymentService();
	private DBRemarksService remarks = new DBRemarksService();
	private DBCapturePayment capture = new DBCapturePayment(); 
	private DBVoidService voidsvc = new DBVoidService();
	
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

	public Logger createLogger() {
		Logger logger = Logger.create("clfclog.txt");
		//ApplicationUtil.showShortMsg("logger -> "+logger.toString());
		return logger;
	} 	

	protected void beforeLoad(Properties appenv) {
		super.beforeLoad(appenv);
		appenv.put("app.context", "clfc");
		appenv.put("app.cluster", "osiris3");
		appenv.put("app.host", ApplicationUtil.getAppHost(networkStatus));
	}

	protected void onTerminateProcess() { 
		super.onTerminateProcess(); 
		NetworkLocationProvider.setEnabled(false); 
	} 	
	
	public int getNetworkStatus() { return networkStatus; }
	void setNetworkStatus(int networkStatus) { 
		this.networkStatus = networkStatus; 
		 
		AppSettingsImpl impl = (AppSettingsImpl) getAppSettings(); 
		getAppEnv().put("app.host", impl.getAppHost(networkStatus)); 
	}

	public void suspend() {
		if (SuspendDialog.isVisible()) return;

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
