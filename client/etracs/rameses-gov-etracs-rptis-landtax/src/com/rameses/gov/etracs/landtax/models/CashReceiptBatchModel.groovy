package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.util.*;
import java.util.concurrent.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.math.RoundingMode
import com.rameses.enterprise.treasury.util.CashReceiptPrintUtil;


class CashReceiptBatchModel extends PageFlowController
{
    @Binding
    def binding;
    
    @Service('RPTBillingService')
    def billSvc;
    
    @Service('AFControlService')
    def afControlSvc;

    @Service('CashReceiptService')
    def receiptSvc;
    
    @Service('RPTReceiptService')
    def svc;
    
    @Service('QueryService')
    def qrySvc;

    @Service('Var')
    def var;

    def mainProcessHandler;
    def afcontrol;
    
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
    def maxreceiptitemcount = 5;
    
    def BARCODE_KEY = '56001';

    def init(){
        loadMaxReceiptItemCount();
        entity.putAll([payoption:PAYOPTION_TAXPAYER, itemsperreceipt:maxreceiptitemcount, totalcash:0.0, totalnoncash:0.0, cashtendered:0.0, change:0.0, amount:0.0]);
        entity.confirmbeforeprint = true;
        entity.showprinterdialog = false;
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
        binding?.refresh('entity.*|msg|msgpnl');
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

    void loadItemsByTaxpayer(){
        if (mode == MODE_INIT || mode == MODE_SELECT) {
            processing = true;
            bill.taxpayer = entity.taxpayer;
            entity.paidby = bill.taxpayer.name;
            if (bill.taxpayer.address instanceof String) {
                entity.paidbyaddress = bill.taxpayer.address;
            } else {
                entity.paidbyaddress = bill.taxpayer.address.text;
            }
            loadItemsForPaymentAsync();
            calcReceiptAmount();
        }
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
        billedLedgers = bill.remove('ledgers');
        // itemsforpayment = bill.remove('ledgers');
        entity.taxpayer = bill.taxpayer 
        entity.paidby = bill.taxpayer.name;
        if (bill.taxpayer.address instanceof String) {
            entity.paidbyaddress = bill.taxpayer.address;
        } else {
            entity.paidbyaddress = bill.taxpayer.address.text;
        }
        entity.barcodeid = null;
        loadItemsForPaymentAsync();
        // calcReceiptAmount();
        // calcReceiptAmount();
        // binding.refresh('entity.barcodeid');
    }

    void reloadProperties() {
        try{
            loadItemsByTaxpayer();
        }
        catch(e){
            e.printStackTrace();
            itemsforpayment = [];
        }
        processing = false;
        listHandler.load();
        mode = MODE_SELECT;
        listHandler.load();
    }
    
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
    
    void updateItemDue(item){
        def tmpbill = cloneBill();
        tmpbill.rptledgerid = item.objid;
        def ledgers = svc.getItemsForPayment(tmpbill);
        def ledger = itemsforpayment.find{it.objid == item.objid}
        if (!ledger) throw new Exception('Expecting a ledger.')
        ledger.clear();
        ledger.putAll(ledgers.first());
        ledger.pay = true;
        bill.partial = [amount:0.0];
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

    def updateChange() {
        entity.change = 0;
        if (entity.totalcash + check.amount <= 0) {
            return;
        }
        entity.balance = entity.amount - check.amount;
        if (entity.cashtendered > entity.balance){
            entity.totalcash = entity.balance;
            entity.change = entity.cashtendered - entity.balance;
        }
        binding.refresh('entity.change');
    }
    
    void validate(){
        if (entity.itemsperreceipt < 1 || entity.itemsperreceipt > maxreceiptitemcount){
            throw new Exception('Items per Receipt must be between 1 and ' + maxreceiptitemcount + '.');
        }
        if (entity.totalcash + check.amount != entity.amount) {
            throw new Exception('Total cash and check must be more or equal to ' + entity.amount + '.');
        }
    }
    
    
    def save(){
        //break when receipts are create
        //handle consumption of receipt, therefore, activition
        validate();
        buildPaymentList();
        
        mode = MODE_PAYMENT;

        entity.cashbalance = entity.totalcash;
        entity.checkbalance = check.amount;
        
        while(true){
            try{
                createReceipt();
                receipt.putAll(receiptSvc.post(receipt));
                if (entity.confirmbeforeprint) {
                    MsgBox.alert('Insert Receipt No. ' + receipt.receiptno + ' into the printer.');
                }
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
                def afSelected = false;
               def onselect = { o-> 
                    afControlSvc.activateSelectedControl([ objid: o.objid ]);
                    afSelected = true;
                }
                Modal.show('cashreceipt:select-af', [entity: receipt, onselect: onselect]);
                if (!afSelected) {
                    break;
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
                throw new Exception(ex);
            } 
            catch(BreakException be) { 
                return null;
            }
        }
        
        if (mode == MODE_PAID){
            binding.fireNavigation(super.signal('done'));
        }
        
        return 'payment'
    }

    def doClose() {
        if( mainProcessHandler ) {
            mainProcessHandler.back();
        }
        else {
            return "_close";
        }
    }

    def doCancel() {
        return doClose();
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
        
        receipt.sharing = [] 
        receipt.items = []
        receipt.ledgers.each {
            receipt.items += it.billitems 
        }
        buildPaymentInfo(receipt)
        return receipt;
    }
    
    void print(receipt) {
        def u = new CashReceiptPrintUtil( binding: binding ); 
        u.showPrinterDialog = entity.showprinterdialog;

        def template_name = afcontrol?.afunit?.cashreceiptprintout; 
        if ( !template_name ) {
            template_name = "cashreceipt-form:" + entity.formno; 
        }
        u.print( template_name, receipt );
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


    /*============================================
    *
    * payment support
    *
    ============================================*/

    def check = [amount: 0];

    void clearPayments() {
        entity.totalcash = 0;
        check.amount = 0;
        entity.change = 0;
        entity.cashtendered = 0;
        updateChange();
        binding.refresh('entity.*cash.*|check.*|add.*');
    }

    def addCash() {
        def onadd = {
            entity.cashtendered = it.cash;
            entity.change = it.change;
            entity.totalcash = it.cash - it.change;
            updateChange();
            binding.refresh('entity.*cash.*|add.*');
        }
        return Inv.lookupOpener('cashreceipt:payment-cash', [saveHandler: onadd]);
    }

    def addCheck() {
        def onsave = {chk ->
            check = chk;
            entity.totalnoncash = check.amount - check.amtused;
            updateChange();
            binding.refresh('entity.*cash.*|check.*|add.*');
        }

        return Inv.lookupOpener('cashreceipt:payment-check', [saveHandler: onsave, fundList: [], exitOnSplitCheck: true]);
    }

    void buildPaymentInfo(receipt) {
        if (entity.cashbalance > 0 && entity.checkbalance == 0) {
            //cash payment only
            receipt.totalcash = receipt.amount;
            receipt.totalnoncash = 0.0;
        } else if (entity.cashbalance == 0 && entity.checkbalance > 0) {
            //check payment only
            receipt.totalnoncash = receipt.amount;
            receipt.totalcash = 0.0;
            receipt.paymentitems = createCheckPayments(receipt)
        } else {
            // cash and checkpayment
            buildMixPaymentInfo(receipt)
        }
        receipt.change = 0.0;
        receipt.totalcredit = 0.0;
    }

    /*=================================================
    * for mix payment:
    *       if taxdue is less than both cash and check 
    *              distribute cash first
    *       then distribute the remaining cash 
    *           for partial (with remaining cash)
    *              distribute check proportionately
    ==================================================*/
    void buildMixPaymentInfo(receipt) {
        if (entity.cashbalance >= receipt.amount) {
            entity.cashbalance =  round(entity.cashbalance - receipt.amount);
            receipt.totalcash = receipt.amount;
            receipt.totalnoncash = 0.0;
        } else {
            receipt.totalcash = entity.cashbalance;
            receipt.totalnoncash = round(receipt.amount - receipt.totalcash);
            entity.cashbalance = 0.0;
            println 'receipt.totalcash => ' + receipt.totalcash;
            println 'receipt.totalnoncash => ' + receipt.totalnoncash;
            receipt.paymentitems = createCheckPayments(receipt)
        }
    }


    def createCheckPayments(receipt) {
        def pmts = [];

        def grpbyfunds = receipt.items.groupBy{ it.item.fund.objid } 
        def fundtotals = getFundTotals(receipt, grpbyfunds)
        println 'fundtotals => ' + fundtotals;

        grpbyfunds.each{fundid, items ->
            def pmt = [:]
            pmt.objid = 'PMT' + new java.rmi.server.UID();
            pmt.check = check;
            pmt.receiptid = receipt.objid;
            pmt.refid = check.objid;
            pmt.refno = check.refno;
            pmt.refdate = check.refdate;
            pmt.reftype = 'CHECK';
            pmt.amount = fundtotals[fundid].total;
            pmt.voidamount = 0.0;
            pmt.particulars = check.refno + ' (' + check.bank?.name + ') dated ' + check.refdate ;
            pmt.fund = [objid: fundid]
            pmts << pmt;
        }
        return pmts;
    }

    def getFundTotals(receipt, grpbyfunds) {
        def fundtotals = [:]
        grpbyfunds.each{fundid, items ->
            fundtotals[fundid] = [total: items.amount.sum()];
        }
        if (receipt.totalcash > 0) {
            //adjust fund totals proportionally (partial)
            def partial = receipt.amount - receipt.totalcash;
            def runningtotal = 0.0;
            def idx = 0;
            fundtotals.each{ fundid, fund ->
                ++idx;
                if (idx == grpbyfunds.size()) {
                    fund.total = round(partial - runningtotal);
                } else {
                    fund.total = round(partial * fund.total / receipt.amount);
                }
                runningtotal = round(runningtotal + fund.total);
            }

            if (runningtotal != receipt.totalnoncash){
                println 'fundTotals ==================';
                fundtotals.each{ 
                    println '   >>> ' + it;
                }
                throw new Exception('Total check payment items does not match check amount.');
            }
        }

        return fundtotals;
    }

    def round(amount) {
        if( amount instanceof Number ) amount = format('0.00000000',amount)
        def bd = new BigDecimal( amount )
        return bd.setScale(2, RoundingMode.HALF_UP)
    }

    def format( pattern, value ) {
        if( ! value ) value = 0
        def df = new DecimalFormat( pattern )
        return df.format( value )
    }

    void loadMaxReceiptItemCount() {
        def cnt = var.get('landtax_receipt_item_printout_count');
        if (cnt) {
            try {
                maxreceiptitemcount = (new BigDecimal(cnt.toString())).intValue();
            } catch( e ) {
                //ignore 
            }
        }
        println 'maxreceiptitemcount => ' + maxreceiptitemcount;
    }
}

