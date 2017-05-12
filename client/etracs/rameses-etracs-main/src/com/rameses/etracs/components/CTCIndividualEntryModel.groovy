package com.rameses.etracs.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class CTCIndividualEntryModel extends ComponentBean {

    @Binding
    def binding;
    
    def parentEntity;
    
    void init() {
        if(ctc==null) {
            setValue([:]);
        }
    }
    
    public def getCtc() {
        return getValue();
    }
    
    void lookupCtc() {
        def h = { o->
            ctc.objid = o.objid;
            ctc.receiptno = o.receiptno;
            ctc.dateissued = o.receiptdate;
            return null;
        }    
        Modal.show("ctc_individual:lookup", [onselect:h, entity: parentEntity] );
    }
    
}
