package com.rameses.clfc.android.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.RemarksDB;
import com.rameses.clfc.android.RemarksRemovedDB;
import com.rameses.clfc.android.db.DBCSRemarks;
import com.rameses.clfc.android.db.DBCollectionSheet;
import com.rameses.clfc.android.db.DBCollectorRemarks;
import com.rameses.clfc.android.db.DBRemarksService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;

public class CollectorRemarksFragment extends Fragment {
	
	private ListView listview;
	private Handler handler = new Handler();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_linear, container, false);
		
		listview = (ListView) view.findViewById(R.id.listview);
		
		return view;
	}
	
	public void onStart() {
		super.onStart();
		try {
			loadRemarks();
		} catch (Throwable t) {
			t.printStackTrace();
			UIDialog.showMessage(t, ((CollectionSheetInfoMainActivity) getActivity()));
		}
	}
	
	public void reloadRemarks() {
		loadRemarks();
	}
	
	private void loadRemarks() {
		Bundle args = getArguments();
		final String objid = args.getString("objid");
		
		handler.post(new Runnable() {
			public void run() {
				DBContext ctx = new DBContext("clfc.db");
				
				DBCollectorRemarks dbremarks = new DBCollectorRemarks();
				dbremarks.setDBContext(ctx);
				dbremarks.setCloseable(false);
				
				List list = new ArrayList();
				
				try {
					list = dbremarks.getRemarks(objid);
				} catch (Throwable t) {
					t.printStackTrace();
					UIDialog.showMessage(t, ((CollectionSheetInfoMainActivity) getActivity()));
				} finally {
					ctx.close();
				}
				
				ctx = new DBContext("clfc.db");
				DBCSRemarks remarkscs = new DBCSRemarks();
				remarkscs.setDBContext(ctx);
				remarkscs.setCloseable(false);
				
				Map item = new HashMap();
				try {
					item = remarkscs.findRemarksById(objid);
				} catch (Throwable t) {
					t.printStackTrace();
					UIDialog.showMessage(t, ((CollectionSheetInfoMainActivity) getActivity()));
				} finally {
					ctx.close();
				}
				
				if (item != null && !item.isEmpty()) {
					Map map = new HashMap();
					String date = new SimpleDateFormat("yyyy-MM-dd").format(Platform.getApplication().getServerDate());
					map.put("txndate", date);
					map.put("collectorname", item.get("collector_name").toString());
					map.put("remarks", item.get("remarks").toString());
					list.add(0, map);
				}
				
				listview.setAdapter(new RemarksAdapter(getActivity(), list));
				listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
						if (position > 0) return true;
						
						boolean hasremarks = false;
						DBContext ctx = new DBContext("clfc.db");
						DBCSRemarks csremarks = new DBCSRemarks();
						csremarks.setDBContext(ctx);
						csremarks.setCloseable(false);
						
						try {
							hasremarks = csremarks.hasRemarksById(objid);
						} catch (Throwable t) {;}
						finally {
							ctx.close();
						}
						
						if (hasremarks == false) return true;
						
						ctx = new DBContext("clfcremarks.db");
						DBRemarksService remarkssvc = new DBRemarksService();
						remarkssvc.setDBContext(ctx);
						remarkssvc.setCloseable(false);
						
						try {
							hasremarks = remarkssvc.hasRemarksById(objid);
						} catch (Throwable t) {;}
						finally {
							ctx.close();
						}
						
						if (hasremarks == false) return true;
						
						CharSequence[] items = {"Edit Remarks", "Remove Remarks"};
						UIDialog dialog = new UIDialog((CollectionSheetInfoMainActivity) getActivity()) {
							public void onSelectItem(int index) {
								switch (index) {
									case 0: editRemarks(); break;
									case 1: removeRemarks(); break;
								}
							}
						};
						
						dialog.select(items);
						
						return false;
					}
					
					private void editRemarks() {
						UIDialog dialog = new UIDialog((CollectionSheetInfoMainActivity) getActivity()) {
							
							public boolean onApprove(Object value) {
								if (value == null || "".equals(value.toString())) {
									ApplicationUtil.showShortMsg("Remarks is required.");
									return false;
								}
								
								SQLTransaction clfcdb = new SQLTransaction("clfc.db");
								SQLTransaction remarksdb = new SQLTransaction("clfcremarks.db");

								final CollectionSheetInfoMainActivity activity = (CollectionSheetInfoMainActivity) getActivity();
								
								try {
									clfcdb.beginTransaction();
									remarksdb.beginTransaction();
									
									onApproveImpl(clfcdb, remarksdb, value.toString());
									
									clfcdb.commit();
									remarksdb.commit();
									
									activity.getHandler().post(new Runnable() {
										public void run() {
											loadRemarks();
											activity.getApp().remarksSvc.start();
										}
									});
								} catch (Throwable t) {
									t.printStackTrace();
									UIDialog.showMessage(t, activity);
								} finally {
									clfcdb.endTransaction();
									remarksdb.endTransaction();
								}
								return true;
							}
							
							private void onApproveImpl(SQLTransaction clfcdb, 
									SQLTransaction remarksdb, String remarks) throws Exception {
								
								Map params = new HashMap();
								params.put("remarks", remarks);
								
								synchronized (MainDB.LOCK) {
									clfcdb.update("remarks", "objid='"+objid+"'", params);
								}

								params.put("state", "PENDING");
								synchronized (RemarksDB.LOCK) {
									remarksdb.update("remarks", "objid='"+objid+"'", params);
								}
//								remarksdb.update("remarks", "loanappid='"+loanappid+"'", params);
								ApplicationUtil.showShortMsg("Successfully updated remark.");
								
							}
						};
						
						DBContext ctx = new DBContext("clfc.db");
						DBCSRemarks csremarks = new DBCSRemarks();
						csremarks.setDBContext(ctx);
						csremarks.setCloseable(false);
						
						Map item = new HashMap();
						try {
							item = csremarks.findRemarksById(objid);
						} catch (Throwable t) {
							t.printStackTrace();
						} finally {
							ctx.close();
						}
						
						String value = "";
						if (item != null && !item.isEmpty()) value = item.get("remarks").toString();
						dialog.input(value);
					}
					
					private void removeRemarks() {
						final CollectionSheetInfoMainActivity activity = (CollectionSheetInfoMainActivity) getActivity();
						
						synchronized (RemarksDB.LOCK) {
							SQLTransaction remarksdb = new SQLTransaction("clfcremarks.db");
							try {
								remarksdb.beginTransaction();
								remarksdb.delete("remarks", "objid=?", new Object[]{objid});
								remarksdb.commit();
							} catch (Throwable t) {
								 UIDialog.showMessage(t, activity);
								
							} finally {
								remarksdb.endTransaction();
							}
						}
						
						synchronized (MainDB.LOCK) {
							SQLTransaction clfcdb = new SQLTransaction("clfc.db");
							try {
								clfcdb.beginTransaction();
								clfcdb.delete("remarks", "objid=?", new Object[]{objid});
								clfcdb.commit();
							} catch (Throwable t) {
								 UIDialog.showMessage(t, activity);
								
							} finally {
								clfcdb.endTransaction();
							}
						}

						Map collectionsheet = new HashMap();
						DBContext ctx = new DBContext("clfc.db");
						DBCollectionSheet dbcs = new DBCollectionSheet();
						dbcs.setDBContext(ctx);
						
						try {
							collectionsheet = dbcs.findCollectionSheet(objid);
						} catch (Throwable t) {
							 UIDialog.showMessage(t, activity);
						}

				 		Map params = new HashMap();
				 		params.put("objid", objid);
				 		params.put("billingid", collectionsheet.get("billingid").toString());
				 		params.put("itemid", collectionsheet.get("itemid").toString());
				 		params.put("state", "PENDING");
				 		
						synchronized (RemarksRemovedDB.LOCK) {
							SQLTransaction remarksremoveddb = new SQLTransaction("clfcremarksremoved.db");
							try {
								remarksremoveddb.beginTransaction();
						 		remarksremoveddb.insert("remarks_removed", params);						 		
								remarksremoveddb.commit();
							} catch (Throwable t) {
								 UIDialog.showMessage(t, activity);
								
							} finally {
								remarksremoveddb.endTransaction();
							}
						}
						
						ApplicationUtil.showShortMsg("Successfully removed remarks.");
						activity.getHandler().post(new Runnable() {
							public void run() {
								loadRemarks();
								activity.getApp().remarksRemovedSvc.start();
								activity.supportInvalidateOptionsMenu();
							}
						});
					}
					
				});
			}
		});
	}
	
}
