[findLedgerbyId]
SELECT 
  rl.objid,
  rl.lastyearpaid,
  rl.lastqtrpaid,
  rl.faasid, 
  rl.nextbilldate AS expirydate,
  DATE_ADD(rl.nextbilldate, INTERVAL -1 DAY) AS validuntil,
  rl.tdno,
  rl.prevtdno,  
  rl.owner_name,
  f.administrator_name,
  f.administrator_address,
  rl.rputype,
  rl.fullpin,
  rl.totalareaha,
  rl.totalareaha * 10000 as totalareasqm,
  rl.totalav,
  b.name AS barangay,
  rl.cadastrallotno,
  rl.barangayid,
  rl.classcode,
  CASE WHEN rl.lastqtrpaid = 4 THEN rl.lastyearpaid + 1 ELSE rl.lastyearpaid END AS fromyear,
  CASE WHEN rl.lastqtrpaid = 4 THEN 1 ELSE rl.lastqtrpaid + 1 END AS fromqtr
FROM rptledger rl 
  INNER JOIN barangay b ON rl.barangayid = b.objid 
  INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
  LEFT JOIN faas f ON rl.faasid = f.objid 
WHERE rl.objid = $P{rptledgerid}
  AND rl.state = 'APPROVED'


[getBilledItems]
select x.* 
from (
  select
    rlf.objid, 
    rlf.tdno,
    sum(bi.av) as assessedvalue,
    sum(bi.av) as originalav,
    concat(bi.year, '-', 
      case when min(bi.qtr) = max(bi.qtr) then min(bi.qtr)
      else concat(min(bi.qtr), max(bi.qtr))
      end 
    ) as period,
    sum(bi.basic) as basic,
    sum(bi.basicint) as basicint,
    sum(bi.basicdisc) as basicdisc,
    sum(bi.basicint - bi.basicdisc) as  basicdp,
    sum(bi.basic - bi.basicdisc + bi.basicint) as basicnet,

    sum(bi.basicidle + bi.basicidleint - bi.basicidledisc) as basicidle,
    
    sum(bi.sef) as sef, 
    sum(bi.sefint) as sefint, 
    sum(bi.sefdisc) as sefdisc, 
    sum(bi.sefint - bi.sefdisc) as  sefdp,
    sum(bi.sef - bi.sefdisc + bi.sefint) as sefnet,
    
    sum(bi.firecode) as firecode,
    
    sum( bi.basic - bi.basicdisc + bi.basicint +
      bi.sef - bi.sefdisc + bi.sefint + 
      bi.basicidle + bi.basicidleint - bi.basicidledisc +
      bi.firecode) as total,
    rl.barangayid,
    bi.taxdifference
  from rptledger rl 
    inner join rptbill_ledger_item bi on rl.objid = bi.rptledgerid 
    inner join rptledgerfaas rlf on bi.rptledgerfaasid = rlf.objid
    inner join rptledgeritem rli on bi.rptledgeritemid = rli.objid 
  where rl.objid = $P{rptledgerid}
    and bi.billid = $P{objid}
    and rli.qtrly = 0
  group by 
    rlf.tdno,
    rlf.assessedvalue,
    rl.barangayid,
    bi.year, 
    bi.taxdifference

  union all 

  select
    rlf.objid, 
    rlf.tdno,
    bi.av as assessedvalue,
    bi.av as originalav,
    concat(bi.year, '-', bi.qtr) as period,
    bi.basic,
    bi.basicint,
    bi.basicdisc,
    bi.basicint - bi.basicdisc as  basicdp,
    bi.basic - bi.basicdisc + bi.basicint as basicnet,

    bi.basicidle + bi.basicidleint - bi.basicidledisc as basicidle,
    
    bi.sef, 
    bi.sefint, 
    bi.sefdisc, 
    bi.sefint - bi.sefdisc as  sefdp,
    bi.sef - bi.sefdisc + bi.sefint as sefnet,
    
    bi.firecode as firecode,
    
    ( bi.basic - bi.basicdisc + bi.basicint +
      bi.sef - bi.sefdisc + bi.sefint + 
      bi.basicidle + bi.basicidleint - bi.basicidledisc +
      bi.firecode) as total,
    rl.barangayid,
    bi.taxdifference
  from rptledger rl 
    inner join rptbill_ledger_item bi on rl.objid = bi.rptledgerid 
    inner join rptledgerfaas rlf on bi.rptledgerfaasid = rlf.objid
    inner join rptledgeritem rli on bi.rptledgeritemid = rli.objid 
  where rl.objid = $P{rptledgerid}
    and bi.billid = $P{objid}
    and rli.qtrly = 1
) x
order by x.period, x.objid 