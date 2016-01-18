package rptis.landtax.facts;

public class AssessedValueFact 
{
    RPTLedgerFact rptledger
    Integer year
    Double assessedvalue
    Double qtrlyav

    def entity    

    public AssessedValueFact(){}

    public AssessedValueFact(ledgerfact, item){
        this.entity         = item
        this.rptledger      = ledgerfact
        this.year           = item.year
        this.assessedvalue  = item.assessedvalue
        this.qtrlyav        = item.qtrlyav
    }

}
