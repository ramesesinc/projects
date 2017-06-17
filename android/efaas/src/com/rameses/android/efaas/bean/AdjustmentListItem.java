package com.rameses.android.efaas.bean;

import java.util.List;

public class AdjustmentListItem {
	
	private AdjustmentItem item;
	private List<ParameterItem> list;
	
	public AdjustmentListItem(AdjustmentItem item, List<ParameterItem> list){
		this.item = item;
		this.list = list;
	}
	
	public AdjustmentItem getItem(){ return item; }
	public List<ParameterItem> getList(){ return list; }

}
