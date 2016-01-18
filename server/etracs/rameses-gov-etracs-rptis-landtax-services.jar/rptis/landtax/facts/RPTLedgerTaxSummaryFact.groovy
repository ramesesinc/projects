package rptis.landtax.facts;

import java.math.*;

public class RPTLedgerTaxSummaryFact 
{
    String  objid          
    RPTLedgerFact rptledger      
    String  revperiod      
    Double basic          
    Double basicint       
    Double basicdisc      
    Double basicidle      
    Double basicidledisc
    Double basicidleint      
    Double sef            
    Double sefint         
    Double sefdisc        
    Double firecode       

    public RPTLedgerTaxSummaryFact(){}

    public RPTLedgerTaxSummaryFact(params){
        this.objid = params.var.key 
        this.rptledger = params.rptledgeritem.rptledger 
        this.revperiod = params.revperiod 
        this.basic  = 0.0
        this.basicint = 0.0
        this.basicdisc = 0.0
        this.basicidle = 0.0 
        this.basicidledisc = 0.0 
        this.basicidleint = 0.0 
        this.sef = 0.0
        this.sefint = 0.0
        this.sefdisc = 0.0
        this.firecode = 0.0
    }
}
