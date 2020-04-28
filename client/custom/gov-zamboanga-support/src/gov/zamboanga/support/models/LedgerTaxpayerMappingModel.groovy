package gov.zamboanga.landtax.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class LedgerTaxpayerMappingModel extends CrudListModel {

	def selectedFaases = [];
	def selectedFaas;

	def includeValidOwner = false;

	@PropertyChangeListener
	def listener = [
		"includeValidOwner" : { search() }
	]

	def onassign = {
        search();
        selectedFaases = [];
        selectedListHandler.reload();
    }

    public def getCustomFilter() {
    	if ( ! includeValidOwner ) {
    		return [" taxpayer.objid is null ", query];
    	} 
        return null;
    }

    public def fetchList(def o ) {
    	def items = super.fetchList(o);
    	items.removeAll(selectedFaases);
    	return items;
    }

    def assignTaxpayer() {
        return Inv.lookupOpener('rptledger:taxpayer:mapping', [items: selectedFaases, onassign: onassign]);
    }

    void addSelected() {
    	def items = listHandler.selectedValue;
        if (!items) throw new Exception('At least one property to assign new taxpayer should be selected.');
        selectedFaases += items;
        selectedListHandler.reload();
        search();
    }

    void removeSelectedFaas() {
    	selectedFaases.remove(selectedFaas);
    	selectedListHandler.reload();
    	search();
    }

    def selectedListHandler = [
    	fetchList: { selectedFaases },
    	getRows: { selectedFaases.size() }
    ] as BasicListModel 

}