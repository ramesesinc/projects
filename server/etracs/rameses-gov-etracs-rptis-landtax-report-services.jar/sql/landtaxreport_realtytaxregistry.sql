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
        0 as sh, 
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
        rp.fromqtr,
        cri.year AS toyear,
        rp.toqtr,
        cri.basic AS basic,
        cri.basicint AS basicint,
        cri.basicdisc AS basicdisc,
        cri.basicidle- cri.basicidledisc + cri.basicidleint AS basicidle,
        cri.sef AS sef,
        cri.sefint AS sefint,
        cri.sefdisc AS sefdisc,
        cri.firecode AS firecode,
        cri.sh - cri.shdisc + cri.shint AS sh,
        (cri.basic+ cri.basicint - cri.basicdisc + 
          cri.basicidle - cri.basicidledisc + cri.basicidleint +
          cri.sef + cri.sefint - cri.sefdisc + 
          cri.sh + cri.shint - cri.shdisc + cri.firecode) AS amount,
        crr.txntype AS mode,
        cri.partialled,
        case when rlf.tdno is null then (select tdno from rptledgerfaas where rptledgerid = rl.objid and cri.year >= fromyear and (cri.year <= toyear or toyear = 0)) else rlf.tdno end as tdno, 
        case when rlf.tdno is null then (select assessedvalue from rptledgerfaas where rptledgerid = rl.objid and cri.year >= fromyear and (cri.year <= toyear or toyear = 0)) else rlf.assessedvalue end as assessedvalue
    FROM cashreceipt_rpt crr
        INNER JOIN cashreceipt cr ON crr.objid = cr.objid 
        inner join rptledger_payment rp on cr.objid = rp.receiptid 
        inner join rptledger_payment_item cri on rp.objid = cri.parentid
        LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
        LEFT JOIN rptledger rl ON rp.rptledgerid = rl.objid 
        LEFT JOIN rptledgerfaas rlf on cri.rptledgerfaasid = rlf.objid 
    WHERE rp.rptledgerid = $P{objid}
        AND cv.objid IS NULL    
    AND cri.partialled = 1 

    UNION ALL 

    SELECT 
        cr.objid AS rptreceiptid, 
        cr.receiptno AS refno,
        cr.receiptdate AS refdate,
        cr.collector_name,
        cr.paidby AS paidby_name,
        min(cri.year) AS fromyear,
        min(rp.fromqtr) AS fromqtr,
        max(cri.year) AS toyear,
        max(rp.toqtr) as toqtr,
        sum(cri.basic) AS basic,
        sum(cri.basicint) AS basicint,
        sum(cri.basicdisc) AS basicdisc,
        sum(cri.basicidle- cri.basicidledisc + cri.basicidleint) AS basicidle,
        sum(cri.sef) AS sef,
        sum(cri.sefint) AS sefint,
        sum(cri.sefdisc) AS sefdisc,
        sum(cri.firecode) AS firecode,
        sum(cri.sh- cri.shdisc + cri.shint) AS sh,
        sum(cri.basic+ cri.basicint - cri.basicdisc + 
          cri.basicidle - cri.basicidledisc + cri.basicidleint +
          cri.sef + cri.sefint - cri.sefdisc + 
          cri.sh + cri.shint - cri.shdisc + cri.firecode) AS amount,
        crr.txntype AS mode,
        cri.partialled,
        case when rlf.tdno is null then (select tdno from rptledgerfaas where rptledgerid = rl.objid and cri.year >= fromyear and (cri.year <= toyear or toyear = 0)) else rlf.tdno end as tdno, 
        case when rlf.tdno is null then (select assessedvalue from rptledgerfaas where rptledgerid = rl.objid and cri.year >= fromyear and (cri.year <= toyear or toyear = 0)) else rlf.assessedvalue end as assessedvalue
    FROM cashreceipt_rpt crr
        INNER JOIN cashreceipt cr ON crr.objid = cr.objid 
        inner join rptledger_payment rp on cr.objid = rp.receiptid 
        inner join rptledger_payment_item cri on rp.objid = cri.parentid
        LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
        left JOIN rptledger rl ON rp.rptledgerid = rl.objid 
        left JOIN rptledgerfaas rlf on cri.rptledgerfaasid = rlf.objid 
    WHERE rp.rptledgerid = $P{objid}
        AND cv.objid IS NULL
        AND cri.partialled = 0  
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
) t
ORDER BY t.refdate desc, t.fromyear desc, t.fromqtr desc 

