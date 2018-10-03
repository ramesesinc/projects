package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.util.*;

class CashReceiptIssueModel extends CashReceiptAbstractIssueModel  {

    def handler;
    boolean showPrintDialog = false;

    //default to af51
    public String getDefaultAfType() {
        return "51";
    }
    
    public String getFormType() {
        return "serial";
    }

    void launchNew() {
        createNew();
        def mp = [
            back : {
                def op = super.signal("back");
                binding.fireNavigation( op );
            },
            forward: {
                //print will be done here
                if( mode == "ONLINE" ) {
                    printReceipt();
                }
                def op = super.signal("forward");
                binding.fireNavigation( op );
            }
        ];
        handler = Inv.lookupOpener("cashreceipt:"+ collectionType.handler, [entity: entity, mainProcessHandler: mp]);         
    }
    
    void cleanUp() {
        entity = [:];
        handler = null;
    }

    
    def getInfoHtml() {
        return TemplateProvider.instance.getResult( "com/rameses/enterprise/treasury/cashreceipt/cashreceipt.gtpl", [entity:entity] );
    }
    
    def loadBarcode() {
        def h = { o->
            mode = "ONLINE";
            collectionType = o.collectiontype;
            afType = o.collectiontype.formno;
            
            //initialize the entity
            createNew();
            def op = null;
            if ( o._paymentorderid ) {
                entity.payer = o.info.payer;
                entity.paidby = o.info.paidby;
                entity.paidbyaddress = o.info.paidbyaddress;
                entity.amount = o.info.amount;
                entity.items = o.info.items.collect{ [item: it.item, amount:it.amount, remarks:it.remarks ] };
                entity.remarks = o.info.particulars;
                handler = Inv.lookupOpener("cashreceipt:"+ collectionType.handler, [entity: entity, _paymentorderid:o._paymentorderid ]);
            }
            else {
                def pp = [:];
                pp.info = o.info;
                pp.entity = entity;
                pp.barcodeid = o.barcodeid;
                handler = Inv.lookupOpener("cashreceipt:barcode:"+ o.prefix, pp );
            }
            super.signal();
        }
        return Inv.lookupOpener( "cashreceipt_barcode", [handler: h] );
    }
    
    public void printReceipt() {
        //load the report forms. This is temporarily loaded,
        if(!afcontrol.afunit.cashreceiptprintout ) {
            throw new Exception("Please define a cashreceipt printout in the af unit")
        }
        print( afcontrol.afunit.cashreceiptprintout );
    }
    
    public void printReceiptDetail() {
        if(!afcontrol.afunit.cashreceiptdetailprintout ) {
            throw new Exception("Please define a cashreceipt detail printout in the af unit")
        }
        print( afcontrol.afunit.cashreceiptdetailprintout );
    }

    public void print( def name ) {
        def op = Inv.lookupOpener( name, [reportData: entity] );
        if( !(op.handle instanceof ReportModel )) {
            throw new Exception("Report Handle for " + n + " must be a ReportModel " );
        }
        op.handle.viewReport(); 
        showPrintDialog = true;
        ReportUtil.print(op.handle.report, showPrintDialog);
    }
    
    public void voidReceipt() {
        Modal.show( "void_cashreceipt", [applySecurity : false, receipt: entity ]);
    }

}    