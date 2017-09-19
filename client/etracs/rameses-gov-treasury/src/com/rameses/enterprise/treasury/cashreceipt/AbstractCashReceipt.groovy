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
    
    @Service("CollectionRuleService")
    def collectionRuleService;

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
    
   
    
    //this is overridable bec. some might not follow this convention.
    public void validateBeforePost() {
        if(entity.totalcredit > 0)
            throw new Exception("Credit is not allowed for this transaction");
    }
    
    public void beforeUpdateBalance() {
    }
    
    public void updateBalances() {
        beforeUpdateBalance();
        entity.amount = getTotalAmount(); 
        if(binding) binding.refresh('entity.amount');
        /*
        
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
        */
    }

    
    public void afterCashPayment() {}
    public void afterCheckPayment() {}
    
    def summarizeByFund() {
        def g = entity.items.groupBy{ it.item.fund };
        def fb = [];
        g.each { k,v->
            fb << [fund:k, amount: v.sum{it.amount}];
        }
        return fb;
    } 
    
     void clearAllPayments() {
        entity.totalcash = 0;
        entity.totalnoncash = 0;
        entity.balancedue = 0;
        entity.cashchange = 0;
        entity.totalcredit = 0;
        if(entity.paymentitems==null) {
            entity.paymentitems = [];
        }
        else {
            entity.paymentitems.clear();
        }
    }
    
    void doCashPayment() { 
        if(!entity.items) throw new Exception("At least one item is required");
        if(entity.amount<=0) throw new Exception("Amount must be greater than 0");
        def success = false; 
        clearAllPayments();
        def handler = { o->
            entity.totalcash = o.cash;
            entity.cashchange = o.change;
            success = true; 
        }
        Modal.show( "cashreceipt:payment-cash", [entity: entity, saveHandler: handler ]); 
        if ( success ) {
            def outcome = post(); 
            binding.fireNavigation( outcome );  
        }
    }
    
    void doCheckPayment() { 
        if(!entity.items) throw new Exception("At least one item is required");
        if(entity.amount<=0) throw new Exception("Amount must be greater than 0");
        def success = false; 
        clearAllPayments();
        def handler = { o-> 
            entity.totalcash = o.totalcash; 
            entity.checks = o.checks;
            entity.paymentitems = o.paymentitems; 
            entity.totalnoncash = o.paymentitems.sum{it.amount};
            success = true; 
        }
        Modal.show( "cashreceipt:payment-check", [entity: entity, saveHandler: handler, fundList:summarizeByFund() ] ); 
        if ( success ) {
            def outcome = post(); 
            binding.fireNavigation( outcome );  
        }
    }
    
    
    def doCreditMemo() {
        if(!entity.items) throw new Exception("At least one item is required");
        if(entity.amount<=0) throw new Exception("Amount must be greater than 0");
        def success = false;
        clearAllPayments();
        def handler = { o->
            entity.totalcash = 0;
            entity.paymentitems = o.paymentitems;
            entity.totalnoncash = o.paymentitems.sum{it.amount};
            success = true; 
        }
        Modal.show( "cashreceipt:payment-creditmemo", [entity: entity, saveHandler: handler, fundList:summarizeByFund() ] );
        if ( success ) {
            def outcome = post(); 
            binding.fireNavigation( outcome );  
        }
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
        if( (entity.totalcash-entity.cashchange) + entity.totalnoncash != entity.amount )
            throw new Exception("Total cash and total non cash must equal the amount");    
            
        if(entity.balancedue > 0)
            throw new Exception("Please ensure that there is no balance unpaid");

        validateBeforePost();
        
        boolean pass = false;
        def h = {
            pass = true;
        }
        Modal.show("cashreceipt_confirm", [handler:h, receiptno: entity.receiptno] );
        if(pass) {
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
        
        def canShowPrinterDialog = ( entity._options?.canShowPrinterDialog == false ? false : true ); 
        ReportUtil.print(handle.report, canShowPrinterDialog);
    }
    
    void reprint() {
        if ( entity._options ) { 
            entity._options.canShowPrinterDialog = true; 
        }
        
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