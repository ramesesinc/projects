package rptis.landtax.facts

import java.math.*


public class RPTLedgerFaasFact
{
    String objid 
    String tdno
    String classificationid
    String actualuseid
    Integer fromyear
    Integer fromqtr
    Integer toyear
    Integer toqtr
    Double assessedvalue

    def entity

    public RPTLedgerFaasFact(){
        this.fromyear = 0;
        this.fromqtr = 0;
        this.toyear = 0;
        this.toqtr = 0;
        this.assessedvalue = 0.0;
    }

    public RPTLedgerFaasFact(ledgerfaas){
        this.tdno = ledgerfaas.tdno
        this.classificationid = ledgerfaas.classification?.objid
        this.actualuseid = ledgerfaas.actualuse?.objid
        this.fromyear = ledgerfaas.fromyear;
        this.fromqtr = ledgerfaas.fromqtr;
        this.toyear = ledgerfaas.toyear;
        this.toqtr = ledgerfaas.toqtr;
        this.assessedvalue = ledgerfaas.assessedvalue;
    }
}
