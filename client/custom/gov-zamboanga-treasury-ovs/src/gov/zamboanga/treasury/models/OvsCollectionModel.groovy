package gov.zamboanga.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.enterprise.treasury.cashreceipt.*;

class OvsCollectionModel extends BasicCashReceipt {
	@Service('ZamboangaCollectionOvsService')
	def svc;

    def violations = [];

    def violationListHandler = [
        fetchList: { violations }
    ] as EditorListModel; 

    def verifyViolator() {
    	def onselect = {data ->
    		entity.paidby = data.violator;
    		violations = data.violations;
    		violationListHandler.reload();
    		binding.refresh('entity.paid.*');
    	}

    	return Inv.lookupOpener('zamboanga_ovs_violator:verify', [
    		violator: entity.paidby, 
    		onselect: onselect
    	]);
    }

    def getApprehendingOffices() {
    	return svc.getApprehendingOffices();
    }
    
}