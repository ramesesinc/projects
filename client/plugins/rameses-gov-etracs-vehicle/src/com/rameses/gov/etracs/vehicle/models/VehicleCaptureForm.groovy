package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.gov.etracs.vehicle.models.*;
import com.rameses.enterprise.models.*;

public class VehicleCaptureForm extends AbstractVehicleEntryForm {
    
    def selectedItem;
    def dataService;

    void create() {
        setUp();
        entity.fees=[];
        afterLoad();
        editmode = 'create';
    }

    void updateBalances() {
        if( !entity.fees) {
            entity.amount = 0;
        }
        else {
            entity.amount = entity.fees?.sum{ it.amount - it.amtpaid };
        }
        binding.refresh('entity.amount');
    }
    
    def addItem() {
        def h = { o->
            def m = [:];
            m.item = o;
            m.amount = 0;
            m.amtpaid = 0;
            m.balance = 0;
            m.txntype = 'fee';
            m.sortorder = 10;
            entity.fees << m;
            feeListModel.reload();
        }
        return Inv.lookupOpener("revenueitem:lookup", [onselect: h]);
    }
    
    void removeItem() {
       if(!selectedItem) throw new Exception("Select an item to remove");
       entity.fees.remove(selectedItem);
       updateBalances();
       feeListModel.reload();
    }
    
    def feeListModel = [
        fetchList: { o->
            return entity.fees;
        },
        onAddItem: { o->
            o.txntype = 'fee';
            o.sortorder = 10;
            entity.fees << o;
        },
        onColumnUpdate: { item,colName ->
            if( item.amount == null ) item.amount = 0;
            if( item.amtpaid == null ) item.amtpaid = 0;
            if( item.balance == null ) item.balance = 0;
            if( colName.matches("amount|amtpaid") ) {
                item.balance = item.amount - item.amtpaid;
                updateBalances();
            }
        }
    ] as EditorListModel;

}