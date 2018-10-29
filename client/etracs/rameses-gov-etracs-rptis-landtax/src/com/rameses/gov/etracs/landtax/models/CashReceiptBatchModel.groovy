package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.util.*;
import java.util.concurrent.*;


class RPTReceiptBatchModel extends PageFlowController
{
    @Binding
    def binding;
    
    @Service('RPTBillingService')
    def billSvc;
    
    @Service('CashReceiptService')
    def receiptSvc;
    
    @Service('RPTReceiptService')
    def svc;
    
    @Service('QueryService')
    def qrySvc;
    
    def MODE_INIT = 'init';
    def MODE_SELECT = 'select';
    def MODE_PAYMENT = 'payment';
    def MODE_PAID = 'paid';
    
    def PAYOPTION_TAXPAYER = 'taxpayer';
    def PAYOPTION_BARCODE = 'barcode';
    
    def entity;
    def mode;
    def bill;
    def itemsforpayment;
    def barcodeid;
    def maxadvanceyear;
    def processing;
    def msg;
    def cancelled;
    def quarters = [1,2,3,4];
    
    def BARCODE_KEY = '56001';
    
    def init(){
        entity.putAll([payoption:PAYOPTION_TAXPAYER, itemsperreceipt:5, totalcash:0.0, totalnoncash:0.0, amount:0.0]);
        entity.showprinterdialog = true;
        bill = billSvc.initBill();
        bill._forpayment = true;
        itemsforpayment = [];
        paymentlist = [];
        mode = MODE_INIT;
        return super.signal('init');
    }
    
    void initItems(){
        if (entity.payoption == PAYOPTION_TAXPAYER && !entity.taxpayer)
            throw new Exception('Taxpayer must be specified.');
        if (entity.payoption == PAYOPTION_BARCODE && !entity.barcodeid)
            throw new Exception('Barcode must be specified');
        
        if (entity.payoption == PAYOPTION_TAXPAYER){
            loadItemsByTaxpayer();
        }
        else{
            loadItemsByBarcode();
        }
        processing = false;
        listHandler.load();
        mode = MODE_SELECT;
    }

    void loadItemsByTaxpayer(){
        processing = true;
        bill.taxpayer = entity.taxpayer;
        entity.paidby = bill.taxpayer.name;
        entity.paidbyaddress = bill.taxpayer.address.text;
        loadItemsForPaymentAsync();
        calcReceiptAmount();
    }

    /*===================================
    *
    * Async Support
    *
    ====================================*/

    def task;
    def billedLedgers;

    def onLoadComplete() {
        processing = false;
        msg = null;
        calcReceiptAmount();
        binding.refresh('entity.*|msg|msgpnl');
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
        msg = 'Processing items for payment. Please wait...';
        itemsforpayment = [];
        new Thread(loadLedgerTask).start();
    }    
    
    void loadItemsByBarcode(){
        def res = svc.initReceiptFromBarcode([barcodeid:entity.barcodeid]);
        bill = res.remove('bill');
        itemsforpayment = bill.remove('ledgers');
        entity.taxpayer = bill.taxpayer 
        entity.paidby = bill.taxpayer.name;
        entity.paidbyaddress = bill.taxpayer.address;
        calcReceiptAmount();
        entity.barcodeid = null;
        binding.refresh('entity.barcodeid');
    }
    
    @PropertyChangeListener
    def listener = [
        'bill.billtoyear|bill.billtoqtr' : {
            try{
                loadItemsByTaxpayer();
            }
            catch(e){
                e.printStackTrace();
                itemsforpayment = [];
            }
            listHandler.load();
        }
    ]
    
    def listHandler = [
        createItem : { return null },
            
        fetchList : { return itemsforpayment },
        
        onColumnUpdate : { item,colname ->
            bill.rptledgerid = item.objid;
            
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


    void validateToYear(item){
        if (item.toyear < item.fromyear){
            throw new Exception('To Year must be greater than or equal to From Year.');
        }
        
        def maxadvanceyear = bill.cy + bill.maxadvanceyear;
        if (item.toyear > maxadvanceyear){
            throw new Exception('To year must not be greater than the Max Allowed Advance Year of ' + maxadvanceyear + '.' )
        }
        
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
    
    void doCancel(){
        cancelled = true;
        init();
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
    
    void calcReceiptAmount(){
        def paiditems = itemsforpayment.findAll{it.pay == true};
        entity.amount = 0.0;
        entity.itemcount = 0;
        if (paiditems){
            entity.amount = paiditems.total.sum();
            entity.itemcount = paiditems.size();
        }
        binding?.refresh('entity.(itemcount|amount)|msg|msgpnl');
    }
    
    void selectAll(){
        itemsforpayment.each{
            it.pay = true;
            it.partialled = false;
            updateItemDue(it);
        }
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
    
    void backToSelect(){
        paymentlist = null;
        mode = MODE_SELECT;
    }
    

/*======================================================
* PAYMENT SUPPORT 
* ========================================================*/
    def issuedreceipts = []
    def selectedReceipt;
    def paymentlist;
    def receipt;
    def selectedItem;
    
    void initPayment(){
        if (entity.amount <= 0.0)
            throw new Exception('Amount must be greater than zero.')
        mode = MODE_PAYMENT;
    }
    
    /* paymentlist is a list of ledgers needed by rptcashreceipt */
    void buildPaymentList(){
        if (paymentlist) return;
        
        paymentlist = []
        def ledgers = []
        
        itemsforpayment.findAll{it.pay == true}.eachWithIndex{item, idx ->
            ledgers << item;
            if ((idx+1) % entity.itemsperreceipt == 0){
                paymentlist << ledgers;
                ledgers = []
            }
        }

        if (ledgers){
            paymentlist << ledgers;
        }
    }
    
    def receiptHandler = [
        getRows : { issuedreceipts.size() + 1 },
        fetchList: { return issuedreceipts },
    ] as BasicListModel
    
    def getChange(){
        entity.cashchange = 0;
        entity.balance = entity.amount - entity.totalnoncash;
        if (entity.totalcash >= entity.balance){
            entity.cashchange = entity.totalcash - entity.balance;
        }
        else if(entity.totalcash > 0.0 ){
            entity.totalcash = 0.0 ;
            binding.refresh('entity.totalcash');
            MsgBox.alert('Cash Tendered must be more than or equal to ' + entity.balance + '.');
        }
        else{
            throw new Exception('Cash tendered must be specified.');
        }
        return entity.cashchange;
    }
    
    void validate(){
        if (entity.itemsperreceipt < 1 || entity.itemsperreceipt > 5){
            throw new Exception('Items per Receipt must be between 1 and 5.');
        }
        getChange();
    }
    
    
    def save(){
        //break when receipts are create
        //handle consumption of receipt, therefore, activition
        validate();
        buildPaymentList();
        
        mode = MODE_PAYMENT;
        
        def selectaf = false;
        
        while(true){
            try{
                createReceipt();
                receipt.putAll(receiptSvc.post(receipt));
                MsgBox.alert('Insert Receipt No. ' + receipt.receiptno + ' into the printer.');
                issuedreceipts << [objid:receipt.objid, receiptno:receipt.receiptno, amount:receipt.amount];
                receiptHandler.reload();
                print(receipt);
                paymentlist.remove(0);
                if (!paymentlist) {
                    mode = MODE_PAID;
                    break;
                }
            }
            catch(Warning w) { 
                selectaf = true;
                break;
            }
            catch(Exception ex){
                ex.printStackTrace();
                throw new Exception(ex);
            } 
            catch(BreakException be) { 
                return null;
            }
        }
        
        if (selectaf){
            return InvokerUtil.lookupOpener('cashreceipt:select-afcontrol', [entity: receipt]);
        }
        
        if (mode == MODE_PAID){
            binding.fireNavigation(super.signal('done'));
        }
        
        return 'payment'
    }

    def doClose() {
        return '_exit';
    }
    
    def createReceipt() {
        receipt  = [
            txnmode         : 'ONLINE', 
            formno          : entity.formno, 
            formtype        : 'serial',
            collectiontype  : entity.collectiontype,
        ]; 
        
        receipt = receiptSvc.init(receipt)
        receipt.objid = 'BRPT' + new java.rmi.server.UID();
        receipt.billid = bill.objid;
        receipt.txntype = 'online';
        receipt.payer = entity.taxpayer 
        receipt.paidby = entity.paidby
        receipt.paidbyaddress = entity.paidbyaddress
        receipt.ledgers = paymentlist[0];
        receipt.amount = receipt.ledgers.total.sum();
        receipt.totalcash = receipt.amount;
        receipt.totalnoncash = 0.0;
        receipt.cashchange = 0.0;
        receipt.totalcredit = 0.0;

        receipt.sharing = [] 
        receipt.items = []
        receipt.ledgers.each {
            receipt.sharing += it.shares 
            receipt.items += it.billitems 
        }
        return receipt;
    }
    
    void print(receipt) {
        def bc = new com.rameses.enterprise.treasury.cashreceipt.BasicCashReceipt();
        bc.entity = receipt
        bc.entity._options = [canShowPrinterDialog:entity.showprinterdialog]
        bc.print();
    }    
    
    def getReceiptcount(){
        return issuedreceipts.size();
    }
    
    void fullPayment(){
        bill.partial = null
        updateItemDue(selectedItem);
        selectedItem.partialled = false;
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
        b.billdate = bill.currentdate;
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
}

