package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.seti2.models.*;

public class MarketAccountNewModel extends CrudPageFlowModel {

    @PropertyChangeListener
    def listener = [
        'entity.owner': { o->
            if(entity.acctname==null) {
                entity.acctname = o.name;
                binding.refresh("entity.acctname");
            }
        }
    ];        
    
    void afterCreate() {
        entity.areasqm = 0;
        entity.rate = 0;
        entity.extrate = 0;
        entity.extarea = 0;
        entity.units = [];
        entity.recurringfees = [];
        itemHandlers.recurringfees = [
            fetchList: {
                return entity.recurringfees;
            },
            onAddItem: { o->
                entity.recurringfees << o;
            },
            onRemoveItem: { o->
                entity.recurringfees.remove(o);
            }
        ] as EditorListModel;
    }
    
    void updateSummary() {
        def low = entity.units.min{ it.unit.code }.unit.code;
        def hi = entity.units.max{ it.unit.code }.unit.code;
        if(low!=hi) {
            entity.unitno = low + "-" + hi;
        }
        else {
            entity.unitno = low;
        }
        entity.rate = entity.units.sum{ it.rate * (it.usage/100)  };
        entity.ratetype = entity.units.first().unit.ratetype;
        entity.areasqm = entity.units.sum{ it.unit.areasqm * (it.usage/100) };    
    }
    
    void selectCluster() {
        if( entity.units ) {
            boolean p = MsgBox.confirm("This will clear the items. Proceed?");
            if( !p ) return;
            entity.units = [];
            entity.rate = 0;
            entity.ratetype = null;
            entity.areasqm = 0; 
        };
        def s = { o->
            entity.cluster = o;
            return null;
        }
        Modal.show( "market_cluster:lookup", [onselect: s] )
    }
    
     def unitListModel = [
        fetchList: {
            return entity.units;
        },
        onColumnUpdate: {o, colName ->
            if(colName == "usage") {
                updateSummary();
                binding.refresh();
            }
        },
        onRemoveItem: { o->
            return false;
        }
    ] as EditorListModel;
    
    void clearUnits() {
        entity.units.clear();
        entity.areasqm = 0;
        entity.rate = 0;
        entity.extrate = 0;
        entity.extarea = 0;
        entity.unitno = "";
    }
    
     void addUnit() {
        if( !entity.cluster?.objid )
            throw new Exception("Please select first a cluster");
        def s = { o-> 
              entity.units << [unit:o, rate:o.rate, areasqm:o.areasqm, ratetype:o.ratetype, usage: 100-o.usage];
              updateSummary();
              binding.refresh();
        }
        int min = -1;
        int max = -1;
        def sid = null;
        if(  entity.units ) {
            min = entity.units.min{ it.unit.indexno }.unit.indexno;
            max = entity.units.max{ it.unit.indexno }.unit.indexno;
            sid = entity.units.first().unit.section.objid;
        }
        
        Modal.show("market_rentalunit:lookup", [onselect:s, clusterid:entity.cluster.objid, sectionid:sid, min:min, max:max] );
    }
                    
    void selectBusiness() {
        def r = { o->
            entity.business = o;
            return null;
        }
        Modal.show("market_business_unassigned:lookup", [onselect:r] );
    }
    
    void save() {
        saveCreate();
        MsgBox.alert('save successful. Account No ' + entity.acctno + ' is created')
    }
}