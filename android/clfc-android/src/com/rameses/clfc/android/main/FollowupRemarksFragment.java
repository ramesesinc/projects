package com.rameses.clfc.android.main;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rameses.clfc.android.R;
import com.rameses.clfc.android.db.DBFollowupRemarks;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;

public class FollowupRemarksFragment extends Fragment {
	
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
			UIDialog.showMessage(t, (CollectionSheetInfoMainActivity) getActivity());
		}
	}
	
	private void loadRemarks() {
		Bundle args = getArguments();
		final String objid = args.getString("objid");
		
		handler.post(new Runnable() {
			public void run() {
				DBContext ctx = new DBContext("clfc.db");
				
				DBFollowupRemarks dbremarks = new DBFollowupRemarks();
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
				
				listview.setAdapter(new RemarksAdapter(getActivity(), list));
				
			}
		});
	}
	
}
