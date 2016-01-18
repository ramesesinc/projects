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
        rl.totalav AS assessedvalue,
        rc.collector as collector_name
    FROM rptledger rl 
        INNER JOIN rptledger_credit rc ON rl.objid = rc.rptledgerid 
    WHERE rl.objid = $P{objid}

    UNION ALL
    
    SELECT 
        cr.objid AS rptreceiptid, 
        cr.receiptno AS refno,
        cr.receiptdate AS refdate,
        cr.paidby AS paidby_name,
        cri.year AS fromyear,
        case when cri.qtr = 0 then 1 else cri.qtr end AS fromqtr,
        cri.year AS toyear,
        case when cri.qtr = 0 then 4 else cri.qtr end AS toqtr,
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
        rlf.tdno, 
        rlf.assessedvalue,
        cr.collector_name
    FROM cashreceipt_rpt crr
        INNER JOIN cashreceipt cr ON crr.objid = cr.objid 
        INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid   
        INNER JOIN rptledger rl ON cri.rptledgerid = rl.objid 
        INNER JOIN rptledgerfaas rlf on cri.rptledgerfaasid = rlf.objid 
        LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid
    WHERE cri.rptledgerid = $P{objid}
        AND cv.objid IS NULL    

) t
ORDER BY t.refdate, t.fromyear, t.fromqtr 


