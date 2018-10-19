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
    
    //new receipting process
    def mainProcessHandler;

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
            clearAllPayments();
            entity = createHandler();
            if( !entity ) return "_close";
            init();
            return "default";
        } 
        return '_close'; 
    } 
    
    def getTotalAmount() {
        return entity.amount; 
    }
    
    public String getEntityType() {
        return null;
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
        //if(!entity.items) throw new Exception("At least one item is required");
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
        //if(!entity.items) throw new Exception("At least one item is required");
        if(entity.amount<=0) throw new Exception("Amount must be greater than 0");

        def funds = summarizeByFund(); 
        if ( !funds ) throw new Exception("Please provide the summarize amount by fund"); 
        
        def success = false; 
        clearAllPayments();
        def handler = { o-> 
            entity.totalcash = o.totalcash; 
            entity.cashchange = o.cashchange;
            entity.checks = o.checks;
            entity.paymentitems = o.paymentitems; 
            entity.totalnoncash = o.paymentitems.sum{it.amount};
            success = true; 
        }
        Modal.show( "cashreceipt:payment-check", [entity: entity, saveHandler: handler, fundList:funds] ); 
        if ( success ) {
            def outcome = post(); 
            binding.fireNavigation( outcome );  
        }
    }
    
    void doEFTPayment() { 
        //if(!entity.items) throw new Exception("At least one item is required");
        if(entity.amount<=0) throw new Exception("Amount must be greater than 0");
        def success = false; 
        clearAllPayments();
        def handler = { o-> 
            entity.paymentitems = o.paymentitems; 
            entity.totalnoncash = o.paymentitems.sum{it.amount};
            entity.eft = o.eft;
            success = true; 
        }
        Modal.show( "cashreceipt:payment-eft", [entity: entity, saveHandler: handler, fundList:summarizeByFund() ] ); 
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
    
    @PropertyChangeListener
    def propertyChange = [
        "entity.payer" : { o->
            if(o) {
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
                    binding.fireNavigation( opener );
                }
            }
            else {
                entity.payer = null; 
                entity.paidby = null;
                entity.paidbyaddress = null;
                binding.refresh("entity.(payer.*|paidby.*)");
                binding.refresh('createEntity|openEntity');     
            }
        }
    ];

    void beforePost() {}
    void postError() {}
    
    def post() {
        if( entity.amount <= 0 ) 
            throw new Exception("Please select at least an item to pay");
        if( entity.totalcash + entity.totalnoncash == 0 )
            throw new Exception("Please make a payment either cash or check");
            
        def numformat = new java.text.DecimalFormat('0.00'); 
        entity.totalcash = new BigDecimal( numformat.format( entity.totalcash )); 
        entity.cashchange = new BigDecimal( numformat.format( entity.cashchange )); 
        entity.totalnoncash = new BigDecimal( numformat.format( entity.totalnoncash )); 
        entity.amount = new BigDecimal( numformat.format( entity.amount )); 
        if( (entity.totalcash-entity.cashchange) + entity.totalnoncash != entity.amount )
            throw new Exception("Total cash and total non cash must equal the amount");    
            
        if(entity.balancedue > 0)
            throw new Exception("Please ensure that there is no balance unpaid");

        validateBeforePost();
        
        boolean pass = false;
        def h = { pass = true; }
        Modal.show("cashreceipt_confirm", [handler:h, receiptno: entity.receiptno] );
        if ( !pass ) return null; 
        
        boolean postok = false;
        try { 
            beforePost();
            entity._paymentorderid = _paymentorderid; 
            def res = service.post( entity ); 
            if ( res ) entity.putAll( res ); 
            
            postok = true; 
        } catch(e) { 
            postError(); 
            throw e; 
        }

        if ( postok ) {
            completed = true; 
            binding.fireNavigation('completed'); 
            binding.refresh(); 
        } 
        
        try {
            if(entity.txnmode.equalsIgnoreCase("ONLINE") && mainProcessHandler==null) { 
                print();
            }    
        }
        catch(e) {
            e.printStackTrace();
            MsgBox.alert("warning! no form handler found for.  " + entity.formno +". Printout is not handled" );
        }

        if( mainProcessHandler ) {
            mainProcessHandler.forward();
        }
        return null; 
    }

    def findReportOpener( reportData ) { 
        //check first if form handler exists. 
        def op = null;
        try {
            def handlerName = "cashreceipt-form:" + entity.formno; 
            op = Inv.lookupOpener( handlerName, [ reportData: reportData ]);
        } 
        catch(Throwable t) {;} 
        
        if ( op == null ) { 
            MsgBox.alert("No available handler found for "+ handlerName); 
            return null; 
        } 
        
        if ( reportData.receiptdate instanceof String ) { 
            // this is only true when txnmode is OFFLINE 
            try {
                def dateobj = YMD.parse( reportData.receiptdate ); 
                reportData.receiptdate = dateobj;  
            } catch( Throwable t ) {;} 
        } 
        return op; 
    } 
    
    void print() {
        def op = findReportOpener( entity ); 

        def handlerName = "cashreceipt-form:" + entity.formno; 
        def handle = findReportModel( op ); 
        if ( handle == null ) {
            MsgBox.alert("No available handler found for "+ handlerName); 

        } else {
            handle.viewReport(); 

            def canShowPrinterDialog = ( entity._options?.canShowPrinterDialog == false ? false : true ); 
            ReportUtil.print(handle.report, canShowPrinterDialog);
        }
    }
    
    private def findReportModel( o ) {
        if ( o == null ) return null; 
        else if (o instanceof ReportModel ) return o; 
        else if (o instanceof Opener) return findReportModel( o.handle ); 
        
        if ( o.metaClass.hasProperty(o, 'report' )) {
            return findReportModel( o.report ); 
        } else {
            return null; 
        }
    }    
    
    def getInfoHtml() {
        return TemplateProvider.instance.getResult( "com/rameses/enterprise/treasury/cashreceipt/cashreceipt.gtpl", [entity:entity] );
    }


    boolean isAllowCreateEntity() {
        return false; 
    }
    
    def createEntity() { 
        return null; 
    } 
    
    def doClose() {
        if( mainProcessHandler ) {
            mainProcessHandler.back();
        }
        else {
            return "_close";
        }
    }
    
}