package com.rameses.gov.etracs.landtax.models;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rptis.util.*;


class CashReceiptModel extends com.rameses.enterprise.treasury.cashreceipt.AbstractCashReceipt implements ViewHandler
{
    @Binding
    def binding;
    
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
    
    String entityName = "cashreceipt_rpt"
    
    def bill;
    def mode;
    def payoption;
    def barcodeid;
    def loadedBarcodes = [];
    def itemsforpayment;
    def quarters = [1,2,3,4];
    def barcodeprocessing = false;
    def processing = false;
    def msg;
    def billedLedgers = [];

                
    
    void init(){
        super.init();
        itemsforpayment = [];
        entity.txntype = 'online';
        entity.amount = 0.0;
        clearAllPayments();
        bill = billSvc.initBill();
        bill.itemcount = 5;
        bill.billdate = entity.receiptdate 
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
        bill._forpayment = true;
        entity.billid = bill.objid; 
        billedLedgers = [];
        if (payoption != PAY_OPTION_BYLEDGER){
            loadItemsForPaymentAsync()
            return 'default';
        } else {
            listHandler?.load();
            mode = MODE_CREATE;
            return 'main'
        }
    }    

    /*===================================
    *
    * Async Support
    *
    ====================================*/

    def task;

    def onLoadComplete() {
        processing = false;
        msg = null;
        binding.fireNavigation('main');
    }


    def loadLedgerTask = {
        run: {
            if (!billedLedgers) {
                billedLedgers = svc.getLedgersForPayment(bill);    
            }
            billedLedgers.each {
                try {
                    msg = 'Processing ledger ' + (it.rptledger ? it.rptledger.tdno : '...');
                    loadItemByLedger(it)
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
            onLoadComplete();
        }
    } as Runnable;

    

    void loadItemsForPaymentAsync() {
        processing = true;
        msg = 'Loading items for payment. Please wait...';
        itemsforpayment = [];
        new Thread(loadLedgerTask).start();
    }
    
    
    public void validateBeforePost() {
        super.validateBeforePost()
        entity.ledgers = itemsforpayment.findAll{it.pay == true}
    }    
    
    void calcReceiptAmount(){
        def paiditems = itemsforpayment.findAll{it.pay == true};
        entity.amount = 0.0;
        if (paiditems){
            entity.amount = paiditems.total.sum();
            entity.sharing = [] 
            entity.items = []
            paiditems.each {
                entity.sharing += it.shares 
                entity.items += it.billitems 
            }
        }
        updateBalances();
        binding?.refresh('totalGeneral|totalSef|entity.*')
    }    
    
    
    def initBarcode(){
        entity = svc.loadBarcode([barcodeid:barcodeid]);
        super.init();
        bill = entity.remove('bill');
        billedLedgers = bill.remove('ledgers');
        loadedBarcodes << barcodeid;
        clearAllPayments();
        calcReceiptAmount();
        payoption = PAY_OPTION_BYLEDGER;
        mode = MODE_INIT;
        barcodeprocessing = true;
        barcodeid = null;
        loadItemsForPaymentAsync();
        return 'default';
    }    
    
    void mergeBarcode(){
        if (!barcodeid) return;
        if (loadedBarcodes.find{it.matches('.*'+barcodeid +'.*')}) {
            clearBarcodeId();
            return;
        }
        def items = svc.getItemsForPaymentByBarcode([barcodeid:barcodeid]);
        items.each{item -> 
            def exist = itemsforpayment.find{it.objid == item.objid}
            if (! exist) {
                itemsforpayment << item 
            }
        }
        calcReceiptAmount();
        listHandler.load();
        loadedBarcodes << barcodeid;
        clearBarcodeId();
    }

    void clearBarcodeId(){
        barcodeid = null;
        binding?.refresh('barcodeid|selectedItem');
        binding?.focus('barcodeid');
    }
    
    
    
    /*==================================================
    *
    * MAIN PAGE SUPPORT 
    *
    =====================================================*/
    def selectedItem;
    
    def listHandler = [
        createItem : { return null },
            
        fetchList : { return itemsforpayment },
        
        onColumnUpdate : { item,colname ->
            bill.rptledgerid = item.objid;
            
            if (colname == 'pay' && item.pay == false){
                item.total = 0.0;
                item.amount = 0.0;
                calcReceiptAmount();
            }
            else if (colname == 'pay' && item.pay == true){
                fullPayment();
                calcReceiptAmount();
            } else {
                if (colname == 'toyear') {
                    validateToYear(item);
                }
                else{ 
                    validateToQtr(item);
                }

                if (item.fromyear == item.toyear && item.toqtr < item.fromqtr){
                    item.toqtr = item.fromqtr;
                }
                    
                bill.billtoyear = item.toyear;
                bill.billtoqtr = item.toqtr;
                
                updateItemDue(item);
            }
            
        },
            
        validate : {li -> 
            validateToYear(li.item);
            validateToQtr(li.item);
        },
    ] as EditorListModel
    
    
    void validateToYear(item){
        if (item.toyear < item.fromyear)
            throw new Exception('To Year must be greater than or equal to From Year.');
            
        def maxyear = (bill.cy + bill.maxadvanceyear)
        if (item.toyear > maxyear)
            throw new Exception('To year must not be greater than the Max Allowed Advance Year of ' + maxyear + '.' )
        
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
        
    
    void updateItemDue(item){
        def tmpbill = cloneBill();
        tmpbill.rptledgerid = item.objid;
        def ledgers = svc.getItemsForPayment(tmpbill);
        def ledger = itemsforpayment.find{it.objid == item.objid}
        if (!ledger) throw new Exception('Expecting a ledger.')
        ledger.clear();
        ledger.putAll(ledgers.first());
        bill.partial = [amount:0.0]
        listHandler.load();
        calcReceiptAmount();
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
    
                
    void loadItemByLedger(rptledger){
        if ( !itemsforpayment.find{it.objid == rptledger.objid}){
            def tmpbill = cloneBill();
            tmpbill.rptledgerid = rptledger.objid;
            def ledgers = svc.getItemsForPayment(tmpbill);
            itemsforpayment += ledgers;
            listHandler.load();
            calcReceiptAmount();
        }
        binding.focus('ledger');
    }
        
    void fullPayment() {
        bill.billtoyear = bill.cy 
        bill.billtoqtr = 4
        selectedItem.toyear = bill.cy 
        selectedItem.toqtr = 4
        selectedItem.pay = true;
        selectedItem.partialled = false;
        updateItemDue(selectedItem);
        listHandler.load();
        calcReceiptAmount();
    }
    
    void selectAll(){
        itemsforpayment.each{
            it.pay = true;
            it.partialled = false;
            if (payoption != PAY_OPTION_ALL){
                updateItemDue(it);
            }
        }
        listHandler.load();
        calcReceiptAmount();
    }
    
    void deselectAll(){
        itemsforpayment.each{
            it.pay = false;
            it.partialled = false;
            it.total = 0.0;
        }
        listHandler.load();
        calcReceiptAmount();
    }
    
    
     def partialPayment(){
        return InvokerUtil.lookupOpener('rptpartialpayment:open', [
                
            amount : selectedItem.total,
                
            onpartial : { partial ->
                def tmpbill = cloneBill();
                tmpbill.rptledger = selectedItem;
                tmpbill.partial = [amount:partial];
                selectedItem.putAll(svc.applyPartialPayment(tmpbill));
                listHandler.load();
                calcReceiptAmount();
                binding.refresh('fullPayment|partialPayment');
            },
        ])
    }
    
    def cloneBill(){
        def b = [:];
        b.objid = bill.objid; 
        b.billid = bill.billid;
        b.billdate = entity.receiptdate;
        b.taxpayer = (bill.taxpayer ? bill.taxpayer : entity.payer);
        b.billtoyear = bill.billtoyear;
        b.billtoqtr = bill.billtoqtr;
        b.ledgers = [];
        b.totals = [:];
        b.advancebill = bill.advancebill;
        b.currentdate = bill.currentdate;
        b.cy = bill.cy;
        b.cqtr = bill.cqtr;
        b.cmonth = bill.cmonth;
        b.barcode = bill.barcode;
        b.maxadvanceyear = bill.maxadvanceyear;
        return b;
    }
    
    
    /*==================================================
    *
    * viewhandler implementation
    *
    =====================================================*/
    void activatePage(binding, pagename){
        
    }
    
    void afterRefresh(binding, pagename){
        binding.requestFocus('ledger');
    }
    
    def getRpuCount() {
        return itemsforpayment.size();
    }
        
}

