package com.rameses.gov.etracs.rpt.report.certification;


import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.etracs.shared.*;

public abstract class AbstractCertificationController 
{
    @Service('ReportParameterService')
    def paramSvc;
    
    @Service('Var')
    def var

    @Service('DateService')
    def dtSvc;
    
    @Binding 
    def binding;
    
    @Invoker
    def inv;
    
    def opener;

    def MODE_CREATE = 'create';
    def MODE_SELECT = 'select';
    def MODE_READ   = 'read';
    
    def entity;
    def mode;
    
    def certificationTypes;
    boolean showasofyear = false;

    
    abstract def getService();
    abstract String getReportName();
    
        
    def getReportData(){
        return entity;
    }
    
    SubReport[] getSubReports() {
        return null;
    }
    
    Map getParameters(){
        return [:];
    }

    
    def getTitle(){
        return 'Certification of ' + inv.caption 
    }
    
    def createEntity(){
        def map = [:]
        def types = certificationTypes
        map.certtype = (types ? types[0] : null)
        return map;
    }
    
    void afterInit(){}
    
    def init() {
         entity = createEntity();
        entity.objid            = RPTUtil.generateId('RC');
        entity.opener           = (inv.properties.opener ? inv.properties.opener : opener);
        entity.certifiedby      = var.get("ASSESSORCERTIFIEDBY");
        entity.certifiedbytitle = var.get("ASSESSORCERTIFIEDBYTITLE");
        entity.byauthority      = var.get("ASSESSORBYAUTHORITY");
        entity.byauthoritytitle = var.get("ASSESSORBYAUTHORITYTITLE");
        entity.purpose          = var.get("TDTRUECOPYPURPOSE");
        if (!entity.purpose){
            entity.purpose = "whatever legal purposes it may serve him/her"; 
        }
            
        entity.asofyear         = dtSvc.getServerYear();
        entity.oramount         = 0.0;
        entity.stampamount      = 0.0;
        entity.office           = 'assessor';
        entity.official         = false;
        officialuse             = false;
        afterInit();
        mode = MODE_CREATE;
        binding?.refresh('entity.*');
        return 'default'
    }
    
    def getCertificationTypes(){
        return [
            [type:'byfaas', caption:'By FAAS'],
            [type:'bytaxpayer', caption:'By Taxpayer'],
        ]
    }
    
    def open(){
        entity = service.openCertification(entity.objid);
        opener = entity.opener;
        mode = MODE_READ;
        return doPreview();
    }

    void afterLookupTaxpayer(){
        
    }
    
    def getLookupTaxpayer(){
        return InvokerUtil.lookupOpener('entity:lookup',[
            onselect : { 
                entity.taxpayer = [objid:it.objid, name:it.name, address:it.address.text];
                entity.requestedby = it.name;
                entity.requestedbyaddress = it.address.text;
                afterLookupTaxpayer();
                binding.refresh('entity.taxpayer.*');
                binding.requestFocus('entity.taxpayer');
            },
            onempty  : { 
                entity.taxpayer = null;
                entity.requestedby = null;
                entity.requestedbyaddress = null;
            },
        ])
    }
    
    
    def getLookupFaas(){
        return InvokerUtil.lookupOpener('faas:lookup',[
            onselect : { 
                if (it.state != 'CURRENT' && it.state != 'CANCELLED'){
                    throw new Exception('FAAS is not current or cancelled.')
                }
                entity.faasid = it.objid;
                entity.tdno= it.tdno;
                entity.taxpayer = it.taxpayer;
                entity.requestedby = it.taxpayer.name;
                entity.requestedbyaddress = it.taxpayer.address;
            },
            onempty  : { 
                entity.faasid = null;
                entity.tdno= null;
                entity.taxpayer = null;
                entity.requestedby = null;
                entity.requestedbyaddress = null;
            },
        ])
    }
       
    def officialuse = false;

    void setOfficialuse( officialuse ){
        this.officialuse = officialuse;
        entity.official = officialuse;
        entity.orno = ( officialuse ) ? 'Official Use' : '';
        entity.ordate = ( officialuse ) ? null : entity.serverDate;
        entity.oramount = ( officialuse ) ? 0.0 : entity.oramount;
        entity.stampamount = ( officialuse ) ? 0.0 : entity.stampamount;
    }


    def report =[
        getReportName : { return getReportName() },
        getSubReports : { return getSubReports() },
        getReportData : { return getReportData() },
        getParameters : { 
            def params = paramSvc.getStandardParameter() + getParameters(); 
            params.GIVENAT = params.LGUADDRESS
            params.LOGOLGU = EtracsReportUtil.getInputStream("lgu-logo.png")
            params.LOGOBLGF = EtracsReportUtil.getInputStream("lgu-blgf.png")
            params.LOGOASSESSOR = EtracsReportUtil.getInputStream("lgu-assessor.png")            
            return params 
        } 
    ] as ReportModel;

    
    void beforeSave(){}
    
    def save(){
        beforeSave();
        return service.createCertification( entity )
    }
    
    public def doSave(){
        def retval = null
        if (MsgBox.confirm('Save certification?')) {
            entity.putAll(save());
            mode = MODE_READ;
            retval = doPreview();
        }
        return retval;
    }
    
    
    def doPreview(){
        report.viewReport();
        return "preview";
    }

}