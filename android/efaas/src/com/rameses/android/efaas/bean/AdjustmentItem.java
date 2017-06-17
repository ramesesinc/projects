package com.rameses.android.efaas.bean;

public class AdjustmentItem {
	
	String objid, code, name, unit, expr;
	
	public AdjustmentItem(String objid, String code, String name, String unit, String expr){
		this.objid = objid;
		this.code = code;
		this.name = name;
		this.unit = unit;
		this.expr = expr;
	}
	
	public String getObjid(){ return objid; }
	public String getCode(){ return code; }
	public String getName(){ return name; }
	public String getUnit(){ return unit; }
	public String getExpr(){ return expr; }

}
