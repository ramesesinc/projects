package rptis.landtax.facts;

import java.math.*;

public class RPTLedgerItemFact 
{
    String objid
    RPTLedgerFact rptledger
    String rptledgeritemid 
    Boolean firstitem
    Integer year
    Integer qtr
    Double originalav
    Double av
    Double basicav
    Double sefav
    String txntype
    String classification
    String actualuse
    Integer numberofmonthsfromqtr
    Integer numberofmonthsfromjan
    Boolean backtax
    String expr
    Boolean idleland
    Integer paidyear
    String revperiod
    Boolean reclassed
    Double basic
    Double basicint
    Double basicdisc
    Double basicidle
    Double basicidledisc
    Double basicidleint
    Double basicnet
    Double sef
    Double sefint
    Double sefdisc
    Double sefnet
    Double totalbasicsef
    Double firecode
    Double total
    Integer idx   
    Boolean taxdifference 
    Boolean fullpayment
    Boolean qtrlypaymentavailed
    Boolean fullypaid

    def entity;

    public RPTLedgerItemFact(){}

    public RPTLedgerItemFact(ledgerfact, item, idx){
        this.entity = item

        this.objid = item.objid
        this.rptledger = ledgerfact
        this.rptledgeritemid = item.rptledgeritemid 
        this.firstitem = item.firstitem
        this.year = item.year
        this.qtr = item.qtr
        this.originalav = item.originalav
        this.av = item.av
        this.basicav = item.basicav
        this.sefav = item.sefav
        this.txntype = item.txntype?.objid 
        this.classification = item.classification?.objid 
        this.actualuse = item.actualuse?.objid
        this.numberofmonthsfromqtr = item.numberofmonthsfromqtr
        this.numberofmonthsfromjan = item.numberofmonthsfromjan
        this.backtax = (item.backtax == 1 || item.backtax ? true : false )
        this.reclassed = (item.reclassed == 1 || item.reclassed ? true : false )
        this.idleland = (item.idleland == 1 || item.idleland ? true : false )
        this.paidyear = item.paidyear
        this.taxdifference = (item.taxdifference == 1 || item.taxdifference ? true : false )
        this.fullpayment = (item.fullpayment != null ? item.fullpayment : false)
        this.fullypaid = (item.fullypaid != null ? item.fullypaid : false)
        this.qtrlypaymentavailed = (item.qtrlypaymentavailed != null ? item.qtrlypaymentavailed : false)

        setBasic(item.basic)
        setBasicint(item.basicint)
        setBasicdisc(item.basicdisc)
        setBasicnet(item.basicnet)
        setBasicidle(item.basicidle)
        setBasicidledisc(item.basicidledisc)
        setBasicidleint(item.basicidleint)
        setSef(item.sef)
        setSefint(item.sefint)
        setSefdisc(item.sefdisc)
        setSefnet(item.sefnet)
        setTotalbasicsef(item.totalbasicsef)
        setFirecode(item.firecode)
        setTotal(item.total)
        this.idx = idx 
    }


    void setBasic(basic){
        if (basic == null) basic = 0.0
        this.basic = basic
        entity.basic = new BigDecimal(basic+'')
    }

    void setBasicint(basicint){
        if (basicint == null) basicint = 0.0
        this.basicint = basicint
        entity.basicint = new BigDecimal(basicint+'')
    }

    void setBasicdisc(basicdisc){
        if (basicdisc == null) basicdisc = 0.0
        this.basicdisc = basicdisc
        entity.basicdisc = new BigDecimal(basicdisc+'')
    }

    void setBasicnet(basicnet){
        if (basicnet == null) basicnet = 0.0
        this.basicnet = basicnet
        entity.basicnet = new BigDecimal(basicnet+'')
    }

    void setBasicidle(basicidle){
        if (basicidle == null) basicidle = 0.0
        this.basicidle = basicidle
        entity.basicidle = new BigDecimal(basicidle+'')
    }

    void setBasicidledisc(basicidledisc){
        if (basicidledisc == null) basicidledisc = 0.0
        this.basicidledisc = basicidledisc
        entity.basicidledisc = new BigDecimal(basicidledisc+'')
    }

    void setBasicidleint(basicidleint){
        if (basicidleint == null) basicidleint = 0.0
        this.basicidleint = basicidleint
        entity.basicidleint = new BigDecimal(basicidleint+'')
    }

    void setSef(sef){
        if (sef == null) sef = 0.0
        this.sef = sef
        entity.sef = new BigDecimal(sef+'')
    }

    void setSefint(sefint){
        if (sefint == null) sefint = 0.0
        this.sefint = sefint
        entity.sefint = new BigDecimal(sefint+'')
    }

    void setSefdisc(sefdisc){
        if (sefdisc == null) sefdisc = 0.0
        this.sefdisc = sefdisc
        entity.sefdisc = new BigDecimal(sefdisc+'')
    }

    void setSefnet(sefnet){
        if (sefnet == null) sefnet = 0.0
        this.sefnet = sefnet
        entity.sefnet = new BigDecimal(sefnet+'')
    }

    void setTotalbasicsef(totalbasicsef){
        if (totalbasicsef == null) totalbasicsef = 0.0
        this.totalbasicsef = totalbasicsef
        entity.totalbasicsef = new BigDecimal(totalbasicsef+'')
    }

    void setFirecode(firecode){
        if (firecode == null) firecode = 0.0
        this.firecode = firecode
        entity.firecode = new BigDecimal(firecode+'')
    }

    void setTotal(total){
        if (total == null) total = 0.0
        this.total = total
        entity.total = new BigDecimal(total+'')
    }

}
