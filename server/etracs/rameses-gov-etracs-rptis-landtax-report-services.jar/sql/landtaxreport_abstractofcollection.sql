[getAbstractOfRPTCollection] 
select t.*
from (
  select
    cr.objid as receiptid, 
    rl.objid as rptledgerid,
    rl.fullpin,
    1 AS idx,
    MIN(cri.year) AS minyear,
    min(rp.fromqtr) as minqtr,
    MAX(cri.year) AS maxyear, 
    max(rp.toqtr) as maxqtr,
    'BASIC' AS type, 
    cr.receiptdate AS ordate, 
    CASE WHEN cv.objid IS NULL THEN cr.payer_name ELSE '*** VOIDED ***' END AS taxpayername, 
    CASE WHEN cv.objid IS NULL THEN rl.tdno ELSE '' END AS tdno, 
    cr.receiptno AS orno, 
    CASE WHEN m.name IS NULL THEN c.name ELSE m.name END AS municityname, 
    b.name AS barangay, 
    CASE WHEN cv.objid IS NULL  THEN rl.classcode ELSE '' END AS classification, 
    CASE WHEN cv.objid IS NULL THEN rl.totalav else 0.0 end as assessvalue,
    rl.titleno, rl.cadastrallotno, rl.rputype, rl.totalmv, 
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basic ELSE 0.0 END) AS currentyear,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basic ELSE 0.0 END) AS previousyear,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicdisc ELSE 0.0 END) AS discount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basicint ELSE 0.0 END) AS penaltycurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basicint ELSE 0.0 END) AS penaltyprevious,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.basicidle else 0.0 end) as basicidlecurrent,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.basicidle else 0.0 end) as basicidleprevious,
    sum(case when cv.objid is null  then cri.basicidledisc else 0.0 end) as basicidlediscount,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.basicidleint else 0.0 end) as basicidlecurrentpenalty,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.basicidleint else 0.0 end) as basicidlepreviouspenalty,

    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.sh else 0.0 end) as shcurrent,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.sh else 0.0 end) as shprevious,
    sum(case when cv.objid is null  then cri.shdisc else 0.0 end) as shdiscount,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.shint else 0.0 end) as shcurrentpenalty,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.shint else 0.0 end) as shpreviouspenalty,

    sum(case when cv.objid is null then cri.firecode else 0.0 end) as firecode,
    sum(
        case when cv.objid is null then 
            cri.basic - cri.basicdisc + cri.basicint + 
            cri.basicidle - cri.basicidledisc + cri.basicidleint + 
            cri.sh - cri.shdisc + cri.shint + 
            cri.firecode 
        else 0.0 end 
    ) as total,

    max(case when cv.objid is null then cri.partialled else 0 end) as partialled
  from collectionvoucher liq 
    inner join remittance rem on liq.objid  = rem.collectionvoucherid 
    inner join cashreceipt cr on rem.objid = cr.remittanceid
    left join cashreceipt_void cv on cr.objid = cv.receiptid 
    inner join rptpayment rp on cr.objid = rp.receiptid
    inner join vw_rptpayment_item cri on rp.objid = cri.parentid
    inner join rptledger rl on rp.refid = rl.objid 
    inner join barangay b on rl.barangayid = b.objid 
    left join district d on b.parentid = d.objid 
    left join city c on d.parentid = c.objid 
    left join municipality m on b.parentid = m.objid 
  where ${filter} 
    and cr.collector_objid LIKE $P{collectorid} 
  GROUP BY cr.objid, cr.receiptdate, cr.payer_name, cr.receiptno, rl.objid, rl.fullpin, rl.tdno, b.name, 
            rl.classcode, cv.objid, m.name, c.name , rl.totalav, rl.titleno, rl.cadastrallotno, rl.rputype, rl.totalmv
   
  union all  

  select
    cr.objid as receiptid,
    rl.objid as rptledgerid,
    rl.fullpin,
    2 AS idx,
    MIN(cri.year) AS minyear,
    min(rp.fromqtr) as minqtr,
    MAX(cri.year) AS maxyear, 
    min(rp.toqtr) as maxqtr,
    'SEF' AS type, 
    cr.receiptdate AS ordate, 
    CASE WHEN cv.objid IS NULL THEN cr.payer_name ELSE '*** VOIDED ***' END AS taxpayername, 
    CASE WHEN cv.objid IS NULL THEN rl.tdno ELSE '' END AS tdno, 
    cr.receiptno AS orno, 
    CASE WHEN m.name IS NULL THEN c.name ELSE m.name END AS municityname, 
    b.name AS barangay, 
    CASE WHEN cv.objid IS NULL  THEN rl.classcode ELSE '' END AS classification, 
    CASE WHEN cv.objid IS NULL THEN rl.totalav else 0.0 end as assessvalue,
    rl.titleno, rl.cadastrallotno, rl.rputype, rl.totalmv, 
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.sef ELSE 0.0 END) AS currentyear,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.sef ELSE 0.0 END) AS previousyear,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicdisc ELSE 0.0 END) AS discount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.sefint ELSE 0.0 END) AS penaltycurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.sefint ELSE 0.0 END) AS penaltyprevious,
    sum(0) as basicidlecurrent,
    sum(0) as basicidleprevious,
    sum(0) as basicidlediscount,
    sum(0) as basicidlecurrentpenalty,
    sum(0) as basicidlepreviouspenalty,

    sum(0) as shcurrent,
    sum(0) as shprevious,
    sum(0) as shdiscount,
    sum(0) as shcurrentpenalty,
    sum(0) as shpreviouspenalty,

    sum(case when cv.objid is null then cri.firecode else 0.0 end) as firecode,
    sum(case when cv.objid is null then cri.sef - cri.sefdisc + cri.sefint else 0.0 end ) as total,
    max(case when cv.objid is null then cri.partialled else 0.0 end) as partialled
  from collectionvoucher liq 
    inner join remittance rem on liq.objid  = rem.collectionvoucherid 
    inner join cashreceipt cr on rem.objid = cr.remittanceid
    left join cashreceipt_void cv on cr.objid = cv.receiptid 
    inner join rptpayment rp on cr.objid = rp.receiptid
    inner join vw_rptpayment_item cri on rp.objid = cri.parentid
    inner join rptledger rl on rp.refid = rl.objid 
    inner join barangay b on rl.barangayid = b.objid 
    left join district d on b.parentid = d.objid 
    left join city c on d.parentid = c.objid 
    left join municipality m on b.parentid = m.objid 
  where ${filter} 
    and cr.collector_objid LIKE $P{collectorid}
  GROUP BY cr.objid, cr.receiptdate, cr.payer_name, cr.receiptno, rl.objid, rl.fullpin, rl.tdno, b.name, 
            rl.classcode, cv.objid, m.name, c.name , rl.totalav,rl.titleno, rl.cadastrallotno, rl.rputype, rl.totalmv
) t
order by t.municityname, t.idx, t.orno 





[getAbstractOfRPTCollectionAdvance] 
select t.*
from (
  select
    cr.objid as receiptid,
    rl.objid as rptledgerid,
    rl.fullpin,
    1 AS idx,
    MIN(cri.year) AS minyear,
    min(rp.fromqtr) as minqtr,
    MAX(cri.year) AS maxyear, 
    min(rp.toqtr) as maxqtr,
    'BASIC' AS type, 
    cr.receiptdate AS ordate, 
    CASE WHEN cv.objid IS NULL THEN cr.payer_name ELSE '*** VOIDED ***' END AS taxpayername, 
    CASE WHEN cv.objid IS NULL THEN rl.tdno ELSE '' END AS tdno, 
    cr.receiptno AS orno, 
    CASE WHEN m.name IS NULL THEN c.name ELSE m.name END AS municityname, 
    b.name AS barangay, 
    CASE WHEN cv.objid IS NULL  THEN rl.classcode ELSE '' END AS classification, 
    CASE WHEN cv.objid IS NULL THEN rl.totalav else 0.0 end as assessvalue,
    rl.titleno, rl.cadastrallotno, rl.rputype, rl.totalmv, 
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basic ELSE 0.0 END) AS currentyear,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basic ELSE 0.0 END) AS previousyear,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicdisc ELSE 0.0 END) AS discount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.basicint ELSE 0.0 END) AS penaltycurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.basicint ELSE 0.0 END) AS penaltyprevious,
    
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.basicidle else 0.0 end) as basicidlecurrent,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.basicidle else 0.0 end) as basicidleprevious,
    sum(case when cv.objid is null  then cri.basicidledisc else 0.0 end) as basicidlediscount,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.basicidleint else 0.0 end) as basicidlecurrentpenalty,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.basicidleint else 0.0 end) as basicidlepreviouspenalty,

    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.sh else 0.0 end) as shcurrent,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.sh else 0.0 end) as shprevious,
    sum(case when cv.objid is null  then cri.shdisc else 0.0 end) as shdiscount,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.shint else 0.0 end) as shcurrentpenalty,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.shint else 0.0 end) as shpreviouspenalty,

    sum(case when cv.objid is null then cri.firecode else 0.0 end) as firecode,

    sum(
        case when cv.objid is null then 
            cri.basic - cri.basicdisc + cri.basicint + 
            cri.basicidle - cri.basicidledisc + cri.basicidleint + 
            cri.sh - cri.shdisc + cri.shint + 
            cri.firecode 
        else 0.0 end 
    ) as total

  from collectionvoucher liq 
    inner join remittance rem on liq.objid  = rem.collectionvoucherid 
    inner join cashreceipt cr on rem.objid = cr.remittanceid
    left join cashreceipt_void cv on cr.objid = cv.receiptid 
    inner join rptpayment rp on cr.objid = rp.receiptid
    inner join vw_rptpayment_item cri on rp.objid = cri.parentid
    inner join rptledger rl on rp.refid = rl.objid 
    inner join barangay b on rl.barangayid = b.objid 
    left join district d on b.parentid = d.objid 
    left join city c on d.parentid = c.objid 
    left join municipality m on b.parentid = m.objid 
  where ${filter}  
    and cri.year > $P{year} 
    and cr.collector_objid LIKE $P{collectorid}
  GROUP BY cr.objid, cr.receiptdate, cr.payer_name, cr.receiptno, rl.objid, rl.tdno, rl.fullpin, b.name, 
            rl.classcode, cv.objid, m.name, c.name, rl.totalav, rl.titleno, rl.cadastrallotno, rl.rputype, rl.totalmv
   
  union all  

  select
    cr.objid as receiptid,
    rl.objid as rptledgerid,
    rl.fullpin,
    2 AS idx,
    MIN(cri.year) AS minyear,
    min(rp.fromqtr) as minqtr,
    MAX(cri.year) AS maxyear, 
    min(rp.toqtr) as maxqtr,
    'SEF' AS type, 
    cr.receiptdate AS ordate, 
    CASE WHEN cv.objid IS NULL THEN cr.payer_name ELSE '*** VOIDED ***' END AS taxpayername, 
    CASE WHEN cv.objid IS NULL THEN rl.tdno ELSE '' END AS tdno, 
    cr.receiptno AS orno, 
    CASE WHEN m.name IS NULL THEN c.name ELSE m.name END AS municityname, 
    b.name AS barangay, 
    CASE WHEN cv.objid IS NULL  THEN rl.classcode ELSE '' END AS classification, 
    CASE WHEN cv.objid IS NULL THEN rl.totalav else 0.0 end as assessvalue,
    rl.titleno, rl.cadastrallotno, rl.rputype, rl.totalmv, 
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.sef ELSE 0.0 END) AS currentyear,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.sef ELSE 0.0 END) AS previousyear,
    SUM(CASE WHEN cv.objid IS NULL  THEN cri.basicdisc ELSE 0.0 END) AS discount,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('current','advance') THEN cri.sefint ELSE 0.0 END) AS penaltycurrent,
    SUM(CASE WHEN cv.objid IS NULL  AND cri.revperiod IN ('previous','prior') THEN cri.sefint ELSE 0.0 END) AS penaltyprevious,
    
    sum(0) as basicidlecurrent,
    sum(0) as basicidleprevious,
    sum(0) as basicidlediscount,
    sum(0) as basicidlecurrentpenalty,
    sum(0) as basicidlepreviouspenalty,

    sum(0) as shcurrent,
    sum(0) as shprevious,
    sum(0) as shdiscount,
    sum(0) as shcurrentpenalty,
    sum(0) as shpreviouspenalty,

    sum(case when cv.objid is null then cri.firecode else 0.0 end) as firecode,
    sum(case when cv.objid is null then cri.sef - cri.sefdisc + cri.sefint else 0.0 end ) as total
  from collectionvoucher liq 
    inner join remittance rem on liq.objid = rem.collectionvoucherid 
    inner join cashreceipt cr on rem.objid = cr.remittanceid
    left join cashreceipt_void cv on cr.objid = cv.receiptid 
    inner join rptpayment rp on cr.objid = rp.receiptid
    inner join vw_rptpayment_item cri on rp.objid = cri.parentid
    inner join rptledger rl on rp.refid = rl.objid 
    inner join barangay b on rl.barangayid = b.objid 
    left join district d on b.parentid = d.objid 
    left join city c on d.parentid = c.objid 
    left join municipality m on b.parentid = m.objid 
  where ${filter} 
    and cri.year > $P{year} 
    and cr.collector_objid LIKE $P{collectorid}
  GROUP BY cr.objid, cr.receiptdate, cr.payer_name, cr.receiptno, rl.objid, rl.fullpin, rl.tdno, b.name, 
            rl.classcode, cv.objid, m.name, c.name, rl.totalav, rl.titleno, rl.cadastrallotno, rl.rputype, rl.totalmv
) t
order by t.municityname, t.idx, t.orno 




[getMuniCityByRemittance]
select 
  distinct t.* 
 from (
  select
    case when m.name is null then c.name else m.name end as municityname 
  from remittance rem 
    inner join cashreceipt cr on rem.objid = cr.remittanceid 
    left join cashreceipt_void cv on cr.objid = cv.receiptid 
    inner join rptpayment rp on cr.objid = rp.receiptid
    inner join vw_rptpayment_item cri on rp.objid = cri.parentid
    inner join rptledger rl on rp.refid = rl.objid 
    inner join barangay b on rl.barangayid = b.objid 
    left join district d on b.parentid = d.objid 
    left join city c on d.parentid = c.objid 
    left join municipality m on b.parentid = m.objid 
  where rem.objid =  $P{remittanceid} 
 ) t 


[getAbstractOfRPTCollectionDetail]
select 
  c.objid,
  c.receiptno,
  c.receiptdate as ordate,
  case when cv.objid is null then c.paidby else '*** VOIDED ***' end as taxpayername, 
  case when cv.objid is null then c.amount else 0.0 end AS amount 
from cashreceipt c 
  inner join cashreceipt_rpt crpt on crpt.objid = c.objid
  left join cashreceipt_void cv on cv.receiptid  = c.objid 
where c.remittanceid=$P{remittanceid} 
  and cv.objid is null 
order by c.receiptno  


[getAbstractOfRPTCollectionDetailItem]
select
  b.name as barangay, rl.tdno, rl.cadastrallotno, rl.totalav as assessedavalue,
  cri.year, cri.qtr ,
  cri.basic, ( cri.basicint + ( cri.basicdisc * - 1) ) as basicdp, 
  (cri.basic - cri.basicdisc + cri.basicint) as basicnet,
  cri.sef, (cri.sefint + ( cri.sefdisc * -1) ) as sefdp, 
  (cri.sef - cri.sefdisc + cri.sefint) as sefnet,
  (cri.basicidle - cri.basicidledisc + cri.basicidleint ) as idlenet, 
  (cri.sh - cri.shdisc + cri.shint ) as shnet, 
  cri.firecode,
  (cri.basic - cri.basicdisc + cri.basicint + 
    cri.basicidle - cri.basicidledisc + cri.basicidleint + 
    cri.sef - cri.sefdisc + cri.sefint + 
    cri.sh - cri.shdisc + cri.shint + 
    cri.firecode) as total
from rptpayment rp
  inner join vw_rptpayment_item cri on rp.objid = cri.parentid
  inner join rptledger rl on rp.refid = rl.objid 
  inner join barangay b on b.objid = rl.barangayid 
where rp.receiptid=$P{objid}
order by b.name, rl.tdno, rl.cadastrallotno, cri.year, cri.qtr
