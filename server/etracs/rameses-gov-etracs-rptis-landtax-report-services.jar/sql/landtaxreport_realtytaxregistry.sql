[findLedgerInfo]
select 
    e.entityno, e.name, e.address_text AS address,  
    pc.code AS classcode, 
    rl.*
from rptledger rl 
    inner join entity e on rl.taxpayer_objid = e.objid 
    inner join propertyclassification pc on rl.classification_objid = pc.objid 
where rl.objid = $P{objid}



[getLedgerCredits]
SELECT t.*
FROM (
    SELECT 
        rc.objid AS rptreceiptid,
        rc.refno,
        rc.refdate ,
        rc.collector as collector_name, 
        rc.paidby_name,
        rc.fromyear,
        rc.fromqtr,
        rc.toyear, 
        rc.toqtr,
        rc.basic,
        rc.basicint,
        rc.basicdisc,
        rc.basicidle,
        rc.sef,
        rc.sefint,
        rc.sefdisc,
        rc.firecode,
        rc.amount,
        rc.type AS mode,
        0 as partialled,
        rl.tdno, 
        rl.totalav AS assessedvalue
    FROM rptledger rl 
            inner join rptledger_credit rc on rl.objid = rc.rptledgerid
    WHERE rc.rptledgerid = $P{objid}
        and rc.type <> 'COMPROMISE'

    UNION ALL
    
    SELECT 
        cr.objid AS rptreceiptid, 
        cr.receiptno AS refno,
        cr.receiptdate AS refdate,
        cr.collector_name,
        cr.paidby AS paidby_name,
        cri.year AS fromyear,
                case when cri.qtr = 0 then cri.fromqtr else cri.qtr end AS fromqtr,
        cri.year AS toyear,
        case when cri.qtr = 0 then cri.toqtr else cri.qtr end AS toqtr,
        cri.basic AS basic,
        cri.basicint AS basicint,
        cri.basicdisc AS basicdisc,
        cri.basicidle- cri.basicidledisc + cri.basicidleint AS basicidle,
        cri.sef AS sef,
        cri.sefint AS sefint,
        cri.sefdisc AS sefdisc,
        cri.firecode AS firecode,
        cri.basic+ cri.basicint - cri.basicdisc + cri.basicidle - cri.basicidledisc + cri.basicidleint +
                cri.sef + cri.sefint - cri.sefdisc + cri.firecode AS amount,
        crr.txntype AS mode,
        cri.partialled,
        case when rlf.tdno is null then (select tdno from rptledgerfaas where rptledgerid = rl.objid and cri.year >= fromyear and (cri.year <= toyear or toyear = 0)) else rlf.tdno end as tdno, 
        case when rlf.tdno is null then (select assessedvalue from rptledgerfaas where rptledgerid = rl.objid and cri.year >= fromyear and (cri.year <= toyear or toyear = 0)) else rlf.assessedvalue end as assessedvalue
    FROM cashreceipt_rpt crr
        INNER JOIN cashreceipt cr ON crr.objid = cr.objid 
        INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid    
        LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
        LEFT JOIN rptledger rl ON cri.rptledgerid = rl.objid 
        LEFT JOIN rptledgerfaas rlf on cri.rptledgerfaasid = rlf.objid 
    WHERE cri.rptledgerid = $P{objid}
        AND cv.objid IS NULL    
    AND cri.partialled = 1 
        and cr.receiptno not like '%001'

    UNION ALL 

    select              
            x.rptreceiptid, 
                x.refno,
                x.refdate,
                x.collector_name,
                x.paidby_name,
                x.fromyear,
                (select min(fromqtr) from cashreceiptitem_rpt_online where rptledgerid = $P{objid} and rptreceiptid = x.rptreceiptid and year = x.fromyear and partialled = 0) as fromqtr,
                x.toyear,
                (select max(toqtr) from cashreceiptitem_rpt_online where rptledgerid = $P{objid} and  rptreceiptid = x.rptreceiptid and year = x.toyear and partialled = 0) as toqtr, 
                x.basic,
                x.basicint,
                x.basicdisc,
                x.basicidle,
                x.sef,
                x.sefint,
                x.sefdisc,
                x.firecode,
                x.amount,
                x.mode,
                x.partialled,
                x.tdno, 
                x.assessedvalue
    from (
        SELECT 
                cr.objid AS rptreceiptid, 
                cr.receiptno AS refno,
                cr.receiptdate AS refdate,
                cr.collector_name,
                cr.paidby AS paidby_name,
                min(cri.year) AS fromyear,
                min(cri.fromqtr) AS fromqtr,
                max(cri.year) AS toyear,
                max(cri.toqtr) as toqtr,
                sum(cri.basic) AS basic,
                sum(cri.basicint) AS basicint,
                sum(cri.basicdisc) AS basicdisc,
                sum(cri.basicidle- cri.basicidledisc + cri.basicidleint) AS basicidle,
                sum(cri.sef) AS sef,
                sum(cri.sefint) AS sefint,
                sum(cri.sefdisc) AS sefdisc,
                sum(cri.firecode) AS firecode,
                sum(cri.basic+ cri.basicint - cri.basicdisc + cri.basicidle - cri.basicidledisc + cri.basicidleint +
                        cri.sef + cri.sefint - cri.sefdisc + cri.firecode) AS amount,
                crr.txntype AS mode,
                cri.partialled,
                case when rlf.tdno is null then (select tdno from rptledgerfaas where rptledgerid = rl.objid and cri.year >= fromyear and (cri.year <= toyear or toyear = 0)) else rlf.tdno end as tdno, 
        case when rlf.tdno is null then (select assessedvalue from rptledgerfaas where rptledgerid = rl.objid and cri.year >= fromyear and (cri.year <= toyear or toyear = 0)) else rlf.assessedvalue end as assessedvalue
            FROM cashreceipt_rpt crr
                INNER JOIN cashreceipt cr ON crr.objid = cr.objid 
                INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid    
                LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
                left JOIN rptledger rl ON cri.rptledgerid = rl.objid 
                left JOIN rptledgerfaas rlf on cri.rptledgerfaasid = rlf.objid 
            WHERE cri.rptledgerid = $P{objid}
                AND cv.objid IS NULL
                AND cri.partialled = 0  
                                and cr.receiptno not like '%001'
        group by 
            cr.objid,
            cr.receiptno,
            cr.receiptdate,
            cr.paidby,
            crr.txntype,
            cri.year, 
            cri.partialled,
            rl.objid, 
            rlf.tdno, 
            rlf.assessedvalue,
            cr.collector_name
    ) x 

) t
ORDER BY t.refdate desc, t.fromyear desc, t.fromqtr desc 

