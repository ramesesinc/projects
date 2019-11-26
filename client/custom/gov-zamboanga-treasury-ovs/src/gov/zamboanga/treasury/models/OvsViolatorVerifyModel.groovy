package gov.zamboanga.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.enterprise.treasury.cashreceipt.*;

class OvsViolatorVerifyModel {
    @Service('ZamboangaCollectionOvsService')
    def svc;

    def onselect = {};
    def violator;
    def violators;
    def selectedItem;

    void search() {
    	violators = svc.getViolators([violator: violator]);
    	listHandler?.reload();
    }

    def select() {
    	if (!selectedItem) throw new Exception('An item should be selected.')
    	selectedItem.violations = svc.getViolations(selectedItem);
    	onselect(selectedItem);
    	return '_close';
    }

    def listHandler = [
    	fetchList : {violators}
    ] as BasicListModel

}