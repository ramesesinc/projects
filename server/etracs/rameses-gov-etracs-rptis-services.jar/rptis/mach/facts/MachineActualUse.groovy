package rptis.mach.facts;

import rptis.facts.*;
import java.math.*;

public class MachineActualUse {
    RPU rpu
    String actualuseid 
    Double basemarketvalue
    Double marketvalue
    Double assesslevel
    Double assessedvalue
    Boolean taxable 

    //data reference
    def entity    

    public MachineActualUse(){}

    public MachineActualUse(rpufact, mu){
        this.rpu = rpufact
        this.actualuseid  = mu.actualuse.objid 
        this.entity       = mu 
        this.taxable = true

        setBasemarketvalue(mu.basemarketvalue)
        setMarketvalue(mu.marketvalue)
        setAssesslevel(mu.assesslevel)
        setAssessedvalue(mu.assessedvalue)
    }

    void setBasemarketvalue( basemarketvalue ){
        this.basemarketvalue = basemarketvalue
        entity.basemarketvalue = new BigDecimal(basemarketvalue+'')
    }

    void setMarketvalue( marketvalue ){
        this.marketvalue = marketvalue
        entity.marketvalue = new BigDecimal(marketvalue+'')
    }

    void setAssesslevel( assesslevel ){
        this.assesslevel = assesslevel
        entity.assesslevel = new BigDecimal(assesslevel+'')
    }

    void setAssessedvalue( assessedvalue ){
        this.assessedvalue = assessedvalue
        entity.assessedvalue = new BigDecimal(assessedvalue+'')
    }

}

