package rptis.landtax.facts;

public class ShareInfoFact 
{
    RPTLedgerFact rptledger 
    String lgutype  
    String sharetype  
    String revperiod 
    String barangayid 
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
    Double sh       
    Double shint    
    Double shdisc     

    public ShareInfoFact(){}

    public ShareInfoFact(params){
        this.rptledger = params.taxsummary.rptledger 
        this.barangayid = params.taxsummary.rptledger.barangayid 
        this.lgutype = params.lgutype  
        this.sharetype = params.sharetype
        this.revperiod = params.taxsummary.revperiod
        this.basic = 0.0
        this.basicint = 0.0
        this.basicdisc = 0.0
        this.basicidle = 0.0
        this.basicidledisc = 0.0
        this.basicidleint = 0.0
        this.sef = 0.0
        this.sefint = 0.0
        this.sefdisc = 0.0
        this.firecode = 0.0
        this.sh = 0.0
        this.shint = 0.0
        this.shdisc = 0.0
    }
}
