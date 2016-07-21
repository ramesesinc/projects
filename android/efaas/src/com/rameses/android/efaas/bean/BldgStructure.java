package com.rameses.android.efaas.bean;

public class BldgStructure {
	
	private String objid, bldgrpuid, structureid, materialid, floorno;
	
	public BldgStructure(String objid, String bldgrpuid, String structureid, String materialid, String floorno){
		this.objid = objid;
		this.bldgrpuid = bldgrpuid;
		this.structureid = structureid;
		this.materialid = materialid;
		this.floorno = floorno;
	}
	
	public String getObjid(){ return objid; }
	public String getBldgRpuId(){ return bldgrpuid; }
	public String getStructureId(){ return structureid; }
	public String getMaterialId(){ return materialid; }
	public String getFloorNo(){ return floorno; }

}
