package com.rameses.android.service;

import com.rameses.client.services.AbstractService;
import java.util.*;

public class MasterFileService extends AbstractService{

	@Override
	public String getServiceName() {
		return "EfaasMasterFileService";
	}
	
	public List<Map> getPropertyClassifications(Map params) throws Exception{
		return (List<Map>) invoke("getPropertyClassifications", params);
	}
	
	public List<Map> getBldgKinds(Map params) throws Exception{
		return (List<Map>) invoke("getBldgKinds", params);
	}
	
	public List<Map> getMaterials(Map params) throws Exception{
		return (List<Map>) invoke("getMaterials", params);
	}
	
	public List<Map> getStructures(Map params) throws Exception{
		return (List<Map>) invoke("getStructures", params);
	}
	
	public List<Map> getMachines(Map params) throws Exception{
		return (List<Map>) invoke("getMachines", params);
	}
	
	public List<Map> getPlantTrees(Map params) throws Exception{
		return (List<Map>) invoke("getPlantTrees", params);
	}
	
	public List<Map> getMiscItems(Map params) throws Exception{
		return (List<Map>) invoke("getMiscItems", params);
	}
	
	public List<Map> getRPTParameters(Map params) throws Exception{
		return (List<Map>) invoke("getRPTParameters", params);
	}

}
