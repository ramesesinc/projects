package com.rameses.gov.etracs.rptis.province.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class EntityMappingModel {
	@Caller
	def caller;

	@Service('ProvinceEntityMappingService')
	def svc;

	def entity;
    def searchtext;
    def items;

    void init() {
    	items = [];
		entity = caller.masterEntity;
		searchtext = entity.name;
		search();
    }

    void search() {
    	if (!searchtext) {
    		throw new Exception('Please specify a search criteria.');
    	}
    	def params = [
    		mainentityid: entity.objid, 
    		type: entity.type,
    		searchtext: searchtext
		]
    	items = svc.getEntities(params)
    	listHandler?.reload();
    }

    def mapEntities() {
    	def selectedItems = listHandler.selectedValue
    	if (!selectedItems) {
    		throw new Exception('At least one mapping should be selected.');
    	}

    	if (MsgBox.confirm('Map selected entities?')) {
	    	svc.saveMapping(selectedItems);
	    	caller.reload();
	    	caller.caller.reload();
	    	return '_close';
    	}
    }

    def listHandler = [
    	getRows: { items.size() },
    	fetchList: { items },
    	isMultiSelect: { true },
    ] as BasicListModel


    void selectAll() {
    	listHandler.selectAll()
    }

    void deselectAll() {
    	listHandler.deselectAll()
    }

}