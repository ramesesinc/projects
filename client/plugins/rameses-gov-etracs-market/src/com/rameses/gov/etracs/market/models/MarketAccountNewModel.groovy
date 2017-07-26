package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.seti2.models.*;

public class MarketAccountNewModel extends CrudFormModel {

    @PropertyChangeListener
    def listener = [
        'entity.owner': { o->
            if(entity.acctname==null) {
                entity.acctname = o.name;
                binding.refresh("entity.acctname");
            }
        },
        'entity.unit' : { o->
            entity.unitno = o.code;
            entity.rate = o.rate;
            entity.ratetype = o.ratetype;
        }
    ];        
    
    void afterCreate() {
        entity.rate = 0;
        entity.extrate = 0;
        entity.extarea = 0;
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
    
    void selectBusiness() {
        def r = { o->
            entity.business = o;
            return null;
        }
        Modal.show("market_business_unassigned:lookup", [onselect:r] );
    }
    
    void afterSave() {
        MsgBox.alert('save successful. Account No ' + entity.acctno + ' is created')
    }
}