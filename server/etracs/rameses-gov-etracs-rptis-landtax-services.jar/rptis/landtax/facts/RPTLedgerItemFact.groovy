package rptis.landtax.facts;

import java.math.*;


public class RPTLedgerItemFact 
{
    String objid
    String parentid 
    RPTLedgerFact rptledger
    String rptledgerfaasid
    String rptledgeritemid 
    Boolean firstitem
    Integer year
    Integer qtr
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
    Double basicpaid 
    Double basicint
    Double basicdisc
    Double basicidle
    Double basicidlepaid 
    Double basicidledisc
    Double basicidleint
    Double sef
    Double sefpaid 
    Double sefint
    Double sefdisc
    Double firecode
    Double firecodepaid
    Double sh
    Double shpaid 
    Double shint
    Double shdisc
    Integer idx   
    Boolean taxdifference 
    Boolean fullpayment
    Boolean qtrlypaymentavailed
    Boolean qtrly 

    def entity

    def qtrlyitems

    public RPTLedgerItemFact(){
        this.basic = 0.0
        this.basicpaid = 0.0
        this.basicint = 0.0
        this.basicdisc = 0.0
        this.basicidle = 0.0
        this.basicidlepaid = 0.0
        this.basicidledisc = 0.0
        this.basicidleint = 0.0
        this.sef = 0.0
        this.sefpaid = 0.0
        this.sefint = 0.0
        this.sefdisc = 0.0
        this.firecode = 0.0
        this.firecodepaid = 0.0
        this.sh = 0.0
        this.shpaid = 0.0
        this.shint = 0.0
        this.shdisc = 0.0
        this.qtrlyitems = []
    }

    public RPTLedgerItemFact(ledgerfact, item, idx){
        this()
        this.entity = item
        this.objid = item.objid
        this.parentid = null 
        this.rptledger = ledgerfact
        this.rptledgerfaasid = item.rptledgerfaasid
        this.rptledgeritemid = item.rptledgeritemid 
        this.firstitem = item.firstitem
        this.year = item.year
        this.qtr = item.qtr
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
        this.qtrlypaymentavailed = (item.qtrlypaymentavailed != null ? item.qtrlypaymentavailed : false)
        this.basic = (item.basic != null ? item.basic : 0.0)
        this.basicpaid = (item.basicpaid != null ? item.basicpaid : 0.0)
        this.basicint = (item.basicint != null ? item.basicint : 0.0)
        this.basicdisc = (item.basicdisc != null ? item.basicdisc : 0.0)
        this.basicidle = (item.basicidle != null ? item.basicidle : 0.0)
        this.basicidlepaid = (item.basicidlepaid != null ? item.basicidlepaid : 0.0)
        this.basicidledisc = (item.basicidledisc != null ? item.basicidledisc : 0.0)
        this.basicidleint = (item.basicidleint != null ? item.basicidleint : 0.0)
        this.sef = (item.sef != null ? item.sef : 0.0)
        this.sefpaid = (item.sefpaid != null ? item.sefpaid : 0.0)
        this.sefint = (item.sefint != null ? item.sefint : 0.0)
        this.sefdisc = (item.sefdisc != null ? item.sefdisc : 0.0)
        this.firecode = (item.firecode != null ? item.firecode : 0.0)
        this.firecodepaid = (item.firecodepaid != null ? item.firecodepaid : 0.0)
        this.sh = (item.sh != null ? item.sh : 0.0)
        this.shpaid = (item.shpaid != null ? item.shpaid : 0.0)
        this.shint = (item.shint != null ? item.shint : 0.0)
        this.shdisc = (item.shdisc != null ? item.shdisc : 0.0)
        this.idx = idx 
        this.qtrly = false 
    }

    def toMap(){
        def m = [:]
        m.objid = objid
        m.parentid = parentid 
        m.year = year
        m.qtr = qtr 
        m.basic = basic
        m.basicint = basicint
        m.basicdisc = basicdisc
        m.basicidle = basicidle
        m.basicidledisc = basicidledisc
        m.basicidleint = basicidleint
        m.sef = sef
        m.sefint = sefint
        m.sefdisc = sefdisc
        m.firecode = firecode
        m.sh = sh
        m.shint = shint
        m.shdisc = shdisc
        m.revperiod = revperiod
        if (!parentid){
            m.qtrlyitems = qtrlyitems
            m.qtrly = false
        }
        return m
    }

    void recalcQtrlyItems(){
        if (parentid) return 
        qtrlyitems = [] 
        def qtrlyvalues = buildQtrlyValues()
        def qtr = this.qtr 
        for (; qtr<=4; qtr++){
            def m = [:]
            m.objid = this.objid + '-' + qtr
            m.parentid = this.objid 
            m.revperiod = this.revperiod
            m.basic = qtrlyvalues[qtr].basic 
            m.basicint = qtrlyvalues[qtr].basicint 
            m.basicdisc = qtrlyvalues[qtr].basicdisc
            m.basicidle = qtrlyvalues[qtr].basicidle
            m.basicidledisc = qtrlyvalues[qtr].basicidledisc
            m.basicidleint = qtrlyvalues[qtr].basicidleint
            m.sef = qtrlyvalues[qtr].sef
            m.sefint = qtrlyvalues[qtr].sefint
            m.sefdisc = qtrlyvalues[qtr].sefdisc
            m.firecode = (qtr == 1 ? firecode : 0.0)
            m.sh = qtrlyvalues[qtr].sh
            m.shint = qtrlyvalues[qtr].shint
            m.shdisc = qtrlyvalues[qtr].shdisc            
            qtrlyitems << m
        }
    }


    def buildQtrlyValues(){
        def basic13 = round(basic / 4)
        def basic4 = round(basic - (basic13 * 3))
        def basicint13 = round(basicint / 4)
        def basicint4 = round(basicint - (basicint13 * 3))
        def basicdisc13 = round(basicdisc / 4)
        def basicdisc4 = round(basicdisc - (basicdisc13 * 3))
        def basicidle13 = round(basicidle / 4)
        def basicidle4 = round(basicidle - (basicidle13 * 3))
        def basicidleint13 = round(basicidleint / 4)
        def basicidleint4 = round(basicidleint - (basicidleint13 * 3))
        def basicidledisc13 = round(basicidledisc / 4)
        def basicidledisc4 = round(basicidledisc - (basicidledisc13 * 3))
        def sef13 = round(sef / 4)
        def sef4 = round(sef - (sef13 * 3))
        def sefint13 = round(sefint / 4)
        def sefint4 = round(sefint - (sefint13 * 3))
        def sefdisc13 = round(sefdisc / 4)
        def sefdisc4 = round(sefdisc - (sefdisc13 * 3))
        def sh13 = round(sh / 4)
        def sh4 = round(sh - (sh13 * 3))
        def shint13 = round(shint / 4)
        def shint4 = round(shint - (shint13 * 3))
        def shdisc13 = round(shdisc / 4)
        def shdisc4 = round(shdisc - (shdisc13 * 3))

        def qv = []
        qv[0] = []
        qv[1] = [basic:basic13, basicint:basicint13, basicdisc:basicdisc13, basicidle:basicidle13, basicidleint:basicidleint13, basicidledisc:basicidledisc13, sef:sef13, sefint:sefint13, sefdisc:sefdisc13, sh:sh13, shint:shint13, shdisc:shdisc13]
        qv[2] = [basic:basic13, basicint:basicint13, basicdisc:basicdisc13, basicidle:basicidle13, basicidleint:basicidleint13, basicidledisc:basicidledisc13, sef:sef13, sefint:sefint13, sefdisc:sefdisc13, sh:sh13, shint:shint13, shdisc:shdisc13]
        qv[3] = [basic:basic13, basicint:basicint13, basicdisc:basicdisc13, basicidle:basicidle13, basicidleint:basicidleint13, basicidledisc:basicidledisc13, sef:sef13, sefint:sefint13, sefdisc:sefdisc13, sh:sh13, shint:shint13, shdisc:shdisc13]
        qv[4] = [basic:basic4, basicint:basicint4, basicdisc:basicdisc4, basicidle:basicidle4, basicidleint:basicidleint4, basicidledisc:basicidledisc4, sef:sef4, sefint:sefint4, sefdisc:sefdisc4, sh:sh4, shint:shint4, shdisc:shdisc4]
        return qv 
    }

    def round( amount) {
        if( amount instanceof Number ) {
            def df = new java.text.DecimalFormat('0.0000000')
            amount = df.format(amount)
        }
        def bd = new BigDecimal(amount)
        return bd.setScale(2, RoundingMode.HALF_UP)
    }
}
