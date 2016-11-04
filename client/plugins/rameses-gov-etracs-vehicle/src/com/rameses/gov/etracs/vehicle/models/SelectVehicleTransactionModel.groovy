package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;

public class SelectVehicleTransactionModel  {

    def txntype;
    
    @FormTitle
    String title;

    String label = ""
    
    @FormId
    def formId;
    
    def nodeContext;
    def homeicon = 'home/icons/folder.png';
    
    def openers;
    
    void init() {
        formId = txntype.objid;
        title = txntype.title;
        if(nodeContext) {
            title += " " +nodeContext.caption;
            formId += "_"+nodeContext.id;
        }
        String t = "vehicle_menu:" + txntype.uihandler;
        def openerList = Inv.lookupOpeners( t, [txntype: txntype ] );
        openers = [];
        openerList.each {
            def m = [
                caption : it.caption, 
                icon    : ((it.properties.icon)? it.properties.icon : homeicon), 
                opener  : it
            ];
            openers << m;        
        }
    }                        

    def listModel = [
        fetchList: { o->
            return openers;
        },
        onOpenItem: { o->
            return o.opener;
        }
    ] as TileViewModel;

    
}