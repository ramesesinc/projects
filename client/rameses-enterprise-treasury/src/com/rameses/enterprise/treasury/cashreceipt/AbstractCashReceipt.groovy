package com.rameses.enterprise.treasury.cashreceipt; 

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;
import com.rameses.osiris2.common.*
import com.rameses.util.*;
        
public abstract class AbstractCashReceipt {
        
    @Binding
    def binding;
    
    @Service("CashReceiptService")
    def service;

    def entity;
    def info;
    def _paymentorderid;
   
    String title;
    boolean completed = false;

    //handlers pass by the caller
    def createHandler; 

    def YMD = new java.text.SimpleDateFormat('yyyy-MM-dd');  
    
    void init() {
        title = entity.collectiontype.title;
        completed = false;

        if ( entity.txnmode.toString().toUpperCase()=='OFFLINE' ) {
            service.verifyOffline( entity ); 
        } 
    } 
    
    def createAnother() { 
        if (createHandler) { 
            createHandler(); 
        } 
        return '_close'; 
    } 
    
    def getTotalAmount() {
        return entity.amount; 
    }
    
    void clearAllPayments() {
        entity.totalcash = 0;
        entity.totalnoncash = 0;
        entity.balancedue = 0;
        entity.cashchange = 0;
        entity.totalcredit = 0;
        entity.paymentitems = [];
        
        entity.amount = getTotalAmount(); 
        paymentListModel.reload();
    }
    
    //this is overridable bec. some might not follow this convention.
    public void validateBeforePost() {
        if(entity.totalcredit > 0)
            throw new Exception("Credit is not allowed for this transaction");
    }
    
    public void beforeUpdateBalance() {
    }
    
    public void updateBalances() {
        beforeUpdateBalance();
        
        entity.cashchange = 0; entity.balancedue = 0; entity.totalcredit = 0;
        def amt = getTotalAmount();
        entity.amount = amt; 
        
        entity.totalnoncash = 0;
        if( entity.paymentitems ) {
            entity.totalnoncash = entity.paymentitems.sum{( it.amount ? it.amount : 0.0)};
        }

        if( entity.totalnoncash > 0 && entity.totalnoncash > amt ) {
            entity.totalcredit = entity.totalnoncash - amt;
            entity.amount = amt + entity.totalcredit; 
        }
        else {
            amt = amt - entity.totalnoncash;
            if( entity.totalcash > 0 && entity.totalcash > amt ) {
                entity.cashchange = entity.totalcash - amt;
            }
            else {
                entity.balancedue = NumberUtil.round( amt - entity.totalcash );
            }
        }

        if(binding) binding.refresh('entity.(amount|totalcash|totalnoncash|totalcredit|balancedue|cashchange)');
    }

    def paymentListModel = [
        fetchList: { o->
            return entity.paymentitems;
        },
        onRemoveItem: { o->
            if(!MsgBox.confirm("You are about to remove this entry. Proceed?")) 
                return false;
            entity.paymentitems.remove( o );
            updateBalances();
            return true;
        }
    ] as EditorListModel;

    
    public void afterCashPayment() {}
    public void afterCheckPayment() {}
    
    
    void doCashPayment() { 
        def success = false; 
        def handler = { o->
            entity.totalcash = o.cash;
            entity.cashchange = o.change;
            afterCashPayment(); 
            updateBalances();
            success = true; 
        }
        Modal.show( "cashreceipt:payment-cash", [entity: entity, saveHandler: handler ]); 
        if ( success ) {
            def outcome = post(); 
            if ( outcome ) binding.fireNavigation( outcome );  
        }
    }
    
    void doCheckPayment() { 
        def success = false; 
        def handler = { o-> 
            if ( o.totalcash > 0 ) { 
                entity.totalcash = o.totalcash; 
            }            
            entity.paymentitems = o.checks; 
            entity.totalcredit = 0.0;
            afterCheckPayment(); 
            updateBalances();
            paymentListModel.reload(); 
            success = true; 
        }
        Modal.show( "cashreceipt:payment-check2", [entity: entity, saveHandler: handler ] ); 
        if ( success ) {
            def outcome = post(); 
            if ( outcome ) binding.fireNavigation( outcome );  
        }
    }
    
    def doCreditMemo() {
        def handler = { o->
            entity.paymentitems << o;
            updateBalances();
        }
        return InvokerUtil.lookupOpener( "cashreceipt:payment-creditmemo",
            [entity: entity, saveHandler: handler ] );
    }

    public def payerChanged( o ) {
        //do nothing for now
    }
    
    protected String getLookupEntityName() {
        return "entity:lookup"; 
    }

    protected void beforeLookupEntity( Object params ) {
        //to be implemented 
    }
    
    def getLookupEntity() {
        def params = [:]; 
        beforeLookupEntity( params ); 

        params.onselect = { o-> 
            def newdata = entity.clone();
            newdata.payer = o;
            newdata.items = null; 
            service.validatePayer( newdata );  
             
            entity.payer = o;
            entity.paidby = o.name;
            entity.paidbyaddress = o.address.text;
            binding.refresh("entity.(payer.*|paidby.*)");
            binding.refresh('createEntity|openEntity');
                        
            def opener = payerChanged( o );
            if( opener != null ) { 
                return opener;
            } else {  
                return "_close"; 
            } 
        }
        params.onempty = { 
            entity.payer = null; 
            binding.refresh('createEntity|openEntity'); 
        } 
        return InvokerUtil.lookupOpener( getLookupEntityName(), params );
    } 

    /*
    def cancelSeries(){
        def oldentity = entity.clone()
        return InvokerUtil.lookupOpener( "cashreceipt:cancelseries", [entity:oldentity,
            handler: { o-> 
                def newentity = service.init( o );
                entity.objid = newentity.objid 
                entity.stub = newentity.stub
                entity.receiptno = newentity.receiptno;
                entity.controlid = newentity.controlid;
		entity.series = newentity.series;
                binding.refresh("entity.*") ;
            }
        ]); 
    }
    */


    void beforePost() {}
    void postError() {}
    
    def post() {
        if( entity.amount <= 0 ) 
            throw new Exception("Please select at least an item to pay");
        if( entity.totalcash + entity.totalnoncash == 0 )
            throw new Exception("Please make a payment either cash or check");
            
        if(entity.balancedue > 0)
            throw new Exception("Please ensure that there is no balance unpaid");

        validateBeforePost();
        
        if(MsgBox.confirm("You are about to post this payment. Please ensure entries are correct")) {
            try { 
                beforePost();
                entity._paymentorderid = _paymentorderid;
                entity = service.post( entity );
            } catch(e) { 
                postError(); 
                throw e; 
            }
            
            try {
                if(entity.txnmode.equalsIgnoreCase("ONLINE")) {
                    print();
                }    
            }
            catch(e) {
                e.printStackTrace();
                MsgBox.alert("warning! no form handler found for.  " + entity.formno +". Printout is not handled" );
            }
            completed = true;
            return "completed";
        } 
    }

    def findReportOpener( reportData ) { 
        //check first if form handler exists. 
        def o = InvokerUtil.lookupOpener( "cashreceipt-form:"+entity.formno, [reportData:reportData] );
        if ( !o ) throw new Exception("Handler not found"); 

        if ( reportData.receiptdate instanceof String ) { 
            // this is only true when txnmode is OFFLINE 
            try {
                def dateobj = YMD.parse( reportData.receiptdate ); 
                reportData.receiptdate = dateobj;  
            } catch( Throwable t ) {;} 
        } 
        return o.handle; 
    } 

    void print() {
        def handle = findReportOpener(entity);
        def opt = handle.viewReport(); 
        if ( opt instanceof Opener ) { 
            // 
            // possible routing of report opener has been configured 
            // 
            handle = opt.handle; 
            handle.viewReport(); 
        } 
        ReportUtil.print(handle.report,true);
    }
    
    void reprint() {
        if( verifyReprint() ){
            print();
        } else {
            MsgBox.alert('Invalid security code'); 
        }
    }
    
    boolean verifyReprint() {
        return (MsgBox.prompt("Please enter security code") == "etracs"); 
    }

    def getInfoHtml() {
        return TemplateProvider.instance.getResult( "com/rameses/enterprise/treasury/cashreceipt/cashreceipt.gtpl", [entity:entity] );
    }

    def doVoid() { 
        def xbinding = binding; 
        def params = [ receipt: entity ]; 
        params.handler = { o-> 
            entity.voided = true;
            xbinding.refresh();
        } 
        return InvokerUtil.lookupOpener( "cashreceipt:void", params );  
    }

    boolean isAllowCreateEntity() {
        return false; 
    }
    
    def createEntity() { 
        return null; 
    } 

    def previewReceipt() {
        return Inv.lookupOpener( "cashreceipt:preview", [entity: entity] );
    }    
}