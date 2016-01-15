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
import com.rameses.clfc.android.db.DBNoteService;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;

public class NotesFragment extends Fragment {

	private Handler handler = new Handler();
	private ListView listview;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_linear, container, false);
		
		listview = (ListView) view.findViewById(R.id.listview);
		
		return view;
	}
	
	public void onStart() {
		super.onStart();
		try {
			loadNotes();
		} catch (Throwable t) {
			t.printStackTrace();
			UIDialog.showMessage(t, ((CollectionSheetInfoMainActivity) getActivity()));
		} 
	}
	
	private void loadNotes() {
		Bundle args = getArguments();
		final String objid = args.getString("objid");
		
		handler.post(new Runnable() {
			public void run() {
				DBContext ctx = new DBContext("clfc.db");
				
				DBNoteService noteSvc = new DBNoteService();
				noteSvc.setDBContext(ctx);
				noteSvc.setCloseable(false);
				
				List list = new ArrayList();
				
				try {
					list = noteSvc.getNotes(objid);
				} catch (Throwable t) {
					t.printStackTrace();
					UIDialog.showMessage(t, ((CollectionSheetInfoMainActivity) getActivity()));
				} finally {
					ctx.close();
				}
				
				listview.setAdapter(new NotesAdapter(getActivity(), list));
			}
		});
	}
	
}
