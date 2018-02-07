package com.rameses.gov.etracs.rpt.collection.ui;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rptis.util.*;


class RPTReceiptController extends com.rameses.enterprise.treasury.cashreceipt.AbstractCashReceipt implements ViewHandler
{
    @Binding
    def binding;
    
    @Service("CashReceiptService")
    def cashReceiptSvc;
                
    @Service('RPTReceiptService')
    def svc;
    
    @Service('RPTBillingService')
    def billSvc;
    
    @Service('ReportParameterService')
    def paramSvc 
    
    def MODE_INIT           = 'init';
    def MODE_CREATE         = 'create';
    def MODE_READ           = 'read';
    
    def PAY_OPTION_ALL      = 'all';
    def PAY_OPTION_BYLEDGER = 'byledger';
    def PAY_OPTION_BYCOUNT  = 'bycount';
    
    def mode;
    def payoption;
    def bill;
    def openledgers;
    def itemsforpayment;
    def barcodeid;
    def barcode;
    def barcodeprocessing = false;
    def maxadvanceyear = null;
            
    String entityName = "cashreceipt_rpt"
    
    def BARCODE_KEY = '56001'
    
    void init(){
        super.init();
        itemsforpayment = [];
        entity.txntype = 'rptonline';
        entity.amount = 0.0;
        clearAllPayments();
        bill = billSvc.initBill(null);
        bill.itemcount = 5;
        entity.billid = bill.objid; 
        maxadvanceyear = billSvc.getMaxAdvanceYear()
        mode = MODE_INIT;
        payoption = PAY_OPTION_ALL;
    }
    
    
    /*-----------------------------------------------------------------
     *
     * INIT PAGE SUPPORT
     *
     -----------------------------------------------------------------*/
    
    def process(){
        RPTUtil.required("Payer", entity.payer);
        bill.taxpayer = entity.payer;
        bill.payoption = payoption;
        entity.billid = bill.objid; 
        if (payoption == PAY_OPTION_ALL || payoption == PAY_OPTION_BYCOUNT){
            recalcBillingStatement();
            loadItems();
        }
        mode = MODE_CREATE;
        return 'main'
    }
    
    void buildBillParams(ledger){
        bill.rptledgerid = ledger.objid;
        bill.billdate   = entity.receiptdate;
        
        if (!entity.txnmode.equalsIgnoreCase('ONLINE')){
            bill.forceRecalcBill = true;
        }
    }
    
    def getLookupLedger(){
        return InvokerUtil.lookupOpener('rptledger:lookup',[

            onselect : {ledger ->
                if (ledger.state != 'APPROVED')
                    throw new Exception('Only approve ledger is allowed.')
                if (bill.ledgers.find{it.objid == ledger.objid})
                    throw new Exception('Ledger has already been added.')
                loadItemByLedger(ledger)
            },
        ])
    }
    
    public void validateBeforePost() {
        super.validateBeforePost()
        entity.rptitems = itemsforpayment.findAll{it.pay == true}
    }
    
    def selectedItem;
    
    def listHandler = [
        createItem : { return null },
            
        fetchList : { return itemsforpayment },
        
        onColumnUpdate : { item,colname ->
            bill.rptledgerid = item.rptledgerid;
            
            if (colname == 'pay' && item.pay == false){
                item.amount = 0.0;
                calcReceiptAmount();
            }
            else {
                if (colname == 'toyear') {
                    validateToYear(item);
                }
                else{ 
                    validateToQtr(item);
                }

                if (item.fromyear == item.toyear && item.toqtr < item.fromqtr)
                    item.toqtr = item.fromqtr 
                    
                bill.billtoyear = item.toyear;
                bill.billtoqtr = item.toqtr;
                
                updateItemDue(item)
            }
            
        },
            
        validate : {li -> 
            validateToYear(li.item);
            validateToQtr(li.item);
        },
    ] as EditorListModel
                
                
    void recalcBillingStatement(){
        def ledgers = svc.getUnpaidPropertiesForPayment(bill);
        if (!ledgers) 
            throw new Exception('There are no unpaid properties for this owner.')
        ledgers.each{
            buildBillParams(it)
            billSvc.generateBill(bill)
        }
    }
        
    def initBillFromBarcode(){
        def bid = barcodeid.startsWith(BARCODE_KEY) ? barcodeid : BARCODE_KEY+':'+barcodeid
        def param = [barcodekey:BARCODE_KEY , barcodeid:bid]; 
        bill = billSvc.buildBillFromBarcode(param);
        entity.billid = bill.objid 
        entity.payer = bill.taxpayer;
        entity.collectiontype = bill.collectiontype;
        loadItems();     
        return bill;
    }     
    
    /* add ledgers for the scanned bill */
    void processBarcode(){
        if (!barcode) return;
        def param = [barcodekey:BARCODE_KEY , barcodeid:barcode]; 
        billSvc.mergeBillBarcode(bill, param)
        loadItems();     
        barcode = null;
        binding.refresh('barcode|selectedItem');
        binding.focus('barcode');
    }
    
    def initBarcode(){
        entity = [formtype: "serial", formno:"56", txnmode: 'ONLINE', txntype:'rptonline', amount:0.0];
        itemsforpayment = [];
        clearAllPayments();
        
        initBillFromBarcode()
        entity = cashReceiptSvc.init( entity );
        entity.payer = bill.taxpayer;
        entity.paidby = bill.taxpayer.name;
        entity.paidbyaddress = bill.taxpayer.address;
        super.init();
        bill.billdate   = entity.receiptdate;
        listHandler.load();
        calcReceiptAmount();
        payoption = PAY_OPTION_BYLEDGER;
        mode = MODE_CREATE;
        barcodeprocessing = true;
        return 'main'
    }
    
    void loadItems(){
        bill.rptledgerid = null;
        itemsforpayment = svc.getItemsForPayment(bill);
        listHandler.load();
        calcReceiptAmount();
    }
       
            
    void loadItemByLedger(rptledger){
        if ( ! itemsforpayment.find{it.rptledgerid == rptledger.objid}){
            buildBillParams([objid:rptledger.objid])
            itemsforpayment << svc.buildPaymentInfoByLedger(rptledger, bill);
            listHandler.load();
            calcReceiptAmount();
        }
        binding.focus('ledger');
    }

    void updateItemDue(item){
        item.partialled = false;
        bill.rptledgerid = item.rptledgerid;
        bill.billdate = entity.receiptdate;
        billSvc.generateBill(bill);
        bill.partial = [amount:0.0]
        item.putAll(svc.buildPaymentInfoByLedger(item, bill));
        listHandler.load();
        calcReceiptAmount();
    }
    
        
    void validateToYear(item){
        if ( maxadvanceyear == null)
            maxadvanceyear = billSvc.getMaxAdvanceYear()
            
        if (item.toyear < item.fromyear)
            throw new Exception('To Year must be greater than or equal to From Year.');
            
        if (item.toyear > maxadvanceyear)
            throw new Exception('To year must not be greater than the Max Allowed Advance Year of ' + maxadvanceyear + '.' )
        
        item.toqtr = 4;
    }
        
    void validateToQtr(item){
        if (item.toqtr < 1 ){
            item.toqtr = 4;
            throw new Exception('To Quarter must be greater than or equal to 1.');
        }
        if (item.toqtr > 4 ){
            item.toqtr = 4;
            throw new Exception('To Quarter must be less than or equal to 4.');
        }
        if (item.toyear == item.fromyear && item.toqtr < item.fromqtr){
            item.toqtr = 4;
            throw new Exception('To Quarter must be greater than or equal to From Quarter.')
        }
            
    }
    
    
    
    
    void selectAll(){
        itemsforpayment.each{
            it.pay = true;
            it.partialled = false;
            if (payoption != PAY_OPTION_ALL){
                updateItemDue(it);
            }
        }
        if (payoption == PAY_OPTION_ALL) {
            loadItems();
        }
        calcReceiptAmount();
    }
    
    void deselectAll(){
        itemsforpayment.each{
            it.pay = false;
            it.partialled = false;
            it.amount = 0.0;
        }
        listHandler.load();
        calcReceiptAmount();
    }
    
    
    
    
    void fullPayment(){
        bill.partial = null
        updateItemDue(selectedItem);
        selectedItem.partialled = false;
    }
    
    def partialPayment(){
        return InvokerUtil.lookupOpener('rptpartialpayment:open', [
                
            amount : selectedItem.amount,
                
            onpartial : { partial ->
                selectedItem.putAll(billSvc.applyPartialPayment([amount:partial], selectedItem));
                listHandler.load();
                calcReceiptAmount();
                binding.refresh('fullPayment|partialPayment')
            },
        ])
    }
    
    
    void calcReceiptAmount(){
        def paiditems = itemsforpayment.findAll{it.pay == true};
        entity.amount = 0.0;
        if (paiditems){
            entity.amount = paiditems.amount.sum();
        }
        updateBalances();
        binding?.refresh('totalGeneral|totalSef|entity.*')
    }
    
    
    
    //viewhandler implementation
    void activatePage(binding, pagename){
        
    }
    
    void afterRefresh(binding, pagename){
        binding.requestFocus('ledger');
    }
    
    
    def printDetail(){
        return InvokerUtil.lookupOpener('rptreceipt:printdetail',[entity:entity])
    }
    
    
    def getTotalGeneral(){
        return itemsforpayment.findAll{it.pay == true}.totalgeneral.sum()
    }
    
    def getTotalSef(){
        return itemsforpayment.findAll{it.pay == true}.totalsef.sum()
    }
    
    def getQuarters(){
        return [1,2,3,4]
    }

            
}

