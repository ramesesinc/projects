package com.rameses.gov.etracs.landtax.report.taxclearance;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.etracs.shared.*;

class TaxClearanceController
{
    @Binding
    def binding;
    
    @Service('ReportParameterService')
    def paramSvc;
    
    @Service('LandTaxReportTaxClearanceService')
    def svc;
    
    @Service('DateService')
    def dtSvc;
    
    @Service('Var')
    def var 
    
    
    def MODE_CREATE = 'create';
    def MODE_PREVIEW = 'preview';
    
    def entity;
    def mode;
    def addoption = 'bytd';
    def lookup;
    def taxpayer;
    def address;
    def selectedItem;
    
    String title = 'Realty Tax Clearance'
            
                
    @PropertyChangeListener
    def listener = [
        'entity.year|entity.qtr|taxpayer' : {
            loadProperties()
        },
    ]
    
    def init(){
        entity = svc.initClearance()
        entity.certifiedby = var.get('LANDTAX_CERTIFIEDBY');
        entity.certifiedbytitle = var.get('LANDTAX_CERTIFIEDBY_TITLE');
        entity.office = 'landtax';
        entity.ordate = dtSvc.getServerDate();
        entity.oramount = toDecimal(var.get('LANDTAX_TAXCLEARANCE_RATE'));
        listHandler.reload();
        mode = MODE_CREATE;
        return 'default'
    }
    
    def doNew(){
        init();
        initTaxpayer(taxpayer);
        return 'default'
    }
    
    def open(){
        entity = svc.openClearance(entity.objid)
        return preview();
    }
    
    def save(){
        if (MsgBox.confirm('Save clearance?')){
            def includeditems = entity.items.findAll{ it.included == true};
            if (!includeditems) throw new Exception('At least one property must be included.');
            entity.items = includeditems;
            entity = svc.createClearance(entity);
            mode = MODE_PREVIEW;
            return preview();
        }
        return null;
    }
    
    def getLookupTaxpayer(){
        return InvokerUtil.lookupOpener('entity:lookup',[
                onselect : {
                    initTaxpayer(it)
                },
                onempty : {
                    entity.taxpayer = null;
                    entity.requestedby = null;
                    entity.requestedbyaddress = null;
                    entity.items = [];
                    listHandler.load();
                    binding.refresh('entity.taxpayeraddress|entity.requested.*')
                },
        ])
    }
    
    void initTaxpayer(tpay){
        if (!taxpayer || taxpayer.objid != tpay.objid){
            this.taxpayer = tpay;
            this.address = tpay.address
        }
        entity.taxpayer = taxpayer;
        entity.taxpayer.address = address.text;
        entity.requestedby = taxpayer.name;
        entity.requestedbyaddress = address.text;
        binding.refresh('entity.taxpayer.*|entity.requested.*')
        loadProperties();
    }
    
    def getLookupLedger(){
        return InvokerUtil.lookupOpener('rptledger:lookup', [
            taxpayerid : entity.taxpayer.objid,
            state      : 'APPROVED',
                
            onselect : { ledger ->
                if (! entity.items.find{it.faasid == ledger.faasid}) {
                    ledger.refid   = ledger.objid
                    ledger.barangay = ledger.barangay.name;
                    ledger.rptcertificationid = entity.objid;
                    ledger.included = true; 
                    entity.items.add(ledger);
                    listHandler.load();
                }
            },
        ])
    }
    
    def listHandler = [
        fetchList : { return entity.items },
    ] as EditorListModel
    
    
    void setAddoption(addoption){
        this.addoption = addoption;
        loadProperties();
    }
    
    
    void loadProperties(){
        if (addoption == 'all'){
            entity.items = svc.getClearedLedgers(entity)
            entity.items.each{
                it.rptcertificationid = entity.objid;
                it.included = true;
            }
            listHandler.load();
        }
    }
    
    
    
    void selectAll(){
        entity.items.each{
            it.included = true;
        }
        listHandler.load();
    }
    
    void deselectAll(){
        entity.items.each{
            it.included = false;
        }
        listHandler.load();
    }
    
    
    def preview(){
        report.viewReport();
        mode = MODE_PREVIEW;
        return 'preview'
    }
    
    def reportpath = 'com/rameses/gov/etracs/landtax/report/taxclearance/'
    def report = [
        getReportName : { return reportpath + 'taxclearance.jasper' },
        getSubReports : { 
            return [
                new SubReport( 'taxclearanceitem', reportpath + 'taxclearanceitem.jasper' ),
                new SubReport( 'taxclearanceitempayment', reportpath + 'taxclearanceitempayment.jasper' ),
            ] as SubReport[]
        },
        getReportData : { return entity },
        getParameters : { 
            def params = paramSvc.getStandardParameter()
            params.LOGOLGU = EtracsReportUtil.getInputStream("lgu-logo.png")
            params.BACKGROUND = EtracsReportUtil.getInputStream("background.png")
            return params 
        }
    ] as ReportModel
            
            
            
    List getQuarters(){
        return [1,2,3,4]
    }
    
    def toDecimal(val){
        if (val == null) return 0.0;
        try{
            return new BigDecimal(val.toString());
        }
        catch(e){
            return 0.0;
        }
    }
}
