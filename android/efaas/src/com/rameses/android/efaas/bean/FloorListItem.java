package com.rameses.android.efaas.bean;

public class FloorListItem {
	
	private FloorItem item;
	private AdjustmentListItem listitem;
	
	public FloorListItem(FloorItem item, AdjustmentListItem listitem){
		this.item = item;
		this.listitem = listitem;
	}
	
	public FloorItem getItem(){ return item; }
	public AdjustmentListItem getList(){ return listitem; }

}
