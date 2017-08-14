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
    def barcode;
    def maxadvanceyear;
    def processing;
    def msg;
    def ledgers;
    def cancelled;
    
    def BARCODE_KEY = '56001';
    
    def init(){
        entity = [payoption:PAYOPTION_TAXPAYER, itemsperreceipt:5, totalcash:0.0, totalnoncash:0.0, amount:0.0];
        entity.showprinterdialog = true;
        bill = billSvc.initBill(null);
        entity.billid = bill.objid;
        maxadvanceyear = billSvc.getMaxAdvanceYear();
        mode = MODE_INIT;
        itemsforpayment = null;
        paymentlist = null;
        ledgers = null;
        return super.signal('init');
    }
    
    void initItems(){
        if (entity.payoption == PAYOPTION_TAXPAYER && !entity.taxpayer)
            throw new Exception('Taxpayer must be specified.');
        if (entity.payoption == PAYOPTION_BARCODE && !entity.barcodeid)
            throw new Exception('Barcode must be specified');
        
        mode = MODE_SELECT;
        listHandler.load();
        if (entity.payoption == PAYOPTION_TAXPAYER){
            bill.taxpayer = entity.taxpayer;
            updatePayerInfo(entity);
            ledgers = svc.getUnpaidPropertiesForPayment(bill);
            recalcItems();
        }
        else{
            initFromBarcode();
        }
    }
    
    void initFromBarcode(){
        def barcodeid = entity.barcodeid;
        def bid = barcodeid.startsWith(BARCODE_KEY) ? barcodeid : BARCODE_KEY+':'+barcodeid
        def param = [barcodekey:BARCODE_KEY , barcodeid:bid]; 
        bill = billSvc.getBillByBarcode(param);
        entity.billid = bill.objid 
        entity.taxpayer = bill.taxpayer;
        entity.collectiontype = bill.collectiontype;
        updatePayerInfo(entity);
        ledgers = bill.ledgers;
        recalcItems(); 
    }
    
    def oncomplete  = { 
        msg = null;
        if (!itemsforpayment){
            msg = 'There are no unpaid ledgers for this taxpayer'
        }
        listHandler.load();
        calcAmountDue();
        processing  = false;
        binding.refresh('.*');
    }
    
    def recalcItems(){
        processing = true;
        msg = 'Initializing. Please wait.'
        binding.refresh('msg.*');
        new Thread(task).start();
    }
    
    def task = [
        run : {
            try{
                recalcBillingStatement();
            }
            catch(e){
                //Task error
                e.printStackTrace();
            }
            oncomplete();
        }
    ]as Runnable;
    
    
    @PropertyChangeListener
    def listener = [
        'bill.billtoyear|bill.billtoqtr' : {
            try{
                svc.updateBill(bill)
                recalcItems();
            }
            catch(e){
                e.printStackTrace();
                itemsforpayment = []
                listHandler.load();
                calcAmountDue();
            }
        }
    ]
    
    void updatePayerInfo(entity){
        entity.paidby = entity.taxpayer.name;
        if (entity.taxpayer.address instanceof String)
            entity.paidbyaddress = entity.taxpayer.address;
        else 
            entity.paidbyaddress = entity.taxpayer.address.text;
    }
    
    def listHandler = [
        createItem : { return null },
            
        fetchList : { return itemsforpayment },
        
        onColumnUpdate : { item,colname ->
            bill.rptledgerid = item.rptledgerid;
            
            if (colname == 'pay' && item.pay == false){
                item.amount = 0.0;
                calcAmountDue();
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
    
    
    void buildBillParams(ledger){
        bill.rptledgerid = ledger.objid;
    }
        
    void doCancel(){
        cancelled = true;
        init();
    }
    
    void recalcBillingStatement(){
        def queue = new LinkedBlockingQueue();
        queue.poll(2, TimeUnit.SECONDS)
        cancelled = false;
        
        itemsforpayment = []
        def item = null; 
        if (!ledgers) return;
        for(int idx=0; !cancelled && idx < ledgers.size(); idx++){
            item = ledgers[idx];
            if (processing){
                msg = 'Recalculating TD ' + (item.tdno ? item.tdno : '') + '  (#' + (idx+1) + '). Please wait.';
                binding.refresh('msg');
            }
            try{
                buildBillParams(item)
                billSvc.generateBillByLedgerId3(bill)
                itemsforpayment << svc.getItemsForPayment(bill)[0]
            }
            catch(e){
                println 'ERROR -> ' + item.objid 
                println e.printStackTrace();
            }
        }
    }
    
    void loadItems(){
        bill.rptledgerid = null;
        try{
            itemsforpayment = svc.getItemsForPayment(bill);
        }
        catch(e){
            //
        }
        listHandler.load();
        calcAmountDue();
    }    
    

    void updateItemDue(item){
        item.partialled = false;
        bill.rptledgerid = item.rptledgerid;
        bill.billdate = entity.receiptdate;
        billSvc.generateBill(bill);
        bill.partial = [amount:0.0]
        item.putAll(svc.buildPaymentInfoByLedger(item, bill));
        listHandler.load();
        calcAmountDue();
    }    
    
    void calcAmountDue(){
        def paiditems = itemsforpayment.findAll{it.pay == true};
        entity.amount = 0.0;
        entity.itemcount = 0;
        if (paiditems){
            entity.amount = paiditems.amount.sum();
            entity.itemcount = paiditems.size();
        }
        binding?.refresh('entity.(itemcount|amount)');
    }
    
    void selectAll(){
        itemsforpayment.each{
            it.pay = true;
            it.partialled = false;
            it.amount = it._amount;
        }
        calcAmountDue();
    }
    
    void deselectAll(){
        itemsforpayment.each{
            it.pay = false;
            it.partialled = false;
            it._amount = 0.0;
            it.amount = 0.0;
        }
        listHandler.load();
        calcAmountDue();
    }
    
    def getQuarters(){
        return [1,2,3,4]
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
    
    /* paymentlist is a list of rptitems needed by rptcashreceipt */
    void buildPaymentList(){
        if (paymentlist) return;
        
        paymentlist = []
        def rptitems = []
        
        itemsforpayment.findAll{it.pay == true}.eachWithIndex{item, idx ->
            rptitems << item;
            if ((idx+1) % entity.itemsperreceipt == 0){
                paymentlist << rptitems;
                rptitems = []
            }
        }

        if (rptitems){
            paymentlist << rptitems;
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
        if (entity.itemsperreceipt < 1 || entity.itemsperreceipt > 5)
            throw new Exception('Items per Receipt must be between 1 and 5.');
        
        
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
                print(receipt);
                issuedreceipts << [objid:receipt.objid, receiptno:receipt.receiptno, amount:receipt.amount];
                receiptHandler.reload();
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
    
    def createReceipt(){
        receipt  = [
            txnmode         : 'ONLINE', 
            formno          : '56', 
            formtype        : 'serial',
            collectiontype  : findCollectionType(),
        ]; 
        
        receipt = receiptSvc.init(receipt)
        receipt.objid = 'BRPT' + new java.rmi.server.UID();
        receipt.billid = bill.objid;
        receipt.txntype = 'rptonline';
        receipt.payer = entity.taxpayer 
        receipt.paidby = entity.paidby
        receipt.paidbyaddress = entity.paidbyaddress
        receipt.rptitems = paymentlist[0];
        receipt.amount = receipt.rptitems.amount.sum();
        receipt.totalcash = receipt.amount;
        receipt.totalnoncash = 0.0;
        receipt.cashchange = 0.0;
        receipt.totalcredit = 0.0;
        return receipt;
    }
    
    def findCollectionType(){
        def p = [_schemaname:'collectiontype']
        p.findBy = [formno:'56', handler:'rptbatch', ]
        return qrySvc.findFirst(p)
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
                
            amount : selectedItem.amount,
                
            onpartial : { partial ->
                selectedItem.putAll(billSvc.applyPartialPayment([amount:partial], selectedItem));
                listHandler.load();
                calcAmountDue();
                binding.refresh('fullPayment|partialPayment')
            },
        ])
    }    
}

