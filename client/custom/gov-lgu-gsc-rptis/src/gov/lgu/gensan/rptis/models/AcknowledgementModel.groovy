package gov.lgu.gensan.rptis.models;
        
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.common.*;

public class AcknowledgementModel extends CrudFormModel
{
    @Service('FAASService')
    def faasSvc;

    @Service('DateService')
    def dtSvc;

    @Service('Var')
    def var;

    @Service('GensanAcknowledgementService')
    def svc;
    
    @Service('RPTTrackingService')
    def trackingSvc;

    @Service('PersistenceService')
    def persistence;

    boolean showConfirm = false;
    def selectedItem;
    
    def types;

    boolean isEditAllowed() {
        if (entity.state != 'APPROVED') {
            return super.isEditAllowed();
        } else {
            def isAdmin = false;
            OsirisContext.env.ROLES.each{k,v ->
                if (k == 'RPT.RECEIVER_ADMIN'){
                    isAdmin = true;
                }
            }
            return isAdmin;
        }
    }
    
    def getTxntypes() {
        if (!types) {
            types = faasSvc.getTxnTypes();
        }
        return types;
    }
    
    def getReleaseModes() {
        return LOV.RPT_RELEASING_MODES*.key
    }
    
    public void afterCreate(){
        entity.state = 'DRAFT';
        entity.releasedate = calcReleaseDate();
        entity.receivedby = OsirisContext.env.FULLNAME;
    }

    public void afterOpen(){
        entity.txntype = getTxntypes().find{it.objid == entity.txntype?.objid}
    }

    def calcReleaseDate() {
        def numdays = var.getProperty('assessor_acknowledgement_receipt_numdays', 9);
        return (dtSvc.getServerDate() + numdays);
    }

    void approve() {
        if (MsgBox.confirm('Approve?')) {
            entity._schemaname = 'rptacknowledgement';
            entity.state = 'APPROVED';
            persistence.update(entity);
            reload();
        }
    }
    
    void addItems() {
        def scount = MsgBox.prompt('Enter number of items to add:');
        if (scount) {
            try {
                int count = new BigDecimal(scount.toString()).intValue();
                if (count > 0) {
                    svc.addItems([objid: entity.objid, count: count]);
                    reloadEntity();
                }
            } catch(e) {
                throw new Exception('Invalid number value.');
            }
        }
    }

    public def getPrintFormData() {
        entity.landcount = entity.items.findAll{it.ref.rputype == 'land'}.size()
        entity.bldgcount = entity.items.findAll{it.ref.rputype == 'bldg'}.size()
        entity.machcount = entity.items.findAll{it.ref.rputype == 'mach'}.size()
        entity.planttreecount = entity.items.findAll{it.ref.rputype == 'planttree'}.size()
        entity.misccount = entity.items.findAll{it.ref.rputype == 'misc'}.size()
        return entity; 
    }
    
    def getLogs() {
        if (selectedItem == null) {
            return [];
        } else if (selectedItem.newfaas && selectedItem.newfaas.objid) {
            return trackingSvc.getLogs(selectedItem.newfaas);
        } else {
            return trackingSvc.getLogs(selectedItem.ref);
        }
    }
    
    def logListHandler = [
        fetchList: { getLogs() },
    ] as BasicListModel



    public void afterColumnUpdate(String name, def item, String colName ) {
        item.refno = item.ref?.tdno;
        item.reftype = 'faas';
    }
}


