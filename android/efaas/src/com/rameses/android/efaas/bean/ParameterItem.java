package com.rameses.android.efaas.bean;

public class ParameterItem {
	
	private String parameter;
	private double value;
	
	public ParameterItem(String parameter, double value){
		this.parameter = parameter;
		this.value = value;
	}
	
	public String getParameter(){ return parameter; }
	public double getValue(){ return value; }

}
