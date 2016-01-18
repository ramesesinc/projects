package com.rameses.gov.treasury.batchcapture.batch; 

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
        
public class BatchCaptureController  {

    @Binding 
    def binding;    

    @Service("CollectionTypeService")
    def collectionTypeSvc;

    @Service("BatchCaptureCollectionService")
    def svc;
    
    @Service("DateService")
    def dtsvc

    
    def mode;
    def entity;
    def selectedItem;

    def formTypes;
    def formType
    def collectionTypes;
    def collectiontype;
    def lookupexpression;

    def copyprevinfo
    
    def onPost; //handler
            
    String getTitle(){
        def suffix = ''
        if (entity.state)
            suffix = '(' + entity.state + ')'
        return 'Batch Capture ' + suffix 
    }
    

    @PropertyChangeListener
    def listener = [
         "formType": { o->
            entity=[:]
            entity.txnmode = 'CAPTURE'
            entity.formno = o.objid;
            entity.formtype = o.formtype;
            collectionTypes = collectionTypeSvc.findAllByFormNoForBatch([formno: o.objid]);
        },
        "collectiontype": { o->
            entity.collectiontype = o;
         }
    ]
    
    def getAfserialHandler() {
        if(! entity.formno) throw new Exception("Please an accountable form first. ")
        return InvokerUtil.lookupOpener("afserialcapture:lookup",[
                "query.formno" : entity.formno,
                onselect:{ m ->
                    entity.collector = m.collector;
                    entity.capturedby = m.subcollector;
                    entity.stub = m.stub;
                    entity.formtype='serial'
                    entity.controlid = m.controlid;
                    entity.prefix = m.prefix
                    entity.suffix = m.suffix
                    entity.serieslength = m.serieslength
                    entity.startseries = m.startseries
                    entity.endseries = m.endseries 
                    entity.sstartseries = formatSeries(m.startseries)
                    entity.sendseries = formatSeries(m.endseries)
                    entity.currentseries = m.currentseries
                    lookupexpression = entity.sstartseries + " - " +  entity.sendseries  
                }]);
    }    
        
    def getLookupAccount() {
        return InvokerUtil.lookupOpener("cashreceiptitem:lookup", [ 
            "query.collectorid": entity.collector.objid,
            "query.collectiontype": selectedItem.collectiontype,
            "query.fund" : entity.collectiontype.fund,
            "query.collectiontype": entity.collectiontype,                
            onselect: { o->
                if(selectedItem.items == null ) selectedItem.items = [];
                selectedItem.items.clear();
                selectedItem.items << [item: o, fund:o.fund];
                selectedItem.acctinfo = o.title;
            }
        ]);
    }
            
    void calculate() {
        entity.totalcash = 0.0
        entity.totalnoncash = 0.0
        entity.totalamount = 0.0
        entity.batchitems.each { 
            if( it.voided != 1 ) { 
                entity.totalcash += it.totalcash
                entity.totalnoncash += it.totalnoncash
                entity.totalamount += it.amount 
            }   
        }
        binding?.refresh('entity.*'); 
    }
     
    def prevEntity = [:]

    def listModel = [
        fetchList: { o->
            return entity.batchitems;
        },
        createItem: {
            if( entity.currentseries > entity.endseries)
                    throw new Exception("Current Series already exceeds the end series.  ");

            def m  = [:];
            m.objid = "BCCE" + new java.rmi.server.UID();
	    m.parentid = entity.objid
            m.series = getNextSeries();
            m.receiptno =  formatSeries(m.series);
            m.receiptdate = entity.defaultreceiptdate;
            m.collectiontype = entity.collectiontype
            m._filetype = "batchcapture:misc"
            m.totalcash = 0.0
            m.totalnoncash = 0.0
            m.amount = 0.0 
            m.collector = entity.collector;
            m.paymentitems = []
            m.voided = 0
            m.newitem = true
            if( copyprevinfo ) {
                if(prevEntity ) {
                    if( prevEntity.items && prevEntity.items.size() == 1  ) {
                        def item =  prevEntity.items[0].clone();
                        item.amount = 0.0 
                        m.items = [ item ]
                        m.acctinfo = prevEntity.acctinfo; 
                    }   
                    m.receiptdate = prevEntity.receiptdate
                    m.paidbyaddress = prevEntity.paidbyaddress;
                } 
            }        
            return m;
        },

        getOpenerParams: {o-> 
            return [
                callerListModel: listModel, 
                calculateHandler: {  en, mode ->    
                    calculate(); 
                    if( mode == 'edit') {
                        svc.addUpdateItem(entity, en) 
                     }   
                } 
            ]; 
        },
        onAddItem: { o->
            validateItem( o );
            prevEntity = o.clone();
            
            entity.batchitems << o 
            moveNext()
        },
        
        isColumnEditable:{item, colname-> 
            if (colname != 'amount') return true;
            if (!item.items) return false; 
            return (item.items.size() == 1); 
        },
        onColumnUpdate: {item, colname-> 
            if( colname == 'receiptdate' ) {
                 def serverdate = dtsvc.parse("yyyy-MM-dd", dtsvc.format( "yyyy-MM-dd", dtsvc.getServerDate())) 
                 if( dtsvc.parse("yyyy-MM-dd", item.receiptdate).after( serverdate) )  throw new Exception("Receipt Date must not greater than the current date.  ");
            }
            
            if (colname == 'amount') {
                item.items[0].amount = item[colname]; 
                item.totalcash = item.amount
                item.totalnoncash = 0.0
                
            }
            if( colname == 'voided') {
                calculate()
                svc.addUpdateItem(entity, item)
            }
        },
        onCommitItem: { o-> 
            if( o.newitem && o.voided != 1 ) {
                entity.totalcash += o.totalcash
                entity.totalnoncash += o.totalnoncash
                entity.totalamount += o.amount 
            } else{ calculate() } 

            svc.addUpdateItem(entity, o)
            if( o.newitem ) { o.newitem = false }
            binding.refresh("entity.totalcash|entity.totalnoncash|entity.totalamount")
        },
        
        onRemoveItem: { o ->
            if( entity.batchitems.indexOf(o) != (entity.batchitems.size()-1)) return false;
            if(! MsgBox.confirm('Remove item? ')) return false;
            
            if( o.voided != 1) {
                entity.totalcash -= o.totalcash
                entity.totalnoncash -= o.totalnoncash
                entity.totalamount -= o.amount 
            }    
            svc.removeItem(o, entity)    
            entity.currentseries -= 1;   
            entity.batchitems.remove(o);
            binding.refresh("entity.totalcash|entity.totalnoncash|entity.totalamount")
            return true;
        }
        
    ] as EditorListModel;
    
    void validateItem( o ) {
        if(! o.receiptdate) throw new Exception("Rct Date is required.   ")
        
        def serverdate = dtsvc.parse("yyyy-MM-dd", dtsvc.format( "yyyy-MM-dd", dtsvc.getServerDate())) 
        if( dtsvc.parse("yyyy-MM-dd", o.receiptdate).after( serverdate) )  throw new Exception("Receipt Date must not be greater than the current date.  ");
        
        if(! o.paidby) throw new Exception("Paid By is required.   ")
        if(! o.paidbyaddress) throw new Exception("Address is required.   ")
        if(! o.acctinfo) throw new Exception("Account is required.   ")
        if(o.amount == null) throw new Exception("Amount is required.   ")
        if(o.amount <= 0.0 ) throw new Exception("Amount must not less than or equal to zero.   ")
        
        
    }

    public String formatSeries( series ) {
        def p = (entity.prefix)?entity.prefix:'';
        def s = (entity.suffix)?entity.suffix:'';
        return p + series.toString().padLeft(entity.serieslength, '0') + s;
    }

    public int getNextSeries() {
        return entity.currentseries;
    }

    public void moveNext() {
        entity.currentseries += 1;
        binding.refresh("entity.currentseries");
    }
    
    /********************************************************
      FormActions 
    ********************************************************/
    
    public void init() { 
        formTypes = collectionTypeSvc.getFormTypesForBatch();
        entity = [:]
        mode='init'
    }   

    def open() {
        entity = svc.open(entity)
        entity.sstartseries = formatSeries(entity.startseries);
        entity.sendseries = formatSeries(entity.endseries);
        mode = 'create'
        return 'main';
    }
    
    def next() {
        entity = svc.initBatchCapture(entity); 
        copyprevinfo = false 
        mode='create'
        return 'main'; 
    }

    def delete(){
        if (MsgBox.confirm('Delete record?')){
            svc.removeBatchCapture([objid:entity.objid]);
            return '_close';
        }
        return null;
    }
    
    void submitForPosting() {
        if (MsgBox.confirm('Submit captured receipts for posting?')){
            calculate() 
            entity = svc.submitForPosting( entity);
            listModel.reload();
        }
    }
    
    void disapprove(){
        entity = svc.disapproved( entity);
        listModel.reload();
    }

    void post() {
        if(!MsgBox.confirm("You are about to post this captured collection. Continue? ")) return;

        entity = svc.post( entity);
        if (onPost) onPost();
    }

    void submitForOnlineRemittance() {
        if(!MsgBox.confirm("You are about to submit this captured collection for online remittance. Continue? ")) return;

        mode = 'submittedforremittance'
        svc.submitForOnlineRemittance( entity )
        if (onPost) onPost();
    }

}