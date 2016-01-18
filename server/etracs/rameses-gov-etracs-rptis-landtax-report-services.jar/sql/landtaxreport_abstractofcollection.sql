[getAbstractOfRPTCollection] 
SELECT 
  (SELECT MIN(CASE WHEN qtr = 0 THEN 1 ELSE qtr END) FROM cashreceiptitem_rpt_online WHERE (rptledgerid IS NULL OR rptledgerid = t.rptledgerid) AND rptreceiptid = t.receiptid AND year = t.minyear) AS minqtr,
  (SELECT MAX(CASE WHEN qtr = 0 THEN 4 ELSE qtr END) FROM cashreceiptitem_rpt_online WHERE (rptledgerid IS NULL OR rptledgerid = t.rptledgerid) AND rptreceiptid = t.receiptid AND year = t.maxyear) AS maxqtr,
  t.*
FROM (
  SELECT
    cr.objid AS receiptid, 
    rl.objid AS rptledgerid,
    1 AS idx,
    MIN(cri.year) AS minyear,
    MAX(cri.year) AS maxyear, 
    'BASIC' AS type, 
    cr.receiptdate AS ordate, 
    CASE WHEN cv.objid IS NULL THEN cr.payer_name ELSE '*** VOIDED ***' END AS taxpayername, 
    CASE WHEN cv.objid IS NULL THEN rl.tdno ELSE '' END AS tdno, 
    cr.receiptno AS orno, 
    CASE WHEN m.name IS NULL THEN c.name ELSE m.name END AS municityname, 
    b.name AS barangay, 
    CASE WHEN cv.objid IS NULL  THEN rl.classcode ELSE '' END AS classification, 
    CASE WHEN cv.objid IS NULL THEN rl.totalav else 0.0 end as assessvalue,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basic ELSE 0.0 END) AS currentyear,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basic ELSE 0.0 END) AS previousyear,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicdisc ELSE 0.0 END) AS discount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basicint ELSE 0.0 END) AS penaltycurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basicint ELSE 0.0 END) AS penaltyprevious,

    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basicidle ELSE 0.0 END) AS basicidlecurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basicidle ELSE 0.0 END) AS basicidleprevious,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicidledisc ELSE 0.0 END) AS basicidlediscount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basicidleint ELSE 0.0 END) AS basicidlecurrentpenalty,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basicidleint ELSE 0.0 END) AS basicidlepreviouspenalty,

    SUM(CASE WHEN cv.objid IS NULL THEN cri.firecode ELSE 0.0 END) AS firecode,
    SUM(
        CASE WHEN cv.objid IS NULL THEN 
            cri.basic - cri.basicdisc + cri.basicint + 
            cri.basicidle - cri.basicidledisc + cri.basicidleint + 
            cri.firecode 
        ELSE 0.0 END 
    ) AS total,

    MAX(CASE WHEN cv.objid IS NULL THEN cri.partialled ELSE 0 END) AS partialled
  FROM remittance rem 
    INNER JOIN remittance_cashreceipt rc ON rem.objid = rc.remittanceid
    INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
    INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid 
    INNER JOIN rptledger rl ON cri.rptledgerid = rl.objid 
    INNER JOIN barangay b ON rl.barangayid = b.objid 
    LEFT JOIN district d ON b.parentid = d.objid 
    LEFT JOIN city c ON d.parentid = c.objid 
    LEFT JOIN municipality m ON b.parentid = m.objid 
  WHERE ${filter} 
    and cri.year <= $P{year} 
    and cr.collector_objid LIKE $P{collectorid}
  GROUP BY cr.objid, cr.receiptdate, cr.payer_name, cr.receiptno, rl.objid, rl.tdno, b.name, 
            rl.classcode, cv.objid, m.name, c.name , rl.totalav 
   
   
  UNION ALL  

  SELECT
    cr.objid AS receiptid,
    null AS rptledgerid,
    1 AS idx,
    MIN(cri.year) AS minyear,
    MAX(cri.year) AS maxyear, 
    'BASIC' AS type, 
    cr.receiptdate AS ordate, 
    CASE WHEN cv.objid IS NULL THEN rn.owner_name ELSE '*** VOIDED ***' END AS taxpayername, 
    CASE WHEN cv.objid IS NULL THEN rn.tdno ELSE '' END AS tdno, 
    cr.receiptno AS orno, 
    CASE WHEN m.name IS NULL THEN c.name ELSE m.name END AS municityname, 
    b.name AS barangay, 
    CASE WHEN cv.objid IS NULL  THEN pc.code ELSE '' END AS classification, 
    CASE WHEN cv.objid IS NULL THEN rn.av else 0.0 end as assessvalue,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basic ELSE 0.0 END) AS currentyear,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basic ELSE 0.0 END) AS previousyear,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicdisc ELSE 0.0 END) AS discount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basicint ELSE 0.0 END) AS penaltycurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basicint ELSE 0.0 END) AS penaltyprevious,
    
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basicidle ELSE 0.0 END) AS basicidlecurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basicidle ELSE 0.0 END) AS basicidleprevious,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicidledisc ELSE 0.0 END) AS basicidlediscount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basicidleint ELSE 0.0 END) AS basicidlecurrentpenalty,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basicidleint ELSE 0.0 END) AS basicidlepreviouspenalty,

    SUM(CASE WHEN cv.objid IS NULL THEN cri.firecode ELSE 0.0 END) AS firecode,
    SUM(
        CASE WHEN cv.objid IS NULL THEN 
            cri.basic - cri.basicdisc + cri.basicint + 
            cri.basicidle - cri.basicidledisc + cri.basicidleint + 
            cri.firecode 
        ELSE 0.0 END 
    ) AS total,    
    MAX(CASE WHEN cv.objid IS NULL THEN cri.partialled ELSE 0 END) AS partialled 
  FROM remittance rem 
    INNER JOIN remittance_cashreceipt rc ON rem.objid = rc.remittanceid
    INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
    INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid 
    INNER JOIN cashreceiptitem_rpt_noledger rn on rn.objid = cri.objid 
    inner join propertyclassification pc on pc.objid = rn.classification_objid 
    INNER JOIN barangay b ON rn.barangay_objid = b.objid 
    LEFT JOIN district d ON b.parentid = d.objid 
    LEFT JOIN city c ON d.parentid = c.objid 
    LEFT JOIN municipality m ON b.parentid = m.objid 
  WHERE ${filter} 
    and cri.year <= $P{year} 
    and cr.collector_objid LIKE $P{collectorid}
  GROUP BY cr.objid, cr.receiptdate, rn.owner_name, cr.receiptno, 
        rn.tdno, b.name, pc.code, cv.objid, m.name, c.name, rn.originalav, rn.av

  UNION ALL

  SELECT
    cr.objid AS receiptid,
    rl.objid AS rptledgerid,
    2 AS idx,
    MIN(cri.year) AS minyear,
    MAX(cri.year) AS maxyear, 
    'SEF' AS type, 
    cr.receiptdate AS ordate, 
    CASE WHEN cv.objid IS NULL THEN cr.payer_name ELSE '*** VOIDED ***' END AS taxpayername, 
    CASE WHEN cv.objid IS NULL THEN rl.tdno ELSE '' END AS tdno, 
    cr.receiptno AS orno, 
    CASE WHEN m.name IS NULL THEN c.name ELSE m.name END AS municityname, 
    b.name AS barangay, 
    CASE WHEN cv.objid IS NULL  THEN rl.classcode ELSE '' END AS classification, 
    CASE WHEN cv.objid IS NULL THEN rl.totalav else 0.0 end as assessvalue,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.sef ELSE 0.0 END) AS currentyear,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.sef ELSE 0.0 END) AS previousyear,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicdisc ELSE 0.0 END) AS discount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.sefint ELSE 0.0 END) AS penaltycurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.sefint ELSE 0.0 END) AS penaltyprevious,
    
    SUM(0) AS basicidlecurrent,
    SUM(0) AS basicidleprevious,
    SUM(0) AS basicidlediscount,
    SUM(0) AS basicidlecurrentpenalty,
    SUM(0) AS basicidlepreviouspenalty,

    SUM(CASE WHEN cv.objid IS NULL THEN cri.firecode ELSE 0.0 END) AS firecode,
    SUM(CASE WHEN cv.objid IS NULL THEN cri.sef - cri.sefdisc + cri.sefint ELSE 0.0 END ) AS total,
    MAX(CASE WHEN cv.objid IS NULL THEN cri.partialled ELSE 0.0 END) AS partialled
  FROM remittance rem 
    INNER JOIN remittance_cashreceipt rc ON rem.objid = rc.remittanceid
    INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
    INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid 
    INNER JOIN rptledger rl ON cri.rptledgerid = rl.objid 
    INNER JOIN barangay b ON rl.barangayid = b.objid 
    LEFT JOIN district d ON b.parentid = d.objid 
    LEFT JOIN city c ON d.parentid = c.objid 
    LEFT JOIN municipality m ON b.parentid = m.objid 
  WHERE ${filter} 
    and cri.year <= $P{year} 
    and cr.collector_objid LIKE $P{collectorid}
  GROUP BY cr.objid, cr.receiptdate, cr.payer_name, cr.receiptno, rl.objid, rl.tdno, b.name, 
            rl.classcode, cv.objid, m.name, c.name , rl.totalav 
   
  UNION ALL  

  SELECT
    cr.objid AS receiptid,
    null AS rptledgerid,
    2 AS idx,
    MIN(cri.year) AS minyear,
    MAX(cri.year) AS maxyear, 
    'SEF' AS type, 
    cr.receiptdate AS ordate, 
    CASE WHEN cv.objid IS NULL THEN rn.owner_name ELSE '*** VOIDED ***' END AS taxpayername, 
    CASE WHEN cv.objid IS NULL THEN rn.tdno ELSE '' END AS tdno, 
    cr.receiptno AS orno, 
    CASE WHEN m.name IS NULL THEN c.name ELSE m.name END AS municityname, 
    b.name AS barangay, 
    CASE WHEN cv.objid IS NULL  THEN pc.code ELSE '' END AS classification, 
    CASE WHEN cv.objid IS NULL THEN rn.av else 0.0 end as assessvalue,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.sef ELSE 0.0 END) AS currentyear,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.sef ELSE 0.0 END) AS previousyear,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicdisc ELSE 0.0 END) AS discount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.sefint ELSE 0.0 END) AS penaltycurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.sefint ELSE 0.0 END) AS penaltyprevious,
    SUM(0) AS basicidlecurrent,
    SUM(0) AS basicidleprevious,
    SUM(0) AS basicidlediscount,
    SUM(0) AS basicidlecurrentpenalty,
    SUM(0) AS basicidlepreviouspenalty,
    SUM(CASE WHEN cv.objid IS NULL THEN cri.firecode ELSE 0.0 END) AS firecode,
    SUM(CASE WHEN cv.objid IS NULL THEN cri.sef - cri.sefdisc + cri.sefint ELSE 0.0 END) AS total,
    MAX(CASE WHEN cv.objid IS NULL THEN cri.partialled ELSE 0.0 END) AS partialled
  FROM remittance rem 
    INNER JOIN remittance_cashreceipt rc ON rem.objid = rc.remittanceid
    INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
    INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid 
    INNER JOIN cashreceiptitem_rpt_noledger rn on rn.objid = cri.objid 
    inner join propertyclassification pc on pc.objid = rn.classification_objid 
    INNER JOIN barangay b ON rn.barangay_objid = b.objid 
    LEFT JOIN district d ON b.parentid = d.objid 
    LEFT JOIN city c ON d.parentid = c.objid 
    LEFT JOIN municipality m ON b.parentid = m.objid 
  WHERE ${filter} 
    and cri.year <= $P{year} 
    and cr.collector_objid LIKE $P{collectorid}
  GROUP BY cr.objid, cr.receiptdate, rn.owner_name, cr.receiptno, rn.tdno, b.name, 
            pc.code, cv.objid, m.name, c.name, rn.originalav, rn.av 
) t
ORDER BY t.municityname, t.idx, t.orno 



[getAbstractOfRPTCollectionAdvance] 
SELECT 
  (SELECT MIN(CASE WHEN qtr = 0 THEN 1 ELSE qtr END) FROM cashreceiptitem_rpt_online WHERE (rptledgerid IS NULL OR rptledgerid = t.rptledgerid) AND rptreceiptid = t.receiptid AND year = t.minyear) AS minqtr,
  (SELECT MAX(CASE WHEN qtr = 0 THEN 4 ELSE qtr END) FROM cashreceiptitem_rpt_online WHERE (rptledgerid IS NULL OR rptledgerid = t.rptledgerid) AND rptreceiptid = t.receiptid AND year = t.maxyear) AS maxqtr,
  t.*
FROM (
  SELECT
    cr.objid AS receiptid,
    rl.objid AS rptledgerid,
    1 AS idx,
    MIN(cri.year) AS minyear,
    MAX(cri.year) AS maxyear, 
    'BASIC' AS type, 
    cr.receiptdate AS ordate, 
    CASE WHEN cv.objid IS NULL THEN cr.payer_name ELSE '*** VOIDED ***' END AS taxpayername, 
    CASE WHEN cv.objid IS NULL THEN rl.tdno ELSE '' END AS tdno, 
    cr.receiptno AS orno, 
    CASE WHEN m.name IS NULL THEN c.name ELSE m.name END AS municityname, 
    b.name AS barangay, 
    CASE WHEN cv.objid IS NULL  THEN rl.classcode ELSE '' END AS classification, 
    CASE WHEN cv.objid IS NULL THEN rl.totalav else 0.0 end as assessvalue,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basic ELSE 0.0 END) AS currentyear,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basic ELSE 0.0 END) AS previousyear,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicdisc ELSE 0.0 END) AS discount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basicint ELSE 0.0 END) AS penaltycurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basicint ELSE 0.0 END) AS penaltyprevious,
    
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basicidle ELSE 0.0 END) AS basicidlecurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basicidle ELSE 0.0 END) AS basicidleprevious,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicidledisc ELSE 0.0 END) AS basicidlediscount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basicidleint ELSE 0.0 END) AS basicidlecurrentpenalty,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basicidleint ELSE 0.0 END) AS basicidlepreviouspenalty,

    SUM(CASE WHEN cv.objid IS NULL THEN cri.firecode ELSE 0.0 END) AS firecode,

    SUM(
        CASE WHEN cv.objid IS NULL THEN 
            cri.basic - cri.basicdisc + cri.basicint + 
            cri.basicidle - cri.basicidledisc + cri.basicidleint + 
            cri.firecode 
        ELSE 0.0 END 
    ) AS total

  FROM remittance rem 
    INNER JOIN remittance_cashreceipt rc ON rem.objid = rc.remittanceid
    INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
    INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid 
    INNER JOIN rptledger rl ON cri.rptledgerid = rl.objid 
    INNER JOIN barangay b ON rl.barangayid = b.objid 
    LEFT JOIN district d ON b.parentid = d.objid 
    LEFT JOIN city c ON d.parentid = c.objid 
    LEFT JOIN municipality m ON b.parentid = m.objid 
  WHERE ${filter}  
    and cri.year > $P{year} 
    and cr.collector_objid LIKE $P{collectorid}
  GROUP BY cr.objid, cr.receiptdate, cr.payer_name, cr.receiptno, rl.objid, rl.tdno, b.name, 
            rl.classcode, cv.objid, m.name, c.name, rl.totalav 
   
  UNION ALL  

  SELECT
    cr.objid AS receiptid,
    null AS rptledgerid,
    1 AS idx,
    MIN(cri.year) AS minyear,
    MAX(cri.year) AS maxyear, 
    'BASIC' AS type, 
    cr.receiptdate AS ordate, 
    CASE WHEN cv.objid IS NULL THEN rn.owner_name ELSE '*** VOIDED ***' END AS taxpayername, 
    CASE WHEN cv.objid IS NULL THEN rn.tdno ELSE '' END AS tdno, 
    cr.receiptno AS orno, 
    CASE WHEN m.name IS NULL THEN c.name ELSE m.name END AS municityname, 
    b.name AS barangay, 
    CASE WHEN cv.objid IS NULL  THEN pc.code ELSE '' END AS classification, 
    CASE WHEN cv.objid IS NULL THEN rn.av else 0.0 end as assessvalue,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basic ELSE 0.0 END) AS currentyear,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basic ELSE 0.0 END) AS previousyear,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicdisc ELSE 0.0 END) AS discount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basicint ELSE 0.0 END) AS penaltycurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basicint ELSE 0.0 END) AS penaltyprevious,
    
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basicidle ELSE 0.0 END) AS basicidlecurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basicidle ELSE 0.0 END) AS basicidleprevious,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicidledisc ELSE 0.0 END) AS basicidlediscount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basicidleint ELSE 0.0 END) AS basicidlecurrentpenalty,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basicidleint ELSE 0.0 END) AS basicidlepreviouspenalty,

    SUM(CASE WHEN cv.objid IS NULL THEN cri.firecode ELSE 0.0 END) AS firecode,
    SUM(
        CASE WHEN cv.objid IS NULL THEN 
            cri.basic - cri.basicdisc + cri.basicint + 
            cri.basicidle - cri.basicidledisc + cri.basicidleint + 
            cri.firecode 
        ELSE 0.0 END
    ) AS total

  FROM remittance rem 
    INNER JOIN remittance_cashreceipt rc ON rem.objid = rc.remittanceid
    INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
    INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid 
    INNER JOIN cashreceiptitem_rpt_noledger rn on rn.objid = cri.objid 
    inner join propertyclassification pc on pc.objid = rn.classification_objid 
    INNER JOIN barangay b ON rn.barangay_objid = b.objid 
    LEFT JOIN district d ON b.parentid = d.objid 
    LEFT JOIN city c ON d.parentid = c.objid 
    LEFT JOIN municipality m ON b.parentid = m.objid 
  WHERE ${filter}  
    and cri.year > $P{year} 
    and cr.collector_objid LIKE $P{collectorid}
  GROUP BY cr.objid, cr.receiptdate, rn.owner_name, cr.receiptno, rn.tdno, b.name, 
        pc.code, cv.objid, m.name, c.name, rn.originalav, rn.av 

  UNION ALL

  SELECT
    cr.objid AS receiptid,
    rl.objid AS rptledgerid,
    2 AS idx,
    MIN(cri.year) AS minyear,
    MAX(cri.year) AS maxyear, 
    'SEF' AS type, 
    cr.receiptdate AS ordate, 
    CASE WHEN cv.objid IS NULL THEN cr.payer_name ELSE '*** VOIDED ***' END AS taxpayername, 
    CASE WHEN cv.objid IS NULL THEN rl.tdno ELSE '' END AS tdno, 
    cr.receiptno AS orno, 
    CASE WHEN m.name IS NULL THEN c.name ELSE m.name END AS municityname, 
    b.name AS barangay, 
    CASE WHEN cv.objid IS NULL  THEN rl.classcode ELSE '' END AS classification, 
    CASE WHEN cv.objid IS NULL THEN rl.totalav else 0.0 end as assessvalue,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.sef ELSE 0.0 END) AS currentyear,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.sef ELSE 0.0 END) AS previousyear,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicdisc ELSE 0.0 END) AS discount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.sefint ELSE 0.0 END) AS penaltycurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.sefint ELSE 0.0 END) AS penaltyprevious,
    
    SUM(0) AS basicidlecurrent,
    SUM(0) AS basicidleprevious,
    SUM(0) AS basicidlediscount,
    SUM(0) AS basicidlecurrentpenalty,
    SUM(0) AS basicidlepreviouspenalty,

    SUM(CASE WHEN cv.objid IS NULL THEN cri.firecode ELSE 0.0 END) AS firecode,
    SUM(CASE WHEN cv.objid IS NULL THEN cri.sef - cri.sefdisc + cri.sefint ELSE 0.0 END ) AS total
  FROM remittance rem 
    INNER JOIN remittance_cashreceipt rc ON rem.objid = rc.remittanceid
    INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
    INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid 
    INNER JOIN rptledger rl ON cri.rptledgerid = rl.objid 
    INNER JOIN barangay b ON rl.barangayid = b.objid 
    LEFT JOIN district d ON b.parentid = d.objid 
    LEFT JOIN city c ON d.parentid = c.objid 
    LEFT JOIN municipality m ON b.parentid = m.objid 
  WHERE ${filter} 
    and cri.year > $P{year} 
    and cr.collector_objid LIKE $P{collectorid}
  GROUP BY cr.objid, cr.receiptdate, cr.payer_name, cr.receiptno, rl.objid, rl.tdno, b.name, 
            rl.classcode, cv.objid, m.name, c.name, rl.totalav 
   
  UNION ALL  

  SELECT
    cr.objid AS receiptid,
    null AS rptledgerid,
    2 AS idx,
    MIN(cri.year) AS minyear,
    MAX(cri.year) AS maxyear, 
    'SEF' AS type, 
    cr.receiptdate AS ordate, 
    CASE WHEN cv.objid IS NULL THEN rn.owner_name ELSE '*** VOIDED ***' END AS taxpayername, 
    CASE WHEN cv.objid IS NULL THEN rn.tdno ELSE '' END AS tdno, 
    cr.receiptno AS orno, 
    CASE WHEN m.name IS NULL THEN c.name ELSE m.name END AS municityname, 
    b.name AS barangay, 
    CASE WHEN cv.objid IS NULL  THEN pc.code ELSE '' END AS classification, 
    CASE WHEN cv.objid IS NULL THEN rn.av else 0.0 end as assessvalue,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.sef ELSE 0.0 END) AS currentyear,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.sef ELSE 0.0 END) AS previousyear,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicdisc ELSE 0.0 END) AS discount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.sefint ELSE 0.0 END) AS penaltycurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.sefint ELSE 0.0 END) AS penaltyprevious,
    SUM(0) AS basicidlecurrent,
    SUM(0) AS basicidleprevious,
    SUM(0) AS basicidlediscount,
    SUM(0) AS basicidlecurrentpenalty,
    SUM(0) AS basicidlepreviouspenalty,

    SUM(CASE WHEN cv.objid IS NULL THEN cri.firecode ELSE 0.0 END) AS firecode,
    SUM(CASE WHEN cv.objid IS NULL THEN cri.sef - cri.sefdisc + cri.sefint ELSE 0.0 END) AS total
  FROM remittance rem 
    INNER JOIN remittance_cashreceipt rc ON rem.objid = rc.remittanceid
    INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
    INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid 
    INNER JOIN cashreceiptitem_rpt_noledger rn on rn.objid = cri.objid 
    inner join propertyclassification pc on pc.objid = rn.classification_objid 
    INNER JOIN barangay b ON rn.barangay_objid = b.objid 
    LEFT JOIN district d ON b.parentid = d.objid 
    LEFT JOIN city c ON d.parentid = c.objid 
    LEFT JOIN municipality m ON b.parentid = m.objid 
  WHERE ${filter}  
    and cri.year > $P{year} 
    and cr.collector_objid LIKE $P{collectorid}
  GROUP BY cr.objid, cr.receiptdate, rn.owner_name, cr.receiptno, rn.tdno, b.name, 
        pc.code, cv.objid, m.name, c.name, rn.originalav, rn.av
) t
ORDER BY t.municityname, t.idx, t.orno 




[getMuniCityByRemittance]
select 
  distinct t.* 
 from (
  SELECT
    CASE WHEN m.name IS NULL THEN c.name ELSE m.name END AS municityname 
  FROM remittance rem 
    INNER JOIN remittance_cashreceipt rc ON rem.objid = rc.remittanceid
    INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
    INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid 
    INNER JOIN rptledger rl ON cri.rptledgerid = rl.objid 
    INNER JOIN barangay b ON rl.barangayid = b.objid 
    LEFT JOIN district d ON b.parentid = d.objid 
    LEFT JOIN city c ON d.parentid = c.objid 
    LEFT JOIN municipality m ON b.parentid = m.objid 
  WHERE rem.objid =  $P{remittanceid} 
  
  union 
  
  SELECT
    CASE WHEN m.name IS NULL THEN c.name ELSE m.name END AS municityname
  FROM remittance rem 
    INNER JOIN remittance_cashreceipt rc ON rem.objid = rc.remittanceid
    INNER JOIN cashreceipt cr ON rc.objid = cr.objid 
    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
    INNER JOIN cashreceiptitem_rpt_online cri ON cr.objid = cri.rptreceiptid 
    INNER JOIN cashreceiptitem_rpt_noledger rn on rn.objid = cri.objid 
    INNER JOIN barangay b ON rn.barangay_objid = b.objid 
    LEFT JOIN district d ON b.parentid = d.objid 
    LEFT JOIN city c ON d.parentid = c.objid 
    LEFT JOIN municipality m ON b.parentid = m.objid 
  WHERE rem.objid = $P{remittanceid}
 ) t 


[getAbstractOfRPTCollectionDetail]
select 
  c.objid,
  c.receiptno,
  c.receiptdate as ordate,
  case when cv.objid is null then c.paidby else '*** VOIDED ***' end as taxpayername, 
  case when cv.objid is null then c.amount else 0.0 end AS amount 
from cashreceipt c 
  inner join remittance_cashreceipt rc on rc.objid = c.objid 
  inner join cashreceipt_rpt crpt on crpt.objid = c.objid
  left join cashreceipt_void cv on cv.receiptid  = c.objid 
where rc.remittanceid=$P{remittanceid} 
  and cv.objid is null 
order by c.receiptno  


[getAbstractOfRPTCollectionDetailItem]
select t.*
from (
  select
    b.name as barangay, rl.tdno, rl.cadastrallotno, rl.totalav as assessedavalue,
    cro.year, cro.qtr ,
    cro.basic, ( cro.basicint + ( cro.basicdisc * - 1) ) as basicdp, cro.basicnet,
    cro.sef, (cro.sefint + ( cro.sefdisc * -1) ) as sefdp, cro.sefnet,  
    (cro.basicidle - cro.basicidledisc + cro.basicidleint ) as idlenet, 
    cro.basic - cro.basicdisc + cro.basicint + 
    cro.basicidle - cro.basicidledisc + cro.basicidleint + 
    cro.sef - cro.sefdisc + cro.sefint + 
    cro.firecode AS total
  from cashreceiptitem_rpt_online cro 
    inner join rptledger rl on rl.objid = cro.rptledgerid 
    inner join barangay b on b.objid = rl.barangayid 
  where cro.rptreceiptid=$P{objid}


  union 

  select
    b.name as barangay, rl.tdno, rl.cadastrallotno, rl.originalav  as assessedavalue,
    cro.year, cro.qtr,
    cro.basic, ( cro.basicint +  ( cro.basicdisc * - 1)  ) as basicdp, cro.basicnet,
    cro.sef, (cro.sefint + ( cro.sefdisc * -1) ) as sefdp, cro.sefnet,  (cro.basicidle - cro.basicidledisc + cro.basicidleint ) as idlenet, cro.total 
  from cashreceiptitem_rpt_online cro 
    inner join cashreceiptitem_rpt_noledger rl on rl.objid = cro.objid 
    inner join barangay b on b.objid = rl.barangay_objid 
  where cro.rptreceiptid=$P{objid}
) t
order by barangay, tdno, cadastrallotno, year, qtr