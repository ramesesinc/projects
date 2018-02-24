package rptis.landtax.facts;

public class AssessedValue
{
    String objid
    Classification classification 
    Classification actualuse 
    String rputype
    String txntype 
    Integer year
    Double av
    Double basicav
    Double sefav
    Boolean taxdifference 

    public AssessedValue(){}

    public AssessedValue(item){
        this.objid = item.objid
        this.classification = item.classification
        this.actualuse = item.actualuse
        this.rputype = item.rputype 
        this.txntype = item.txntype 
        this.year = item.year
        this.av = item.av
        this.basicav = item.basicav
        this.sefav = item.sefav
        this.taxdifference = (item.taxdifference ? item.taxdifference : false)
    }
}
