package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 
import com.rameses.gov.etracs.rptis.util.RPTUtil;

public class RPUMachDetailModel
{
    @Binding
    def binding;
    
    //handlers
    def onadd
    def onupdate 
    
    def svc;
    
    def entity;
    def rpu;
    def machine;
    def machuse;
    def machdetail;
    def allowEdit;
    def mode; 
    def loading 
            
    def yearacquired
    def yearinstalled
    def operationyear 
    def estimatedlife            
            
    def imported        = 0
    def newlyinstalled  = 0
    def originalcost    = 0.0
    def freightcost     = 0.0
    def installationcost = 0.0
    def insurancecost   = 0.0
    def brokeragecost   = 0.0
    def arrastrecost    = 0.0
    def othercost       = 0.0
    def depreciation    = 0.0
    def autodepreciate  = 1
    String title = 'Machinery Information';
    
    
    @PropertyChangeListener
    def listener = [
        'machdetail.(useswornamount|swornamount)' : { recalc();},
        'rpu.dtappraised' : { recalc();},
    ]
            
    void create() { 
        mode = 'create' 
        createMachDetail()
    }
    
    void open() {
        mode = 'view';
        initInfo()
    }
    
    def save() {
        machdetail.machine = machine;
        if( machdetail.yearsused < 0 ) throw new Exception('Years Used must be more than or equal to zero.')
        if( mode == 'create' )
            onadd(machdetail);
        else 
            onupdate(machdetail);
        return close();
    }
    
    void initInfo(){
        loading = true;
        if(!machine){
            machine             = machdetail.machine
        }
        yearacquired        = machdetail.yearacquired
        yearinstalled       = machdetail.yearinstalled
        operationyear       = machdetail.operationyear
        estimatedlife       = machdetail.estimatedlife 
        conversionfactor    = machdetail.conversionfactor 
        
        newlyinstalled      = machdetail.newlyinstalled
        imported            = machdetail.imported
        autodepreciate      = machdetail.autodepreciate
        originalcost        = machdetail.originalcost
        freightcost         = machdetail.freightcost
        installationcost    = machdetail.installationcost
        insurancecost       = machdetail.insurancecost
        brokeragecost       = machdetail.brokeragecost
        arrastrecost        = machdetail.arrastrecost
        othercost           = machdetail.othercost
        depreciation        = machdetail.depreciation
        
        if (rpu.dtappraised){
            if (!entity.appraiser) {
                entity.appraiser = [:]
            }
            entity.appraiser.dtsigned = rpu.dtappraised
        }
        loading = false;
    }
    
    
    /*---------------------------------------------------------------
    *
    * MachDetail Support
    *
    ---------------------------------------------------------------*/
    def conversionfactor = 1.0
    
    void setConversionfactor( conversionfactor ) {
        if(conversionfactor == null || conversionfactor == 0.0) {
            conversionfactor = 1.0
        }
        this.conversionfactor = conversionfactor
        machdetail.conversionfactor = conversionfactor
        recalc()
    }
    

    
    void setYearacquired( yearacquired ) {
        this.yearacquired = yearacquired
        yearinstalled = yearacquired
        operationyear = yearacquired
        
        machdetail.yearacquired = yearacquired
        machdetail.yearinstalled = yearacquired
        machdetail.operationyear = yearacquired
        recalc()
    }
    
    void setYearinstalled( yearinstalled ) {
        this.yearinstalled = yearinstalled
        machdetail.yearinstalled = yearinstalled
        recalc()
    }
    
    void setOperationyear( operationyear ) {
        this.operationyear = operationyear
        machdetail.operationyear = operationyear 
        recalc()
    }
    
    void setEstimatedlife( estimatedlife ) {
        this.estimatedlife = estimatedlife
        machdetail.estimatedlife = estimatedlife 
        recalc()
    }
    
    

    
    void setAutodepreciate( autodepreciate ) {
        this.autodepreciate = autodepreciate
        machdetail.autodepreciate = autodepreciate
        recalc()
    }
    
    void setNewlyinstalled( newlyinstalled ) {
        this.newlyinstalled = newlyinstalled
        machdetail.newlyinstalled = newlyinstalled
        recalc()
    }
    
    void setImported( imported ) {
        this.imported = imported
        machdetail.imported = imported
        recalc()
    }   
    
    void setOthercost( othercost )  {
        this.othercost  = othercost
        machdetail.othercost = othercost
        recalc()
    }
    
    void setArrastrecost( arrastrecost ) {
        this.arrastrecost = arrastrecost
        machdetail.arrastrecost = arrastrecost
        recalc()
    }
    
    void setBrokeragecost( brokeragecost ) {
        this.brokeragecost = brokeragecost 
        machdetail.brokeragecost = brokeragecost
        recalc()
    }
    
    void setInsurancecost( insurancecost ) {
        this.insurancecost = insurancecost
        machdetail.insurancecost = insurancecost
        recalc()
    }
    
    void setInstallationcost( installationcost ) {
        this.installationcost = installationcost 
        machdetail.installationcost = installationcost
        recalc()
    }
    
    void setFreightcost( freightcost ) {
        this.freightcost = freightcost
        machdetail.freightcost = freightcost
        recalc()
    }
    
    void setOriginalcost( originalcost) {
        this.originalcost = originalcost
        machdetail.originalcost = originalcost
        recalc()
    }
    
    void setDepreciation( depreciation ) {
        this.depreciation = depreciation
        machdetail.depreciation = depreciation
        recalc()
    }
    
    
    
    void recalc() {
        if (loading) return;
        machdetail.putAll(svc.assessMachine(rpu, machuse, machdetail));
        initInfo();
        binding.refresh('.*');
    }
    
  
    void createMachDetail() {
        machdetail = [
            objid               : RPTUtil.generateId('MD'),
            machuseid           : machuse.objid,
            machrpuid           : rpu.objid,
            replacementcost     : 0.0,
            depreciation        : 0.0,
            depreciationvalue   : 0.0,
            basemarketvalue     : 0.0, 
            marketvalue         : 0.0, 
            assesslevel         : 0.0,
            assessedvalue       : 0.0,
            originalcost        : 0.0,
            freightcost         : 0.0,  
            insurancecost       : 0.0,
            installationcost    : 0.0,
            brokeragecost       : 0.0,
            arrastrecost        : 0.0,
            othercost           : 0.0,
            acquisitioncost     : 0.0,
            ferac               : 0.0,
            forex               : 0.0,
            residualrate        : 0.0,
            conversionfactor    : 0.0,
            useswornamount      : false,
            swornamount         : 0.0,
            imported            : false,
            newlyinstalled      : false,
            autodepreciate      : true,
            taxable             : true,
        ]
    }

    
    
    def close(){
        return '_close'
    }
}
